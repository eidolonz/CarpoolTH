package com.parse.carpool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.parse.integratingfacebooktutorial.R;

/**
 * Created by JUMRUS on 12/9/2558.
 */
public class MatchMe extends Fragment{
    private static View view;
    private boolean isDriver = false;
    private ImageView riderPic;
    private ImageView driverPic;
    private Button startBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try{
            view = inflater.inflate(R.layout.match_me,container,false);
        }catch (InflateException e){
			/* map is already there, just return view as it is */
        }
        startBtn = (Button) view.findViewById(R.id.riderBtn);

        driverPic = (ImageView) view.findViewById(R.id.driverPic);
        driverPic.setClickable(true);
        driverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDriver();
            }
        });
        riderPic = (ImageView) view.findViewById(R.id.riderPic);
        riderPic.setClickable(true);
        riderPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseRider();
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDriver){
                    findRider();
                }else{
                    findDriver();
                }
            }
        });

        return view;
    }

    public void chooseDriver(){
        riderPic.setImageResource(R.drawable.rider);
        driverPic.setImageResource(R.drawable.driver01);
        startBtn.setText("Create trip");
        isDriver = true;
    }
    public void chooseRider(){
        riderPic.setImageResource(R.drawable.rider01);
        driverPic.setImageResource(R.drawable.driver);
        startBtn.setText("Find trip");
        isDriver = false;
    }

    public void findRider(){
        Intent intent = new Intent(getActivity(), MapActivity.class);
        startActivity(intent);
    }
    public void findDriver(){
        Intent intent = new Intent(getActivity(), FindDriver.class); //FindDriver.class
        intent.putExtra("SetLatitudeDestination", "0");
        intent.putExtra("SetLongitudeDestination", "0");
        intent.putExtra("SetLatitudeSource", "0");
        intent.putExtra("SetLongitudeSource", "0");
        startActivity(intent);




    }
}
