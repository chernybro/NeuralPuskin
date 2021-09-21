package com.uralsiberianworks.neuralpushkin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.uralsiberianworks.neuralpushkin.api.PushkinApi;

public class MainActivity extends BaseActivity {

    public static final String URL = "http://46.17.97.44:5000";
    private static final String TAG = "MainActivity";
    private PushkinApi pushkinApi;
    TextView chats;
    NavigationView navigationView, navigationViewBottom;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar(R.id.toolbar, "Messages");
        toolbar = findViewById(R.id.toolbar);

        TextView title = (TextView) toolbar.findViewById(R.id.tv_title);
        title.setText("Messages");

        FragmentTransaction ft;
        FragmentHolder fragmentHome = new FragmentHolder();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frameLayout, fragmentHome).commit();


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                FragmentTransaction ft;
                int id = item.getItemId();

                if (id == R.id.nav_contacts) {
                    FragmentContacts fragmentContacts = new FragmentContacts();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout, fragmentContacts).addToBackStack(null).commit();
                }
                else if (id == R.id.nav_chats) {
                    FragmentHolder fragmentHome = new FragmentHolder();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout, fragmentHome).commit();
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        chats = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_chats));

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}