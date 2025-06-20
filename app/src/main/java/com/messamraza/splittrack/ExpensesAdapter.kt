package com.messamraza.splittrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.messamraza.splittrack.database.Expense
import java.text.SimpleDateFormat
import java.util.*

class ExpensesAdapter(
    private val onExpenseClick: (Expense) -> Unit,
    private val onExpenseLongClick: (Expense) -> Unit
) : ListAdapter<Expense, ExpensesAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryText: TextView = itemView.findViewById(R.id.categoryText)
        private val amountText: TextView = itemView.findViewById(R.id.amountText)
        private val dateText: TextView = itemView.findViewById(R.id.dateText)
        private val notesText: TextView = itemView.findViewById(R.id.notesText)

        fun bind(expense: Expense) {
            categoryText.text = expense.category
            amountText.text = "$${String.format("%.2f", expense.amount)}"
            dateText.text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(expense.date)
            notesText.text = expense.notes
            notesText.visibility = if (expense.notes.isBlank()) View.GONE else View.VISIBLE

            itemView.setOnClickListener { onExpenseClick(expense) }
            itemView.setOnLongClickListener {
                onExpenseLongClick(expense)
                true
            }
        }
    }
}

class ExpenseDiffCallback : DiffUtil.ItemCallback<Expense>() {
    override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
        return oldItem == newItem
    }
}
