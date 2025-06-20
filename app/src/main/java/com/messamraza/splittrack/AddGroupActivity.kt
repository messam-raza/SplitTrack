package com.messamraza.splittrack

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.messamraza.splittrack.database.AppDatabase
import com.messamraza.splittrack.database.Group
import com.messamraza.splittrack.database.GroupMember
import kotlinx.coroutines.launch

class AddGroupActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var groupNameEditText: TextInputEditText
    private lateinit var memberNameEditText: TextInputEditText
    private lateinit var membersAdapter: MembersAdapter
    private val membersList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)

        database = AppDatabase.getDatabase(this)
        setupViews()
    }

    private fun setupViews() {
        groupNameEditText = findViewById(R.id.groupNameEditText)
        memberNameEditText = findViewById(R.id.memberNameEditText)

        val membersRecyclerView = findViewById<RecyclerView>(R.id.membersRecyclerView)
        membersAdapter = MembersAdapter(membersList) { position ->
            membersList.removeAt(position)
            membersAdapter.notifyItemRemoved(position)
        }
        membersRecyclerView.adapter = membersAdapter
        membersRecyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.addMemberButton).setOnClickListener {
            addMember()
        }

        findViewById<Button>(R.id.cancelButton).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.createGroupButton).setOnClickListener {
            createGroup()
        }
    }

    private fun addMember() {
        val memberName = memberNameEditText.text.toString().trim()
        if (memberName.isNotBlank() && !membersList.contains(memberName)) {
            membersList.add(memberName)
            membersAdapter.notifyItemInserted(membersList.size - 1)
            memberNameEditText.text?.clear()
        } else if (membersList.contains(memberName)) {
            Toast.makeText(this, "Member already added", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createGroup() {
        val groupName = groupNameEditText.text.toString().trim()

        if (groupName.isBlank()) {
            Toast.makeText(this, "Please enter a group name", Toast.LENGTH_SHORT).show()
            return
        }

        if (membersList.size < 2) {
            Toast.makeText(this, "Please add at least 2 members", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val group = Group(name = groupName)
            val groupId = database.groupDao().insertGroup(group)

            // Add members to the group
            membersList.forEach { memberName ->
                val member = GroupMember(groupId = groupId, name = memberName)
                database.groupDao().insertGroupMember(member)
            }

            runOnUiThread {
                Toast.makeText(this@AddGroupActivity, "Group created successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
