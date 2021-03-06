package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton fab;
    Adapter adapter;
    ArrayList<Model> notesList;
    DatabaseClass db;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        coordinatorLayout =findViewById(R.id.layout_main);
        recyclerView = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.btn_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AddNotesActivity.class);
                startActivity(intent);
            }
        });


        notesList = new ArrayList<>();

        db = new DatabaseClass(this);
        //create method fetching notes from database
        fetchAllNotesFromDatabase();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new Adapter(this,MainActivity.this,notesList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper helper =new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    void fetchAllNotesFromDatabase(){//now will get notes from database
        Cursor cursor = db.readAllData();

        if(cursor.getCount()==0)//it means there is no data
        {
            Toast.makeText(MainActivity.this,"No data to show",Toast.LENGTH_LONG).show();
        }
        else
        {
            while ((cursor.moveToNext()))
            {
                notesList.add(new Model(cursor.getString(0),cursor.getString(1),cursor.getString(2)));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);

        MenuItem search = menu.findItem(R.id.searchbar);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Search Notes Here");

        SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() { // ???????? ????????Query ???? ???????? ?????????????? ???? ????????????
            @Override
            public boolean onQueryTextSubmit(String query) {//???? ???? ?????? ???????? ???????? ?????? ?????? ???????????????? ???????? ???????????? ?????????? ?????????? submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {//???? ???? ?????????? ?????? ???? ???? ?????? ???????????? ????????????
                adapter.getFilter().filter(newText);
                return true;
            }
        };

        searchView.setOnQueryTextListener(listener);// ???? listener ?????????? ???????? ?????? ???? ?????????????? (??????????????????) ?????????? ?????? ?????????? ?????? ???????? ???????? ???????????? new SearchView.OnQueryTextListener()

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {



        if(item.getItemId()==R.id.delete_all_notes)
        {
            deleteAllNotes();
        }

         return super.onOptionsItemSelected(item);
    }

    private void deleteAllNotes(){
        DatabaseClass databaseClass =new DatabaseClass(MainActivity.this);
        databaseClass.deleteAllNotes();
        recreate();
    }


    ItemTouchHelper.SimpleCallback callback= new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            Model item =adapter.getList().get(position);

            adapter.removeItem(position);

            Snackbar snackbar = Snackbar.make(coordinatorLayout,"Item Deleted",Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {//this situation up when Snackbar is visible(means undo remove the item)
                    adapter.restoreItem(item,position);
                    recyclerView.scrollToPosition(position);
                }
            }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {// is non-visible(means remove the item)
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);

                            if(!(event==DISMISS_EVENT_ACTION))
                            {
                                DatabaseClass db = new DatabaseClass(MainActivity.this);
                                db.deleteSingleItem(item.getId());
                            }


                        }
                    });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    };



}