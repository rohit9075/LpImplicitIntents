package com.rohit.lpregister.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rohit.lpregister.R;
import com.rohit.lpregister.database.DatabaseHelper;
import com.rohit.lpregister.utils.InputValidation;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

    private EditText mEditTextEmail, mEditTextPassword;

    private Button mButtonLogin;

    private TextView mTextViewRegister, mTextViewAdminLogin, mTextViewForgotPassword;

    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView(); // initView() method call

        clickListener(); // clickListener(); method call

        initObject(); // initObject method call
    }


    /**
     * clickListener() method definition
     */

    private void clickListener() {

        mButtonLogin.setOnClickListener(this);

        mTextViewAdminLogin.setOnClickListener(this);
        mTextViewRegister.setOnClickListener(this);
        mTextViewForgotPassword.setOnClickListener(this);

    }

    /**
     * intiView Method definition
     */
    private void initView() {

      mEditTextEmail = findViewById(R.id.editText_login_email);
      mEditTextPassword = findViewById(R.id.editText_login_password);

      mButtonLogin = findViewById(R.id.button_login);

      mTextViewAdminLogin = findViewById(R.id.textView_admin);
      mTextViewRegister = findViewById(R.id.textView_register);
      mTextViewForgotPassword = findViewById(R.id.textView_forgot_password);


    }

    public void initObject(){

        mDatabaseHelper = new DatabaseHelper(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.button_login:

                candidateLogin();
                break;

            case R.id.textView_admin:

                Toast.makeText(this, "Admin Login Clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.textView_register:
                Intent mIntentRegister = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(mIntentRegister);
                break;

            case R.id.textView_forgot_password:
                break;
        }

    }

    private void candidateLogin() {
        boolean login = mDatabaseHelper.checkCandidate(mEditTextEmail.getText().toString().trim(),mEditTextPassword.getText().toString().trim());

        if (login){

            Intent mIntentLogin = new Intent(LoginActivity.this,CandidateProfileActivity.class);
            mIntentLogin.putExtra("email", mEditTextEmail.getText().toString().trim());
            startActivity(mIntentLogin);
            finish();

        }else {
            Toast.makeText(this, "Invalid id and password", Toast.LENGTH_SHORT).show();
        }
    }
}
