package com.example.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var cdTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val start:Button=findViewById(R.id.start)
        val stop:Button=findViewById(R.id.stopButton)


        start.setOnClickListener {
            startTimer()
        }
        stop.setOnClickListener {
            stoptimer()
        }
    }


     private fun startTimer() {
        val time = 5 * 60 * 1000L
        println("TimerStart")
        cdTimer = object : CountDownTimer(time, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                println("CountDown")

                var minutes = millisUntilFinished / 60 / 1000
                var seconds = millisUntilFinished / 1000 % 60

                println("$minutes:$seconds")

                val timeView:TextView=findViewById(R.id.timerView)
                timeView.text = "$minutes:$seconds"

            }

            override fun onFinish() {
                println("Finish")

            }

        }.start()
    }

     private fun stoptimer() {
        cdTimer?.cancel()
        println("STOP")
    }


}
