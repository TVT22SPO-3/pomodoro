package com.example.pomodoro

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pomodoro.databinding.ActivityMainBinding
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlin.concurrent.timer
import androidx.lifecycle.Observer
import kotlinx.coroutines.flow.collect



class MainActivity : AppCompatActivity(), TaskItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var viewModel: TimerViewModel
    private var timerOn = false
    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(this)
        checkNotificationPermission()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(TimerViewModel::class.java)
        val start: Button = findViewById(R.id.start)
        val stop: Button = findViewById(R.id.stopButton)
        val pomodoro: Button = findViewById(R.id.pomodoro)
        val sbreak: Button = findViewById(R.id.Sbreak)
        val lbreak: Button = findViewById(R.id.Lbreak)
        val timerView: TextView = findViewById(R.id.timerView)

        viewModel.timeState.observe(this, Observer{
                value  ->
            timerView.text = value.toString()
            if(value == "00:00"){
                sendNotification(this@MainActivity)

            }
        })

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


        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        binding.newTaskButton.setOnClickListener {
            NewTaskSheet(null).show(supportFragmentManager, "newTaskTag")
        }
        setRecyclerView()

    }
    private fun setRecyclerView() {
        val mainActivity = this
        taskViewModel.taskItems.observe(this){
            binding.todoListRecyclerView.apply{
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TaskItemAdapter(it, mainActivity)
            }
        }
    }

    override fun editTaskItem(taskItem: TaskItem) {
        NewTaskSheet(taskItem).show(supportFragmentManager,"newTaskTag")
    }

    override fun completeTaskItem(taskItem: TaskItem) {
        taskViewModel.setCompleted(taskItem)
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
