package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class EditItemActivity extends ActionBarActivity {

    Button btSave;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        btSave = (Button) findViewById(R.id.btSave);

        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(getIntent().getStringExtra("edit_value"));
        position = getIntent().getIntExtra("edit_position", 0);
        etEditItem.requestFocus();
    }

    public void onSaveItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etEditItem);
        String itemText = etNewItem.getText().toString();
        Intent data = new Intent();
        data.putExtra("new_value", itemText);
        data.putExtra("edit_position", position);
        setResult(RESULT_OK, data);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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


}
