package es.academy.solidgear.surveyx.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import es.academy.solidgear.surveyx.R;


public class MainActivity extends Activity {

    public static final String EXTRA_TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
        String token = null;
        if (extras != null) {
            // TODO Read token when it is stored
            token = extras.getString(EXTRA_TOKEN, null);
        }

        if (token == null) {
            showLoginPage();
        } else {
            showSurveyListPage(token);
        }

        finish();
    }

    private void showLoginPage() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void showSurveyListPage(String token) {
        Intent intent = new Intent(this, SurveysActivity.class);
        intent.putExtra(MainActivity.EXTRA_TOKEN, token);
        startActivity(intent);
    }

}
