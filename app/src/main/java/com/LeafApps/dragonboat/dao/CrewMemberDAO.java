package com.LeafApps.dragonboat.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.LeafApps.dragonboat.model.Boat;
import com.LeafApps.dragonboat.model.CrewMember;

import java.util.ArrayList;
import java.util.List;


public class CrewMemberDAO {

    public static final String TAG = "CrewMemberDAO";

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private String[] mAllColumns = {DBHelper.COLUMN_CREWMEMBER_ID, DBHelper.COLUMN_CREW_BOAT_ID,
            DBHelper.COLUMN_CREWMEMBER_FIRST_NAME, DBHelper.COLUMN_CREWMEMBER_LAST_NAME,
            DBHelper.COLUMN_CREWMEMBER_WEIGHT};

    public CrewMemberDAO(Context context) {
        mDbHelper = new DBHelper(context);
        this.mContext = context;
        // open the database
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public CrewMember createCrewMember(long id, long boat_id, String firstName, String lastName,
                                       int weight) {
        //Cursor cursor1 = mDatabase.query(DBHelper.TABLE_CREWMEMBERS,
        //        mAllColumns, null, null, null, null, null);
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_CREWMEMBER_ID, id);
        values.put(DBHelper.COLUMN_CREWMEMBER_FIRST_NAME, firstName);
        values.put(DBHelper.COLUMN_CREWMEMBER_LAST_NAME, lastName);
        values.put(DBHelper.COLUMN_CREWMEMBER_WEIGHT, weight);
        values.put(DBHelper.COLUMN_CREW_BOAT_ID, boat_id);
        //alues.put(DBHelper.COLUMN_CREWMEMBER_TASK, task);
        //creates a DB line

       // long insertId =
                mDatabase.insert(DBHelper.TABLE_CREWMEMBERS, null, values); //insert id ist row id, diese wird implizit erstellt
        //creates a table from the DB for Cursor. Here only one line becouse of the row id
       /* Cursor cursor = mDatabase.query(DBHelper.TABLE_CREWMEMBERS,
                mAllColumns, DBHelper.COLUMN_CREWMEMBER_ID + " = " + id+ " AND " + DBHelper.COLUMN_CREW_BOAT_ID + " = "+boot_id, null, null, null, null);

        cursor.moveToFirst();
 */

        Cursor cursor = mDatabase.query(DBHelper.TABLE_CREWMEMBERS,
                mAllColumns, DBHelper.COLUMN_CREWMEMBER_ID + " =?  AND " + DBHelper.COLUMN_CREW_BOAT_ID + " =? "
                , new String[]{Long.toString(id),Long.toString(boat_id)}, null, null, null);
        cursor.moveToFirst();


        //creates an instance of CrewMember for processing
        CrewMember newCrewMember = cursorToCrewMember(cursor);
        cursor.close();
        return newCrewMember;
    }

    public void deleteCrewMember(CrewMember CrewMember) {
        long id = CrewMember.getId();
        System.out.println("the deleted crew member has the id: " + id);
        mDatabase.delete(DBHelper.TABLE_CREWMEMBERS, DBHelper.COLUMN_CREW_BOAT_ID + " = ?" + " AND "
                        + DBHelper.COLUMN_CREWMEMBER_ID  + " = ? ",
                new String[] { String.valueOf(CrewMember.getBoat().getId()), String.valueOf(id)});
    }

    public List<CrewMember> getAllCrewMembers() {
        List<CrewMember> listCrewMembers = new ArrayList<CrewMember>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_CREWMEMBERS,
                mAllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CrewMember CrewMember = cursorToCrewMember(cursor);
            listCrewMembers.add(CrewMember);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return listCrewMembers;
    }

    public List<CrewMember> getCrewMembersOfBoat(long boatId) {
        List<CrewMember> listCrewmember = new ArrayList<>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_CREWMEMBERS, mAllColumns
                , DBHelper.COLUMN_CREW_BOAT_ID + " = " + String.valueOf(boatId), //vorher " =?"
                null, null, null, DBHelper.COLUMN_CREWMEMBER_ID); //vorher new String[]{String.valueOf(boatId)}

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CrewMember crewmember = cursorToCrewMember(cursor);
            listCrewmember.add(crewmember);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return listCrewmember;
    }

    public List<CrewMember> getCrewMembersOfBoatSortedByWeight (long boatId) {
        List<CrewMember> listCrewmember = new ArrayList<>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_CREWMEMBERS, mAllColumns
                , DBHelper.COLUMN_CREW_BOAT_ID + " = " + String.valueOf(boatId), //vorher " =?"
                null, null, null, DBHelper.COLUMN_CREWMEMBER_WEIGHT); //vorher new String[]{String.valueOf(boatId)}

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CrewMember crewmember = cursorToCrewMember(cursor);
            listCrewmember.add(crewmember);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return listCrewmember;
    }


    public CrewMember getCrewMemberOfBoatbyId(long boat_Id, long crew_Id){
        /*
        Cursor cursor = mDatabase.query(DBHelper.TABLE_BOATS, mAllColumns,
                DBHelper.COLUMN_BOAT_ID + " = ?" ,
                new String[]{String.valueOf(id)}, null, null, null);*/

        Cursor cursor = mDatabase.query(DBHelper.TABLE_CREWMEMBERS, mAllColumns
                , DBHelper.COLUMN_CREW_BOAT_ID + " = ?" + " AND "
                + DBHelper.COLUMN_CREWMEMBER_ID  + " = ? "
                , new String[] { String.valueOf(boat_Id), String.valueOf(crew_Id) }
                ,  null, null, null);
        if (cursor != null) {
            if (cursor.getCount() != 0){
            cursor.moveToFirst();
            CrewMember member = cursorToCrewMember(cursor);
            return member;
            }
            else{
                return null;
            }
        }
        else {
        return null;
        }
    }


    private CrewMember cursorToCrewMember(Cursor cursor) {
        CrewMember CrewMember = new CrewMember();
        CrewMember.setId(cursor.getLong(0));
        CrewMember.setFirstName(cursor.getString(2));
        CrewMember.setLastName(cursor.getString(3));
        CrewMember.setWeight(cursor.getInt(4));
        //CrewMember.setTask(cursor.getString(5));

        // get the boat by id
        long boatId = cursor.getLong(1);
        BoatDAO boatDAO= new BoatDAO(mContext);
        Boat boat = boatDAO.getBoatById(boatId);
        if (boat != null)
            CrewMember.setBoat(boat);

        return CrewMember;
    }

}
