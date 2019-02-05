package com.imbuegen.alumniapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.imbuegen.alumniapp.Service.SFHandler;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends BaseActivity{

    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private EditText inputEmail, inputPassword;
    private Button btnLogIn;
    private TextView textSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }
    @Override
    protected void onResume() {
        super.onResume();

        init();
        setOnClicks();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        textSignUp = findViewById(R.id.txt_signUp);
        btnLogIn= findViewById(R.id.btn_login);
        inputEmail = findViewById(R.id.editTxt_email);
        inputPassword = findViewById(R.id.editTxt_password);

        inputEmail.requestFocus();
    }

    private void setOnClicks() {
        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.PhoneBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build());
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setTheme(R.style.CustomTheme)
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);

            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //authenticate user
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    if (password.length() < 6)
                                        Toast.makeText(LoginActivity.this, "Password too short!", Toast.LENGTH_LONG).show();
                                    else
                                        Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Login Successful :)",
                                            Toast.LENGTH_SHORT).show();
                                    SFHandler.setString(getSharedPreferences("Auth",MODE_PRIVATE),SFHandler.USER_KEY,"Alumni");


                                    gotToAlumniScreen();
                                }
                            }
                        });
            }
        });
    }

    private void gotToAlumniScreen() {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successful sign in
                SFHandler.setString(getSharedPreferences("Auth",MODE_PRIVATE),SFHandler.USER_KEY,"Student");
                Toast.makeText(this, "Signed IN", Toast.LENGTH_SHORT).show();

                goToStudentScreen();
            } else {
                Toast.makeText(this, "Student Sign in Uncessfull", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void goToStudentScreen() {
        Intent i = new Intent(this,DepartmentsActivity.class);
        startActivity(i);
        finish();
    }
}
