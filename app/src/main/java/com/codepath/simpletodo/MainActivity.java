package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    List<TodoItem> items;
    TodoItemsAdapter itemsAdapter;
    ListView lvItems;
    Button btnAddItem;
    ToDoItemDb db;


    public static final int REQ_CODE_EDIT_VALUE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new ToDoItemDb(this);

        lvItems = (ListView) findViewById(R.id.lvItems);
        items = db.getAllTodoItems();
        itemsAdapter = new TodoItemsAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        btnAddItem = (Button) findViewById(R.id.btnAddItem);

        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener (
          new AdapterView.OnItemLongClickListener() {
              @Override
              public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                  TodoItem removeItem = items.get(position);
                  db.deleteTodoItem(removeItem); //-- Remove from Database
                  items.remove(position); //-- Remove from List
                  itemsAdapter.notifyDataSetChanged(); //-- Update the Adapter
                  return true;
              }
          }
        );

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        i.putExtra("edit_value", items.get(position).getBody());
                        i.putExtra("edit_position", position);
                        startActivityForResult(i, REQ_CODE_EDIT_VALUE);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        TodoItem newItem = new TodoItem(itemText);
        newItem.setCompleted(false);
        items.add(newItem);
        itemsAdapter.notifyDataSetChanged();
        db.addTodoItem(newItem);
        etNewItem.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQ_CODE_EDIT_VALUE) {

            String newValue = data.getExtras().getString("new_value");
            int position = data.getIntExtra("edit_position",0);
            TodoItem oldItem = items.get(position); //-- Get the Old Item
            oldItem.setBody(newValue);  //-- Update the Body
            db.updateTodoItem(oldItem); //-- Update the Database
            items.set(position, oldItem); //-- Update the items for display
            itemsAdapter.notifyDataSetChanged(); //-- Notify the Adapter to update itself.
        }
    }
}
