package com.example.todolistt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.todolistt.adapter.RVadapter;
import com.example.todolistt.getsetgo.getset;
import com.example.todolistt.handler.DBHandler;

import java.util.ArrayList;

public class ViewData extends AppCompatActivity {

    private ArrayList<getset> taskList;
    private DBHandler dbHandler;
    private RVadapter rVadapter;
    private RecyclerView taskRV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        taskList = new ArrayList<>();
        dbHandler = new DBHandler(ViewData.this);

        taskList = dbHandler.read();
        rVadapter = new RVadapter(taskList,ViewData.this);
        taskRV = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewData.this, RecyclerView.VERTICAL, false);
        taskRV.setLayoutManager(linearLayoutManager);
        taskRV.setHasFixedSize(true);

        taskRV.setAdapter(rVadapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflates the menu(delete-all button & seacrh button)
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem search=menu.findItem(R.id.actionSearch);
        MenuItem delall = menu.findItem(R.id.deleteall);

        //delete all records
        delall.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Log.d("del","It works");
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewData.this);
                builder.setTitle("Confirmation!");
                builder.setMessage("Are you sure to delete ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int k) {
                        dbHandler.deleteAll();

                        int size = rVadapter.getItemCount();
                        if (size > 0) {
                            for (int i = 0; i < size; i++) {
                                rVadapter.delete(0);
                            }

                            rVadapter.notifyItemRangeRemoved(0, size);
                        }

                    }
                });
                builder.setNegativeButton("NO",null);
                builder.show();
                return false;
            }
        });

        //search tool
        SearchView searchView = (SearchView)search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                rVadapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}