package com.messamraza.splittrack

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.messamraza.splittrack.database.AppDatabase
import com.messamraza.splittrack.database.GroupExpense
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddGroupExpenseActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var titleEditText: TextInputEditText
    private lateinit var amountEditText: TextInputEditText
    private lateinit var paidBySpinner: Spinner
    private lateinit var dateEditText: TextInputEditText
    private lateinit var forWhomContainer: LinearLayout

    private var groupId: Long = 0
    private var groupName: String = ""
    private var selectedDate = Date()
    private val membersList = mutableListOf<String>()
    private val selectedMembers = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group_expense)

        groupId = intent.getLongExtra("GROUP_ID", 0)
        groupName = intent.getStringExtra("GROUP_NAME") ?: ""

        database = AppDatabase.getDatabase(this)
        setupViews()
        loadGroupMembers()
    }

    private fun setupViews() {
        findViewById<TextView>(R.id.titleText).text = "Add Expense to $groupName"

        titleEditText = findViewById(R.id.expenseTitleEditText)
        amountEditText = findViewById(R.id.expenseAmountEditText)
        paidBySpinner = findViewById(R.id.paidBySpinner)
        dateEditText = findViewById(R.id.expenseDateEditText)
        forWhomContainer = findViewById(R.id.forWhomContainer)

        setupDatePicker()
        setupButtons()
        updateDateText()
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

        findViewById<Button>(R.id.saveExpenseButton).setOnClickListener {
            saveGroupExpense()
        }
    }

    private fun loadGroupMembers() {
        lifecycleScope.launch {
            val members = database.groupDao().getGroupMembers(groupId)
            membersList.clear()
            membersList.addAll(members.map { it.name })

            runOnUiThread {
                setupPaidBySpinner()
                setupForWhomCheckboxes()
            }
        }
    }

    private fun setupPaidBySpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, membersList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        paidBySpinner.adapter = adapter
    }

    private fun setupForWhomCheckboxes() {
        forWhomContainer.removeAllViews()

        membersList.forEach { member ->
            val checkBox = CheckBox(this)
            checkBox.text = member
            checkBox.isChecked = true
            selectedMembers.add(member)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedMembers.add(member)
                } else {
                    selectedMembers.remove(member)
                }
            }

            forWhomContainer.addView(checkBox)
        }
    }

    private fun saveGroupExpense() {
        val title = titleEditText.text.toString().trim()
        val amountText = amountEditText.text.toString()
        val paidBy = paidBySpinner.selectedItem?.toString() ?: ""

        if (title.isBlank() || amountText.isBlank() || paidBy.isBlank()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedMembers.isEmpty()) {
            Toast.makeText(this, "Please select at least one member", Toast.LENGTH_SHORT).show()
            return
        }

        val forWhom = selectedMembers.joinToString(",")

        val groupExpense = GroupExpense(
            groupId = groupId,
            title = title,
            amount = amount,
            paidBy = paidBy,
            forWhom = forWhom,
            date = selectedDate
        )

        lifecycleScope.launch {
            database.groupDao().insertGroupExpense(groupExpense)
            runOnUiThread {
                Toast.makeText(this@AddGroupExpenseActivity, "Expense saved!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
