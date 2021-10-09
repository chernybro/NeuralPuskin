package com.uralsiberianworks.neuralpushkin

import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

open class BaseActivity : AppCompatActivity() {
    fun setupToolbar(toolbarId: Int, titlePage: String?) {
        val toolbar = findViewById<View>(toolbarId) as Toolbar
        setSupportActionBar(toolbar)
        val title = toolbar.findViewById<View>(R.id.tv_title) as TextView
        title.text = titlePage
        supportActionBar!!.title = ""
    }
}