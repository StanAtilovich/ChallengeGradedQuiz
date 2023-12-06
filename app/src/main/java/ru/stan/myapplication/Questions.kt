package ru.stan.myapplication

import androidx.annotation.StringRes

data class Questions(
    @StringRes val textRes: Int,
    val answer: Boolean,
    var userAnswer: Boolean?
)