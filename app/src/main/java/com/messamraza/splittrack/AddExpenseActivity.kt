package com.messamraza.splittrack

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.messamraza.splittrack.database.AppDatabase
import com.messamraza.splittrack.database.Expense
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var amountEditText: TextInputEditText
    private lateinit var categorySpinner: AutoCompleteTextView
    private lateinit var dateEditText: TextInputEditText
    private lateinit var notesEditText: TextInputEditText
    private lateinit var titleText: TextView
    private lateinit var saveButton: Button

    private var selectedDate = Date()
    private var isEditMode = false
    private var expenseId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        database = AppDatabase.getDatabase(this)
        checkEditMode()
        setupViews()
    }

    private fun checkEditMode() {
        isEditMode = intent.getBooleanExtra("IS_EDIT_MODE", false)
        if (isEditMode) {
            expenseId = intent.getLongExtra("EXPENSE_ID", 0)
        }
    }

    private fun setupViews() {
        amountEditText = findViewById(R.id.amountEditText)
        categorySpinner = findViewById(R.id.categorySpinner)
        dateEditText = findViewById(R.id.dateEditText)
        notesEditText = findViewById(R.id.notesEditText)
        titleText = findViewById(R.id.titleText)
        saveButton = findViewById(R.id.saveButton)

        // Update UI for edit mode
        if (isEditMode) {
            titleText.text = "Edit Expense"
            saveButton.text = "Update"
            loadExpenseData()
        } else {
            titleText.text = getString(R.string.add_expense)
            saveButton.text = getString(R.string.save)
        }

        setupCategorySpinner()
        setupDatePicker()
        setupButtons()

        // Set current date as default for new expenses
        if (!isEditMode) {
            updateDateText()
        }
    }

    private fun loadExpenseData() {
        val amount = intent.getDoubleExtra("EXPENSE_AMOUNT", 0.0)
        val category = intent.getStringExtra("EXPENSE_CATEGORY") ?: ""
        val dateMillis = intent.getLongExtra("EXPENSE_DATE", System.currentTimeMillis())
        val notes = intent.getStringExtra("EXPENSE_NOTES") ?: ""

        amountEditText.setText(amount.toString())
        categorySpinner.setText(category, false)
        selectedDate = Date(dateMillis)
        notesEditText.setText(notes)
        updateDateText()
    }

    private fun setupCategorySpinner() {
        val categories = arrayOf("Food", "Travel", "Shopping", "Entertainment", "Utilities", "Other")
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, categories)
        categorySpinner.setAdapter(adapter)

        if (!isEditMode) {
            categorySpinner.setText(categories[0], false)
        }

        categorySpinner.setOnClickListener {
            categorySpinner.showDropDown()
        }

        categorySpinner.keyListener = null
    }

    private fun setupDatePicker() {
        dateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = selectedDate

            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedDate = calendar.time
                    updateDateText()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateText() {
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        dateEditText.setText(formatter.format(selectedDate))
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.cancelButton).setOnClickListener {
            finish()
        }

        saveButton.setOnClickListener {
            if (isEditMode) {
                updateExpense()
            } else {
                saveExpense()
            }
        }
    }

    private fun saveExpense() {
        val expense = createExpenseFromInput() ?: return

        lifecycleScope.launch {
            database.expenseDao().insertExpense(expense)
            runOnUiThread {
                Toast.makeText(this@AddExpenseActivity, "Expense saved!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun updateExpense() {
        val expense = createExpenseFromInput() ?: return
        val updatedExpense = expense.copy(id = expenseId)

        lifecycleScope.launch {
            database.expenseDao().updateExpense(updatedExpense)
            runOnUiThread {
                Toast.makeText(this@AddExpenseActivity, "Expense updated!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun createExpenseFromInput(): Expense? {
        val amountText = amountEditText.text.toString().trim()
        val category = categorySpinner.text.toString().trim()
        val notes = notesEditText.text.toString().trim()

        if (amountText.isBlank()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
            return null
        }

        if (category.isBlank()) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show()
            return null
        }

        val amount = amountText.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return null
        }

        return Expense(
            amount = amount,
            category = category,
            date = selectedDate,
            notes = notes
        )
    }
}
