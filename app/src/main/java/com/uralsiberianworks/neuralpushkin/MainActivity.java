package com.uralsiberianworks.neuralpushkin;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.uralsiberianworks.neuralpushkin.db.NeuralDatabase;
import com.uralsiberianworks.neuralpushkin.recyclerviewAdapters.FragmentContacts;
import com.uralsiberianworks.neuralpushkin.recyclerviewAdapters.FragmentHolder;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static final String URL = "http://46.17.97.44:5000";
    private static final String TAG = "MainActivity";
    private DrawerLayout drawer;
    NavigationView navigationView, navigationViewBottom;
    private FragmentHolder fragmentHolder;
    private FragmentContacts fragmentContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton addButton = findViewById(R.id.add_button);

        setupToolbar(R.id.toolbar, "Messages");
        Toolbar toolbar = findViewById(R.id.toolbar);

        TextView title = toolbar.findViewById(R.id.tv_title);
        title.setText("Messages");

        NeuralDatabase db = ((NeuralApp) getApplication()).getDb();

        fragmentHolder = new FragmentHolder(db);
        fragmentContacts = new FragmentContacts(db);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frameLayout, fragmentHolder).commit();



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationViewBottom = (NavigationView) findViewById(R.id.nav_view_bottom);
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

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
             // OPEN DRAWER
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager ft = getSupportFragmentManager();
        int id = item.getItemId();

        if (id == R.id.nav_contacts) {
                    ft.beginTransaction().replace(R.id.frameLayout, fragmentContacts).addToBackStack(null).commit();
        } else if (id == R.id.nav_chats) {
                    ft.beginTransaction().replace(R.id.frameLayout, fragmentHolder).commit();
        } else if (id == R.id.telegram) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/alexworteg"));
                    startActivity(intent);
                }
            });

        } else if (id == R.id.mail) {

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                    /* Fill it with Data */
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"alexwortega@yandex.ru"});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "PushkinApp");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");

                    /* Send it off to the Activity-Chooser */
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                }
            });
        } else if (id == R.id.web) {

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.pushkin.monetka.name"));
                    startActivity(intent);
                }
            });
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}