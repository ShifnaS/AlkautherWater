package com.example.alkautherwater.activity;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.alkautherwater.R;

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
                Intent i = new Intent(MainActivity.this, Home1Activity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
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
}
