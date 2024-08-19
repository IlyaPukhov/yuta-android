package com.yuta.common.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtils {

    private val EUROPEAN_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun formatToIso(date: String): String {
        val localDate = LocalDate.parse(date, EUROPEAN_FORMATTER)
        return localDate.format(ISO_FORMATTER)
    }

    fun formatToEuropean(date: String): String {
        val localDate = LocalDate.parse(date, ISO_FORMATTER)
        return localDate.format(EUROPEAN_FORMATTER)
    }
}
