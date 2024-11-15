package store.data

import java.time.LocalDate

data class Promotion(
    val name: String,
    val buy: Int,
    val get: Int,
    val startDate: LocalDate,
    val endDate: LocalDate
)
