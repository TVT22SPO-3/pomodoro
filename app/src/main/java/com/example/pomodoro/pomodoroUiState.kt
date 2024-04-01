package com.example.pomodoro

data class pomodoroUiState(
    val time: String = "",
    var pomodoro: Int = 1,
    val sbreak: Int = 1,
    val lbreak: Int = 1,
    var timerOn: Boolean = false
)
