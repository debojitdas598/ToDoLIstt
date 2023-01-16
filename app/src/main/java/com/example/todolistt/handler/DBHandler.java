package com.example.todolistt.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todolistt.getsetgo.getset;


import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

        private static final String DB_NAME = "TodoList";
        private static final int DB_VERSION = 1;
        private static final String TABLE_NAME = "todo_list";
        private static final String ID_COL = "id";
        private static final String ITEM_COL = "item";

        


        public DBHandler(Context context){
            super(context,DB_NAME,null,DB_VERSION);
        }



    @Override
    public void onCreate(SQLiteDatabase db) {

            String query = "CREATE TABLE " + TABLE_NAME + "("
                    + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ITEM_COL + " TEXT" + ");";

        db.execSQL(query);
    }



    public void addNew(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

       values.put(ITEM_COL,item);

        db.insert(TABLE_NAME,null,values);
        db.close();


    }

    public ArrayList<getset> read(){
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);

        ArrayList<getset> contactArrayList = new ArrayList<>();

        if(cursor.moveToFirst()) {
            do {
                contactArrayList.add(new getset(cursor.getString(1)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return contactArrayList;
        }

        public void update(String ogitem,String item)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(ITEM_COL,item);

            db.update(TABLE_NAME,values,"item=?",new String[]{ogitem});
            db.close();
        }


    public int deletion(String item) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(TABLE_NAME, "item=?", new String[]{item});
        db.close();
        return 1;
    }

    public int deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.execSQL("delete from "+ TABLE_NAME);
        db.close();
        return 1;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
