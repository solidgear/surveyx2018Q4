package es.academy.solidgear.surveyx.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import es.academy.solidgear.surveyx.R;

public class SocialNetworkActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_network);
        initToolbar();

        Button finishButton = (Button) findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
