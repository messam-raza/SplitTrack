package com.messamraza.splittrack

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.messamraza.splittrack.database.AppDatabase
import com.messamraza.splittrack.database.GroupExpense
import com.messamraza.splittrack.database.GroupMember
import kotlinx.coroutines.launch

class GroupDetailActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var groupExpensesAdapter: GroupExpensesAdapter
    private lateinit var membersAdapter: GroupMembersAdapter
    private lateinit var balanceAdapter: MemberBalanceAdapter
    private lateinit var settlementAdapter: SettlementAdapter
    private lateinit var emptyStateText: TextView
    private lateinit var totalExpensesText: TextView

    private var groupId: Long = 0
    private var groupName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_detail)

        groupId = intent.getLongExtra("GROUP_ID", 0)
        groupName = intent.getStringExtra("GROUP_NAME") ?: ""

        database = AppDatabase.getDatabase(this)
        setupViews()
        loadGroupData()
    }

    private fun setupViews() {
        findViewById<TextView>(R.id.groupNameTitle).text = groupName
        emptyStateText = findViewById(R.id.emptyStateText)
        totalExpensesText = findViewById(R.id.totalExpensesText)

        // Setup expenses RecyclerView
        val expensesRecyclerView = findViewById<RecyclerView>(R.id.expensesRecyclerView)
        groupExpensesAdapter = GroupExpensesAdapter { expense ->
            // Handle expense click if needed
        }
        expensesRecyclerView.adapter = groupExpensesAdapter
        expensesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Setup members RecyclerView
        val membersRecyclerView = findViewById<RecyclerView>(R.id.membersRecyclerView)
        membersAdapter = GroupMembersAdapter()
        membersRecyclerView.adapter = membersAdapter
        membersRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Setup member balances RecyclerView
        val balanceRecyclerView = findViewById<RecyclerView>(R.id.memberBalanceRecyclerView)
        balanceAdapter = MemberBalanceAdapter()
        balanceRecyclerView.adapter = balanceAdapter
        balanceRecyclerView.layoutManager = LinearLayoutManager(this)

        // Setup settlement RecyclerView
        val settlementRecyclerView = findViewById<RecyclerView>(R.id.settlementRecyclerView)
        settlementAdapter = SettlementAdapter()
        settlementRecyclerView.adapter = settlementAdapter
        settlementRecyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<FloatingActionButton>(R.id.fabAddExpense).setOnClickListener {
            val intent = Intent(this, AddGroupExpenseActivity::class.java)
            intent.putExtra("GROUP_ID", groupId)
            intent.putExtra("GROUP_NAME", groupName)
            startActivity(intent)
        }
    }

    private fun loadGroupData() {
        lifecycleScope.launch {
            val expenses = database.groupDao().getGroupExpenses(groupId)
            val members = database.groupDao().getGroupMembers(groupId)

            runOnUiThread {
                groupExpensesAdapter.submitList(expenses)
                membersAdapter.submitList(members)

                emptyStateText.visibility = if (expenses.isEmpty()) View.VISIBLE else View.GONE

                val totalAmount = expenses.sumOf { it.amount }
                totalExpensesText.text = "Total: $${String.format("%.2f", totalAmount)}"

                calculateAndShowBalances(expenses, members)
            }
        }
    }

    private fun calculateAndShowBalances(expenses: List<GroupExpense>, members: List<GroupMember>) {
        val memberBalances = mutableMapOf<String, Double>()

        // Initialize all members with 0 balance
        members.forEach { member ->
            memberBalances[member.name] = 0.0
        }

        // Calculate balances
        expenses.forEach { expense ->
            val splitMembers = expense.forWhom.split(",").map { it.trim() }
            val amountPerPerson = expense.amount / splitMembers.size

            // Person who paid gets positive balance
            memberBalances[expense.paidBy] = memberBalances[expense.paidBy]!! + expense.amount

            // People who owe get negative balance
            splitMembers.forEach { member ->
                memberBalances[member] = memberBalances[member]!! - amountPerPerson
            }
        }

        // Create member balance list
        val memberBalanceList = memberBalances.map { (name, balance) ->
            MemberBalance(name, balance)
        }.sortedByDescending { it.balance }

        balanceAdapter.submitList(memberBalanceList)

        // Create settlement list
        val settlements = calculateOptimalSettlements(memberBalances)
        settlementAdapter.submitList(settlements)
    }

    private fun calculateOptimalSettlements(balances: Map<String, Double>): List<Settlement> {
        val settlements = mutableListOf<Settlement>()
        val creditors = balances.filter { it.value > 0.01 }.toMutableMap()
        val debtors = balances.filter { it.value < -0.01 }.toMutableMap()

        for ((debtor, debtAmount) in debtors) {
            var remainingDebt = -debtAmount

            for ((creditor, creditAmount) in creditors.toMap()) {
                if (remainingDebt <= 0.01) break
                if (creditAmount <= 0.01) continue

                val settlementAmount = minOf(remainingDebt, creditAmount)
                settlements.add(
                    Settlement(
                        from = debtor,
                        to = creditor,
                        amount = settlementAmount
                    )
                )

                remainingDebt -= settlementAmount
                creditors[creditor] = creditAmount - settlementAmount
            }
        }

        return settlements
    }

    override fun onResume() {
        super.onResume()
        loadGroupData()
    }
}

data class Settlement(
    val from: String,
    val to: String,
    val amount: Double
)

data class MemberBalance(
    val name: String,
    val balance: Double
)
