package com.example.user.mymapfinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by user on 7/15/2015.
 */
public class DBHelper {
    public static final String KEY_ROWID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PICKUP = "pickup";
    public static final String KEY_DESTINATION = "destination";
    public static final String KEY_TYPE = "type";
    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "FavouritesDB";
    private static final String DATABASE_TABLE = "favourites";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = "create table if not exists favourites (id integer primary key autoincrement,"
            + "name VARCHAR not null, pickup VARCHAR not null, destination VARCHAR not null, type VARCHAR not null);";

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBHelper(Context ctx) {

        this.context = ctx;
        DBHelper = new DatabaseHelper(context);

    }

    public static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {

                db.execSQL(DATABASE_CREATE);
            } catch (android.database.SQLException e) {

                e.printStackTrace();
            }


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version" + oldVersion + "to" + newVersion + ",which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS assignments");
            onCreate(db);

        }
    }

    public  DBHelper open()throws android.database.SQLException{

        db = DBHelper.getWritableDatabase();
        return this;
    }
    public void close(){

        DBHelper.close();
    }

    public long insertRecord(String name, String pickup, String destination, String type){


        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_NAME,name);
        initialValues.put(KEY_PICKUP,pickup);
        initialValues.put(KEY_DESTINATION,destination);
        initialValues.put(KEY_TYPE, type);

        return db.insert(DATABASE_TABLE,null,initialValues);

    }

    public boolean deleteRecord(long rowId){

        int ri = (int)rowId;
        db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null);

        return true;

    }

    public Favourites getFavourite(int id) {


        Cursor cursor = db.query(DATABASE_TABLE, new String[] {KEY_ROWID,
                        KEY_NAME, KEY_PICKUP,KEY_DESTINATION,KEY_TYPE}, KEY_ROWID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Favourites A = new Favourites();
        A.setId(cursor.getString(0));
        A.setName(cursor.getString(1));
        A.setPickup(cursor.getString(2));
        A.setDestination(cursor.getString(3));
        A.setType(cursor.getString(4));
        //Integer.parseInt(cursor.getString(0)),
        //cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4));

        return A;
    }

    public ArrayList<String> getData() {
        // TODO Auto-generated method stub
        String[]columns=new String[]{ KEY_ROWID,KEY_NAME, KEY_PICKUP, KEY_DESTINATION,KEY_TYPE};
        Cursor c =db.query(DATABASE_TABLE, columns, null, null, null, null, null);
        ArrayList<String> result = new ArrayList<String>();
        int iRow=c.getColumnIndex(KEY_ROWID);
        int iName=c.getColumnIndex(KEY_NAME);
        int iPickup=c.getColumnIndex(KEY_PICKUP);
        int iDestination=c.getColumnIndex(KEY_DESTINATION);
        int iType=c.getColumnIndex(KEY_TYPE);


        for(c.moveToFirst(); !c.isAfterLast();c.moveToNext()){
            result.add(c.getString(iRow)+" - "+c.getString(iName)+"\n   "+c.getString(iPickup)+"\n   "+c.getString(iDestination)+"\n   "+c.getString(iType));
        }
        return result;
    }

    public  boolean updateRecord(long rowId,String name,String pickup,String destination,String type){

        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_PICKUP,pickup);
        args.put(KEY_DESTINATION,destination);
        args.put(KEY_TYPE,type);
        return db.update(DATABASE_TABLE,args,KEY_ROWID +"="+ rowId,null)> 0;

    }

}