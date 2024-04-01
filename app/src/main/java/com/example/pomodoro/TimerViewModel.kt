package com.example.pomodoro


import android.os.CountDownTimer
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class TimerViewModel() : ViewModel() {

    val _uiState = MutableStateFlow(pomodoroUiState())
    var uiState: StateFlow<pomodoroUiState> = _uiState.asStateFlow()
    val timeState: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private var cdTimer: CountDownTimer? = null


    fun setTimer(time: Int) {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(pomodoro = time)
    }

     fun startTimer( ) {

        val time = _uiState.value.pomodoro * 60 * 1000L
        println("TimerStart")

        cdTimer = object : CountDownTimer(time, 1000) {

            override fun onTick(millisUntilFinished: Long) {


                var minutes = millisUntilFinished / 60 / 1000
                var seconds = millisUntilFinished / 1000 % 60
                println("$minutes:$seconds")

                val timertime = String.format("%02d:%02d", minutes, seconds)

                _uiState.value = pomodoroUiState(time = timertime)
                timeState.postValue(timertime)


            }


            override fun onFinish() {
                println("Finish")
                val message = ("Aika on loppunut")
                _uiState.value.timerOn = false

            }

        }.start()
    }

    fun stoptimer() {
        cdTimer?.cancel()
        println("STOP")
    }




}