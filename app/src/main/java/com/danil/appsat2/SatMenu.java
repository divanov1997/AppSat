package com.danil.appsat2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SatMenu extends Activity {

    private Spinner sat_name_spinner;// = (Spinner) findViewById(R.id.spinner_satnames);
    private Spinner cat_spinner ;//= (Spinner) findViewById(R.id.spinner_cat);
    private ArrayAdapter<String> dataAdapter;
    private Button btnSubmit;

    //values for satellite selection (sat names, tle lines from corresponding files)
    String category[] = {"Space Stations", "100 (or so) Brightest"};
    //filenames
    String siscat[] = {"tle-new.txt","stations.txt","visual.txt","1999-025.txt","iridium-33-debris.txt",
            "cosmos-2251-debris.txt","2012-044.txt"};
    String filename; //selected category filename

    ArrayList<String> sat_name, tle_line1, tle_line2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sat_menu);

        //create spinner objects
        cat_spinner = (Spinner) findViewById(R.id.spinner_cat);
        sat_name_spinner = (Spinner) findViewById(R.id.spinner_satnames);

        //initialise spinners
        initSpinner(cat_spinner, siscat);

        //Change lists according to user selection
        cat_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                filename = siscat[pos];
                addItemsSpinner(sat_name_spinner, ReadFile(filename, 1));
                dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //Listen to button changes
        addListenerOnButton();

    }

    // add items into spinner dynamically
    public void addItemsSpinner(Spinner spinner, List<String> list) {
        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Test");
        spinner.setAdapter(dataAdapter);
    }

    // add items into spinner dynamically
    public void initSpinner(Spinner spinner, String spinner_items[]) {
        List<String> list = new ArrayList<String>();
        int i; //var for for loop
        for (i = 0; i < spinner_items.length; i = i + 1) {
            list.add(spinner_items[i]);
        }
        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Test");
        spinner.setAdapter(dataAdapter);
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        sat_name_spinner = (Spinner) findViewById(R.id.spinner_satnames);
        cat_spinner = (Spinner) findViewById(R.id.spinner_cat);
        btnSubmit = (Button) findViewById(R.id.button);

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("FILENAME : ", filename);
                //Find tle line 1 based on satelites name position
                String tle_line1 = ReadFile(filename,2).toArray(new String[ReadFile(filename, 2).size()])[sat_name_spinner.getSelectedItemPosition()];
                //Find tle line 2 based on satelites name position
                String tle_line2 = ReadFile(filename,3).toArray(new String[ReadFile(filename, 3).size()])[sat_name_spinner.getSelectedItemPosition()];

                Intent i = new Intent(SatMenu.this, MapsActivity.class);
                i.putExtra("SatName",sat_name_spinner.getSelectedItem().toString());
                i.putExtra("TLE_Line1", tle_line1);
                i.putExtra("TLE_Line2", tle_line2);
                startActivity(i);
            }

        });
    }

    //Reads a file, but only keeps every third line and puts it in a string List
    public ArrayList<String> ReadFile(String filename, int firstline){

        String strLine;
        ArrayList<String> arrayList = new ArrayList<String>();

        try{
            // Open the file
            InputStream inputStream = new FileInputStream(new File(this.getFilesDir(),filename));

            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(inputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            LineNumberReader reader = new LineNumberReader(br);

            //read every third line
            while ((strLine = reader.readLine()) != null){
                if ((reader.getLineNumber()+3-firstline) % 3 == 0){
                    arrayList.add(strLine);
                }
            }

            //Close the input stream
            in.close();


        }catch (Exception e){	//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }

        return arrayList;

    }

}