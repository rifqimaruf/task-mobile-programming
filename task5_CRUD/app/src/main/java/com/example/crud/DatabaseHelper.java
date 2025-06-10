package com.example.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT NOT NULL,"
                + KEY_EMAIL + " TEXT NOT NULL UNIQUE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public long addUser(String name, String email) {
        if (isInvalidInput(name, email)) return -1;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name.trim());
        values.put(KEY_EMAIL, email.trim());
        long result = db.insertOrThrow(TABLE_USERS, null, values);
        db.close();
        return result;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " ORDER BY " + KEY_NAME + " ASC", null);

        if (cursor.moveToFirst()) {
            do {
                userList.add(new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    public int updateUser(int id, String name, String email) {
        if (isInvalidInput(name, email)) return 0;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name.trim());
        values.put(KEY_EMAIL, email.trim());
        int result = db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return result;
    }

    public boolean deleteUser(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rowsAffected = db.delete(TABLE_USERS, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    private boolean isInvalidInput(String name, String email) {
        return name == null || email == null || name.trim().isEmpty() || email.trim().isEmpty();
    }
}