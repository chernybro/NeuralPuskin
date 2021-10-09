package com.uralsiberianworks.neuralpushkin;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.uralsiberianworks.neuralpushkin.contactsRoom.FragmentContacts;
import com.uralsiberianworks.neuralpushkin.chatsRoom.FragmentChats;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static final String URL = "http://46.17.97.44:5000";
    private static final String TAG = "MainActivity";
    private DrawerLayout drawer;
    private Fragment mFragmentToSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton addButton = findViewById(R.id.add_button);

        setupToolbar(R.id.toolbar, "Messages");
        Toolbar toolbar = findViewById(R.id.toolbar);

        mFragmentToSet = new FragmentChats();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frameLayout, mFragmentToSet).commit();
        mFragmentToSet = null;


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override public void onDrawerSlide(View drawerView, float slideOffset) {}
            @Override public void onDrawerOpened(View drawerView) {}
            @Override public void onDrawerStateChanged(int newState) {}

            @Override
            public void onDrawerClosed(View drawerView) {
                //Set your new fragment here
                if (mFragmentToSet != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout, mFragmentToSet)
                            .commit();
                    mFragmentToSet = null;
                }
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        NavigationView navigationViewBottom = (NavigationView) findViewById(R.id.nav_view_bottom);
        navigationViewBottom.setNavigationItemSelectedListener(this);


        addButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this.getApplicationContext(), SetContactActivity.class)));

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        mFragmentToSet = null;

            switch (item.getItemId()) {
                case R.id.nav_contacts:
                    mFragmentToSet = new FragmentContacts();
                    break;
                case R.id.nav_chats:
                    mFragmentToSet = new FragmentChats();
                    break;
                case R.id.telegram:
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/alexworteg"));
                    startActivity(intent);
                    break;
                case R.id.mail:
                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"alexwortega@yandex.ru"});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "PushkinApp");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
                    /* Send it off to the Activity-Chooser */
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    break;
                case R.id.web:
                    Intent intentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.pushkin.monetka.name"));
                    startActivity(intentWeb);
                    break;
            }

            drawer.closeDrawer(GravityCompat.START);
           return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}