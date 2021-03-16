package com.anthonystark.module

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anthonystark.module.librarytest.mapNotNullTest
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_code_lab.setOnClickListener { CodeLabActivity.startActivity(this) }
    }
}