package com.example.randomscripturegeneratorapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class FilterBookActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_filter_activity);

        //Drop down menu for Works
        Spinner works = (Spinner) findViewById(R.id.dropDown_works);
        works.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> workAdapter = ArrayAdapter.createFromResource(this,
                R.array.works, android.R.layout.simple_spinner_item);
        workAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        works.setAdapter(workAdapter);

        works.getOnItemSelectedListener();

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
            String work = parent.getItemAtPosition(i).toString();
            Spinner spinner = (Spinner) parent;
            //Differentiate between which spinner was changed
            if(spinner.getId() == R.id.dropDown_works)
            {
                updateBookSpinner(parent.getItemAtPosition(i).toString());
            }
            else if(spinner.getId() == R.id.dropDown_books)
            {
                //This is where the filter books function starts!
            }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void updateBookSpinner(String position)
    {
        Spinner books = (Spinner) findViewById(R.id.dropDown_books);
        books.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> bookAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        switch(position)
        {
            case "Old Testament":
                bookAdapter = ArrayAdapter.createFromResource(this,R.array.ot, android.R.layout.simple_spinner_dropdown_item);
                bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                books.setAdapter(bookAdapter);
                break;
            case "New Testament":
                bookAdapter = ArrayAdapter.createFromResource(this,
                        R.array.nt, android.R.layout.simple_spinner_item);
                bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                books.setAdapter(bookAdapter);
                break;
            case "Book of Mormon":
                bookAdapter = ArrayAdapter.createFromResource(this,
                        R.array.bom, android.R.layout.simple_spinner_item);
                bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                books.setAdapter(bookAdapter);
                break;
            case "Doctrine and Covenants":
                bookAdapter = ArrayAdapter.createFromResource(this,
                        R.array.doc, android.R.layout.simple_spinner_item);
                bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                books.setAdapter(bookAdapter);
                break;
            case "Pearl of Great Price":
                bookAdapter = ArrayAdapter.createFromResource(this,
                        R.array.pogp, android.R.layout.simple_spinner_item);
                bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                books.setAdapter(bookAdapter);
                break;
        }
        books.getOnItemSelectedListener();

    }
    public void goHome(View view) {
        Intent goHome = new Intent(this, MainActivity.class);
        startActivity(goHome);
    }
}

