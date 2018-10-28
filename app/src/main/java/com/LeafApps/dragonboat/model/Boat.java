package com.LeafApps.dragonboat.model;

import java.io.Serializable;


public class Boat implements Serializable {

    public static final String TAG = "Boat";
    private static final long serialVersionUID = -7406082437623008161L;

    private long mId;
    private String mName;
    private String mTeamName;

    public Boat() {
    }


    public Boat(String name, String TeamName) {
        this.mName = name;
        this.mTeamName = TeamName;
    }


    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getTeamName() {
        return mTeamName;
    }

    public void setTeamName(String mTeamName) {
        this.mTeamName = mTeamName;
    }

}

