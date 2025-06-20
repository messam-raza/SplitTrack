package com.messamraza.splittrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class MemberBalanceAdapter : ListAdapter<MemberBalance, MemberBalanceAdapter.MemberBalanceViewHolder>(MemberBalanceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberBalanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_member_balance, parent, false)
        return MemberBalanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberBalanceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MemberBalanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val memberNameText: TextView = itemView.findViewById(R.id.memberNameText)
        private val balanceText: TextView = itemView.findViewById(R.id.balanceText)
        private val statusText: TextView = itemView.findViewById(R.id.statusText)

        fun bind(memberBalance: MemberBalance) {
            memberNameText.text = memberBalance.name

            when {
                memberBalance.balance > 0.01 -> {
                    balanceText.text = "+$${String.format("%.2f", memberBalance.balance)}"
                    balanceText.setTextColor(ContextCompat.getColor(itemView.context, R.color.accent_green))
                    statusText.text = "Gets back"
                    statusText.setTextColor(ContextCompat.getColor(itemView.context, R.color.accent_green))
                }
                memberBalance.balance < -0.01 -> {
                    balanceText.text = "-$${String.format("%.2f", -memberBalance.balance)}"
                    balanceText.setTextColor(ContextCompat.getColor(itemView.context, R.color.expense_red))
                    statusText.text = "Owes"
                    statusText.setTextColor(ContextCompat.getColor(itemView.context, R.color.expense_red))
                }
                else -> {
                    balanceText.text = "$0.00"
                    balanceText.setTextColor(ContextCompat.getColor(itemView.context, R.color.text_secondary))
                    statusText.text = "Settled"
                    statusText.setTextColor(ContextCompat.getColor(itemView.context, R.color.text_secondary))
                }
            }
        }
    }
}

class MemberBalanceDiffCallback : DiffUtil.ItemCallback<MemberBalance>() {
    override fun areItemsTheSame(oldItem: MemberBalance, newItem: MemberBalance): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: MemberBalance, newItem: MemberBalance): Boolean {
        return oldItem == newItem
    }
}
