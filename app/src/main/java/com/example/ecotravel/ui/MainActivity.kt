package com.example.ecotravel.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.ecotravel.R
import com.example.ecotravel.databinding.ActivityMainBinding
import com.example.ecotravel.utils.PreferencesManager
import com.example.ecotravel.viewmodel.TravelViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TravelViewModel by viewModels()
    private lateinit var prefs: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = PreferencesManager(this)

        loadStats()
        setupDarkModeSwitch()
        setupCalculateButton()
        setupHistoryButton()


    }

    /**
     * trae distancia total y ultimo destino desde shared preferences y lo muestra en la tarjeta
     */
    private fun loadStats() {
        val totalDistance = prefs.getTotalDistance()
        binding.tvTotalDistance.text = getString(R.string.label_total_distance, totalDistance.toFloat())

        val lastDestination = prefs.getLastDestination()
        if (lastDestination.isNotEmpty()) {
            binding.tvLastDestination.text = getString(R.string.label_last_destination, lastDestination)
        } else {
            binding.tvLastDestination.text = getString(R.string.label_last_destination, "-")
        }
    }

    /**
     * modo guardado (oscuro o claro)
     */
    private fun setupDarkModeSwitch() {
        binding.switchDarkMode.isChecked = prefs.isDarkModeEnabled()

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.setDarkModeEnabled(isChecked)
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    /**
     * valida y pasa al activityresult si no hay errores
     */
    private fun setupCalculateButton() {
        binding.btnCalculate.setOnClickListener {
            if (validateInputs()) {
                navigateToResult()
            }
        }
    }

    private fun setupHistoryButton() {
        binding.btnHistory.setOnClickListener {
            val travels = viewModel.travels.value ?: emptyList()
            if (travels.isEmpty()) {
                // Opcional: mostrar un mensaje si no hay viajes
                return@setOnClickListener
            }
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra("travels", ArrayList(travels))
            startActivity(intent)
        }
    }

    /**
     * valida los puntos anteriores
     */
    private fun validateInputs(): Boolean {
        var isValid = true

        binding.tilDestination.error = null
        binding.tilDistance.error = null

        val destination = binding.etDestination.text.toString().trim()
        if (destination.isEmpty()) {
            binding.tilDestination.error = getString(R.string.error_empty_destination)
            isValid = false
        }

        val distanceText = binding.etDistance.text.toString().trim()
        val distance = distanceText.toDoubleOrNull()
        if (distance == null || distance <= 0) {
            binding.tilDistance.error = getString(R.string.error_invalid_distance)
            isValid = false
        }

        return isValid
    }

    /**
     * transporte seleccionado
     */
    private fun navigateToResult() {
        val destination = binding.etDestination.text.toString().trim()
        val distance = binding.etDistance.text.toString().trim().toDouble()
        val isBusiness = binding.cbBusiness.isChecked

        val transport = when (binding.rgTransport.checkedRadioButtonId) {
            R.id.rbPlane -> TravelViewModel.TRANSPORT_PLANE
            R.id.rbCar   -> TravelViewModel.TRANSPORT_CAR
            R.id.rbTrain -> TravelViewModel.TRANSPORT_TRAIN
            else -> TravelViewModel.TRANSPORT_CAR
        }

        val record = viewModel.addTravel(destination, distance, transport, isBusiness)

        // guarda en shared preferences
        prefs.addToTotalDistance(distance)
        prefs.saveLastDestination(destination)

        // pasa al result activity
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("travel_record", record)
        startActivity(intent)
    }



    override fun onResume() {
        super.onResume()
        loadStats()
    }

}