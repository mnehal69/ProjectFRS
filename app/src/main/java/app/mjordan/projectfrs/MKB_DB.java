package app.mjordan.projectfrs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MKB_DB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app.mjordan.projectfrs.MKB_DB";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_ISLOGGED = "Islogged";
    private static final String TABLE_ORDER = "UserOrder";

    // Post Table Columns
    private static final String KEY_MKB_USERID = "UID";
    //Order Table Columns
    private static final String KEY_MKB_ORDERID = "OID";
    private static final String KEY_MKB_ORDERNAME = "ONAME";
    private static final String KEY_MKB_ORDERQUANTITY = "OQUAT";
    private static final String KEY_MKB_ORDERPRICE = "OPRICE";

    MKB_DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_ISLOGGED +
                "(" + KEY_MKB_USERID + " TEXT)";
        Log.d("sadder",CREATE_POSTS_TABLE);
        db.execSQL(CREATE_POSTS_TABLE);
        String CREATE_ORDER_TABLE = "CREATE TABLE " + TABLE_ORDER +
                "(" + KEY_MKB_ORDERID + " TEXT,"
                + KEY_MKB_ORDERNAME + " TEXT,"
                + KEY_MKB_ORDERPRICE + " TEXT,"
                + KEY_MKB_ORDERQUANTITY +" TEXT)";
        Log.d("sadder",CREATE_ORDER_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
    }


     int getCount(){
        SQLiteDatabase db;
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_ISLOGGED, null);
        return cursor.getCount();
    }

    int getOrderCount(){
        SQLiteDatabase db;
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_ORDER, null);
        return cursor.getCount();
    }

    ArrayList<Menu> Get_Order(){
        ArrayList<Menu> return_array=new ArrayList<>();
        SQLiteDatabase db;
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_ORDER, null);
        String Name=null,Price=null,Quantity=null,ID=null;
        while( cursor.moveToNext()) {
            ID=cursor.getString(cursor.getColumnIndexOrThrow(KEY_MKB_ORDERID));
            Name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MKB_ORDERNAME));
            Price = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MKB_ORDERPRICE));
            Quantity = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MKB_ORDERQUANTITY));
            return_array.add(new Menu(true,false, ID,"","",Name,Quantity,Price));
        }
        return return_array;
    }

    void Update_quantityUsingID(String ID,String quantity){
        SQLiteDatabase db;
        db = getWritableDatabase();
        String strSQL = "UPDATE "+TABLE_ORDER+" SET "+KEY_MKB_ORDERQUANTITY+" = "+quantity+" WHERE "+KEY_MKB_ORDERID+" = '"+ ID+"'";
        db.execSQL(strSQL);
    }

   void Insert_Order(ArrayList<Menu> menulist){
        DeleteAll_IsLogged();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (Menu menu : menulist) {
                values.put(KEY_MKB_ORDERID, menu.getID());
                values.put(KEY_MKB_ORDERNAME, menu.getProduct());
                values.put(KEY_MKB_ORDERPRICE, menu.getPrice());
                values.put(KEY_MKB_ORDERQUANTITY, menu.getDes());

                db.insert(TABLE_ORDER, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    void DeleteAll_Order(){
        SQLiteDatabase db;
        db = getWritableDatabase();
        db.delete(TABLE_ORDER, null, null );
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
