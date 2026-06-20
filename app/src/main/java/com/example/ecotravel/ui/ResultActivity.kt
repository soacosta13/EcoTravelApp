package com.example.ecotravel.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecotravel.R
import com.example.ecotravel.databinding.ActivityResultBinding
import com.example.ecotravel.model.TravelRecord
import com.example.ecotravel.viewmodel.TravelViewModel
import android.content.Intent

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var record: TravelRecord

    override fun onCreate(savedInstanceState: Bundle?) {
        //aplica el color del transporte seleccionado
        val record = intent.getSerializableExtra("travel_record") as TravelRecord
        applyTransportTheme(record.transport)

        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayResult(record)
        setupSaveButton()

    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            finish() // Vuelve a MainActivity con el viaje ya guardado en el ViewModel
        }
    }

    /**
     * trae datos del travel record
     */
    private fun displayResult(record: TravelRecord) {
        val factor = TravelViewModel.getFactorForTransport(record.transport)

        binding.tvDestination.text = getString(R.string.label_destination, record.destination)
        binding.tvTransportFactor.text = getString(
            R.string.label_transport_factor,
            record.transport,
            factor
        )
        binding.tvCo2.text = getString(R.string.label_co2, record.co2Kg)

        if (record.isBusiness) {
            binding.tvBusiness.visibility = android.view.View.VISIBLE
        }

        // Set el icon de transporte
        val iconRes = when (record.transport) {
            TravelViewModel.TRANSPORT_PLANE -> android.R.drawable.ic_menu_upload
            TravelViewModel.TRANSPORT_CAR   -> android.R.drawable.ic_menu_directions
            TravelViewModel.TRANSPORT_TRAIN -> android.R.drawable.ic_menu_sort_by_size
            else -> android.R.drawable.ic_menu_directions
        }
        binding.ivTransportIcon.setImageResource(iconRes)
    }

    /**
     * aplica el color del transporte
     */
    private fun applyTransportTheme(transport: String) {
        val themeRes = when (transport) {
            TravelViewModel.TRANSPORT_PLANE -> R.style.Theme_EcoTravel_Plane
            TravelViewModel.TRANSPORT_CAR   -> R.style.Theme_EcoTravel_Car
            TravelViewModel.TRANSPORT_TRAIN -> R.style.Theme_EcoTravel_Train
            else -> R.style.Theme_EcoTravel
        }
        setTheme(themeRes)
    }
}