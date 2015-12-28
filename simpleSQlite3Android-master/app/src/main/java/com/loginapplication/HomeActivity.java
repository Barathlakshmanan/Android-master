package com.loginapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by cipher1729 on 10/9/2015.
 */
public class HomeActivity extends Activity
{
    Button btnSignIn,btnSignUp;
    LoginDataBaseAdapter loginDataBaseAdapter;
    String currUserName, currPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        /*// create a instance of SQLite Database
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();*/

        // Get The Refference Of Buttons
        btnSignIn=(Button)findViewById(R.id.buttonSignIN);
        btnSignUp=(Button)findViewById(R.id.buttonSignUP);

        // Set OnClick Listener on SignUp button
        //need to make post request here
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub

                /// Create Intent for SignUpActivity  abd Start The Activity
                Intent intentSignUP=new Intent(getApplicationContext(),SignUPActivity.class);
                startActivity(intentSignUP);
            }
        });
    }
    // Methos to handleClick Event of Sign In Button
    public void signIn(View V)
    {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.login);
        dialog.setTitle("Login");

        // get the Refferences of views
        final EditText editTextUserName=(EditText)dialog.findViewById(R.id.editTextUserNameToLogin);
        final  EditText editTextPassword=(EditText)dialog.findViewById(R.id.editTextPasswordToLogin);

        Button btnSignIn=(Button)dialog.findViewById(R.id.buttonSignIn);

        // Set On ClickListener
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // get The User name and Password
                String userName=editTextUserName.getText().toString();
                String password=editTextPassword.getText().toString();
                currUserName = userName;
                currPassword = password;
                // fetch the Password form database for respective user name
                //String storedPassword=loginDataBaseAdapter.getSinlgeEntry(userName);
                validate(userName, password);

                // check if the Stored password matches with  Password entered by user
                /*if(password.equals(storedPassword))
                {
                    Toast.makeText(HomeActivity.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(HomeActivity.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                }*/
            }
        });

        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database
        /*loginDataBaseAdapter.close();*/
    }


    private void validate(String userName, String passWord)
    {
        Util.validateCredentials("http://192.168.2.16:3000/login"+"?phone="+currUserName+"&password="+currPassword);

        while(Util.polled==false);
        if(Util.sb.toString().equals("Validated!")) {
            Toast.makeText(getApplicationContext(), "Validated. ", Toast.LENGTH_LONG).show();
            Intent intentDisplay = new Intent(getApplicationContext(), DisplayGarage.class);
            intentDisplay.putExtra("username", currUserName);
            startActivity(intentDisplay);
        }
        else
            Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_LONG).show();
        Util.polled=false;
    }
}
