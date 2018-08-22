package com.example.android.shustudenthelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.shustudenthelper.userlogin.UserloginActivity;

import static com.example.android.shustudenthelper.UsernameRegisterActivity.MyPREFERENCES;
import static com.example.android.shustudenthelper.UsernameRegisterActivity.userRegistration_Complete;

public class SplashActivity extends AppCompatActivity {
    public Boolean check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //testClearSharedPreferences();

        checkUserLoginDetailsValid();// Method to Check if the user is registered
        finish();
    }

    public void checkUserLoginDetailsValid(){

        //gets value from shared preference
        SharedPreferences loginDetailsSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        check =  loginDetailsSharedPreferences.getBoolean(userRegistration_Complete, false);
        if (check == true){
            Intent intent = new Intent(this, UserloginActivity.class);
            //Intent intent = new Intent("com.example.android.shustudenthelper.userlogin.UserloginActivity");
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, UsernameRegisterActivity.class);
            startActivity(intent);
        }
    }
    public void  testClearSharedPreferences(){

        SharedPreferences loginDetailsSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = loginDetailsSharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
//    public static String getDefaults(String key, Context context) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        return preferences.getString(key, null);
//    }

}//end class