package com.uralsiberianworks.neuralpushkin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends BaseActivity {

    public static final String URL = "http://46.17.97.44:5000";
    private static final String TAG = "MainActivity";
    TextView chats;
    NavigationView navigationView, navigationViewBottom;
    DrawerLayout drawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton addButton = findViewById(R.id.add_button);



        setupToolbar(R.id.toolbar, "Messages");
        toolbar = findViewById(R.id.toolbar);

        TextView title = toolbar.findViewById(R.id.tv_title);
        title.setText("Messages");

        FragmentTransaction ft;
        FragmentHolder fragmentHome = new FragmentHolder();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frameLayout, fragmentHome).commit();


        drawer = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            // Handle navigation view item clicks here.
            FragmentTransaction ft1;
            int id = item.getItemId();

            if (id == R.id.nav_contacts) {
                FragmentContacts fragmentContacts = new FragmentContacts();
                ft1 = getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.frameLayout, fragmentContacts).addToBackStack(null).commit();
            }
            else if (id == R.id.nav_chats) {
                FragmentHolder fragmentHome1 = new FragmentHolder();
                ft1 = getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.frameLayout, fragmentHome1).commit();
            }

            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        navigationViewBottom = (NavigationView) findViewById(R.id.nav_view_bottom);
        navigationViewBottom.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) item -> {
            int id = item.getItemId();
            if (id == R.id.telegram) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/alexworteg"));
                startActivity(intent);
            }
            else if (id == R.id.mail) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                /* Fill it with Data */
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"alexwortega@yandex.ru"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "PushkinApp");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");

                /* Send it off to the Activity-Chooser */
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
            else if (id == R.id.web) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/groupname"));
                startActivity(intent);
            }
            return true;
        });

        chats = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_chats));

        addButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), AddContactActivity.class)));

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