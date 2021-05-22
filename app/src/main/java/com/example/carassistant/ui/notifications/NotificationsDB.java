package com.example.carassistant.ui.notifications;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.carassistant.ui.Notifications;

import java.util.ArrayList;

public class NotificationsDB {

    private static final String DATABASE_NAME = "carassistant.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "notifications";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_DATE = "Date";
    private static final String COLUMN_WAY = "Way";
    private static final String COLUMN_REPEATCOUNTER = "RepeatCounter";
    private static final String COLUMN_REPEATTYPE = "RepeatType";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_NAME = 1;
    private static final int NUM_COLUMN_DATE = 2;
    private static final int NUM_COLUMN_WAY = 3;
    private static final int NUM_COLUMN_REPEATCOUNTER = 4;
    private static final int NUM_COLUMN_REPEATTYPE = 5;


    private final SQLiteDatabase mDataBase;

    public NotificationsDB(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(String name,String date, int way, int repeatCounter, int repeatType) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_WAY, way);
        cv.put(COLUMN_REPEATCOUNTER, repeatCounter);
        cv.put(COLUMN_REPEATTYPE, repeatType);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

    public int update(Notifications nt) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_NAME, nt.getName());
        cv.put(COLUMN_DATE, nt.getDate());
        cv.put(COLUMN_WAY, nt.getWay());
        cv.put(COLUMN_REPEATCOUNTER, nt.getRepeatCounter());
        cv.put(COLUMN_REPEATTYPE, nt.getRepeatType());

        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?",new String[] { String.valueOf(nt.getId())});
    }

    public void deleteAll() {
        mDataBase.delete(TABLE_NAME, null, null);
    }

    public void delete(long id) {
        mDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public Notifications select(long id) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        mCursor.moveToFirst();
        String name = mCursor.getString(NUM_COLUMN_NAME);
        String date = mCursor.getString(NUM_COLUMN_DATE);
        int way = mCursor.getInt(NUM_COLUMN_WAY);
        int repeatCounter = mCursor.getInt(NUM_COLUMN_REPEATCOUNTER);
        int repeatType = mCursor.getInt(NUM_COLUMN_REPEATTYPE);
        return new Notifications(id, name, date, way, repeatCounter, repeatType);
    }

    public ArrayList<Notifications> selectAll() {
        @SuppressLint("Recycle") Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Notifications> arr = new ArrayList<>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long id = mCursor.getLong(NUM_COLUMN_ID);
                String name = mCursor.getString(NUM_COLUMN_NAME);
                String date = mCursor.getString(NUM_COLUMN_DATE);
                int way = mCursor.getInt(NUM_COLUMN_WAY);
                int repeatCounter = mCursor.getInt(NUM_COLUMN_REPEATCOUNTER);
                int repeatType = mCursor.getInt(NUM_COLUMN_REPEATTYPE);
                arr.add(new Notifications(id, name, date, way, repeatCounter, repeatType));
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    private static class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME+ " TEXT, " +
                    COLUMN_DATE+ " TEXT, " +
                    COLUMN_WAY + " INT, " +
                    COLUMN_REPEATCOUNTER + " INT, " +
                    COLUMN_REPEATTYPE + " INT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

}
