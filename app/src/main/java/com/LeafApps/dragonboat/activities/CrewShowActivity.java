package com.LeafApps.dragonboat.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.LeafApps.dragonboat.R;
import com.LeafApps.dragonboat.dao.BoatDAO;
import com.LeafApps.dragonboat.dao.CrewMemberDAO;
import com.LeafApps.dragonboat.dao.PictureDAO;

import com.LeafApps.dragonboat.model.CrewMember;
import java.util.Arrays;
import java.util.List;


public class CrewShowActivity extends ActionBarActivity
        implements View.OnClickListener{

    public static final String EXTRA_SELECTED_BOAT_ID = "SELECT_BOAT_ID";
    public static final String EXTRA_SELECTED_BOAT_NAME ="SELECT_BOAT_NAME";
    public static final String EXTRA_SELECTED_TEAM_NAME ="SELECT_TEAM_NAME";
    BoatDAO boatDao;
    CrewMemberDAO crewDao;
    List<CrewMember> crewmemberlist;


    // crew member buttons
    public Button main_button_0;
    public Button main_button_1;
    public Button main_button_2;
    public Button main_button_3;
    public Button main_button_4;
    public Button main_button_5;
    public Button main_button_6;
    public Button main_button_7;
    public Button main_button_8;
    public Button main_button_9;
    public Button main_button_10;
    public Button main_button_11;
    public Button main_button_12;
    public Button main_button_13;
    public Button main_button_14;
    public Button main_button_15;
    public Button main_button_16;
    public Button main_button_17;
    public Button main_button_18;
    public Button main_button_19;
    public Button main_button_20;
    public Button main_button_21;

    //Button to sort by weight
    private Button main_button_sort;

    // crew member buttons as an array
    private Button[] buttonArray;

    //Boat Id
    private static long boatId;
    private String boatName;
    private String teamName;
    //crewmember information
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mWeight;
    private RelativeLayout v;
    private CrewMember crewMember;
    //private String task;

    // variables for class functionality
    Toast toast;
    private static Context mContext;
    LayoutInflater inflater;
    View dialogView;
    Button testButton;
    View view;

    //crew id
    private long CrewId;
   //anfang
    public static PictureDAO pictureDAO;
//ende


//----------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crew_show);

        //instance the buttons
        mContext=this;

        boatDao = new BoatDAO(this);
        crewDao = new CrewMemberDAO(this);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getLongExtra(EXTRA_SELECTED_BOAT_ID, -1) != -1) {
                this.boatId = getIntent().getLongExtra(EXTRA_SELECTED_BOAT_ID, -1);
                this.boatName= getIntent().getStringExtra(EXTRA_SELECTED_BOAT_NAME);
                this.teamName= getIntent().getStringExtra(EXTRA_SELECTED_TEAM_NAME);
            }
        }
        //Anfang
        pictureDAO =new PictureDAO(this,null,boatDao,boatId);
        //Ende
        initviews();

        fillbuttons();

    }


/*
    @Override
    protected void onResume() {
        if (boatId == 0) {
            this.boatId = ChronometerActivity.boatId;
        }
        super.onResume();
    }
*/
    //Anfang
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        pictureDAO.onActivityResult(requestCode,resultCode,data);
    }
    //Ende

    public void fillbuttons() {

        buttonArray[0].setText(R.string.drummer);
        buttonArray[21].setText(R.string.helmsman);
        for(int i=1;i<buttonArray.length-1;i++){
            buttonArray[i].setText(R.string.paddler);
        }
        if (this.crewmemberlist != null ) {
            if (this.crewmemberlist.size() != 0) {

                for (int i = 0; i <= this.crewmemberlist.size() - 1; i = i + 1) {
                    if(crewmemberlist.get(i).getLastName().length()==0) {
                        buttonArray[(int) this.crewmemberlist.get(i).getId()]
                                .setText(this.crewmemberlist.get(i).getFirstName().substring(0, 1) + ". ");
                    }
                    if(crewmemberlist.get(i).getLastName().length()!=0) {
                        buttonArray[(int) this.crewmemberlist.get(i).getId()]
                                .setText(this.crewmemberlist.get(i).getFirstName().substring(0, 1) + ". " +
                                        this.crewmemberlist.get(i).getLastName().substring(0, 1) + ".");
                    }

                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crew_show, menu);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final View v = findViewById(R.id.action_photo);

                if (v != null) {
                    v.setOnLongClickListener(new CustomLongOnClickListener());
                }
            }
        });

        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_sort:
                showSortDialogConfirmation();
                return true;
            case R.id.action_photo:
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){



                    pictureDAO.makepictureorshow();

                return true; }
            case R.id.chronometerbtn:
               Intent chronometer=new Intent(this,ChronometerActivity.class);
                chronometer.putExtra(ChronometerActivity.EXTRA_SELECTED_BOAT_ID, boatId);
                chronometer.putExtra(ChronometerActivity.EXTRA_SELECTED_BOAT_NAME, boatName);
                chronometer.putExtra(ChronometerActivity.EXTRA_SELECTED_BOAT_TEAMNAME, teamName);

               startActivity(chronometer);
               return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        crewDao.close();
    }



    @Override
    public void onClick(View V) {

/*
    if (V == main_button_sort) {
        long j;
        CrewMember crewmember_0 = new CrewMember();
        CrewMember crewmember_21 = new CrewMember();
        List<CrewMember> crewmembers_old;

        try {
            crewmember_0 = crewDao.getCrewMemberOfBoatbyId(boatId, 0);
            crewDao.deleteCrewMember(crewmember_0);
        }
        catch (Exception e){
        }
        try {
            crewmember_21 = crewDao.getCrewMemberOfBoatbyId(boatId, 21);
            crewDao.deleteCrewMember(crewmember_21);
        }
        catch (Exception e){
        }

        crewmembers_old =crewDao.getCrewMembersOfBoat(this.boatId);

        this.crewmemberlist = crewDao.getCrewMembersOfBoatSortedByWeight(this.boatId);


        if (crewmemberlist != null & crewmemberlist.size() != 0) {
            //int id = (int) CrewId;

            for (int i = 0; i < crewmemberlist.size(); i++) {
                crewDao.deleteCrewMember(crewmembers_old.get(i));
                crewDao.createCrewMember( (long)  i + 21 - crewmemberlist.size(), boatId, crewmemberlist.get(i).getFirstName(),
                        crewmemberlist.get(i).getLastName(), crewmemberlist.get(i).getWeight());
            }
        }
        if( crewmember_0 != null) {
            crewDao.createCrewMember(crewmember_0.getId(), boatId, crewmember_0.getFirstName(),
                    crewmember_0.getLastName(), crewmember_0.getWeight());
        }
        if( crewmember_21 != null) {
            crewDao.createCrewMember(crewmember_21.getId(), boatId, crewmember_21.getFirstName(),
                    crewmember_21.getLastName(), crewmember_21.getWeight());
        }

        this.crewmemberlist = crewDao.getCrewMembersOfBoat(this.boatId);
        fillbuttons();

    }
*/


/*
        //checks if arraybutton was pushed
        //popup should only be used for the crewmemberbuttons
        boolean isarraybutton = false;
        for (int i = 0; i < buttonArray.length; i++) {
            if (V.getId() == buttonArray[i].getId()) {
                isarraybutton = true;
            }
        }
*/
        //if (isarraybutton == true) {
            inflater = this.getLayoutInflater();
            dialogView = inflater.inflate(R.layout.view_popup, null);
            mFirstName = (EditText) dialogView.findViewById(R.id.FirstName_view);
            mLastName = (EditText) dialogView.findViewById(R.id.LastName_view);
            mWeight = (EditText) dialogView.findViewById(R.id.Weight_view);



            testButton = (Button) findViewById(V.getId());


            CrewId = (long) Arrays.asList(buttonArray).indexOf(testButton);
    /*        CrewId = CrewId + 1; //das coding ist nur für Ruderer

        if (CrewId == 0) {
            if (testButton.getId() == main_button_0.getId()) {
                CrewId = 0;
            }
            if (testButton.getId() == main_button_21.getId()) {
                CrewId = 21;
            }
        }
*/

            crewMember = crewDao.getCrewMemberOfBoatbyId(boatId, CrewId);
            if (crewMember != null) {
                mFirstName.setText(crewMember.getFirstName());
                mLastName.setText(crewMember.getLastName());
                mWeight.setText(Integer.toString(crewMember.getWeight()));
            }

            showbuttonPopUptext();
       /* crewmemberlist = crewDao.getCrewMembersOfBoat(this.boatId);
        if (crewmemberlist != null & crewmemberlist.size() != 0) {
            int id = (int)(long)CrewId;

            for (int i=0;i<crewmemberlist.size();i++){
                if (crewmemberlist.get(i).getId()==id) {
                    mFirstName.setText(crewmemberlist.get(i).getFirstName().replaceAll(" ", ""));
                    mLastName.setText(crewmemberlist.get(i).getLastName().replaceAll(" ", ""));
                    mWeight.setText(Integer.toString(crewmemberlist.get(i).getWeight()).replaceAll(" ", ""));
            }
            }
        }*/

        }
   // }
    private void showbuttonPopUptext(){
        final AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setInverseBackgroundForced(true);
        helpBuilder.setTitle(R.string.addcrewmember_popup_title);
        helpBuilder.setMessage(R.string.addcrewmember_popup_text);
        helpBuilder.setCancelable(true);



        //view PopUp Inflator

        //+++++++++++++++++
        /*
         v = (RelativeLayout)inflater.inflate(R.layout.view_popup, null);
        mFirstName = (EditText) v.findViewById(R.id.FirstName_view);
        mLastName = (EditText) v.findViewById(R.id.LastName_view);
        mWeight = (EditText) v.findViewById(R.id.Weight_view);
*/
        //helpBuilder.setView(inflater.inflate(R.layout.view_popup, null))
        helpBuilder.setView(dialogView)
                // Add action buttons
               // .setCancelable(false)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    //bleibt leer, weil weiter unten überschrieben wird
                    }

                })
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        // Remember, create doesn't show the dialog
        final AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
        helpDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean wantToCloseDialog = false;

                //texte in string xml

                // es wird immer you did not enter a firstname angezeigt, wenn ein eintrag fehlt, last name und weight werden nicht angezeigt

                //die if bed lässt " " zu
                // if (mFirstName.getText().toString().matches("")) {

                mFirstName.setText(mFirstName.getText().toString().replaceAll(" ", ""));
                mLastName.setText(mLastName.getText().toString().replaceAll(" ", ""));
                mWeight.setText(mWeight.getText().toString().replaceAll(" ", ""));


                if(mFirstName.getText().toString().matches("")){
                    toast = Toast.makeText(mContext, R.string.noFirstName, Toast.LENGTH_SHORT);
                    toast.show();

                    /*
                    //last name nicht zwingend erforderlich, also rausgenommen
                } else if (mLastName.getText().toString().matches("")) {
                    Toast.makeText(mContext, "You did not enter a last name", Toast.LENGTH_SHORT);
                    toast.show();

                    */

                } else if (mWeight.getText().toString().matches("")) {
                    toast = Toast.makeText(mContext, R.string.noweight, Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    if(crewDao.getCrewMemberOfBoatbyId(boatId, CrewId)!=(null)){
                        crewDao.deleteCrewMember(crewDao.getCrewMemberOfBoatbyId(boatId, CrewId));
                    }
                    crewDao.createCrewMember(CrewId, boatId, mFirstName.getText().toString(),
                            mLastName.getText().toString(), Integer.valueOf(mWeight.getText().toString()));
                    testButton.setText(mFirstName.getText().toString().substring(0, 1) + ". ");
                    if ( mLastName.length() != 0) {
                        testButton.setText(testButton.getText().toString()
                                + mLastName.getText().toString().substring(0, 1) + ".");
                    }
                    helpDialog.dismiss();
                }

                //stuff end
                if(wantToCloseDialog)
                    helpDialog.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });


    }
/*
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ViewGroup parentViewGroup = (ViewGroup) view.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }
*/

    private void showSortDialogConfirmation() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.sort_crew);
        alertDialogBuilder.setMessage(R.string.sort_text);
        // set positive button YES message
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // delete the boat and refresh the list
                    long j;
                    CrewMember crewmember_0 = new CrewMember();
                    CrewMember crewmember_21 = new CrewMember();
                    List<CrewMember> crewmembers_old;

                    try {
                        crewmember_0 = crewDao.getCrewMemberOfBoatbyId(boatId, 0);
                        crewDao.deleteCrewMember(crewmember_0);
                    }
                    catch (Exception e){
                    }
                    try {
                        crewmember_21 = crewDao.getCrewMemberOfBoatbyId(boatId, 21);
                        crewDao.deleteCrewMember(crewmember_21);
                    }
                    catch (Exception e){
                    }

                    crewmembers_old =crewDao.getCrewMembersOfBoat(boatId);

                    crewmemberlist = crewDao.getCrewMembersOfBoatSortedByWeight(boatId);


                    if (crewmemberlist != null & crewmemberlist.size() != 0) {
                        //int id = (int) CrewId;

                        for (int i = 0; i < crewmemberlist.size(); i++) {
                            crewDao.deleteCrewMember(crewmembers_old.get(i));
                            crewDao.createCrewMember( (long)  i + 21 - crewmemberlist.size(), boatId, crewmemberlist.get(i).getFirstName(),
                                    crewmemberlist.get(i).getLastName(), crewmemberlist.get(i).getWeight());
                        }
                    }
                    if( crewmember_0 != null) {
                        crewDao.createCrewMember(crewmember_0.getId(), boatId, crewmember_0.getFirstName(),
                                crewmember_0.getLastName(), crewmember_0.getWeight());
                    }
                    if( crewmember_21 != null) {
                        crewDao.createCrewMember(crewmember_21.getId(), boatId, crewmember_21.getFirstName(),
                                crewmember_21.getLastName(), crewmember_21.getWeight());
                    }

                    crewmemberlist = crewDao.getCrewMembersOfBoat(boatId);
                    fillbuttons();



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

    public void initviews(){
        crewmemberlist = crewDao.getCrewMembersOfBoat(this.boatId);


        main_button_0 = (Button) findViewById(R.id.button_0);
        main_button_1 = (Button) findViewById(R.id.button_1);
        main_button_2 = (Button) findViewById(R.id.button_2);
        main_button_3 = (Button) findViewById(R.id.button_3);
        main_button_4 = (Button) findViewById(R.id.button_4);
        main_button_5 = (Button) findViewById(R.id.button_5);
        main_button_6 = (Button) findViewById(R.id.button_6);
        main_button_7 = (Button) findViewById(R.id.button_7);
        main_button_8 = (Button) findViewById(R.id.button_8);
        main_button_9 = (Button) findViewById(R.id.button_9);
        main_button_10 = (Button) findViewById(R.id.button_10);
        main_button_11 = (Button) findViewById(R.id.button_11);
        main_button_12 = (Button) findViewById(R.id.button_12);
        main_button_13 = (Button) findViewById(R.id.button_13);
        main_button_14 = (Button) findViewById(R.id.button_14);
        main_button_15 = (Button) findViewById(R.id.button_15);
        main_button_16 = (Button) findViewById(R.id.button_16);
        main_button_17 = (Button) findViewById(R.id.button_17);
        main_button_18 = (Button) findViewById(R.id.button_18);
        main_button_19 = (Button) findViewById(R.id.button_19);
        main_button_20 = (Button) findViewById(R.id.button_20);
        main_button_21 = (Button) findViewById(R.id.button_21);

        //create on click events
        main_button_0.setOnClickListener(this);
        main_button_1.setOnClickListener(this);
        main_button_2.setOnClickListener(this);
        main_button_3.setOnClickListener(this);
        main_button_4.setOnClickListener(this);
        main_button_5.setOnClickListener(this);
        main_button_6.setOnClickListener(this);
        main_button_7.setOnClickListener(this);
        main_button_8.setOnClickListener(this);
        main_button_9.setOnClickListener(this);
        main_button_10.setOnClickListener(this);
        main_button_11.setOnClickListener(this);
        main_button_12.setOnClickListener(this);
        main_button_13.setOnClickListener(this);
        main_button_14.setOnClickListener(this);
        main_button_15.setOnClickListener(this);
        main_button_16.setOnClickListener(this);
        main_button_17.setOnClickListener(this);
        main_button_18.setOnClickListener(this);
        main_button_19.setOnClickListener(this);
        main_button_20.setOnClickListener(this);
        main_button_21.setOnClickListener(this);

        //Button to sort by weight
       // main_button_sort = (Button) findViewById(R.id.button_sort);
       // main_button_sort.setOnClickListener(this);



        buttonArray=new Button[]{
                main_button_0,
                main_button_1,
                main_button_2,
                main_button_3,
                main_button_4,
                main_button_5,
                main_button_6,
                main_button_7,
                main_button_8,
                main_button_9,
                main_button_10,
                main_button_11,
                main_button_12,
                main_button_13,
                main_button_14,
                main_button_15,
                main_button_16,
                main_button_17,
                main_button_18,
                main_button_19,
                main_button_20,
                main_button_21
        };

    }

    private class CustomLongOnClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {


            showpictureDialogConfirmation();

            return true;
        }

        private void showpictureDialogConfirmation() {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CrewShowActivity.mContext);
           // alertDialogBuilder.setTitle("Titel");
            alertDialogBuilder.setMessage(R.string.photo_text);
            // set positive button YES message
            alertDialogBuilder.setPositiveButton(R.string.photo_newphoto, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PackageManager pm = CrewShowActivity.mContext.getPackageManager();

                    if ( pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) |
                            pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
                        CrewShowActivity.pictureDAO.dispatchTakePictureIntent();
                        dialog.dismiss();
                    }
                }
            });

            // set neutral button OK //weiss noch nicht was das macht
            alertDialogBuilder.setNeutralButton(R.string.Choosepic, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CrewShowActivity.pictureDAO.getpicturefromgallerybyuser();
                    dialog.dismiss();
                }
            });
            alertDialogBuilder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

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
}
