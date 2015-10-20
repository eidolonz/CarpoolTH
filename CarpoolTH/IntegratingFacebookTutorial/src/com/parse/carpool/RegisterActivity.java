package com.parse.carpool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.integratingfacebooktutorial.R;

/**
 * Created by JUMRUS on 20/10/2558.
 */
public class RegisterActivity extends AppCompatActivity {
    RadioButton radioMale;
    RadioButton radioFemale;
    RadioGroup radioSexGroup;
    Button regisBtn;
    EditText regisMail;
    EditText regisPhone;
    String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initiate();

        regisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRegis();
            }
        });



    }

    public void initiate(){
        radioFemale = (RadioButton) findViewById(R.id.radioFemale);
        radioMale = (RadioButton) findViewById(R.id.radioMale);
        regisBtn = (Button) findViewById(R.id.regisBtn);
        regisMail = (EditText) findViewById(R.id.regisMail);
        regisPhone = (EditText) findViewById(R.id.regisPhone);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSexGroup);

        radioMale.setChecked(true);
        radioFemale.setChecked(false);
        this.sex = "Male";

        radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioMale){
                    sex = "Male";
                }else{
                    sex = "Female";
                }
            }
        });

    }

    public void saveRegis(){
        final String mail = regisMail.getText().toString();
        final String phone = regisPhone.getText().toString();
        if(!mail.isEmpty() && !phone.isEmpty()){
            ParseUser currentUser = ParseUser.getCurrentUser();
            final ParseQuery<ParseObject> user = ParseQuery.getQuery("_User");
            user.getInBackground(currentUser.getObjectId(), new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        parseObject.put("email", mail);
                        parseObject.put("mobileNo", phone);
                        parseObject.put("gender", sex);
                        parseObject.saveInBackground();
                    }
                }
            });
            Toast.makeText(this,
                    "Register Complete!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this,
                    "คุณยังกรอกข้อมูลไม่ครบ", Toast.LENGTH_LONG).show();
        }
    }
}
