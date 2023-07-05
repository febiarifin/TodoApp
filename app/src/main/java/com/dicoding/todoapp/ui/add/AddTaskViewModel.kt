package com.dicoding.todoapp.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import kotlinx.coroutines.launch

class AddTaskViewModel(private val taskRepository: TaskRepository): ViewModel() {

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _isSucess = MutableLiveData<Boolean?>()
    val isSucess: LiveData<Boolean?> = _isSucess

    fun insertNewTask(newTask: Task) = viewModelScope.launch {
        taskRepository.insertTask(newTask)
        _isSucess.value = true
        _message.value = "Task added successfully"
    }
}