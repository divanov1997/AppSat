package com.danil.appsat2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity {

	String category[] = {"Space Stations", "100 (or so) Brightest","SatMenu"};
	String cl4cat[]  =  {"SpaceStation",   "HundredBrightest","SatMenu"};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setTitle("Please choose a category");
		setListAdapter(new ArrayAdapter<String>(Menu.this,
				android.R.layout.simple_list_item_1, category));

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment dialog = new UpdateTLEDialog();
        dialog.show(ft,"dialog");

		
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

    public class UpdateTLEDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Update TLEs ?")
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                            Intent refreshTLE = new Intent(Menu.this, RefreshTLE.class);
                            startService(refreshTLE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

}
