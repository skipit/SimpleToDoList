package com.codepath.simpletodo;

/**
 * Created by pavan on 1/18/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ToDoItemDb extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "SimpleToDoListDB";
    private static final String TABLE_NAME = "todo";

    private static final String KEY_ID = "id";
    private static final String KEY_BODY = "body";
    private static final String KEY_COMPLETED = "completed";

    public ToDoItemDb(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_BODY + " TEXT,"
                + KEY_COMPLETED + " INTEGER" + ")";
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 1) {
            // Wipe older tables if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            // Create tables again
            onCreate(db);
        }
    }


    public void addTodoItem(TodoItem item) {
        Log.d("SimpleToDo Adding: ", item.getBody());
        // Open database connection
        SQLiteDatabase db = this.getWritableDatabase();
        // Define values for each field
        ContentValues values = new ContentValues();
        values.put(KEY_BODY, item.getBody());
        values.put(KEY_COMPLETED, item.isCompleted()?1:0);
        // Insert Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public TodoItem getTodoItem(int id) {
        // Open database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Construct and execute query
        Cursor cursor = db.query(TABLE_NAME,  // TABLE
                new String[] { KEY_ID, KEY_BODY, KEY_COMPLETED }, // SELECT
                KEY_ID + "= ?", new String[] { String.valueOf(id) },  // WHERE, ARGS
                null, null, null, null); // GROUP BY, HAVING, ORDER BY
        if (cursor != null)
            cursor.moveToFirst();
        // Load result into model object
        TodoItem item = new TodoItem(cursor.getString(1));
        item.setCompleted(cursor.getInt(2) == 1 );
        item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
        return item;
    }

    public List<TodoItem> getAllTodoItems() {
        List<TodoItem> todoItems = new ArrayList<TodoItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TodoItem item = new TodoItem(cursor.getString(1));
                item.setCompleted(cursor.getInt(2) == 1 );
                item.setId(cursor.getInt(0));
                todoItems.add(item);
            } while (cursor.moveToNext());
        }

        return todoItems;
    }

    public int getTodoItemCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        Log.d("Database: ", countQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    public int updateTodoItem(TodoItem item) {
        Log.d("SimpleToDo Updating: ", item.getBody() +","+item.getId());
        // Open database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Setup fields to update
        ContentValues values = new ContentValues();
        values.put(KEY_BODY, item.getBody());
        values.put(KEY_COMPLETED, item.isCompleted());
        // Updating row
        int result = db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
        // Close the database
        db.close();
        return result;
    }

    public void deleteTodoItem(TodoItem item) {
        Log.d("SimpleToDo Deleting: ", item.getBody() +","+item.getId());
        // Open database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete the record with the specified id
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
        // Close the database
        db.close();
    }
}
