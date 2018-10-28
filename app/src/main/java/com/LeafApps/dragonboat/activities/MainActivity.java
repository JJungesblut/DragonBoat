package com.LeafApps.dragonboat.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.LeafApps.dragonboat.R;
import com.LeafApps.dragonboat.dao.BoatDAO;
//Ads
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;



public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    public Button main_button_select;
    public Button main_button_insert;
  //  public Button main_button_drop;
    public BoatDAO boatDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set Buttons
        main_button_select = (Button) findViewById(R.id.main_button_select);
        main_button_insert = (Button) findViewById(R.id.main_button_insert);
      //  main_button_drop = (Button) findViewById(R.id.main_button_drop);
        //create on click events
        main_button_select.setOnClickListener(this);
        main_button_insert.setOnClickListener(this);
       // main_button_drop.setOnClickListener(this);
        //Boat DAO
        boatDAO = new BoatDAO(this);
        //Ads
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if ( v == main_button_insert){
         //goto bootSelect Activity
            Intent BoatInsert=new Intent(this,BoatInsert.class);
            startActivity(BoatInsert);
        }
        if ( v == main_button_select){
            //goto boatInsert Activity
            Intent BoatSelect=new Intent(this,BoatsShowActivity.class);
            startActivity(BoatSelect);
        }
        /*if ( v == main_button_drop){
            boatDAO.deleteAllBoats(0);
        }*/
    }
    @Override
         protected void onDestroy() {
        super.onDestroy();
        boatDAO.close();
    }

}
