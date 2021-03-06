package infogateway.alkautherwater.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import infogateway.alkautherwater.adapter.NotificationCustomAdapter;
import infogateway.alkautherwater.app.Config;
import infogateway.alkautherwater.helper.SQLiteOperations;
import infogateway.alkautherwater.model.Notification;
import infogateway.alkautherwater.R;

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
      //  setNotification(regId);
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
