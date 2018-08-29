package com.example.alkautherwater.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.alkautherwater.model.Notification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SQLiteOperations {
    private DBHelper dbHelper;
    Context context;
    Cursor cursor=null;

    public SQLiteOperations(Context context) {
        dbHelper = new DBHelper(context);
        this.context=context;
    }

    public void saveNotification(String title,String message)
    {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("message", message);
        values.put("order_date",  getDateTime());

        db.insert("tbl_notification", null, values);
        db.close(); // Closing database connection
        Log.e("title","//////////////*********** "+getDateTime());
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    public ArrayList<Notification> getAllNotifications() {
        String TITLE="title";
        String MSG="message";
        String DATE="order_date";
        Notification notification;
        ArrayList<Notification> notificationList  = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String q="select * from tbl_notification";
        cursor=db.rawQuery(q,null);
        if (cursor.moveToFirst()) {
            try
            {
                do {
                    notification=new Notification();
                    String title=cursor.getString(cursor.getColumnIndex(TITLE));
                    String message=cursor.getString(cursor.getColumnIndex(MSG));
                    String date=cursor.getString(cursor.getColumnIndex(DATE));
                    Log.e("title","********* "+title);
                    notification.setTitle(title);
                    notification.setMessage(message);
                    notification.setDate(date);
                    notificationList.add(notification) ;
                } while (cursor.moveToNext());
            }
            catch (Exception e)
            {
                Log.e("EXCEPTION ","eeeeeeeeeeeeeeeeeeeee "+e);
            }
        }
        cursor.close();
        db.close();
        return notificationList;
    }
}
