package com.LeafApps.dragonboat.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.LeafApps.dragonboat.R;
import com.LeafApps.dragonboat.dao.BoatDAO;
import com.LeafApps.dragonboat.model.Boat;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class BoatInsert extends ActionBarActivity implements View.OnClickListener {

    //xml elements
    private Button insert_button_gotoShowBoat;
    private EditText insert_edittext_BoatName;
    private EditText insert_edittext_TeamName;
    private Button btnsaveboat;
    // shared preferences for data transfer
    //SharedPreferences sharedPref;
    //data store object for Boat
    BoatDAO boatDao;
    Boat boat;

    Toast toast;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boat_insert);

        //instances
        insert_edittext_BoatName = (EditText) findViewById(R.id.boatname);
        insert_edittext_TeamName = (EditText) findViewById(R.id.teamname);
        insert_button_gotoShowBoat = (Button) findViewById(R.id.boatshow);
        btnsaveboat=(Button) findViewById(R.id.btnsaveboat);
        //on click event
        insert_button_gotoShowBoat.setOnClickListener(this);
        btnsaveboat.setOnClickListener(this);
        //shared preferences
        //sharedPref = getSharedPreferences("BoatInformation", MODE_PRIVATE);

        //data store object for Boat
        boatDao = new BoatDAO(this);
        //Ads
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_boat_insert, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //save data into shared preferences
        //sharedPref.getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        if (v == insert_button_gotoShowBoat){
            Intent intent = new Intent(this, BoatsShowActivity.class);
            startActivity(intent);
        }
        if (v == btnsaveboat){

            //boat=boatDao.createBoat(insert_edittext_BoatName.getText().toString(),
            //        insert_edittext_TeamName.getText().toString());
            insert_edittext_BoatName.setText(insert_edittext_BoatName.getText().toString().replaceAll(" ", ""));
            insert_edittext_TeamName.setText(insert_edittext_TeamName.getText().toString().replaceAll(" ", ""));

            if(insert_edittext_BoatName.getText().toString().matches("")) {
                toast = Toast.makeText(this,R.string.noboatname, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            if(insert_edittext_TeamName.getText().toString().matches("")) {
                toast = Toast.makeText(this, R.string.noteamname, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            boat = boatDao.createBoatWithId(insert_edittext_BoatName.getText().toString(),
                            insert_edittext_TeamName.getText().toString());

            Intent intent = new Intent(this, CrewShowActivity.class);
            intent.putExtra(CrewShowActivity.EXTRA_SELECTED_BOAT_ID,boat.getId());
            startActivity(intent);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        boatDao.close();
    }

}
