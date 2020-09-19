package com.basusingh.nsspgdav.app_start;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.activity.MainActivity;
import com.basusingh.nsspgdav.database_preference.AppData;
import com.basusingh.nsspgdav.database_preference.UserPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginUser extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            if(!new UserPreference(getApplicationContext()).isFirstDataLoaded()){
                startActivity(new Intent(getApplicationContext(), FcmAndLoadFirstData.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            } else {
                startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }
        setContentView(R.layout.activity_login_user);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginUser.this, RegisterUser.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginUser.this, ForgetPassword.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isValidEmail(email)){
                    Toast.makeText(getApplicationContext(), "Invalid email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                hideKeyboard();

                progressBar.setVisibility(View.VISIBLE);

                setButtonNotClickable();

               try{
                   auth.signInWithEmailAndPassword(email, password)
                           .addOnCompleteListener(LoginUser.this, new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   // If sign in fails, display a message to the user. If sign in succeeds
                                   // the auth state listener will be notified and logic to handle the
                                   // signed in user can be handled in the listener.
                                   progressBar.setVisibility(View.GONE);
                                   setButtonClickable();
                                  try{
                                      if (!task.isSuccessful()) {
                                          // there was an error
                                          Toast.makeText(LoginUser.this, "Authentication failed. Please check entered credentials.", Toast.LENGTH_LONG).show();

                                      } else {
                                          Toast.makeText(LoginUser.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                                          new UserPreference(getApplicationContext()).setUserRegistered();
                                          new UserPreference(getApplicationContext()).setEmail(email);
                                          if(!new UserPreference(getApplicationContext()).isFirstDataLoaded()){
                                              startActivity(new Intent(getApplicationContext(), FcmAndLoadFirstData.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                                              overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                              finish();
                                          } else {
                                              startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                                              overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                              finish();
                                          }
                                      }
                                  } catch (Exception e){
                                      e.printStackTrace();
                                  }
                               }
                           });
               } catch (Exception e){
                   e.printStackTrace();
               }
            }
        });

    }

    public void setButtonNotClickable(){
        btnLogin.setClickable(false);
        btnSignup.setClickable(false);
        btnReset.setClickable(false);
    }

    public void setButtonClickable(){
        btnLogin.setClickable(true);
        btnSignup.setClickable(true);
        btnReset.setClickable(true);
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onBackPressed(){

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        return true;
    }
}
