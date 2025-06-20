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
import com.messamraza.splittrack.database.Group
import kotlinx.coroutines.launch

class GroupSplitsActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var groupsAdapter: GroupsAdapter
    private lateinit var emptyStateText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_splits)

        database = AppDatabase.getDatabase(this)
        setupViews()
        loadGroups()
    }

    private fun setupViews() {
        emptyStateText = findViewById(R.id.emptyStateText)

        val recyclerView = findViewById<RecyclerView>(R.id.groupsRecyclerView)
        groupsAdapter = GroupsAdapter { group ->
            val intent = Intent(this, GroupDetailActivity::class.java)
            intent.putExtra("GROUP_ID", group.id)
            intent.putExtra("GROUP_NAME", group.name)
            startActivity(intent)
        }
        recyclerView.adapter = groupsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<FloatingActionButton>(R.id.fabAddGroup).setOnClickListener {
            startActivity(Intent(this, AddGroupActivity::class.java))
        }
    }

    private fun loadGroups() {
        lifecycleScope.launch {
            val groups = database.groupDao().getAllGroups()
            runOnUiThread {
                groupsAdapter.submitList(groups)
                emptyStateText.visibility = if (groups.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadGroups()
    }
}
