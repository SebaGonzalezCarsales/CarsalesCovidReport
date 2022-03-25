package com.gonzalezcs.coviddatemvvm.ui.view

import android.app.DatePickerDialog
import android.database.sqlite.SQLiteConstraintException
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.room.Room
import com.gonzalezcs.coviddatemvvm.MyAppApplication
import com.gonzalezcs.coviddatemvvm.ui.utils.AnimationViewClass
import com.gonzalezcs.coviddatemvvm.ui.utils.ValueFormatClass
import com.gonzalezcs.coviddatemvvm.R
import com.gonzalezcs.coviddatemvvm.data.AppDatabase
import com.gonzalezcs.coviddatemvvm.data.model.DataCovidModel
import com.gonzalezcs.coviddatemvvm.data.model.FormatCalendarObject
import com.gonzalezcs.coviddatemvvm.databinding.ActivityMainBinding
import com.gonzalezcs.coviddatemvvm.ui.utils.StateView
import com.gonzalezcs.coviddatemvvm.ui.viewmodel.CovidDateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainActivity : AppCompatActivity(){
    // You want Dagger to provide an instance of LoginViewModel from the graph
    @Inject lateinit var covidDateViewModel: CovidDateViewModel
    private lateinit var binding: ActivityMainBinding

    private val calendarInstance = ValueFormatClass().getCalendarInstance(null)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        // Make Dagger instantiate @Inject fields in MainnActivity
        (applicationContext as MyAppApplication).applicationComponent.inject(this)
        // Now is available viewModel

        //setting databinding with activity xml
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.formatcalendarobject = FormatCalendarObject(
            calendarInstance.year,
            calendarInstance.month,
            calendarInstance.day,
            ""
        )

        //contains all the observers in the activity
        observersActivity()
        binding.tvFecha.text = calendarInstance.stringDate

        //loading visible until first load (day-1)
        covidDateViewModel.getCovidByDate(ValueFormatClass().setCalendarFormat(
            calendarInstance.year,
            calendarInstance.month,
            calendarInstance.day)
        )

        binding.btnDate.setOnClickListener {
            val datepicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, y, m, d ->
                covidDateViewModel.getCovidByDate(ValueFormatClass().setCalendarFormat(y,m,d))
                calendarInstance.year = y
                calendarInstance.month = m
                calendarInstance.day = d
            }, calendarInstance.year, calendarInstance.month, calendarInstance.day)
            datepicker.show()
        }
    }

    //this function groups all observers in the activity
    private fun observersActivity(){
        covidDateViewModel.covidStateViewLiveData.observe(this, androidx.lifecycle.Observer {
            when (it){
                is StateView.Error -> {
                    AnimationViewClass().setViewAnimationVisibility(
                        binding.appLoading,
                        View.GONE,
                        0.0f ,
                        400)
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
                is StateView.Loading -> {
                    AnimationViewClass().setViewAnimationVisibility(
                        binding.appLoading,
                        it.visibility,
                        if(it.visibility==View.GONE) 0.0f else 1.0f,
                        400)
                }
                is StateView.Success -> {
                    AnimationViewClass().setViewAnimationVisibility(
                        binding.appLoading,
                        View.GONE,
                        0.0f ,
                        400)

                    bindingCovidElements(it.data)
                    bindingFormatCalendar(FormatCalendarObject(calendarInstance.year,calendarInstance.month,calendarInstance.day,""))
                }
            }
        })
    }

    //binding coviddatemodel to view
    private fun bindingCovidElements(covidDateModel:DataCovidModel?){
        covidDateModel?.let {
            binding.datacovidmodel = it
        }?: run {
            Toast.makeText(this@MainActivity,getString(R.string.info_not_available), Toast.LENGTH_LONG)
        }
    }

    //binding formatcalendar to view
    private fun bindingFormatCalendar(formatCalendarObject:FormatCalendarObject?){

        formatCalendarObject?.let {
            binding.formatcalendarobject = it
        }
    }
}