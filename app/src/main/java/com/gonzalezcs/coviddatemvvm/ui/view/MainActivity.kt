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
import androidx.lifecycle.ViewModelProvider
import com.gonzalezcs.coviddatemvvm.MyAppApplication
import com.gonzalezcs.coviddatemvvm.OwnFunctions
import com.gonzalezcs.coviddatemvvm.R
import com.gonzalezcs.coviddatemvvm.databinding.ActivityMainBinding
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

        //first date oncreate
        val calendar = Calendar.getInstance()

        //get year,month and day
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)-1

        calendar.add(Calendar.DAY_OF_MONTH,-1)

        setFechaText(calendar)

        //loading visible until first load (day-1)
        covidDateViewModel.appLoadingLiveData.postValue(View.VISIBLE)
        covidDateViewModel.getCovidByDate(OwnFunctions().fixDateCalendar(year,month,day))

        binding.btnDate.setOnClickListener {
            covidDateViewModel.appLoadingLiveData.postValue(View.VISIBLE)
            val datepicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, y, m, d ->
                covidDateViewModel.appLoadingLiveData.postValue(View.VISIBLE)
                covidDateViewModel.getCovidByDate(OwnFunctions().fixDateCalendar(y,m,d))
                val calen : Calendar = Calendar.getInstance()
                calen.set(y,m,d)
                setFechaText(calen)
                year = y
                month = m
                day = d
            }, year, month, day)
            datepicker.show()

            //cancel button loadingBackground hide
            datepicker.setOnCancelListener(DialogInterface.OnCancelListener {
                covidDateViewModel.appLoadingLiveData.postValue(View.GONE)
            })

        }
    }

    fun setFechaText(calendar:Calendar){
        val simpleDate = SimpleDateFormat("d 'de' MMMM 'del' yyyy")
        binding.tvFecha.text = simpleDate.format(calendar.time)

    }

    private fun observersActivity(){
        //covidDateViewModel = ViewModelProvider(this)[CovidDateViewModel::class.java]

        //change the visibility of the appLoading
        covidDateViewModel.appLoadingLiveData.observe(this) {
            val alpha = if(it==View.GONE) 0.0f else 1.0f
            OwnFunctions().animationVisibleGone(binding.appLoading.layout,it,alpha,400)
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