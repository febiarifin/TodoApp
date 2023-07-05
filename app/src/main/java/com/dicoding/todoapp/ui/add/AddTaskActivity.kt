package com.dicoding.todoapp.ui.add

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.ui.list.TaskActivity
import com.dicoding.todoapp.utils.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {

    private var dueDateMillis: Long = System.currentTimeMillis()
    private lateinit var addTaskViewModel: AddTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        supportActionBar?.title = getString(R.string.add_task)

        val factory = ViewModelFactory.getInstance(this)
        addTaskViewModel = ViewModelProvider(this, factory).get(AddTaskViewModel::class.java)

        addTaskViewModel.isSucess.observe(this,{
            if (it == true){
                startActivity(Intent(this, TaskActivity::class.java))
                finish()
            }
        })

        addTaskViewModel.message.observe(this, {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                //TODO 12 : Create AddTaskViewModel and insert new task to database
                val addEdTitle: EditText = findViewById(R.id.add_ed_title)
                val addEdDescription: EditText = findViewById(R.id.add_ed_description)
                val addTvDueDate: TextView = findViewById(R.id.add_tv_due_date)

                var title = addEdTitle.text.toString()
                var description = addEdDescription.text.toString()
                var dueDate = addTvDueDate.text.toString()

                if (title.isNotEmpty() && description.isNotEmpty() && dueDate != "Due date") {
                    val newTask = Task(0,title, description, dueDateMillis, false)
                    addTaskViewModel.insertNewTask(newTask)
                }else if (title.isEmpty() && description.isEmpty() && dueDate == "Due date") {
                    Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
                } else if (title.isEmpty()) {
                    addEdTitle.setError("Title can't be empty")
                } else if (description.isEmpty()) {
                    addEdDescription.setError("Description can't be empty")
                } else if(dueDate == "Due date"){
                    addTvDueDate.setError("Haven't set a date yet")
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showDatePicker(view: View) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.add_tv_due_date).text = dateFormat.format(calendar.time)

        dueDateMillis = calendar.timeInMillis
    }
}