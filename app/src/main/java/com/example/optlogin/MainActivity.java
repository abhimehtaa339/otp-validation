package com.example.optlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextInputEditText mobile_number;
    MaterialButton submit;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mobile_number = findViewById(R.id.mobile_number);
        submit = findViewById(R.id.extended_fab);
        bar = findViewById(R.id.number_enter_progressbar);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                  String number =  mobile_number.getText().toString();

                  if (number.length() != 10){
                      Toast.makeText(MainActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                  }
                  else {
                      bar.setVisibility(View.VISIBLE);
                      submit.setVisibility(View.INVISIBLE);

                      PhoneAuthProvider.getInstance().verifyPhoneNumber(
                              "+91" + mobile_number.getText().toString(),
                              60,
                              TimeUnit.SECONDS,
                              MainActivity.this,
                              new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                  @Override
                                  public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                      bar.setVisibility(View.GONE);
                                      submit.setVisibility(View.VISIBLE);
                                  }

                                  @Override
                                  public void onVerificationFailed(@NonNull FirebaseException e) {
                                      bar.setVisibility(View.GONE);
                                      submit.setVisibility(View.VISIBLE);
                                      Toast.makeText(MainActivity.this , e.getMessage() , Toast.LENGTH_SHORT).show();
                                  }

                                  @Override
                                  public void onCodeSent(@NonNull String backend_otp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                      bar.setVisibility(View.GONE);
                                      submit.setVisibility(View.VISIBLE);
                                      Intent second_screen = new Intent(MainActivity.this, otp_enter_screen.class);
                                      second_screen.putExtra("otp",backend_otp);
                                      second_screen.putExtra("number" , number);
                                      startActivity(second_screen);
                                  }
                              }
                      );

                  }
                }
                catch (Exception e){
                    System.out.println(e);
                }
            }
        });

    }
}