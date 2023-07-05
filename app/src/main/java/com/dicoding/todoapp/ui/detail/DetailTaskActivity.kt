package com.dicoding.todoapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.ui.list.TaskActivity
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var detailTaskViewModel: DetailTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //TODO 11 : Show detail task and implement delete action
        val factory = ViewModelFactory.getInstance(this)
        detailTaskViewModel = ViewModelProvider(this, factory).get(DetailTaskViewModel::class.java)

        val taskId = intent.getIntExtra(TASK_ID,0)
        detailTaskViewModel.setTaskId(taskId)

        detailTaskViewModel.task.observe(this, {
            if (it != null){
                val detailEdTitle: EditText = findViewById(R.id.detail_ed_title)
                detailEdTitle.setText(it.title)

                val detailEdDescription: EditText = findViewById(R.id.detail_ed_description)
                detailEdDescription.setText(it.description)

                val detailEdDueDate: EditText = findViewById(R.id.detail_ed_due_date)
                detailEdDueDate.setText(DateConverter.convertMillisToString(it.dueDateMillis))
            }
        })

        val btnDeleteTask: Button = findViewById(R.id.btn_delete_task)
        btnDeleteTask.setOnClickListener {
            detailTaskViewModel.deleteTask()
            Toast.makeText(this, "Task deleted successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, TaskActivity::class.java))
            finish()
        }
    }
}