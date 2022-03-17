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
import com.gonzalezcs.coviddatemvvm.databinding.ActivityMainBinding
import com.gonzalezcs.coviddatemvvm.ui.utils.StateView
import com.gonzalezcs.coviddatemvvm.ui.viewmodel.CovidDateViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(){


    // You want Dagger to provide an instance of LoginViewModel from the graph
    @Inject lateinit var covidDateViewModel: CovidDateViewModel

    private lateinit var binding: ActivityMainBinding


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

        val calendarInstance = ValueFormatClass().getCalendarInstance()
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
                /*
                val calen : Calendar = Calendar.getInstance()
                calen.set(y,m,d)
                setFechaText(calen)
                 */
                calendarInstance.year = y
                calendarInstance.month = m
                calendarInstance.day = d
            }, calendarInstance.year, calendarInstance.month, calendarInstance.day)
            datepicker.show()

            //cancel button loadingBackground hide
            datepicker.setOnCancelListener(DialogInterface.OnCancelListener {
                covidDateViewModel.appLoadingLiveData.postValue(View.GONE)
            })

        }
    }

    private fun observersActivity(){
        covidDateViewModel.covidStateViewLiveData.observe(this, androidx.lifecycle.Observer {
            when (it){
                is StateView.Error -> {


                }
                is StateView.Loading -> {


                }
                is StateView.Success -> {

                }
            }
        })

        covidDateViewModel.appLoadingLiveData.observe(this) {
            AnimationViewClass().setViewAnimationVisibility(
                binding.appLoading.layout,
                it,
                if(it==View.GONE) 0.0f else 1.0f,
                400)
        }
        //change the values of the textViews
        covidDateViewModel.covidDateLiveData.observe(this, androidx.lifecycle.Observer { covidDateModel ->

            covidDateViewModel.appLoadingLiveData.postValue(View.GONE)
            covidDateModel?.let {

                binding.tvFecha.visibility = View.GONE
                binding.tvCasosConfirmados.visibility = View.GONE
                binding.tvCantidadFallecidos.visibility = View.GONE

                binding.tvFecha.visibility = View.VISIBLE
                binding.tvFecha.animation = AnimationUtils.loadAnimation(this, R.anim.item_animation_fall_down)

                binding.tvCasosConfirmados.text = String.format("%s: %s",getString(R.string.casos_confirmados),it.confirmed.toString())
                binding.tvCasosConfirmados.visibility = View.VISIBLE
                binding.tvCasosConfirmados.animation = AnimationUtils.loadAnimation(this, R.anim.item_animation_fall_down)

                binding.tvCantidadFallecidos.text =String.format("%s: %s",getString(R.string.cantidad_de_personas_fallecidas),it.deaths.toString())
                binding.tvCantidadFallecidos.visibility = View.VISIBLE
                binding.tvCantidadFallecidos.animation = AnimationUtils.loadAnimation(this, R.anim.item_animation_fall_down)

            }?: run {
                Toast.makeText(this@MainActivity,"Fecha sin datos", Toast.LENGTH_LONG)
            }

        })
    }
}