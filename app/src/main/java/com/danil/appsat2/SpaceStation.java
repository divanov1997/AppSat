package com.danil.appsat2;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class SpaceStation extends ListActivity {
    /** Called when the activity is first created. */
   
	
	
	
   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ArrayList<String> category = SatName();
        
        setListAdapter(new ArrayAdapter<String>(SpaceStation.this,
				android.R.layout.simple_list_item_1, category));
        
    }
   
   public ArrayList<String> SatName(){ 
     
     String strLine;
	 ArrayList<String> stringList = new ArrayList<String>();
	 
	 
	
 try{
 // Open the file that is the first 
 // command line parameter

     AssetManager am = getAssets();
     InputStream inputStream = am.open("spacestations.txt");
 
 // Get the object of DataInputStream
 DataInputStream in = new DataInputStream(inputStream);
 BufferedReader br = new BufferedReader(new InputStreamReader(in));
 LineNumberReader reader = new LineNumberReader(br);
 
 //read every third line
 while ((strLine = reader.readLine()) != null){
	 if ((reader.getLineNumber()+2) % 3 == 0){
		 stringList.add(strLine);
	 }
 }

 //Close the input stream
 in.close();
 
 
   }catch (Exception e){	//Catch exception if any
 System.err.println("Error: " + e.getMessage());
 }
 
return stringList;
 
 }
   public ArrayList<String> Line1(){ 
	     
	     String strLine;
		 ArrayList<String> Line1List = new ArrayList<String>();
		
	 try{
	 
	 // Open the file that is the first 
	 // command line parameter

         AssetManager am = getAssets();
         InputStream inputStream = am.open("spacestations.txt");

	 // Get the object of DataInputStream
	 DataInputStream in = new DataInputStream(inputStream);
	 BufferedReader br = new BufferedReader(new InputStreamReader(in));
	 LineNumberReader reader = new LineNumberReader(br);
	 
	 //read every third line
	 while ((strLine = reader.readLine()) != null){
		 if ((reader.getLineNumber()+1) % 3 == 0){
			 Line1List.add(strLine);
		 }
	 }

	 //Close the input stream
	 in.close();
	 
	 
	   }catch (Exception e){	//Catch exception if any
	 System.err.println("Error: " + e.getMessage());
	 }
	 
	return Line1List;
	 
	 }   
   public ArrayList<String> Line2(){ 
	     
	     String strLine;
		 ArrayList<String> Line2List = new ArrayList<String>();
		 
	 try{
	 
	 // Open the file that is the first 
	 // command line parameter

         AssetManager am = getAssets();
         InputStream inputStream = am.open("spacestations.txt");

	 // Get the object of DataInputStream
	 DataInputStream in = new DataInputStream(inputStream);
	 BufferedReader br = new BufferedReader(new InputStreamReader(in));
	 LineNumberReader reader = new LineNumberReader(br);
	 
	 //populate array list by reading every third line
	 while ((strLine = reader.readLine()) != null){
		 if ((reader.getLineNumber()) % 3 == 0){
			 Line2List.add(strLine);
		 }
	 }

	 //Close the input stream
	 in.close();
	 
	 
	   }catch (Exception e){	//Catch exception if any
	 System.err.println("Error: " + e.getMessage());
	 }
	 
	return Line2List;
	 
	 }

@Override
protected void onListItemClick(ListView l, View v, int position, long id) {
	// TODO Auto-generated method stub
	super.onListItemClick(l, v, position, id);
	
	
	
	Intent intent = new Intent (SpaceStation.this, PosCalc.class);
	String satname = SatName().get(position);
	String tleline1 = Line1().get(position);
	String tleline2 = Line2().get(position);
	
	intent.putExtra("DataLine1", tleline1);
	intent.putExtra("DataLine2", tleline2);
	intent.putExtra("satname", satname);
	
	startActivity(intent);
}




   
}
