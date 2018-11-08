package es.academy.solidgear.surveyx.ui.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import es.academy.solidgear.surveyx.R;
import es.academy.solidgear.surveyx.api.APIService;
import es.academy.solidgear.surveyx.managers.ApiManager;
import es.academy.solidgear.surveyx.models.LoginModel;

public class LoginActivity extends BaseActivity {
    private static final String AUTH_ERROR = "com.android.volley.AuthFailureError";

    private ProgressBar progressBar;
    private Button loginButton;
    private EditText usernameTextBox;
    private EditText passwordTextBox;
    private ApiManager apiManager;

    private APIService.OnResponse<LoginModel> onLoginResponse = new APIService.OnResponse<LoginModel>() {
        @Override
        public void onSuccess(LoginModel login) {
            String token = login.getToken();
            openMainActivity(token);
            finish();
        }

        @Override
        public void onFailure() {
            showAuthenticationError();
            hideLoginInProgress();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiManager = new ApiManager(this);

        getSupportActionBar().hide();

        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
        usernameTextBox = (EditText) findViewById(R.id.userLoginText);
        passwordTextBox = (EditText) findViewById(R.id.passLoginText);
        loginButton = (Button) findViewById(R.id.login_button);
        TextView sgLoginText = (TextView) findViewById(R.id.sgLoginText);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/KGSecondChancesSketch.ttf");
        sgLoginText.setTypeface(tf);
        loginButton.setTypeface(tf);

        Typeface tfMuseum = Typeface.createFromAsset(getAssets(), "fonts/Museo300-Regular.otf");
        usernameTextBox.setTypeface(tfMuseum);
        passwordTextBox.setTypeface(tfMuseum);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
    }

    private void doLogin() {

        showLoginInProgress();

        String username = usernameTextBox.getText().toString();
        String password = passwordTextBox.getText().toString();

        apiManager.login(username, password, onLoginResponse);
    }

    private void showLoginInProgress() {
        loginButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoginInProgress() {
        loginButton.setEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void openMainActivity(String token) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_TOKEN, token);
        startActivity(intent);
    }

    private void showAuthenticationError() {
        Toast.makeText(LoginActivity.this, getString(R.string.incorrect_login), Toast.LENGTH_LONG).show();
    }

    public void setApiManager(ApiManager apiManager) {
        this.apiManager = apiManager;
    }

    public void setOnLoginResponse(APIService.OnResponse<LoginModel> onLoginResponse) {
        this.onLoginResponse = onLoginResponse;
    }
}
