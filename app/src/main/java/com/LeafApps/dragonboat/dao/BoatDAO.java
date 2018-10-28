package com.LeafApps.dragonboat.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.LeafApps.dragonboat.activities.BoatsShowActivity;
import com.LeafApps.dragonboat.model.Boat;
import com.LeafApps.dragonboat.model.CrewMember;

import java.util.ArrayList;
import java.util.List;


public class BoatDAO {

    public static final String TAG = "BOAT_DAO";

    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private Context mContext;
    private String[] mAllColumns = {DBHelper.COLUMN_BOAT_ID,
            DBHelper.COLUMN_BOAT_NAME, DBHelper.COLUMN_BOAT_TEAMNAME};

    public BoatDAO(Context context) {
        this.mContext = context;
        mDbHelper = new DBHelper(context);

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

    public Boat createBoat(long insertId,  String name, String team_name, String uri, String time){
        //insertId = checkBoatId(insertId);
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_BOAT_ID, insertId);
        values.put(DBHelper.COLUMN_BOAT_NAME, name);
        values.put(DBHelper.COLUMN_BOAT_TEAMNAME, team_name);
        values.put(DBHelper.COLUMN_BOAT_URI, uri);
        values.put(DBHelper.COLUMN_BOAT_TIME, time);

        //long insertId = mDatabase
        //        .insert(DBHelper.TABLE_BOATS, null, values);
        mDatabase.insert(DBHelper.TABLE_BOATS, null, values);
        Cursor cursor = mDatabase.query(DBHelper.TABLE_BOATS, mAllColumns,
                DBHelper.COLUMN_BOAT_ID + " = " + insertId, null, null, //vorher anstatt id war "insertId". das ist filterung an welchser stelle das eingefügt wird
                null, null);
        cursor.moveToFirst();
        Boat newBoat = cursorToBoat(cursor);
        cursor.close();
        return newBoat;
    }

    public Boat createBoat(long insertId,  String name, String team_name){
        //insertId = checkBoatId(insertId);
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_BOAT_ID, insertId);
        values.put(DBHelper.COLUMN_BOAT_NAME, name);
        values.put(DBHelper.COLUMN_BOAT_TEAMNAME, team_name);


        //long insertId = mDatabase
        //        .insert(DBHelper.TABLE_BOATS, null, values);
        mDatabase.insert(DBHelper.TABLE_BOATS, null, values);
        Cursor cursor = mDatabase.query(DBHelper.TABLE_BOATS, mAllColumns,
                DBHelper.COLUMN_BOAT_ID + " = " + insertId, null, null, //vorher anstatt id war "insertId". das ist filterung an welchser stelle das eingefügt wird
                null, null);
        cursor.moveToFirst();
        Boat newBoat = cursorToBoat(cursor);
        cursor.close();
        return newBoat;
    }


    public void deleteBoat(Boat boat) {
        long id = boat.getId();
        CrewMemberDAO CrewMemberDAO = new CrewMemberDAO(mContext);
        List<CrewMember> listCrewMember = CrewMemberDAO.getCrewMembersOfBoat(id);
        if (listCrewMember != null && !listCrewMember.isEmpty()) {
            for (CrewMember c : listCrewMember) {
                CrewMemberDAO.deleteCrewMember(c);
            }

        }

        //System.out.println("the deleted boat has the id: " + id);
        mDatabase.delete(DBHelper.TABLE_BOATS, DBHelper.COLUMN_BOAT_ID
                + " = " + id, null);
    }

    public List<Boat> getAllBoats() {
        List<Boat> listBoats = new ArrayList<Boat>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_BOATS, mAllColumns,
                null, null, null, null, DBHelper.COLUMN_BOAT_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Boat boat = cursorToBoat(cursor);
                listBoats.add(boat);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return listBoats;
    }

    public Boat getBoatById(long id) {
        Cursor cursor = mDatabase.query(DBHelper.TABLE_BOATS, mAllColumns,
                DBHelper.COLUMN_BOAT_ID + " = ?" ,
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Boat boat = cursorToBoat(cursor);
        return boat;
    }

    protected Boat cursorToBoat(Cursor cursor) {
        Boat boat = new Boat();
        boat.setId(cursor.getLong(0));
        boat.setName(cursor.getString(1));
        boat.setTeamName(cursor.getString(2));
        return boat;
    }

    public void deleteAllBoats(int delTable) {

        if (delTable == 1) {
            mDbHelper.onUpgrade(mDatabase, 1, 2);
        }

        List<Boat> delBoats = getAllBoats();
        Boat delBoat;
        Cursor cursor = mDatabase.query(DBHelper.TABLE_BOATS,
                mAllColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            delBoat = delBoats.get(cursor.getPosition());
            //delBoat = cursorToBoat(cursor);
            deleteBoat(delBoat);
            cursor.moveToNext();
        }

    }

/*
    public long checkBoatId(){
        //this nethod should be executed before creation of boat
        //this method will deside which id should be taken
        //there are only ids from 0 - 9
        long id = 0;
        int maxcount=10;
        String [] DBboatIds = {DBHelper.COLUMN_BOAT_ID};
        Cursor cursor = mDatabase.query(DBHelper.TABLE_BOATS, DBboatIds,
                null, null, null, //vorher anstatt id war "insertId". das ist filterung an welchser stelle das eingefügt wird
                null, DBHelper.COLUMN_BOAT_ID);

        if (cursor != null & cursor.getCount() != 0) {
            cursor.moveToLast();

            if (cursor.getLong(0) < maxcount-1) {
                id = cursor.getLong(0) + 1;
            }
            else{
                Boat boat = getBoatById(0);
                deleteBoat(boat);
                for (long i = 1; i < maxcount; i++){
                    boat = getBoatById(i);

                    createBoat(i-1, boat.getName(), boat.getTeamName());

                    CrewMemberDAO CrewMemberDAO = new CrewMemberDAO(mContext);
                    List<CrewMember> listCrewMember = CrewMemberDAO.getCrewMembersOfBoat(i);
                    if (listCrewMember != null && !listCrewMember.isEmpty()) {
                        for (CrewMember c : listCrewMember) {
                            CrewMemberDAO.createCrewMember(c.getId(), boat.getId()-1,c.getFirstName(),c.getLastName(),c.getWeight());
                            CrewMemberDAO.deleteCrewMember(c);
                        }
                    }
                    mDatabase.delete(DBHelper.TABLE_BOATS, DBHelper.COLUMN_BOAT_ID
                            + " = " + i, null);

                }
                id = maxcount-1;
            }

            cursor.close();



        }
        return id;
    }
*/
    public long checkBoatId(){
        //this nethod should be executed before creation of boat
        //this method will deside which id should be taken
        //there are only ids from 0 - 9
        Boat boat;
        List<CrewMember> listCrewMember;
        CrewMemberDAO CrewMemberDAO = new CrewMemberDAO(mContext);
        long id = 0;
        int maxcount= BoatsShowActivity.maxcount;
        String [] DBboatIds = {DBHelper.COLUMN_BOAT_ID};
        Cursor cursor = mDatabase.query(DBHelper.TABLE_BOATS, DBboatIds,
                null, null, null, //vorher anstatt id war "insertId". das ist filterung an welchser stelle das eingefügt wird
                null, DBHelper.COLUMN_BOAT_ID);

        if (cursor != null & cursor.getCount() != 0) {
            cursor.moveToLast();

            if (cursor.getLong(0) < maxcount) {
                id = cursor.getLong(0) + 1;
            }
            else{//für den fall dass maxcount errreicht ist. das älteste boot wird gelöscht, die ids werden angepasst
                boat = getBoatById(1);
                deleteBoat(boat);
                for (long i = 2; i <= maxcount; i++){
                    boat = getBoatById(i);
                    createBoat(i-1, boat.getName(), boat.getTeamName(),getPath(i) ,getTime(i));


                    listCrewMember = CrewMemberDAO.getCrewMembersOfBoat(i);
                    if (listCrewMember != null && !listCrewMember.isEmpty()) {
                        for (CrewMember c : listCrewMember) {
                            CrewMemberDAO.createCrewMember(c.getId(), i-1, c.getFirstName(),c.getLastName(),c.getWeight());

                            CrewMemberDAO.deleteCrewMember(c);
                        }

                    }
                    deleteBoat(boat);
                    //mDatabase.delete(DBHelper.TABLE_BOATS, DBHelper.COLUMN_BOAT_ID
                    //        + " = " + i, null);

                }
                id = maxcount;
            }

            cursor.close();



        }
        else{
            id = 1;
        }
        return id;
    }

public Boat createBoatWithId(String name, String team_name){
    long id = checkBoatId();
    Boat boat = createBoat(id,  name, team_name);
    return boat;
}
/*
public Boat updateboattime(int boatid, long time){
    mDatabase.update(DBHelper.TABLE_BOATS, DBHelper.COLUMN_BOAT_TIME+"=" + String.valueOf(time),+ "WHERE" +DBHelper.COLUMN_BOAT_ID+"=" + String.valueOf(boatid));
}*/
 public void setTime( long boatid, String time) {
        ContentValues values = new ContentValues();

        //values.put("BOATID", boatid);
        values.put(DBHelper.COLUMN_BOAT_TIME, time);

        String selection = DBHelper.COLUMN_BOAT_ID;
        String[] selelectionArgs = { String.valueOf(boatid) };

        int count = mDatabase.update(
                DBHelper.TABLE_BOATS,
                values,
                selection+" = ?",
                selelectionArgs);
 }

    public String getTime( long boatid) {
        Cursor cursor = mDatabase.query(DBHelper.TABLE_BOATS, new String[]{DBHelper.COLUMN_BOAT_TIME},
                DBHelper.COLUMN_BOAT_ID + " = ?",
                new String[]{String.valueOf(boatid)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor.getString(0);
    }

    public void setPath( long boatid, String uri) {
        ContentValues values = new ContentValues();

        //values.put("BOATID", boatid);
        values.put(DBHelper.COLUMN_BOAT_URI, uri);

        String selection = DBHelper.COLUMN_BOAT_ID;
        String[] selelectionArgs = { String.valueOf(boatid) };

        int count = mDatabase.update(
                DBHelper.TABLE_BOATS,
                values,
                selection+"= ?",
                selelectionArgs);
    }

    public String getPath( long boatid) {
        Cursor cursor = mDatabase.query(DBHelper.TABLE_BOATS, new String[]{DBHelper.COLUMN_BOAT_URI},
                DBHelper.COLUMN_BOAT_ID + " = ?",
                new String[]{String.valueOf(boatid)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor.getString(0);
    }

    public void replaceBoatById(long boatid_from, long boatid_to){
        Boat boat;
        boat = getBoatById(boatid_from);

        createBoat(boatid_to, boat.getName(), boat.getTeamName());

        CrewMemberDAO CrewMemberDAO = new CrewMemberDAO(mContext);
        List<CrewMember> listCrewMember = CrewMemberDAO.getCrewMembersOfBoat(boatid_from);
        if (listCrewMember != null && !listCrewMember.isEmpty()) {
            for (CrewMember c : listCrewMember) {
                CrewMemberDAO.createCrewMember(c.getId(), boatid_to,c.getFirstName(),c.getLastName(),c.getWeight());
                CrewMemberDAO.deleteCrewMember(c);
            }
        }
        mDatabase.delete(DBHelper.TABLE_BOATS, DBHelper.COLUMN_BOAT_ID
                + " = " + boatid_from, null);

    }


}













