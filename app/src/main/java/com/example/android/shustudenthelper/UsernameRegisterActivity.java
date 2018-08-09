package com.example.android.shustudenthelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import static android.R.attr.value;
import static android.os.Build.VERSION_CODES.N;
import static com.example.android.shustudenthelper.UserloginActivity.USERNAME;

public class UsernameRegisterActivity extends AppCompatActivity {
    /*Encryption variables and keys*/
    public static final String TAG = "SymmetricAlgorithmAES";
    public SecretKeySpec sks1 = null;
    private String saveEncryptedEmail;
    private String saveEncryptedPassword;

    // Encode the original data with AES
    public static byte[] encodedUserName = null;
    public static byte[] encodedPassword = null;

    /* New instances of xml elements*/
    private static EditText user_Email;
    private static EditText user_password;
    private static EditText confirm_password;
    private static Button register_user;

    /* strings to Store user inputs*/
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static String useremail_stringstore = "userEmail";
    public static String userpassword_stringstore = "userPassword";
    public static String userpasswordconfirm_stringstore = "userConfirmPassword";
    public static String userRegistration_Complete = "registration_complete";

    SharedPreferences loginDetailsSharedPreferences;

    /* Shared Preferences key values*/
    public static String userEmailSharedPrefValue;
    public static String userPasswordSharedPrefValue;
    public static String confirmPasswordSharedPrefValue;
    public static Boolean registrationCompleteSharedPrefValue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_register);
        loginDetailsSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        onclickUserRegisterButton();/* User clicks on Register button*/
    }

    public void onclickUserRegisterButton() {

        /* Registering the instances with real ID's*/
        user_Email = (EditText) findViewById(R.id.email_user);
        user_password = (EditText) findViewById(R.id.password_user);
        confirm_password = (EditText) findViewById(R.id.password_confirm);
        register_user = (Button) findViewById(R.id.email_sign_up_button);

        /* Setting OnClick Listener*/
        register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* Saving the user input to Shared Preferences key values*/
                userEmailSharedPrefValue = user_Email.getText().toString();
                userPasswordSharedPrefValue = user_password.getText().toString();
                confirmPasswordSharedPrefValue = confirm_password.getText().toString();

                // Set up secret key spec for 128-bit AES encryption and decryption


                /* Checking for validation*/
                if (userPasswordSharedPrefValue.equals(confirmPasswordSharedPrefValue) && (userEmailSharedPrefValue.isEmpty())){
                    Toast.makeText(UsernameRegisterActivity.this, "Please enter valid e-mail address", Toast.LENGTH_SHORT).show();
                }

                else if ((userPasswordSharedPrefValue.isEmpty()) && (confirmPasswordSharedPrefValue.isEmpty()) &&(!userEmailSharedPrefValue.isEmpty())){
                    Toast.makeText(UsernameRegisterActivity.this, "Please enter password!!!", Toast.LENGTH_SHORT).show();
                }

                else if ((!userPasswordSharedPrefValue.isEmpty()) && (confirmPasswordSharedPrefValue.isEmpty()) &&(!userEmailSharedPrefValue.isEmpty())){
                    Toast.makeText(UsernameRegisterActivity.this, "Please confirm password!!!", Toast.LENGTH_SHORT).show();
                }
                else if(!isEmailValid(userEmailSharedPrefValue)){
                    Toast.makeText(UsernameRegisterActivity.this, "Please Enter Valid Email!!!", Toast.LENGTH_SHORT).show();
                }
                else if(!isPasswordValid(userPasswordSharedPrefValue)){
                    Toast.makeText(UsernameRegisterActivity.this, "Please Enter more than 4 characters for password!!!", Toast.LENGTH_SHORT).show();
                }

                else if (userPasswordSharedPrefValue.equals(confirmPasswordSharedPrefValue) && (!userEmailSharedPrefValue.isEmpty())
                && (!userPasswordSharedPrefValue.isEmpty()) && (!confirmPasswordSharedPrefValue.isEmpty())) {

                    Encryption();//Encrypt userName and Password

                    Toast.makeText(UsernameRegisterActivity.this, "Username Registered Successfully", Toast.LENGTH_SHORT).show();

                    /* Storing the user input values to shared Preferences(committing) using key values*/
                    setLoginDetailsSharedPreferences(useremail_stringstore, saveEncryptedEmail);
                    setLoginDetailsSharedPreferences(userpassword_stringstore,saveEncryptedPassword);
                    setLoginDetailsSharedPreferences(userpasswordconfirm_stringstore, confirmPasswordSharedPrefValue);
                    setBooleanValueSharedPreferences(userRegistration_Complete,registrationCompleteSharedPrefValue);


                    /* Starts new activity if the user successfully registers*/
                    Intent intent = new Intent("com.example.android.shustudenthelper.PasswordRecoveryActivity");
                    startActivity(intent);
                } else {
                    /* Else make sure, error is shown to the user upon unsuccess full registration */
                    Toast.makeText(UsernameRegisterActivity.this, "Email or Password details are incorrect", Toast.LENGTH_SHORT).show();
                    confirm_password.setText("");
                    user_password.setText("");
                }

            }
        });
    }
    /* method to store strings Shared Preferences key values*/
    public void setLoginDetailsSharedPreferences(String key, String value){

        SharedPreferences.Editor editor = loginDetailsSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    /* method to store boolean Shared Preferences key values*/
    public void setBooleanValueSharedPreferences(String key, Boolean value){

        SharedPreferences.Editor editor = loginDetailsSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    private void Encryption(){

        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);

            // Get the Key
            byte[] key = (getResources().getString(R.string.secret_key_encryption)).getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            sks1 = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            Log.e(TAG, "AES secret key spec error");
        }

        try {
            Cipher c1 = Cipher.getInstance("AES");
            Cipher c2 = Cipher.getInstance("AES");
            c1.init(Cipher.ENCRYPT_MODE, sks1);
            c2.init(Cipher.ENCRYPT_MODE, sks1);
            encodedUserName = c1.doFinal(userEmailSharedPrefValue.getBytes());
            encodedPassword = c2.doFinal(userPasswordSharedPrefValue.getBytes());
        } catch (Exception e) {
            Log.e(TAG, "AES encryption error");
        }

        saveEncryptedEmail = Base64.encodeToString(encodedUserName, Base64.DEFAULT);
        saveEncryptedPassword = Base64.encodeToString(encodedPassword, Base64.DEFAULT);
    }


    private boolean isEmailValid(String email) {

        if (email.contains("@") && (email.contains(".com") || email.contains(".edu"))){
            return true;
        }else
            return false;
    }

    private boolean isPasswordValid(String password) {

        if (password.length() > 4){
            return true;
        }else{
            return false;
        }

    }

}//end class
