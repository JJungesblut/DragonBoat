package com.LeafApps.dragonboat.model;

import java.io.Serializable;

/**
 * Created by Dimitri on 10.01.2015.
 */
public class CrewMember implements Serializable {

    public static final String TAG = "Boat";
    private static final long serialVersionUID = -7406082437623008161L;

    private long mId;

    private String mTask;
    private String mFirstName;
    private String mLastName;
    private int mWeight;
    private Boat mBoat;

    public CrewMember() {
    }


    public CrewMember(String FirstName, String LastName, int Weight) {
        this.mFirstName = FirstName;
        this.mLastName = LastName;
        //this.mId = Id;
        this.mId = Weight;
    }


    public String getTask() {
        return mTask;
    }

    public void setTask(String mTask) {
        this.mTask = mTask;
    }


    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int mWeight) {
        this.mWeight = mWeight;
    }

    //Connection of crew memeber to a boat
    public Boat getBoat() {
        return mBoat;
    }

    public void setBoat(Boat mBoat) {
        this.mBoat = mBoat;
    }

}
