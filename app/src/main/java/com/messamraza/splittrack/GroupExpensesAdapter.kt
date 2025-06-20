package com.messamraza.splittrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.messamraza.splittrack.database.GroupExpense
import java.text.SimpleDateFormat
import java.util.*

class GroupExpensesAdapter(
    private val onExpenseClick: (GroupExpense) -> Unit
) : ListAdapter<GroupExpense, GroupExpensesAdapter.GroupExpenseViewHolder>(GroupExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_group_expense, parent, false)
        return GroupExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GroupExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.expenseTitleText)
        private val amountText: TextView = itemView.findViewById(R.id.expenseAmountText)
        private val paidByText: TextView = itemView.findViewById(R.id.paidByText)
        private val dateText: TextView = itemView.findViewById(R.id.expenseDateText)

        fun bind(expense: GroupExpense) {
            titleText.text = expense.title
            amountText.text = "$${String.format("%.2f", expense.amount)}"
            paidByText.text = "Paid by ${expense.paidBy}"
            dateText.text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(expense.date)

            itemView.setOnClickListener { onExpenseClick(expense) }
        }
    }
}

class GroupExpenseDiffCallback : DiffUtil.ItemCallback<GroupExpense>() {
    override fun areItemsTheSame(oldItem: GroupExpense, newItem: GroupExpense): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GroupExpense, newItem: GroupExpense): Boolean {
        return oldItem == newItem
    }
}
