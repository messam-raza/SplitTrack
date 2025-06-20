package com.messamraza.splittrack

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        findViewById<CardView>(R.id.personalExpensesCard).setOnClickListener {
            startActivity(Intent(this, PersonalExpensesActivity::class.java))
        }

        findViewById<CardView>(R.id.groupSplitsCard).setOnClickListener {
            startActivity(Intent(this, GroupSplitsActivity::class.java))
        }
    }
}
