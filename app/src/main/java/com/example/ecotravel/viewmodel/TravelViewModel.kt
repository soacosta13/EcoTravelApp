package com.example.ecotravel.viewmodel

import android.graphics.pdf.content.PdfPageGotoLinkContent
import android.media.Image
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecotravel.model.TravelRecord

class TravelViewModel : ViewModel() {
    //constantes
    companion object {
        const val TRANSPORT_PLANE = "Avión"
        const val TRANSPORT_CAR = "Auto"
        const val TRANSPORT_TRAIN = "Tren"

        private const val FACTOR_PLANE = 0.255
        private const val FACTOR_CAR = 0.171
        private const val FACTOR_TRAIN = 0.041

        private const val BUSINESS_EXTRA = 1.15

        fun getFactorForTransport(transport: String): Double {
            return when (transport) {
                TRANSPORT_PLANE -> FACTOR_PLANE
                TRANSPORT_CAR   -> FACTOR_CAR
                TRANSPORT_TRAIN -> FACTOR_TRAIN
                else            -> 0.0
            }
        }
    }

    //livedata

    // lista privada y mutable
    private val _travels = MutableLiveData<List<TravelRecord>>(emptyList())

    // lista publica, solo lectura
    val travels: LiveData<List<TravelRecord>> get() = _travels

    //funciones publicas
    /**
     * Calcula emision de co2 y agrega un viaje nuevo
     * retorna viaje creado
     */
    fun addTravel (
        destination: String,
        distanceKm: Double,
        transport: String,
        isBusiness: Boolean
    ) : TravelRecord{
        val co2 = calculateCo2(distanceKm, transport, isBusiness)

        val record = TravelRecord(
            destination = destination,
            distanceKm = distanceKm,
            transport = transport,
            isBusiness = isBusiness,
            co2Kg = co2
        )

        val currentList = _travels.value.orEmpty().toMutableList()
        currentList.add(record)
        _travels.value = currentList

        return record
    }

    /**
     * elimina un viaje de la lista por el id
     */
    fun removeTravel(record: TravelRecord) {
        val currentList = _travels.value.orEmpty().toMutableList()
        currentList.removeIf { it.id == record.id }
        _travels.value = currentList
    }

    /**
     * retorna el co2 emitido segun transporte
     */


    //funciones privadas
    /**
     * calculo de co2
     */
    private fun calculateCo2(
        distanceKm: Double,
        transport: String,
        isBusiness: Boolean
    ): Double {
        val factor = getFactorForTransport(transport)
        val base = distanceKm * factor
        return if (isBusiness) base * BUSINESS_EXTRA else base
    }

}