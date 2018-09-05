package com.example.alkautherwater.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.alkautherwater.R;
import com.example.alkautherwater.app.Config;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        RunAnimation();

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if(!isNetworkAvailable()) {

                    Intent i = new Intent(MainActivity.this, RefreshActivity.class);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(MainActivity.this, Products.class);
                    startActivity(i);

                }


                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

      //  SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
      //  String regId = pref.getString("regId", null);
      //  Log.e("regId",regId);
    }

    private void RunAnimation() {
        Animation a = AnimationUtils.loadAnimation(this,R.anim.push_left_out);
        a.reset();
        Animation a1 = AnimationUtils.loadAnimation(this,R.anim.push_left_in);
        a1.reset();
       // TextView tv=findViewById(R.id.textView);
        LinearLayout ll=findViewById(R.id.layout);
        ll.clearAnimation();
        ll.startAnimation(a);
      //  tv.clearAnimation();
        //tv.startAnimation(a);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
