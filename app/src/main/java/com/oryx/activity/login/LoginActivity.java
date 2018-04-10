package com.oryx.activity.login;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oryx.R;
import com.oryx.activity.core.AbstractActivity;
import com.oryx.context.IUser;
import com.oryx.prefs.IUserPrefs;
import com.oryx.utils.PrefUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AbstractActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.emailField)
    EditText _emailField;
    @BindView(R.id.passwordField)
    EditText _passwordField;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.rememberMeField)
    CheckBox _remember_me;
    @BindView(R.id.link_signup)
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        IUser.userEmail = _emailField.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically

                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailField.getText().toString();
        String password = _passwordField.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailField.setError("enter a valid email address");
            valid = false;
        } else {
            _emailField.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordField.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordField.setError(null);
        }

        return valid;
    }

    @Override
    protected void savePreferences() {
        if(_remember_me.isChecked()) {
            SharedPreferences settings = PrefUtils.loadSettingsPreferences(this);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(IUserPrefs.PREF_EMAIL, _emailField.getText().toString());
            editor.putString(IUserPrefs.PREF_PASSWORD, _passwordField.getText().toString());
            editor.putBoolean(IUserPrefs.PREF_REMEMBER, _remember_me.isChecked());
            editor.commit();
        }
    }

    @Override
    protected void loadPreferences() {
        SharedPreferences settings = PrefUtils.loadSettingsPreferences(this);
        _emailField.setText(settings.getString(IUserPrefs.PREF_EMAIL, ""));
        _passwordField.setText(settings.getString(IUserPrefs.PREF_PASSWORD, ""));
        _remember_me.setChecked(settings.getBoolean(IUserPrefs.PREF_REMEMBER, false));
    }
}