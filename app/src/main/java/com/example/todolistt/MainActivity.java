package com.example.todolistt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todolistt.getsetgo.getset;
import com.example.todolistt.handler.DBHandler;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button add,view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHandler dbHandler = new DBHandler(this);
        editText = findViewById(R.id.idTask);
        add = findViewById(R.id.idAdd);
        view = findViewById(R.id.idView);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter task",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    dbHandler.addNew(editText.getText().toString());
                    editText.setText("");
                    Intent i = new Intent(MainActivity.this,ViewData.class);
                    startActivity(i);
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,ViewData.class);
                startActivity(i);
            }
        });

          //logbilla
        ArrayList<getset> a = dbHandler.read();
        for (getset contact : a) {
            Log.d("dbharry",
                    "Name: " + contact.getItem() + "\n");
    }
}
}