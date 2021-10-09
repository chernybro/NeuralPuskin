package com.uralsiberianworks.neuralpushkin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.uralsiberianworks.neuralpushkin.chatsRoom.FragmentChats
import com.uralsiberianworks.neuralpushkin.contactsRoom.FragmentContacts

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var drawer: DrawerLayout? = null
    private var mFragmentToSet: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val addButton = findViewById<FloatingActionButton>(R.id.add_button)
        setupToolbar(R.id.toolbar, "Messages")
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        var mFragmentChats: Fragment = FragmentChats()
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.frameLayout, mFragmentChats).commit()
        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        //drawer.addDrawerListener(toggle);
        toggle.syncState()
        drawer!!.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerStateChanged(newState: Int) {}
            override fun onDrawerClosed(drawerView: View) {
                //Set your new fragment here
                if (mFragmentToSet != null) {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.frameLayout, mFragmentToSet!!)
                            .commit()
                    mFragmentToSet = null
                }
            }
        })
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val navigationViewBottom = findViewById<View>(R.id.nav_view_bottom) as NavigationView
        navigationViewBottom.setNavigationItemSelectedListener(this)
        addButton.setOnClickListener { view: View? -> startActivity(Intent(this@MainActivity.applicationContext, SetContactActivity::class.java)) }
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.itemId == android.R.id.home) {
            drawer!!.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_contacts -> mFragmentToSet = FragmentContacts()
            R.id.nav_chats -> mFragmentToSet = FragmentChats()
            R.id.telegram -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/alexworteg"))
                startActivity(intent)
            }
            R.id.mail -> {
                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.type = "plain/text"
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("alexwortega@yandex.ru"))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "PushkinApp")
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Text")
                /* Send it off to the Activity-Chooser */startActivity(Intent.createChooser(emailIntent, "Send mail..."))
            }
            R.id.web -> {
                val intentWeb = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.pushkin.monetka.name"))
                startActivity(intentWeb)
            }
        }
        drawer!!.closeDrawer(GravityCompat.START)
        return false
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    companion object {
        const val URL = "http://46.17.97.44:5000"
        private const val TAG = "MainActivity"
    }
}