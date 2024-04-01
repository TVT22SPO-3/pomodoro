package com.example.pomodoro

data class pomodoroUiState(
    val status: String = "",
    val time: String = "",
    var pomodoro: Int = 0,
    //val sbreak: Int = 1,
    //val lbreak: Int = 1,
    var timerOn: Boolean = false
)
