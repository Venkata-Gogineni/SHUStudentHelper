package com.example.android.shustudenthelper.userlogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shustudenthelper.R;
import com.example.android.shustudenthelper.UsernameRegisterActivity;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static com.example.android.shustudenthelper.R.id.email;
import static com.example.android.shustudenthelper.R.id.forgotpassword;
import static com.example.android.shustudenthelper.UsernameRegisterActivity.MyPREFERENCES;
import static com.example.android.shustudenthelper.UsernameRegisterActivity.useremail_stringstore;
import static com.example.android.shustudenthelper.UsernameRegisterActivity.userpassword_stringstore;

public class UserloginActivity extends AppCompatActivity {

    private SecretKeySpec sks1;
    private String USERNAME_ENCRYPT;
    private String PASSWORD_ENCRYPT;
    private byte[] decodedUserEmail = null;
    private byte[] decodedUserPassword = null;

    private static EditText emailAddress;//EditText object for email ID
    private static EditText password;//EditText object for password
    private static Button signinButton;//Button object for sign in
    private static CheckBox saveLoginCheckBox;//checkBox object for remember me

    public static String checkedOrNot = "Checked";//string object, SharedPreference key
    public static Boolean saveLogin = false;//Boolean object to compare the date from SharedPreferences

    public static String USERNAME;//string object to store date from StoredPreference(email)
    public static String PASSWORD;//string object to store date from StoredPreference(password)

    SharedPreferences loginDetailsSharedPreferences;//SharedPreferences object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);


        emailAddress = (EditText) findViewById(email); //Casting with id of EditText(xml) to EditText object
        password = (EditText) findViewById(R.id.password); //Casting with id of EditText(xml) to EditText object
        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckbox);//Casting with id of CheckBox(xml) to CheckBox object
        signinButton = (Button) findViewById(R.id.email_sign_in_button);//Casting with id of Button(xml) to Button object

        //Get the details from SharedPreferences of Boolean type to check if the value is true or false
        loginDetailsSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = loginDetailsSharedPreferences.edit();
        saveLogin = loginDetailsSharedPreferences.getBoolean(checkedOrNot, false);

        if (saveLogin == true) {

            //if true, get the details of email and password from SharedPreferences and set them to EditText fields
            SharedPreferences loginDetailsSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            USERNAME =  loginDetailsSharedPreferences.getString(useremail_stringstore, null);
            PASSWORD =  loginDetailsSharedPreferences.getString(userpassword_stringstore, null);

            decrypt(USERNAME,PASSWORD);

            emailAddress.setText((new String(decodedUserEmail)));//set text to email field
            password.setText((new String(decodedUserPassword)));//set text to password field
            saveLoginCheckBox.setChecked(true);//keep the checkBox checked for next view
        }
        loginButtonClick();
        forgotPasswordClick();
    }//end method

    public void loginButtonClick() {


        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences loginDetailsSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = loginDetailsSharedPreferences.edit();
                USERNAME_ENCRYPT =  loginDetailsSharedPreferences.getString(useremail_stringstore, null);
                PASSWORD_ENCRYPT =  loginDetailsSharedPreferences.getString(userpassword_stringstore, null);

                decrypt(USERNAME_ENCRYPT,PASSWORD_ENCRYPT);//decrypt user name and password to validate the user.

                if (emailAddress.getText().toString().equals(new String(decodedUserEmail)) && password.getText().toString().equals(new String(decodedUserPassword))) {

                    if (saveLoginCheckBox.isChecked()) {

                        setBooleanValueSharedPreferences(checkedOrNot,true);


                    }else{
                        setBooleanValueSharedPreferences(checkedOrNot,false);
                    }

                    Toast.makeText(UserloginActivity.this, "Signing-in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent("com.example.android.shustudenthelper.MyCustomHomeActivity");
                    startActivity(intent);
                } else {
                    Toast.makeText(UserloginActivity.this, "Email or Password details are incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }//end method
    public void forgotPasswordClick() {

        TextView forgot_Password= (TextView) findViewById(forgotpassword);

        forgot_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserloginActivity.this, "You are being redirected to password reset page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("com.example.android.shustudenthelper.ForgotPasswordActivity");
                    startActivity(intent);
            }
        });
    }//end method


    public void setBooleanValueSharedPreferences(String key, Boolean value){

        SharedPreferences.Editor editor = loginDetailsSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }//end method

    private void decrypt(String USERNAME_ENCRYPT,String PASSWORD_ENCRYPT){
        try {
            // Get the Key
            byte[] key = (getResources().getString(R.string.secret_key_encryption)).getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            sks1 = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            Log.e(UsernameRegisterActivity.TAG, "AES secret key spec error");
        }

        // Decode the encoded data with AES


        try {
            Cipher c1 = Cipher.getInstance("AES");
            c1.init(Cipher.DECRYPT_MODE, sks1);
            decodedUserEmail = c1.doFinal(Base64.decode(USERNAME_ENCRYPT,Base64.DEFAULT));

            Cipher c2 = Cipher.getInstance("AES");
            c2.init(Cipher.DECRYPT_MODE, sks1);
            decodedUserPassword = c2.doFinal(Base64.decode(PASSWORD_ENCRYPT,Base64.DEFAULT));

        } catch (Exception e) {
            Log.e(UsernameRegisterActivity.TAG, "AES decryption error");
        }
    }

}//end class