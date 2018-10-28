package com.LeafApps.dragonboat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.LeafApps.dragonboat.R;

import com.LeafApps.dragonboat.model.Boat;

import java.util.List;

public class BoatAdapter extends BaseAdapter {
    public static final String TAG = "ListBoatsAdapter";

    private List<Boat> mItems;
    private LayoutInflater mInflater;
    private Context mContext;
    public long id;

    public BoatAdapter(Context context, List<Boat> listBoats) {
        this.setItems(listBoats);
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0;
    }

    @Override
    public Boat getItem(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position).getId() : position;
    }

    @Override
    //man erstell ein View mit 4 TextViews angeordnet wie man will und übergibt hierhin gesammelt irgendwie. hier werden diese besetzt
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final ViewHolder holder; // wird unten deklariert
/*
        Boat currentItem = getItem(position);
        if (currentItem != null) {
            id = currentItem.getId();
        }
*/
        if (v == null) {
            v = mInflater.inflate(R.layout.view_list_boats, parent, false);
            holder = new ViewHolder();
         //   holder.setId(id);
            holder.listBoatName = (TextView) v.findViewById(R.id.listBoatName);
            holder.listTeamName = (TextView) v.findViewById(R.id.listTeamName);
           // holder.listBtnChronometer = (Button) v.findViewById(R.id.button_chronometer);
/*
            holder.listBtnChronometer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //informationen für Bootname Teamname ID irgendwie erfasst werden
                    //variable Informationen hierhin
                    //intent putextra("bezeichnung", information zb id )
                    //information:id,currentitem.getname. currentitem.getteamname
                    Intent chronometer=new Intent(mContext, ChronometerActivity.class);
            //        chronometer.putExtra(ChronometerActivity.EXTRA_SELECTED_BOAT_ID, holder.id);
                    chronometer.putExtra(ChronometerActivity.EXTRA_SELECTED_BOAT_NAME, holder.listBoatName.getText().toString());
                    chronometer.putExtra(ChronometerActivity.EXTRA_SELECTED_BOAT_TEAMNAME, holder.listTeamName.getText().toString());

                    mContext.startActivity(chronometer);
                }
            });*/

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        // fill row data
        Boat currentItem = getItem(position);
        if (currentItem != null) {
            //id = currentItem.getId();   FAILCODE
         //   holder.setId(currentItem.getId());// EPIC WIN
            holder.listBoatName.setText(currentItem.getName());
            holder.listTeamName.setText(currentItem.getTeamName());
        }

        return v;
    }

    public List<Boat> getItems() {
        return mItems;
    }

    public void setItems(List<Boat> mItems) {
        this.mItems = mItems;
    }

    class ViewHolder {
      //  long id;
        TextView listBoatName;
        TextView listTeamName;
      //  Button listBtnChronometer;

      //  public void setId(long id){
       //     this.id = id;
     //   }//Hier wird wahrscheinlich eine falsche id gewählt, die für den absturz sorgt
    }
}
