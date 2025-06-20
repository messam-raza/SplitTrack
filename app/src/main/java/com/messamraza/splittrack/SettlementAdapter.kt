package com.messamraza.splittrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class SettlementAdapter : ListAdapter<Settlement, SettlementAdapter.SettlementViewHolder>(SettlementDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettlementViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_settlement, parent, false)
        return SettlementViewHolder(view)
    }

    override fun onBindViewHolder(holder: SettlementViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SettlementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val settlementText: TextView = itemView.findViewById(R.id.settlementText)
        private val amountText: TextView = itemView.findViewById(R.id.settlementAmountText)

        fun bind(settlement: Settlement) {
            settlementText.text = "${settlement.from} owes ${settlement.to}"
            amountText.text = "$${String.format("%.2f", settlement.amount)}"
        }
    }
}

class SettlementDiffCallback : DiffUtil.ItemCallback<Settlement>() {
    override fun areItemsTheSame(oldItem: Settlement, newItem: Settlement): Boolean {
        return oldItem.from == newItem.from && oldItem.to == newItem.to
    }

    override fun areContentsTheSame(oldItem: Settlement, newItem: Settlement): Boolean {
        return oldItem == newItem
    }
}
