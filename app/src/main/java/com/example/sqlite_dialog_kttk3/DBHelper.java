package com.example.sqlite_dialog_kttk3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "book_list", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table Book(id integer primary key, title text, author text)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "drop table if exists Book";
        db.execSQL(query);
        onCreate(db);
    }

    public boolean insertBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", book.getId());
        contentValues.put("title", book.getTitle());
        contentValues.put("author", book.getAuthor());
        db.insert("Book", null, contentValues);
        db.close();
        return true;
    }

    public Book getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from Book where id = " + id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null)
            cursor.moveToFirst();
        Book book = new Book(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        cursor.close();
        return book;
    }

    public ArrayList<Book> getAllBook() {
        ArrayList<Book> list = new ArrayList<Book>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from Book";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null)
            cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            list.add(new Book(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return list;
    }

    public boolean deleteBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "delete from Book where id = " + id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor == null) {
            return false;
        } else {
            cursor.moveToFirst();
            cursor.close();
        }
        return true;
    }

    public boolean updateBook(int id, String title, String author) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("title", title);
        contentValues.put("author", author);
        if (db.update("Book", contentValues, "id = ?", new String[]{String.valueOf(id)}) > 0) {
            db.close();
            return true;
        }
        return false;
    }
}
