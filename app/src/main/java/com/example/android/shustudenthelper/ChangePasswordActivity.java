package com.example.android.shustudenthelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.shustudenthelper.userlogin.UserloginActivity;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import static com.example.android.shustudenthelper.UsernameRegisterActivity.MyPREFERENCES;
import static com.example.android.shustudenthelper.UsernameRegisterActivity.userpassword_stringstore;
import static com.example.android.shustudenthelper.UsernameRegisterActivity.userpasswordconfirm_stringstore;

/**
 * A screen that offers changing password via new password/verify.
 */
public class ChangePasswordActivity extends AppCompatActivity {

    private String PASSWORD;
    private SecretKeySpec sks1;
    private byte[] decodedUserPassword = null;
    private String newPassword;
    /*create Instances to cast to real ID's*/
    private AutoCompleteTextView mCurrentPassword;
    private EditText mNewPassword;
    private EditText mVerifyPassword;
    private Button mChangePasswordButton;
    private String saveEncryptedNewPassword;
    private byte[] encodedNewPassword;
    SharedPreferences loginDetailsSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);//cast layout

        // cast instances to read ID's
        mCurrentPassword = (AutoCompleteTextView) findViewById(R.id.current_password);
        mNewPassword = (EditText) findViewById(R.id.new_password);
        mVerifyPassword = (EditText) findViewById(R.id.new_password_verify);
        mChangePasswordButton = (Button) findViewById(R.id.change_password_button);

        /*Set listener on change password button
        * check for valid info
        * Then let user to change the password*/
        mChangePasswordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                loginDetailsSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = loginDetailsSharedPreferences.edit();

                //Get the old password from Shared Preferences
                PASSWORD =  loginDetailsSharedPreferences.getString(userpassword_stringstore, null);

                decryption();

                //Read the user entered data into strings to verify
                String currentEnteredPassword = mCurrentPassword.getText().toString();
                newPassword = mNewPassword.getText().toString();
                String verifyPassword = mVerifyPassword.getText().toString();

                //verify the user provided data with sharedPreferences
                if(currentEnteredPassword.equals(new String(decodedUserPassword)) && newPassword.equals(verifyPassword)){
                    Toast.makeText(ChangePasswordActivity.this, "Password Changed successfully", Toast.LENGTH_SHORT).show();

                    //On success, change the password in shared preferences and send the user to login activity to login
                    encryption();
                    setLoginDetailsSharedPreferences(userpassword_stringstore, saveEncryptedNewPassword);
                    setLoginDetailsSharedPreferences(userpasswordconfirm_stringstore, verifyPassword);

                    //send the user to login
                    Intent intent = new Intent(ChangePasswordActivity.this, UserloginActivity.class);
                    startActivity(intent);

                }else if(!currentEnteredPassword.equals(PASSWORD) || !newPassword.equals(verifyPassword))
                {   //if not success, show the error to user
                    Toast.makeText(ChangePasswordActivity.this, "Password does not match, Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }//end On create method

    /*Method to store sharedPreferences*/
    private void setLoginDetailsSharedPreferences(String key, String value){

        SharedPreferences.Editor editor = loginDetailsSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }//end method

    private void decryption(){
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
            decodedUserPassword = c1.doFinal(Base64.decode(PASSWORD,Base64.DEFAULT));

        } catch (Exception e) {
            Log.e(UsernameRegisterActivity.TAG, "AES decryption error");
        }
    }

    private void encryption(){

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
            Log.e(UsernameRegisterActivity.TAG, "AES secret key spec error");
        }

        try {
            Cipher c1 = Cipher.getInstance("AES");
            c1.init(Cipher.ENCRYPT_MODE, sks1);
            encodedNewPassword = c1.doFinal(newPassword.getBytes());
        } catch (Exception e) {
            Log.e(UsernameRegisterActivity.TAG, "AES encryption error");
        }

        saveEncryptedNewPassword = Base64.encodeToString(encodedNewPassword, Base64.DEFAULT);
    }


}//end class






