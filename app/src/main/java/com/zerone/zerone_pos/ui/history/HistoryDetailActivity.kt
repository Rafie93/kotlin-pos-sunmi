package com.zerone.zerone_pos.ui.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zerone.zerone_pos.R

class HistoryDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_detail)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
