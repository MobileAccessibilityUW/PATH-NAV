package com.boruili.path_nav;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class ListTrainActivity extends Activity implements AdapterView.OnItemClickListener{
    EditText editText;
    Button addButton;
    ListView listView;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    SharedPreferences sharedPref;
    PrintStream ps;
    Scanner scan;
    File file;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_train);
        editText = (EditText) findViewById(R.id.editText);
        addButton = (Button) findViewById(R.id.addItem);
        listView = (ListView) findViewById(android.R.id.list);

        file = new File(this.getFilesDir(), "names");

        listItems = new ArrayList<String>();
        listItems.add("Red Square");

        try {
            scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while(scan.hasNextLine()) {
            String path = scan.nextLine().trim();
            Log.d("path names", path);
            listItems.add(path);
        }
        Log.d("++++++ BEFORE size", listItems.size() + "");



        while(scan.hasNextLine()) {
            String path = scan.nextLine().trim();
            Log.d("path names", path);
            listItems.add(path);
        }
        Log.d("+++++ AFTER size", listItems.size() + "");

        try {
            ps = new PrintStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        Log.d("DIR", this.getFilesDir() + "/");

        ///////// PARSE THE LOCATION //////////

        while(scan.hasNextLine()) {
            String path = scan.nextLine().trim();
            Log.d("path names", path);
            listItems.add(path);
        }
        Log.d("array size", listItems.size() + "");

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
        addButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                listItems.add(editText.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(ListTrainActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG)
                .show();
        Intent intent = new Intent(this, TrainPathActivity.class);
        intent.putExtra("FILE_NAME", parent.getItemAtPosition(position).toString() + "_path");
        startActivity(intent);

        while(scan.hasNextLine()) {
            String path = scan.nextLine().trim();
            Log.d("path names", path);
            listItems.add(path);
        }
        Log.d("BEFORE APP array size", listItems.size() + "");


        ps.append(parent.getItemAtPosition(position).toString() + '\n');

        Log.d("seperate", "++++++++++++");
        Log.d("path names", parent.getItemAtPosition(position).toString());
        while(scan.hasNextLine()) {
            String path = scan.nextLine().trim();
            Log.d("path names", path);
            listItems.add(path);
        }
        Log.d("array size", listItems.size() + "");
    }

    public Activity getActivity() {
        return this;
    }
}