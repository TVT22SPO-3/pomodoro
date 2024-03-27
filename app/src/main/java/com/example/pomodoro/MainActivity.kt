package com.example.pomodoro

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    private var cdTimer: CountDownTimer? = null

    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted){
            // permission granted
        } else {
            // permission denied or forever denied
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(this)
        checkNotificationPermission()

        setContentView(R.layout.activity_main)
        val start:Button=findViewById(R.id.start)
        val stop:Button=findViewById(R.id.stopButton)
        val sendNotification:Button=findViewById(R.id.notifButton)

        start.setOnClickListener {
            startTimer()
        }
        stop.setOnClickListener {
            stoptimer()
        }

        sendNotification.setOnClickListener{
            sendNotification(this)
        }
        
    }

    private fun checkNotificationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            // permission granted
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                // show rationale and then launch launcher to request permission
            } else {
                // first request or forever denied case
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
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
