package com.parse.carpool;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.integratingfacebooktutorial.R;

import java.util.List;

/**
 * Created by JUMRUS on 12/9/2558.
 */
public class Home extends Fragment {
    private static View view;
    private static TextView tv1;
    private static TextView tv2;
    private static TextView tv3;
    private String name = "Null";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try{
            view = inflater.inflate(R.layout.fragment_a,container,false);
        }catch (InflateException e){
			/* map is already there, just return view as it is */
        }

        tv1 = (TextView) view.findViewById(R.id.textView1);

        setText();
        return view;
    }

    public void setText(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("objectId", "J1QYchSKy3");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjects, ParseException e) {


                if (e == null) {
                    for (ParseObject object : parseObjects) {
                        // This does not require a network access.
                       name = object.getString("name");
                    }

                }
                else {
                    Log.d("score", "Error: " + e.getMessage());
                    name = " Not found Name";
                    tv1.setText(name);
                }
                tv1.setText(name);
            }
        });

    }


}
