package com.example.ashish.selectmultipleimagefromgallery.demo.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.ashish.selectmultipleimagefromgallery.demo.constant.Global;
import com.example.ashish.selectmultipleimagefromgallery.demo.constant.MyConstant;

public class SpashScreen extends AppCompatActivity {

    Handler handler;
    private static final int SPLASH_TIME_MILLISEC = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_spash_screen);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Global.getPreference(MyConstant.PREF_KEY_IS_LOGGED_IN,true)){
                    Intent intent = new Intent(SpashScreen.this,DisplayProfile.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(SpashScreen.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        },SPLASH_TIME_MILLISEC);
    }
}
