package com.example.sqlite_dialog_kttk3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText et_id, et_title, et_author;
    Button button_save, button_select, button_update, button_delete, button_exit;
    GridView gv_display;
    ArrayAdapter<String> adapter;
    Dialog dialog;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventClickExit();
    }

    private void eventClickExit() {
        button_exit = (Button) findViewById(R.id.button_exit);
        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void evenClickUpdate() {
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id1 = et_id.getText().toString();
                String title = et_title.getText().toString();
                String author = et_author.getText().toString();
                if (isEmpty(id1, title, author)) {
                    int id = Integer.parseInt(et_id.getText().toString());
                    boolean isUpdate = dbHelper.updateBook(id, title, author);
                    if (isUpdate) {
                        notifyList();
                        clear();
                        Toast.makeText(getApplicationContext(), "Updated Successfully !", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error ! Update Unsuccessfully.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter full information !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void evenClickDelete() {
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = et_id.getText().toString();
                if (!id.isEmpty()) {
                    int idkq = Integer.parseInt(id);
                    boolean del = dbHelper.deleteBook(idkq);
                    if (del) {
                        adapter.notifyDataSetChanged();
                        notifyList();
                        clear();
                        Toast.makeText(getApplicationContext(), "Deleted Successfully !", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error ! Delete Unsuccessfully.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter ID !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void evenClickSave() {
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
                String id = et_id.getText().toString();
                String title = et_title.getText().toString();
                String author = et_author.getText().toString();
                if (isEmpty(id, title, author)) {
                    book.setId(Integer.parseInt(et_id.getText().toString()));
                    book.setTitle(title);
                    book.setAuthor(author);
                    if (dbHelper.insertBook(book)) {
                        notifyList();
                        clear();
                        Toast.makeText(getApplicationContext(), "Saved Successfully !", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error ! Save Unsuccessfully.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter full information !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void evenClickSelect() {
        button_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                ArrayList<Book> booklist = new ArrayList<>();
                String id = et_id.getText().toString();
                if (!id.isEmpty()) {
                    int idkq = Integer.parseInt(id);
                    Book book = dbHelper.getBook(idkq);
                    list.add(book.getId() + "");
                    list.add(book.getTitle());
                    list.add(book.getAuthor());
                } else {
                    booklist = dbHelper.getAllBook();
                    for (Book b : booklist) {
                        list.add(b.getId() + "");
                        list.add(b.getTitle());
                        list.add(b.getAuthor());
                    }
                }
                adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list);
                gv_display.setAdapter(adapter);
            }
        });
    }

    private void notifyList() {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<Book> booklist = new ArrayList<>();
        booklist = dbHelper.getAllBook();
        for (Book b : booklist) {
            list.add(b.getId() + "");
            list.add(b.getTitle());
            list.add(b.getAuthor());
        }
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list);
        gv_display.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_book:
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Book Information");
        dialog.setContentView(R.layout.dialog);
        mappingView();
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        evenClickSelect();
        evenClickSave();
        evenClickDelete();
        evenClickUpdate();
    }

    private void clear() {
        et_id.setText("");
        et_title.setText("");
        et_author.setText("");
        et_id.requestFocus();
    }

    private boolean isEmpty(String id, String title, String author) {
        if (id.isEmpty() || title.isEmpty() || author.isEmpty()) {
            return false;
        }
        return true;
    }

    private void mappingView() {
        //EditText
        et_id = (EditText) dialog.findViewById(R.id.editTextID);
        et_title = (EditText) dialog.findViewById(R.id.editTextTitle);
        et_author = (EditText) dialog.findViewById(R.id.editTextName);

        //GridView
        gv_display = (GridView) dialog.findViewById(R.id.gridView_listItem);

        //DBHelper
        dbHelper = new DBHelper(this);

        //Button
        button_save = (Button) dialog.findViewById(R.id.buttonSave);
        button_select = (Button) dialog.findViewById(R.id.buttonSelect);
        button_delete = (Button) dialog.findViewById(R.id.buttonDelete);
        button_update = (Button) dialog.findViewById(R.id.buttonUpdate);
    }
}
