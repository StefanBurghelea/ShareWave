package com.example.sharewave.auth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sharewave.classes.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String table = "user";

    // colunas
    public static final String COLUNA_ID = "id";
    public static final String COLUNA_NAME = "name";
    public static final String COLUNA_EMAIL = "email";
    public static final String COLUNA_FIRSTNAME = "firstname";
    public static final String COLUNA_LASTNAME = "lastname";

    // Creating table query
    static final String CREATE_TABLE = "create table " + table + "(" +
            COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + COLUNA_NAME + " TEXT NOT NULL," +
            "" + COLUNA_EMAIL + " TEXT NOT NULL," +
            "" + COLUNA_FIRSTNAME + " TEXT NOT NULL," +
            "" + COLUNA_LASTNAME+ " TEXT NOT NULL);";


    // Database Information
    static final String DB_NAME = "user.DB";

    // database version
    static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        //db.execSQL( "insert into "+table+"(titulo, texto,data) values ('Teste', 'Teste','2020');" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + table);
        onCreate(db);
    }



    public List<User> getUser() {
        List<User> users = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + table;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getString(cursor.getColumnIndex(COLUNA_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(COLUNA_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUNA_EMAIL)));
                user.setFirstname(cursor.getString(cursor.getColumnIndex(COLUNA_FIRSTNAME)));
                user.setLastname(cursor.getString(cursor.getColumnIndex(COLUNA_LASTNAME)));


                users.add(user);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return users;
    }

    public boolean inserirUser(String id,String name,String email,String firstname,String lastname){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id",id);
        contentValues.put("name",name);
        contentValues.put("email",email);
        contentValues.put("firstname",firstname);
        contentValues.put("lastname",lastname);

        db.insert(table,null,contentValues);
        return true;
    }

    public int usersGuardados() {
        String countQuery = "SELECT  * FROM " + table;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public void deleteUsers() {

        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + table);


    }

}
