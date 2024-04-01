package com.example.pomodoro

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TimerViewModel
    private var timerOn = false

    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // permission granted
        } else {
            // permission denied or forever denied
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(this)
        checkNotificationPermission()
        viewModel = ViewModelProvider(this).get(TimerViewModel::class.java)
        setContentView(R.layout.activity_main)
        val start: Button = findViewById(R.id.start)
        val stop: Button = findViewById(R.id.stopButton)
        val sendNotification: Button = findViewById(R.id.notifButton)
        val pomodoro: Button = findViewById(R.id.pomodoro)
        val sbreak: Button = findViewById(R.id.Sbreak)
        val lbreak: Button = findViewById(R.id.Lbreak)
        val timerView: TextView = findViewById(R.id.timerView)

        lifecycleScope.launch {

            viewModel.uiState.collect { pomodoroUiState ->
                timerView.text = pomodoroUiState.time
                println(pomodoroUiState.time)
                if(pomodoroUiState.time == "00:00") {
                    sendNotification(this@MainActivity)
                }
            }

        }

        start.setOnClickListener {
            if (!viewModel.uiState.value.timerOn) {
                viewModel.uiState.value.timerOn = true
                viewModel.startTimer()
            }
        }
        stop.setOnClickListener {
            viewModel.stoptimer()
            viewModel.uiState.value.timerOn = false
        }

        pomodoro.setOnClickListener {
        val pomodoroTime = 25
        viewModel.setTimer(25)
        }

        sbreak.setOnClickListener {
            val pomodoroTime = 5
            viewModel.setTimer(5)
        }

        lbreak.setOnClickListener {
            val pomodoroTime = 30
            viewModel.setTimer(30)
        }

        sendNotification.setOnClickListener {
            sendNotification(this)
        }

    }




    private fun checkNotificationPermission() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            println("permission granted")
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            ) {
            } else {
                // first request or forever denied case
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}


