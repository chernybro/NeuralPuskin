package com.uralsiberianworks.neuralpushkin;

import android.content.Context;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class BaseActivity extends AppCompatActivity {

    public final void setupToolbar(int toolbarId, String titlePage){
        Toolbar toolbar = (Toolbar) findViewById(toolbarId);
        setSupportActionBar(toolbar);

        TextView title = (TextView) toolbar.findViewById(R.id.tv_title);
        title.setText(titlePage);

        getSupportActionBar().setTitle("");
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
