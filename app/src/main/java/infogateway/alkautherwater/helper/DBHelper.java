package infogateway.alkautherwater.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 4;
    public Context context;
    // Database Name
    private static final String DATABASE_NAME = "crud.db";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String q="CREATE TABLE tbl_notification(ID INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT,message TEXT,order_date TEXT,status TEXT)";
        db.execSQL(q);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS tbl_notification");
        // Create tables again
        onCreate(db);

    }
}