package com.example.uniplanner

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Setup Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Setup Drawer Layout
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        // Setup ActionBarDrawerToggle
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Handle Drawer Menu Clicks
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    auth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_clear_all -> {
                    clearAllTasks()
                    true
                }
                else -> false
            }
        }

        // Setup Floating Action Button
        val fabAddTask: FloatingActionButton = findViewById(R.id.fab_add_task)
        fabAddTask.setOnClickListener {
            showAddTaskDialog()
        }

        // Setup RecyclerView for Task Cards
        taskRecyclerView = findViewById(R.id.recyclerViewTasks)
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(emptyList(), ::onDeleteTask)
        taskRecyclerView.adapter = taskAdapter

        fetchTasks()
    }

    private fun fetchTasks() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val tasksRef = database.child("tasks").child(currentUser.uid)
            tasksRef.get().addOnSuccessListener { snapshot ->
                val tasks = snapshot.children.mapNotNull { it.getValue(Task::class.java) }
                taskAdapter.updateTasks(tasks)
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to fetch tasks", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAddTaskDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_task, null)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Task")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val taskTitle = dialogView.findViewById<EditText>(R.id.editTextTaskTitle).text.toString()
                val taskDetails = dialogView.findViewById<EditText>(R.id.editTextTaskDetails).text.toString()
                val dueDate = dialogView.findViewById<TextView>(R.id.textViewDueDate).text.toString()
                addTaskToFirebase(taskTitle, taskDetails, dueDate)
            }
            .setNegativeButton("Cancel", null)
            .create()

        // Set up a date picker for due date
        dialogView.findViewById<TextView>(R.id.textViewDueDate).setOnClickListener {
            showDatePickerDialog(dialogView.findViewById(R.id.textViewDueDate))
        }

        dialog.show()
    }

    private fun addTaskToFirebase(title: String, details: String, dueDate: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val taskId = database.child("tasks").child(currentUser.uid).push().key
            val task = Task(id = taskId, title = title, details = details, dueDate = dueDate)
            taskId?.let {
                database.child("tasks").child(currentUser.uid).child(it).setValue(task)
                fetchTasks()
            }
        }
    }

    private fun onDeleteTask(taskId: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            database.child("tasks").child(currentUser.uid).child(taskId).removeValue()
            fetchTasks()
        }
    }

    private fun clearAllTasks() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            database.child("tasks").child(currentUser.uid).removeValue()
            fetchTasks()
        }
    }

    private fun showDatePickerDialog(dueDateTextView: TextView) {
        calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                dueDateTextView.text = dateFormat.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}
