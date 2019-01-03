package app.mjordan.projectfrs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MKB_DB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app.mjordan.projectfrs.MKB_DB";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_ISLOGGED = "Islogged";

    // Post Table Columns
    private static final String KEY_MKB_USERID = "UID";

    MKB_DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_ISLOGGED +
                "(" + KEY_MKB_USERID + " TEXT)";
        Log.d("sadder",CREATE_POSTS_TABLE);
        db.execSQL(CREATE_POSTS_TABLE);
    }

     int getCount(){
        SQLiteDatabase db;
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_ISLOGGED, null);
        return cursor.getCount();
    }
    String get_UserLoggedID(){
        SQLiteDatabase db;
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_ISLOGGED, null);
        String UserID=null;
        while( cursor.moveToNext()) {
            UserID = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MKB_USERID));
        }
        return UserID;
    }

    public void Insert_IsLogged(String UserID) {
        if(getCount()>=1) {
            DeleteAll_IsLogged();
        }
        SQLiteDatabase db;
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_MKB_USERID, UserID);
        db.insert(TABLE_ISLOGGED, null, cv);
    }

    void DeleteAll_IsLogged(){
        SQLiteDatabase db;
        db = getWritableDatabase();
        db.delete(TABLE_ISLOGGED, null, null );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
