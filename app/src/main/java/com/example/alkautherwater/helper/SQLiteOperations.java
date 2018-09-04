package com.example.alkautherwater.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.alkautherwater.model.Notification;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
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

    public void saveNotification(String title,String message,int status)
    {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("message", message);
        values.put("order_date",  getDateTime());
        values.put("status", status);


        db.insert("tbl_notification", null, values);
        db.close(); // Closing database connection
        Log.e("title","//////////////*********** "+getDateTime());
    }
    public int getcount(int status) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int count = (int) DatabaseUtils.queryNumEntries(db,"tbl_notification","status = ?",new String[]{String.valueOf(status)});
        db.close();
        return count;
    }

    public void clearData()
    {
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date date=calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm", Locale.getDefault());
        String lastday=dateFormat.format(date);
        Log.e("date",""+lastday);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
      //  String sql = "DELETE FROM tbl_notification WHERE order_date <= "+lastday;
        db.delete("tbl_notification", "order_date <= ?", new String[]{String.valueOf(lastday)});
        db.close();
    }
    public void updateStatus()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("status",1);
        db.update("tbl_notification", values,"status = ?", new String[]{String.valueOf(0)});
        db.close();

    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm", Locale.getDefault());
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
        String q="select * from tbl_notification ORDER BY datetime(order_date) DESC";
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
