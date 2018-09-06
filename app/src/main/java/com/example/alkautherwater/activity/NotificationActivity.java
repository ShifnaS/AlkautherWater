package com.example.alkautherwater.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.alkautherwater.R;
import com.example.alkautherwater.adapter.GalleryAdapter;
import com.example.alkautherwater.adapter.NotificationCustomAdapter;
import com.example.alkautherwater.api.APIService;
import com.example.alkautherwater.api.APIUrl;
import com.example.alkautherwater.app.Config;
import com.example.alkautherwater.helper.SQLiteOperations;
import com.example.alkautherwater.model.Notification;
import com.example.alkautherwater.model.ResultNotification;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressDialog pDialog;
    private NotificationCustomAdapter mAdapter;
    ArrayList<Notification> notifications;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        SQLiteOperations sqLiteOperations=new SQLiteOperations(getApplicationContext());
        sqLiteOperations.clearData();
        recyclerView =  findViewById(R.id.recycler_view);
        notifications=new ArrayList<>();
        pDialog = new ProgressDialog(getApplicationContext());
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        //setNotification(regId);
        notifications= sqLiteOperations.getAllNotifications();
        sqLiteOperations.updateStatus();
       // boolean success = ShortcutBadger.removeCount(getApplicationContext());

        if(notifications.isEmpty())
        {
            Toast.makeText(this, "There is no notifications", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAdapter = new NotificationCustomAdapter(getApplicationContext(), notifications);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }


    }
/*    private void prepareNotificationData() {
        Notification notification = new Notification("success","Action & Adventure", "12/08/2018");
        notifications.add(notification);

        notification = new Notification("success","Action & Adventure", "12/08/2018");
        notifications.add(notification);

        notification = new Notification("success","Action & Adventure", "12/08/2018");
        notifications.add(notification);

        notification = new Notification("success","Action & Adventure", "12/08/2018");
        notifications.add(notification);

        notification = new Notification("success","Action & Adventure", "12/08/2018");
        notifications.add(notification);
    }*/
/*    private void setNotification(String fid) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);
        Call<ResultNotification> call = service.getNotificationItems(fid);


        call.enqueue(new Callback<ResultNotification>() {
            @Override
            public void onResponse(Call<ResultNotification> call, Response<ResultNotification> response) {


                mAdapter = new NotificationCustomAdapter(getApplicationContext(), response.body().getItem());
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            @Override
            public void onFailure(Call<ResultNotification> call, Throwable t) {

            }
        });
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                Intent intent = new Intent(this, Products.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            // Something else

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
