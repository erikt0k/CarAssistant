package com.example.carassistant.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.carassistant.ui.Notifications;
import com.example.carassistant.ui.Spendings;

import java.util.ArrayList;

public class DashboardDB {

    private static final String DATABASE_NAME = "carassistantDash.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "spendings";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_DATE = "Date";
    private static final String COLUMN_TYPE = "Type";
    private static final String COLUMN_SUM = "Sum";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_NAME = 1;
    private static final int NUM_COLUMN_DATE = 2;
    private static final int NUM_COLUMN_TYPE = 3;
    private static final int NUM_COLUMN_SUM = 4;



    private final SQLiteDatabase mDataBase;

    public DashboardDB(Context context) {
        DashboardDB.OpenDashHelper mOpenHelper = new DashboardDB.OpenDashHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(String name,String date, String type, int sum) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_TYPE, type);
        cv.put(COLUMN_SUM, sum);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

    public int update(Spendings sp) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_NAME, sp.getSpendingName());
        cv.put(COLUMN_DATE, sp.getSpendingDate());
        cv.put(COLUMN_TYPE, sp.getSpendingType());
        cv.put(COLUMN_SUM, sp.getSpendingSum());

        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?",new String[] { String.valueOf(sp.getSpendingId())});
    }

    public void deleteAll() {
        mDataBase.delete(TABLE_NAME, null, null);
    }

    public void delete(long id) {
        mDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public Spendings select(long id) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        mCursor.moveToFirst();
        String name = mCursor.getString(NUM_COLUMN_NAME);
        String date = mCursor.getString(NUM_COLUMN_DATE);
        String type = mCursor.getString(NUM_COLUMN_TYPE);
        int sum = mCursor.getInt(NUM_COLUMN_SUM);
        return new Spendings(id, name, date, type, sum);
    }

    public ArrayList<Spendings> selectAll() {
        @SuppressLint("Recycle") Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Spendings> arr = new ArrayList<>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long id = mCursor.getLong(NUM_COLUMN_ID);
                String name = mCursor.getString(NUM_COLUMN_NAME);
                String date = mCursor.getString(NUM_COLUMN_DATE);
                String type = mCursor.getString(NUM_COLUMN_TYPE);
                int sum = mCursor.getInt(NUM_COLUMN_SUM);
                arr.add(new Spendings(id, name, date, type, sum));
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    private static class OpenDashHelper extends SQLiteOpenHelper {

        OpenDashHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME+ " TEXT, " +
                    COLUMN_DATE+ " TEXT, " +
                    COLUMN_TYPE + " TEXT, " +
                    COLUMN_SUM + " INT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

}
