package tech.team17;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tech.team17.util.Utility;

/**
 * Created by ryu on 4/11/16.
 */

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";
    public static final String BASE_URL = "http://app.kenalyse.io/";
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView failedLoginMessage;
    View focusView = null;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();
        failedLoginMessage = (TextView) findViewById(R.id.failed_login);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        assert mEmailSignInButton != null;
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                failedLoginMessage.setText("");
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        boolean mCancel = this.loginValidation();
        if (mCancel) {
            focusView.requestFocus();
        } else {
            loginProcessWithRetrofit(new LoginForm(email, password, "token"));
        }
    }

    private boolean loginValidation() {

        mEmailView.setError(null);
        mPasswordView.setError(null);

        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        boolean cancel = false;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        return cancel;
    }

    private void populateAutoComplete() {
        String[] countries = getResources().getStringArray(R.array.autocomplete);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        mEmailView.setAdapter(adapter);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private ApiInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiInterface mInterfaceService = retrofit.create(ApiInterface.class);
        return mInterfaceService;
    }

    private void loginProcessWithRetrofit(final LoginForm form) {
        ApiInterface mApiService = this.getInterfaceService();
        Call<LoginResponse> mService = mApiService.authenticate(form);

        mService.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse mLoginResponseObject = response.body();

                if (mLoginResponseObject != null && mLoginResponseObject.token != null) {
                    Toast.makeText(LoginActivity.this, "successful login " + mLoginResponseObject.token, Toast.LENGTH_LONG).show();

                    String id = mLoginResponseObject.token ;

                    SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", Context.MODE_PRIVATE);
                    Utility user_token = new Utility();
                    String s = user_token.user_token;
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(s, id);
                    editor.apply();

                    sp = getApplicationContext().getSharedPreferences("sharedPrefName", Context.MODE_PRIVATE);
                    id = sp.getString(s, null);

                    Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                    loginIntent.putExtra("EMAIL", email);
                    startActivity(loginIntent);

                } else {
                    Toast.makeText(LoginActivity.this, "failed login", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                call.cancel();
                Toast.makeText(LoginActivity.this, "check your network connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void setUserObject(Context c, String userObject, String key) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, userObject);
        editor.apply();
    }
    public static String getUserObject(Context ctx, String key) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String userObject = pref.getString(key, null);
        return userObject;
    }

}