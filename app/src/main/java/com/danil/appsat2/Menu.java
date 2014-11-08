package com.danil.appsat2;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity {

	String category[] = {"Space Stations", "100 (or so) Brightest"};
	String cl4cat[]  =  {"SpaceStation",   "HundredBrightest" };


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setTitle("Please choose a category");
		setListAdapter(new ArrayAdapter<String>(Menu.this,
				android.R.layout.simple_list_item_1, category));
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String SelectedButton = cl4cat[position];
		try {

			Class<?> ourClass = Class.forName("com.danil.appsat2." + SelectedButton);
			Intent ourIntent = new Intent(Menu.this, ourClass);
			startActivity(ourIntent);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
