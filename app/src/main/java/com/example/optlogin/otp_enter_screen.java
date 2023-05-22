package com.example.optlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class otp_enter_screen extends AppCompatActivity {
    TextView  number_display , resend;
    ImageView backbutton;
    String number , backendotp;
    EditText num1 , num2 , num3 , num4 , num5 , num6;
    ProgressBar  bar;

    MaterialButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_enter_screen);

        number_display = findViewById(R.id.numbertv);
        backbutton = findViewById(R.id.back_button);
        resend = findViewById(R.id.resendOtpagain);
        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        num3 = findViewById(R.id.num3);
        num4 = findViewById(R.id.num4);
        num5 = findViewById(R.id.num5);
        num6 = findViewById(R.id.num6);

        submit = findViewById(R.id.submit);
        bar= findViewById(R.id.otp_enter_progressbar);

        gettingIntent("mobile_number");
        gettingIntent("otp");


        number_display.setText(number);




        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(otp_enter_screen.this , MainActivity.class);
                startActivity(intent);
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + number,
                        60,
                        TimeUnit.SECONDS,
                        otp_enter_screen.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(otp_enter_screen.this , e.getMessage() , Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newbackend_otp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                backendotp = newbackend_otp;
                                Toast.makeText(otp_enter_screen.this, "otp sended susfully", Toast.LENGTH_SHORT).show();

                            }
                        }
                );
                Toast.makeText(otp_enter_screen.this, "Resending...", Toast.LENGTH_SHORT).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!num1.getText().toString().trim().isEmpty() && !num2.getText().toString().trim().isEmpty() &&! num3.getText().toString().trim().isEmpty() && !num4.getText().toString().trim().isEmpty() && !num5.getText().toString().trim().isEmpty() && !num6.getText().toString().trim().isEmpty()){
                    String entercodeotp = num1.getText().toString() +
                            num2.getText().toString() +
                            num3.getText().toString() +
                            num4.getText().toString() +
                            num5.getText().toString() +
                            num6.getText().toString() ;

                    if(backendotp != null){
                        bar.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.INVISIBLE);


                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(
                                backendotp , entercodeotp
                        );
                        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                bar.setVisibility(View.GONE);
                                submit.setVisibility(View.VISIBLE);

                                if (task.isSuccessful()){
                                    Intent intent3 = new Intent(otp_enter_screen.this , after_otp_completed.class);
                                    intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent3);
                                    Toast.makeText(otp_enter_screen.this, "otp verified", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(otp_enter_screen.this, "Please check the internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(otp_enter_screen.this, "Please enter all all numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });

        numberotpmove();
    }
    private void numberotpmove() {
        num1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    num2.requestFocus();
                }
            }



            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        num2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    num3.requestFocus();
                }
            }



            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        num3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    num4.requestFocus();
                }
            }



            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void gettingIntent(String key){
        Intent intent1 = getIntent();
        number = intent1.getStringExtra("mobile_number");
        backendotp = intent1.getStringExtra(key);
    }
}