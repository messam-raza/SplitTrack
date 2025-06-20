package com.messamraza.splittrack

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.messamraza.splittrack.database.AppDatabase
import com.messamraza.splittrack.database.Expense
import kotlinx.coroutines.launch

class PersonalExpensesActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var expensesAdapter: ExpensesAdapter
    private lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_expenses)

        database = AppDatabase.getDatabase(this)
        setupViews()
        loadExpenses()
    }

    private fun setupViews() {
        pieChart = findViewById(R.id.pieChart)
        setupPieChart()

        val recyclerView = findViewById<RecyclerView>(R.id.expensesRecyclerView)
        expensesAdapter = ExpensesAdapter(
            onExpenseClick = { expense -> editExpense(expense) },
            onExpenseLongClick = { expense -> showDeleteDialog(expense) }
        )
        recyclerView.adapter = expensesAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<FloatingActionButton>(R.id.fabAddExpense).setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }
    }

    private fun editExpense(expense: Expense) {
        val intent = Intent(this, AddExpenseActivity::class.java)
        intent.putExtra("EXPENSE_ID", expense.id)
        intent.putExtra("EXPENSE_AMOUNT", expense.amount)
        intent.putExtra("EXPENSE_CATEGORY", expense.category)
        intent.putExtra("EXPENSE_DATE", expense.date.time)
        intent.putExtra("EXPENSE_NOTES", expense.notes)
        intent.putExtra("IS_EDIT_MODE", true)
        startActivity(intent)
    }

    private fun showDeleteDialog(expense: Expense) {
        AlertDialog.Builder(this)
            .setTitle("Delete Expense")
            .setMessage("Are you sure you want to delete this expense?")
            .setPositiveButton("Delete") { _, _ ->
                deleteExpense(expense)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteExpense(expense: Expense) {
        lifecycleScope.launch {
            database.expenseDao().deleteExpense(expense)
            loadExpenses()
        }
    }

    private fun setupPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.dragDecelerationFrictionCoef = 0.95f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.setDrawCenterText(true)
        pieChart.rotationAngle = 0f
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true
        pieChart.legend.isEnabled = false
    }

    private fun loadExpenses() {
        lifecycleScope.launch {
            val expenses = database.expenseDao().getAllExpenses()
            runOnUiThread {
                expensesAdapter.submitList(expenses)
                updatePieChart(expenses)
            }
        }
    }

    private fun updatePieChart(expenses: List<Expense>) {
        val categoryTotals = expenses.groupBy { it.category }
            .mapValues { it.value.sumOf { expense -> expense.amount } }

        if (categoryTotals.isEmpty()) {
            pieChart.clear()
            return
        }

        val entries = categoryTotals.map { PieEntry(it.value.toFloat(), it.key) }

        val dataSet = PieDataSet(entries, "Expenses")
        dataSet.colors = listOf(
            Color.parseColor("#4CAF50"),
            Color.parseColor("#2196F3"),
            Color.parseColor("#FF9800"),
            Color.parseColor("#E91E63"),
            Color.parseColor("#9C27B0"),
            Color.parseColor("#607D8B")
        )

        val data = PieData(dataSet)
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)

        pieChart.data = data
        pieChart.invalidate()
    }

    override fun onResume() {
        super.onResume()
        loadExpenses()
    }
}
