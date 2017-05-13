package com.example.audreycelia.homeworkapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cloud.ListTeachersAsyncTask;
import db.DatabaseHelper;

public class LoadingActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private boolean isActivated;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadCloudSetting();
        //SET ACTIVITY TO FULL SCREEN
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_loading);

        //INITIALISING TEXT VIEW FOR LOADING
        TextView state = (TextView) findViewById(R.id.tv_loading_state);

        //OPENING DATABASE HELPER
        if(isActivated) {
            db = new DatabaseHelper(getApplicationContext());
            new ListTeachersAsyncTask(db, this, state).execute();
        }
        else
        {
            state.setText(R.string.loading);
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        Thread.sleep(3000);
                        finishLoading();
                    }
                    catch (InterruptedException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }).start();
        }



    }

    //FINISH THE LOADING ACTIVITY
    public void finishLoading()
    {
        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadCloudSetting()
    {
        settings = getApplicationContext().getSharedPreferences("com.example.audreycelia.homeworkapp", Context.MODE_PRIVATE);
        isActivated = settings.getBoolean("CLOUD", false);
    }


}
