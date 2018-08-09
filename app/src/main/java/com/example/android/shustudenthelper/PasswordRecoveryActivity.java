package com.example.android.shustudenthelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static com.example.android.shustudenthelper.UsernameRegisterActivity.MyPREFERENCES;

public class PasswordRecoveryActivity extends AppCompatActivity {

    /*New Instances to register with real ID's */
    private Spinner securityQuestionSpinner;
    private Button btnSubmit;
    private static EditText userSecurityAnswer;

    /*Strings to store into the shared Preferences*/
    public static String store_Question = "Question";
    public static String store_Answer = "Answer";
    public static String securityQuestionSharedPrefValue;
    public static String securityAnswerSharedPrefValue;

    SharedPreferences loginDetailsSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        loginDetailsSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        addListenerOnSpinnerItemSelection();/*listener on Selected Spinner Item */
        addListenerOnButton();/*listener on Password recover button*/
    }
    public void addListenerOnSpinnerItemSelection() {
        /* register the new instance with real spinner id*/
        securityQuestionSpinner = (Spinner) findViewById(R.id.question_spinner);
        /*listener on Selected Spinner Item */
        securityQuestionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(),
                        "Selected Question : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addListenerOnButton() {

         /* register the new instance with real id's*/
         userSecurityAnswer = (EditText)findViewById(R.id.security_answer_user);
         btnSubmit = (Button) findViewById(R.id.next_set_up_button);

        /* set listener on button*/
         btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* get the details entered by user and store them in shared preference strings*/
                securityAnswerSharedPrefValue = userSecurityAnswer.getText().toString();
                securityQuestionSharedPrefValue = securityQuestionSpinner.getSelectedItem().toString();

                if (!securityAnswerSharedPrefValue.isEmpty()) {

                    /* commit user entered details to shared preference after user enters the answer for security question*/
                    setLoginDetailsSharedPreferences(store_Question, securityQuestionSharedPrefValue);
                    setLoginDetailsSharedPreferences(store_Answer, securityAnswerSharedPrefValue);

                    /* Toast message to user*/
                    Toast.makeText(PasswordRecoveryActivity.this,
                            "Question : " +
                                    String.valueOf(securityQuestionSpinner.getSelectedItem()) + "\nAnswer : " + securityAnswerSharedPrefValue,
                            Toast.LENGTH_SHORT).show();

                    /* Start course selection activity once the user enters the security answer successfully */
                    Intent intent = new Intent("com.example.android.shustudenthelper.CourseSelectionActivity");
                    startActivity(intent);
                }else{
                    /*If the user didn't the security answer, display the error toast message*/
                    Toast.makeText(PasswordRecoveryActivity.this, "Please enter security answer", Toast.LENGTH_SHORT).show();
                }
            }
        }

        );
    }
    /* method to commit strings into sharedPreferences*/
    public void setLoginDetailsSharedPreferences(String key, String value){

        SharedPreferences.Editor editor = loginDetailsSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }



}//end class
