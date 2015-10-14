package com.example.nu.taxidriver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Hasini Gamage on 6/9/2015.
 */
public class ResetPassword extends ActionBarActivity {

    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        email = (EditText)findViewById(R.id.emailtext);

        Button emailbtn = (Button)findViewById(R.id.emailbtn);
        emailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    protected void sendEmail() {
        Log.i("Send email", "");
       String[] TO = {String.valueOf(email)};
       String[] CC = {"hasinipgamage@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL,TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email..", "");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ResetPassword.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public ResetPassword(){}


}
