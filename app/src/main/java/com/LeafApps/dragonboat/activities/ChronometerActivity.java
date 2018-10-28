package com.LeafApps.dragonboat.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.LeafApps.dragonboat.R;
import com.LeafApps.dragonboat.dao.BoatDAO;

public class ChronometerActivity extends ActionBarActivity implements View.OnClickListener {

    public static final String EXTRA_SELECTED_BOAT_ID = "SELECT_BOAT_ID";
    public static final String EXTRA_SELECTED_BOAT_NAME = "SELECT_BOAT_NAME";
    public static final String EXTRA_SELECTED_BOAT_TEAMNAME = "SELECT_BOAT_TEAMNAME";
    private Intent intent;

    public static long boatId;
    private String BoatName;
    private String TeamName;


    private Button startButton;
    private Button pauseButton;
    private Button savebtn;
    private Button resetbtn;
    private TextView besttime;

    private Toast toast;
    private static Context mContext;
    // private String s;

    private TextView timerValue;

    private long startTime = 0L;

    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    boolean isstart = true;

    private BoatDAO mBoatDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometer);
        mContext=this;

        initialViews();

        besttime.setText("0:00:000");
        if(mBoatDao.getTime(boatId)!=null) {
            besttime.setText(mBoatDao.getTime(boatId));
        }

        //resetbtn.setVisibility(View.GONE); //hide is resetet
    }

    private void initialViews(){
        mBoatDao = new BoatDAO(this);

        boatId = getIntent().getLongExtra(EXTRA_SELECTED_BOAT_ID, -1);
        BoatName = getIntent().getStringExtra(EXTRA_SELECTED_BOAT_NAME);
        TeamName = getIntent().getStringExtra(EXTRA_SELECTED_BOAT_TEAMNAME);

        timerValue = (TextView) findViewById(R.id.timerValue);
        besttime = (TextView)findViewById(R.id.besttime);

        startButton = (Button) findViewById(R.id.startButton);
        savebtn = (Button) findViewById(R.id.save);
        resetbtn = (Button) findViewById(R.id.reset);

        startButton.setOnClickListener(this);
        savebtn.setOnClickListener(this);
        resetbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startButton:
                        if(!isstart){//pausieren
                            timeSwapBuff += timeInMilliseconds;
                            customHandler.removeCallbacks(updateTimerThread);
                            startButton.setText("Start");
                        }
                        if(isstart == true){//starten
                            // timeSwapBuff += timeInMilliseconds;
                            startTime = SystemClock.uptimeMillis();
                            customHandler.postDelayed(updateTimerThread, 0);
                            startButton.setText("Pause");
                        }
                        if(isstart){
                            isstart=false;
                        }
                        else {
                            isstart = true;
                        }
                        //resetbtn.setVisibility(View.VISIBLE); //nicht löschen
                        break;
            case R.id.save:
                        newBestTimeAlertDialog();
                        break;
            case R.id.reset:
                        timeSwapBuff = 0;
                        customHandler.removeCallbacks(updateTimerThread);
                        timerValue.setText("" + 0 + ":"
                                + String.format("%02d", 00) + ":"
                                + String.format("%03d", 000));
                        //resetbtn.setVisibility(View.GONE);//hide is resetet

                        if(!isstart){
                            isstart=true;
                            startButton.setText("Start");
                        }
                         break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chronometer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
/*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText("" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }
    };

    private void newBestTimeAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);


        //alertDialogBuilder.setTitle(R.string.boatshowdelete_popup_title);
        alertDialogBuilder.setMessage(getString(R.string.alert_new_best_time));

        // set positive button YES message
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
        //        s=timerValue.getText().toString();
        //        s=s+" Boatid:"+boatId+" BoatName:" + BoatName + " TeamName:" + TeamName + " " ;
        //        toast = Toast.makeText(mContext, s, Toast.LENGTH_SHORT);
        //        toast.show();
                mBoatDao.setTime(boatId, timerValue.getText().toString());
                besttime.setText(timerValue.getText().toString());

                dialog.dismiss();
            }
        });

        // set neutral button OK //weiss noch nicht was das macht
        alertDialogBuilder.setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }

}
