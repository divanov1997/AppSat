package com.danil.appsat2;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class AndroidDashboardDesignActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dashboard_layout);

        DashboardLayout dashl = (DashboardLayout) findViewById(R.id.DashboardLayout);
        //create parameters for button display
        DashboardLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //Create buttons dynamically
        int i;//loop counter
        String[] files = {"1","2","3"};
        String[] categorynames = {"Special Interest Satellites","Miscellaneous Satellites","Update TLEs"};
        int[] id = {R.id.btn_sisSat,R.id.btn_miscSat,R.id.btn_updateTLE};


        for(i=0; i <files.length; i++){

            //create Button
            final Button btn = new Button(this);
            //Set button id
            btn.setId(id[i]);
            btn.setText(categorynames[i]);
            //set layout parameters for buttons
            btn.setLayoutParams(params);

        }
        /**
         * Creating all buttons instances
         * */

         // Set on click listner for Special Interest Satellites button
        findViewById(R.id.btn_sisSat).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), HundredBrightest.class);
                startActivity(i);
            }
        });
        // Miscellaneous Satellites Button
        findViewById(R.id.btn_miscSat).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), SpaceStation.class);
                startActivity(i);
            }
        });
        // Update TLEs button
        findViewById(R.id.btn_updateTLE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment dialog = new UpdateTLEDialog();
                dialog.show(ft,"dialog");
            }
        });

    }
    public class UpdateTLEDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Update TLEs ?");
            builder.setMessage("Would you like to update TLE data ?")
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent refreshTLE = new Intent(AndroidDashboardDesignActivity.this, RefreshTLE.class);
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