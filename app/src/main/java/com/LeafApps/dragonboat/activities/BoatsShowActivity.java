package com.LeafApps.dragonboat.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.LeafApps.dragonboat.R;
import com.LeafApps.dragonboat.adapter.BoatAdapter;
import com.LeafApps.dragonboat.dao.BoatDAO;
import com.LeafApps.dragonboat.model.Boat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoatsShowActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {


    public static final String TAG = "ListBoatActivity";

    public static final int REQUEST_CODE_ADD_BOAT = 40;
    public static final String EXTRA_ADDED_BOAT = "extra_key_added_boat";
    public static int maxcount=10;

    private ListView mListviewBoats;
    private TextView mTxtEmptyListBoats;
    //private Button mBtnAddBoat;

    private BoatAdapter mAdapter;
    private List<Boat> mListBoats;
    private BoatDAO mBoatDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boats_show);

        // initialize views
        initViews();

        // fill the listView
        mBoatDao = new BoatDAO(this);
        mListBoats = mBoatDao.getAllBoats();
        if (mListBoats != null && !mListBoats.isEmpty()) {
            mTxtEmptyListBoats.setVisibility(View.INVISIBLE);
            Collections.reverse(mListBoats);
            mAdapter = new BoatAdapter(this,mListBoats );
            mListviewBoats.setAdapter(mAdapter);
        } else {
            mTxtEmptyListBoats.setVisibility(View.VISIBLE);
            mListviewBoats.setVisibility(View.GONE);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_boats_show, menu);
       // return true;
        return super.onCreateOptionsMenu(menu);
    }


    private void initViews() {
        this.mListviewBoats = (ListView) findViewById(R.id.listView_Boats);
        this.mTxtEmptyListBoats = (TextView) findViewById(R.id.textView_noBoats);
        //this.mBtnAddBoat = (Button) findViewById(R.id.btn_add_boat);
        //this.mBtnAddBoat.setOnClickListener(this); //jans coding
        this.mListviewBoats.setOnItemClickListener(this);
        this.mListviewBoats.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //adds a new boat in BoatInsert //man muss die reihenfolge ändern
           // case R.id.btn_add_boat: //jans coding
            //    Intent intent = new Intent(this, BoatInsert.class);
             //   startActivityForResult(intent, REQUEST_CODE_ADD_BOAT);
             //   break;

           // default:
           //     break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_delete:
                showDeleteAllDialogConfirmation();

                return true;
            //Hier die activity boatInsert starten
            case R.id.action_addboat:
                //starteboatinsert
                Intent intent = new Intent(this, BoatInsert.class);
                //intent.putExtra(CrewShowActivity.EXTRA_SELECTED_BOAT_ID, clickedBoat.getId());
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_BOAT) {
            if (resultCode == RESULT_OK) {
                // add the added boat to the listCompanies and refresh the listView
                if (data != null) {
                    Boat createdBoat = (Boat) data.getSerializableExtra(EXTRA_ADDED_BOAT);
                    if (createdBoat != null) {
                        if (mListBoats == null)
                            mListBoats = new ArrayList<Boat>();
                        mListBoats.add(createdBoat);

                        if (mListviewBoats.getVisibility() != View.VISIBLE) {
                            mListviewBoats.setVisibility(View.VISIBLE);
                            mTxtEmptyListBoats.setVisibility(View.GONE);
                        }

                        if (mAdapter == null) {
                            mAdapter = new BoatAdapter(this, mListBoats);
                            mListviewBoats.setAdapter(mAdapter);
                        } else {
                            mAdapter.setItems(mListBoats);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBoatDao.close();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Boat clickedBoat = mAdapter.getItem(position);
        Log.d(TAG, "clickedItem : " + clickedBoat.getName());
        Intent intent = new Intent(this, CrewShowActivity.class);
        intent.putExtra(CrewShowActivity.EXTRA_SELECTED_BOAT_ID, clickedBoat.getId());
        intent.putExtra(CrewShowActivity.EXTRA_SELECTED_BOAT_NAME, clickedBoat.getName());
        intent.putExtra(CrewShowActivity.EXTRA_SELECTED_TEAM_NAME, clickedBoat.getTeamName());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Boat clickedBoat = mAdapter.getItem(position);
        Log.d(TAG, "longClickedItem : " + clickedBoat.getName());
        showDeleteDialogConfirmation(clickedBoat);
        return true;
    }

    private void showDeleteDialogConfirmation(final Boat clickedBoat) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(R.string.boatshowdelete_popup_title);
        alertDialogBuilder.setMessage(getString(R.string.boatshowdelete_popup_text_1) + " \""
                + clickedBoat.getName() + "\" " + getString(R.string.boatshowdelete_popup_text_2));

        // set positive button YES message
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // delete the boat and refresh the list
                if (mBoatDao != null) {
                    //mBoatDao.deleteBoat(clickedBoat);
                    //mListBoats.size();

                    //Wenn ein boot gelöscht werden soll und es boote mit höherer id gibt, müssen die nachfolgenden boote auf die niedrigerern ids geschoben werden
                    for(long i = clickedBoat.getId(); i<mListBoats.size(); i++){
                        mBoatDao.replaceBoatById(i+1, i);
                    }
                    //es muss nicht geschoben werden, wenn das boot schon an der letzten stelle ist. dann wurd nur das letzte boot gelöscht und die for schleife läuft nicht
                    if ( clickedBoat.getId()==mListBoats.size())
                        mBoatDao.deleteBoat(clickedBoat);
                    mListBoats.remove(clickedBoat);

                    //refresh the listView
                    if (mListBoats.isEmpty()) {
                        mListBoats = null;
                        mListviewBoats.setVisibility(View.GONE);
                        mTxtEmptyListBoats.setVisibility(View.VISIBLE);
                    }
                    mAdapter.setItems(mListBoats);
                    mAdapter.notifyDataSetChanged();
                }

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

    private void showDeleteAllDialogConfirmation() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);


        //alertDialogBuilder.setTitle(R.string.boatshowdelete_popup_title);
        alertDialogBuilder.setMessage(getString(R.string.boatshowdelete_popup_text_3));

        // set positive button YES message
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                mBoatDao.deleteAllBoats(0); // 1 for reset database


                mListBoats = mBoatDao.getAllBoats();
                if (mListBoats.isEmpty()) {
                    mListBoats = null;
                    mListviewBoats.setVisibility(View.GONE);
                    mTxtEmptyListBoats.setVisibility(View.VISIBLE);
                }
                else {
                    mAdapter.setItems(mListBoats);
                    mAdapter.notifyDataSetChanged();
                }


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
