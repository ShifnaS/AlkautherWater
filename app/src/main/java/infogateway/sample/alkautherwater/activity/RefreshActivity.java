package infogateway.sample.alkautherwater.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import infogateway.sample.alkautherwater.R;

public class RefreshActivity extends AppCompatActivity {

    Button bt_refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        bt_refresh=findViewById(infogateway.sample.alkautherwater.R.id.refresh);
        bt_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNetworkAvailable()) {

                    Toast.makeText(RefreshActivity.this, "please make sure there is a network connection", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent i = new Intent(getApplicationContext(), Products.class);
                    startActivity(i);

                }
            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
