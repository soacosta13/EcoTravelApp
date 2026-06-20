package com.example.ecotravel.model

import java.io.Serializable

data class TravelRecord(
    val id: Long = System.currentTimeMillis(),
    val destination: String,
    val distanceKm: Double,
    val transport: String,
    val isBusiness: Boolean,
    val co2Kg: Double
) : Serializable
