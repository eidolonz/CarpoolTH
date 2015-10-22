package com.parse.carpool;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.integratingfacebooktutorial.R;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by JUMRUSs on 20/9/2558.
 */
public class CreateTrip extends ActionBarActivity {

    private boolean allowSmoking = false;
    private boolean isDaily = true;
    private int money = 10;
    private int passenger = 1;
    private int startMinute;
    private int startHour;
    private int returnMinute;
    private int returnHour;

    private static View view;
    TextView tvStartTime;
    TextView tvReturnTime;
    TableRow trStartTime;
    TableRow trReturnTime;
    TableRow dailyOnLayout;
    TableLayout locationLO;
    ToggleButton su;
    ToggleButton mo;
    ToggleButton tu;
    ToggleButton we;
    ToggleButton th;
    ToggleButton fr;
    ToggleButton sa;
    LinearLayout dailyOffLayout;
    EditText source;
    EditText destination;
    EditText carType;
    EditText tel;
    EditText email;
    EditText description;
    TextView moneyTV;
    TextView coTV;
    TextView sourceTV;
    TextView destinationTV;
    Switch dailySwitch;
    Switch cigaSwitch;
    Button addMoneyBtn;
    Button subMoneyBtn;
    Button addPassBtn;
    Button subPassBtn;
    Button createTrip;
    Button setLocation;
    String sourceDetail;
    String destinationDetail;
    JSONObject date = new JSONObject();
    JSONObject week = new JSONObject();
    private Double latitudeDestination;
    private Double longitudeDestination;
    private Double latitudeSource;
    private Double longitudeSource;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.trip_create);

        Intent intent = getIntent();
        sourceDetail =  intent.getStringExtra("sourceDetail");
        destinationDetail = intent.getStringExtra("destinationDetail");
        latitudeDestination = Double.parseDouble(intent.getStringExtra("SetLatitudeDestination"));
        longitudeDestination = Double.parseDouble(intent.getStringExtra("SetLongitudeDestination"));
        latitudeSource = Double.parseDouble(intent.getStringExtra("SetLatitudeSource"));
        longitudeSource = Double.parseDouble(intent.getStringExtra("SetLongitudeSource"));

        setCurrentTimeOnView();
        addListenerOnTime();
        sourceTV = (TextView) findViewById(R.id.sourceTV);
        destinationTV = (TextView) findViewById(R.id.destinationTV);
        sourceTV.setText("From: "+sourceDetail);
        destinationTV.setText("To: "+destinationDetail);

        locationLO = (TableLayout) findViewById(R.id.location);
        locationLO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });

        initialWeekObj();

        dailyOnLayout = (TableRow) findViewById(R.id.dailyOn);
        dailyOffLayout = (LinearLayout) findViewById(R.id.dailyOff);
        dailySwitch = (Switch) findViewById(R.id.dailySwitch);
        dailySwitch.setChecked(true);
        dailyOffLayout.setVisibility(LinearLayout.GONE);
        dailySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isDaily = true;
                    dailyOffLayout.setVisibility(LinearLayout.GONE);
                    dailyOnLayout.setVisibility(TableRow.VISIBLE);
                } else {
                    isDaily = false;
                    dailyOffLayout.setVisibility(LinearLayout.VISIBLE);
                    dailyOnLayout.setVisibility(TableRow.GONE);
                }

            }
        });

        su = (ToggleButton) findViewById(R.id.su);
        mo = (ToggleButton) findViewById(R.id.mo);
        tu = (ToggleButton) findViewById(R.id.tu);
        we = (ToggleButton) findViewById(R.id.we);
        th = (ToggleButton) findViewById(R.id.th);
        fr = (ToggleButton) findViewById(R.id.fr);
        sa = (ToggleButton) findViewById(R.id.sa);

        su.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    try {
                        week.put("sun", true);
                    }catch (Exception e){}
                }else{
                    try {
                        week.put("sun", false);
                    }catch (Exception e){}
                }
            }
        });
        mo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    try {
                        week.put("mon", true);
                    }catch (Exception e){}
                }else{
                    try {
                        week.put("mon", false);
                    }catch (Exception e){}
                }
            }
        });
        tu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    try {
                        week.put("tue", true);
                    }catch (Exception e){}
                }else{
                    try {
                        week.put("tue", false);
                    }catch (Exception e){}
                }
            }
        });
        we.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    try {
                        week.put("wed", true);
                    }catch (Exception e){}
                }else{
                    try {
                        week.put("wed", false);
                    }catch (Exception e){}
                }
            }
        });
        th.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    try {
                        week.put("thu", true);
                    }catch (Exception e){}
                }else{
                    try {
                        week.put("thu", false);
                    }catch (Exception e){}
                }
            }
        });
        fr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    try {
                        week.put("fri", true);
                    }catch (Exception e){}
                }else{
                    try {
                        week.put("fri", false);
                    }catch (Exception e){}
                }
            }
        });
        sa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    try {
                        week.put("sat", true);
                    }catch (Exception e){}
                }else{
                    try {
                        week.put("sat", false);
                    }catch (Exception e){}
                }
            }
        });

        carType = (EditText) findViewById(R.id.carType);
        tel = (EditText) findViewById(R.id.tel);
        email = (EditText) findViewById(R.id.email);
        description = (EditText) findViewById(R.id.description);

        cigaSwitch = (Switch) findViewById(R.id.ciga);
        cigaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    allowSmoking = true;
                } else {
                    allowSmoking = false;
                }
            }
        });

        moneyTV = (TextView) findViewById(R.id.money_TV);
        addMoneyBtn = (Button) findViewById(R.id.addMoneyBtn);
        subMoneyBtn = (Button) findViewById(R.id.subMoneyBtn);
        setLocation = (Button) findViewById(R.id.setLocationBtn);

        setLocation.setOnClickListener(new View.OnClickListener(){

            @Override
            public  void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });
        addMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(money>=0){
                    money += 10;
                }else{
                    money = 0;
                }
                moneyTV.setText(money+" บาท/คน");
            }
        });
        subMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(money > 0){
                    money -= 10;
                }else{
                    money=0;
                }
                moneyTV.setText(money + " บาท/คน");
            }
        });

        coTV = (TextView) findViewById(R.id.co_TV);
        addPassBtn = (Button) findViewById(R.id.addPassBtn);
        subPassBtn = (Button) findViewById(R.id.subPassBtn);
        addPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passenger >= 0){
                    passenger++;
                }else{
                    passenger = 1;
                }
                coTV.setText("" + passenger);
            }
        });
        subPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passenger > 1){
                    passenger--;
                }else {
                    passenger = 1;
                }
                coTV.setText("" + passenger);
            }
        });

        createTrip = (Button) findViewById(R.id.create_trip);
        createTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTrip();
            }
        });






    }

    public void setCurrentTimeOnView(){
        final Calendar c = Calendar.getInstance();
        startHour = c.get(Calendar.HOUR_OF_DAY);
        startMinute = c.get(Calendar.MINUTE);
        returnHour = c.get(Calendar.HOUR_OF_DAY);
        returnMinute = c.get(Calendar.MINUTE);
    }
    public void addListenerOnTime(){
        trStartTime = (TableRow) findViewById(R.id.trStartTime);
        trReturnTime = (TableRow) findViewById(R.id.trReturnTime);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvReturnTime = (TextView) findViewById(R.id.tvReturnTime);

        trStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });
        trReturnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(2);
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id){
        switch (id) {
            case 1:
                // set time picker as current time
                return new TimePickerDialog(this,timePickerListener1, startHour, startMinute,false);
            case 2:
                return new TimePickerDialog(this,timePickerListener2, returnHour, returnMinute,false);

        }
        return null;

    }
    private TimePickerDialog.OnTimeSetListener timePickerListener1 =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    startHour = selectedHour;
                    startMinute = selectedMinute;

                    tvStartTime.setText(startHour + ":" + startMinute + " Hrs");


                }
            };
    private TimePickerDialog.OnTimeSetListener timePickerListener2 =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    returnHour = selectedHour;
                    returnMinute = selectedMinute;

                    tvReturnTime.setText(returnHour + ":" + returnMinute + " Hrs");


                }
            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public void initialWeekObj(){
        try {
            this.week.put("sun", false);
            this.week.put("mon", true);
            this.week.put("tue", true);
            this.week.put("wed", true);
            this.week.put("thu", true);
            this.week.put("fri", true);
            this.week.put("sat", false);
        }
        catch (Exception e){}
    }



    public void saveTrip(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        final ParseObject dataObject = new ParseObject("CreateTrip");
            try {
                if (isDaily) {
                    dataObject.put("daily", week);
                } else {
                    dataObject.put("StartHour", startHour);
                    dataObject.put("StartMinute", startMinute);
                    dataObject.put("ReturnHour", returnHour);
                    dataObject.put("ReturnMinute", returnMinute);
                }
            }
            catch (Exception e){}
            dataObject.put("Smoke", allowSmoking);
            dataObject.put("Money", money);
            dataObject.put("Passenger", passenger);
            dataObject.put("LatitudeDestination", latitudeDestination.toString());
            dataObject.put("LongitudeDestination", longitudeDestination.toString());
            dataObject.put("LatitudeSource", latitudeSource.toString());
            dataObject.put("LongitudeSource", longitudeSource.toString());
            dataObject.put("Source", sourceDetail);
            dataObject.put("Destination", destinationDetail);
            dataObject.put("Car", carType.getText().toString());
            dataObject.put("Tel", tel.getText().toString());
            dataObject.put("Mail", email.getText().toString());
            dataObject.put("Description", description.getText().toString());
            //String createBy = currentUser.getObjectId();
            dataObject.put("CreateBy", currentUser);
            String ownerName = currentUser.getString("name");
            dataObject.put("OwnerName", ownerName);
            HashMap fb = (HashMap)currentUser.get("profile");
            dataObject.put("FacebookId", fb.get("facebookId"));
            dataObject.saveInBackground();
        //add tripId to User table
        final ParseQuery<ParseObject> user = ParseQuery.getQuery("_User");
        user.getInBackground(currentUser.getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.put("YourTrip", dataObject.getObjectId());
                    parseObject.saveInBackground();
                }
            }
        });
        toMain();
    }

    public void toMain(){
        Intent intent = new Intent(getApplication(), MainActivity.class);
        Toast.makeText(getApplication(),"Trip created", Toast.LENGTH_LONG).show();
        startActivity(intent);
    }
}