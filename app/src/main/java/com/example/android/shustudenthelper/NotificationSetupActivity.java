package com.example.android.shustudenthelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import static com.example.android.shustudenthelper.UsernameRegisterActivity.MyPREFERENCES;

public class NotificationSetupActivity extends AppCompatActivity {

    //Instances to register with real ID's
    private  Button btnSubmit_Finish;
    private Spinner driveTypeSpinner;
    public Switch aSwitch;
    public Switch bSwitch;
    public Switch cSwitch;

    public static boolean courseNotificationIsOnOrNot = false;
    public static boolean weatherNotificationIsOnOrNot = false;
    public static boolean durationNotificationIsOnOrNot = false;

    public static String selectedDriveType;

    SharedPreferences loginDetailsSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setup);//register the layout with activity

        loginDetailsSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //add listener on spinner drive type selection
        addListenerOnSpinnerItemSelection();
        //add listener on finish button
        addListenerOnButton();
    }

    public void addListenerOnSpinnerItemSelection() {
        /* Cast the instances to real ID's*/
        driveTypeSpinner = (Spinner) findViewById(R.id.drive_type_spinner);
        aSwitch = (Switch) findViewById(R.id.course_switch);
        bSwitch = (Switch) findViewById(R.id.weather_switch);
        cSwitch = (Switch) findViewById(R.id.duration_switch);

        /*Set listener on course switch to see if the notifications on courses is selected*/
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    courseNotificationIsOnOrNot = true;
                }else{
                    courseNotificationIsOnOrNot = false;
                }
            }
        });

        /*Set listener on weather switch to see if the notifications on weather is selected*/
        bSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    weatherNotificationIsOnOrNot = true;
                }else{
                    weatherNotificationIsOnOrNot = false;
                }
            }
        });

        /*Set listener on duration switch to see if the notifications on duration is selected*/
        cSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    durationNotificationIsOnOrNot = true;

                }else{
                    durationNotificationIsOnOrNot = false;

                }
            }
        });

        /*Set listener on drive type spinner to see which drive type has been selected*/
        driveTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedDriveType = parent.getItemAtPosition(position).toString();

                Toast.makeText(parent.getContext(),
                        "Selected Transport Type : " + selectedDriveType,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /*Set listener on finish button*/
    public void addListenerOnButton() {

        btnSubmit_Finish = (Button) findViewById(R.id.finish_button);

        DatabaseOpenHelper myDbHelper = new DatabaseOpenHelper(NotificationSetupActivity.this);

        /* Set the status of course selected to true in the database(update database) for the courses selected
         in Course Selection Activity only after
        * finish button is pressed*/
        for (CourseDisplay p : CourseSelectionActivity.newAdapter.getBox()) {
            if (p.getCourseSelected().equalsIgnoreCase("true")){
                String result;
                String status = "true";
                result = p.getCourseName();
                System.out.println("Result is: " + result);
                myDbHelper.updateSelectedCourseField(result,status);//update database

            }
        }

        btnSubmit_Finish.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {


                                                    /*setting the boolean SharedPreferences value to true only when the setup
                                                    is finished successfully.Only if this is true, user can view the sign-in screen.
                                                    If false, user will be moved to register Activity*/


                                                    setBooleanValueSharedPreferences("registration_complete", true);

                                                    //showing the info to user on success full registration
                                                    Toast.makeText(NotificationSetupActivity.this, "You are done with Set-up",
                                                            Toast.LENGTH_SHORT).show();

                                                    //redirecting user to Home Activity on successful registration
                                                    Intent intent = new Intent("com.example.android.shustudenthelper.MyCustomHomeActivity");
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);


                                                }
                                            }

        );
    }

    /*Storing and committing the variables to editor in SharedPreferences.*/
    public void setBooleanValueSharedPreferences(String key, Boolean value) {

        SharedPreferences.Editor editor = loginDetailsSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

}//end class
