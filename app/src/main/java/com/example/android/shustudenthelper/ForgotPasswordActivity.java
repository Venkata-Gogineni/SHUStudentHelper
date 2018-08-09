package com.example.android.shustudenthelper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static android.provider.Telephony.Carriers.PASSWORD;
import static com.example.android.shustudenthelper.PasswordRecoveryActivity.store_Answer;
import static com.example.android.shustudenthelper.PasswordRecoveryActivity.store_Question;
import static com.example.android.shustudenthelper.UsernameRegisterActivity.MyPREFERENCES;
import static com.example.android.shustudenthelper.UsernameRegisterActivity.useremail_stringstore;
import static com.example.android.shustudenthelper.UsernameRegisterActivity.userpassword_stringstore;

public class ForgotPasswordActivity extends AppCompatActivity {

    /*New instances to cast to the read ID's */
    private TextView securityQuestionTextView;
    private EditText securityAnswerEditText;
    private Button btnShow;

    /*Strings to store data after reading from shared preferences*/
    public static String QUESTION;
    public static String ANSWER;
    public static String PASSWORD;

    /*Decryption variables*/
    private SecretKeySpec sks1;
    private byte[] decodedUserPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //setting the instances to real ID's
        securityQuestionTextView = (TextView)findViewById(R.id.security_question_textView);
        securityAnswerEditText = (EditText) findViewById(R.id.security_prompt_answer);
        btnShow = (Button) findViewById(R.id.show_password_button);

        //read data from shared preferences
        SharedPreferences loginDetailsSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        QUESTION =  loginDetailsSharedPreferences.getString(store_Question, null);//gets question value from SharedPreferences
        ANSWER =  loginDetailsSharedPreferences.getString(store_Answer, null);//gets answer value from SharedPreferences
        PASSWORD = loginDetailsSharedPreferences.getString(userpassword_stringstore,null);

        decrypt(PASSWORD);

        securityQuestionTextView.setText(QUESTION);//setting the text for the object

        onClickListenerShowDetails();//Listener method

    }
//set listener on button to handle the dialog box
    public void onClickListenerShowDetails(){
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userAnswer = securityAnswerEditText.getText().toString();
                if (userAnswer.equals(ANSWER)){

                    //show dialog box with stored password
                    AlertDialog.Builder showPasswordDialogBox = new AlertDialog.Builder(ForgotPasswordActivity.this);
                    showPasswordDialogBox.setMessage("Your Password is : " + new String(decodedUserPassword))
                            .setNegativeButton(R.string.okay, new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = showPasswordDialogBox.create();//creating dialog alert
                    alertDialog.show();//showing alert
                }
                else {
                    Toast.makeText(ForgotPasswordActivity.this, "Security answer provided doesn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }//end onclickListenerShowDetails method

    private void decrypt(String PASSWORD_ENCRYPT){
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

            Cipher c2 = Cipher.getInstance("AES");
            c2.init(Cipher.DECRYPT_MODE, sks1);
            decodedUserPassword = c2.doFinal(Base64.decode(PASSWORD_ENCRYPT,Base64.DEFAULT));

        } catch (Exception e) {
            Log.e(UsernameRegisterActivity.TAG, "AES decryption error");
        }
    }

}//end class
