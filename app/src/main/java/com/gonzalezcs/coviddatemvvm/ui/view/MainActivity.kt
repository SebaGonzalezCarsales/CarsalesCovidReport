package com.gonzalezcs.coviddatemvvm.ui.view

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.gonzalezcs.coviddatemvvm.MyAppApplication
import com.gonzalezcs.coviddatemvvm.ui.utils.AnimationViewClass
import com.gonzalezcs.coviddatemvvm.ui.utils.ValueFormatClass
import com.gonzalezcs.coviddatemvvm.R
import com.gonzalezcs.coviddatemvvm.data.model.DataCovidModel
import com.gonzalezcs.coviddatemvvm.databinding.ActivityMainBinding
import com.gonzalezcs.coviddatemvvm.ui.utils.StateView
import com.gonzalezcs.coviddatemvvm.ui.viewmodel.CovidDateViewModel
import java.util.*
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    private fun observersActivity(){
        covidDateViewModel.covidStateViewLiveData.observe(this, androidx.lifecycle.Observer {
            when (it){
                is StateView.Error -> {
                    AnimationViewClass().setViewAnimationVisibility(
                        binding.appLoading.layout,
                        View.GONE,
                        0.0f ,
                        400)
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
                is StateView.Loading -> {
                    AnimationViewClass().setViewAnimationVisibility(
                        binding.appLoading.layout,
                        it.visibility,
                        if(it.visibility==View.GONE) 0.0f else 1.0f,
                        400)
                }
                is StateView.Success -> {

                    AnimationViewClass().setViewAnimationVisibility(
                        binding.appLoading.layout,
                        View.GONE,
                        0.0f ,
                        400)

                    bindingCovidElements(it.data)
                }
            }
        })

    }

    private fun bindingCovidElements(covidDateModel:DataCovidModel?){
        covidDateModel?.let {

            binding.tvFecha.text = ValueFormatClass().getCalendarInstance(ValueFormatClass.FormatCalendarObject(calendarInstance.year,calendarInstance.month,calendarInstance.day,"")).stringDate

            binding.tvFecha.visibility = View.VISIBLE

            binding.tvCasosConfirmados.text = String.format("%s: %s",getString(R.string.casos_confirmados),it.confirmed.toString())
            binding.tvCasosConfirmados.visibility = View.VISIBLE

            binding.tvCantidadFallecidos.text =String.format("%s: %s",getString(R.string.cantidad_de_personas_fallecidas),it.deaths.toString())
            binding.tvCantidadFallecidos.visibility = View.VISIBLE

            binding.imgBlack1.visibility = View.VISIBLE
            binding.imgBlack2.visibility = View.VISIBLE
            binding.btnDate.visibility = View.VISIBLE
            binding.imageView3.visibility = View.VISIBLE


        }?: run {
            Toast.makeText(this@MainActivity,"Fecha sin datos", Toast.LENGTH_LONG)
        }
    }
}