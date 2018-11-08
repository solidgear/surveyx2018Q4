package es.academy.solidgear.surveyx;

import android.app.Application;
import android.content.Intent;
import android.test.ApplicationTestCase;
import android.widget.Button;
import android.widget.EditText;

import junit.framework.Assert;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;

import es.academy.solidgear.surveyx.api.APIService;
import es.academy.solidgear.surveyx.managers.ApiManager;
import es.academy.solidgear.surveyx.models.LoginModel;
import es.academy.solidgear.surveyx.ui.activities.LoginActivity;
import es.academy.solidgear.surveyx.ui.activities.MainActivity;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(org.robolectric.RobolectricTestRunner.class)
public class LoginTest {

    @Test
    public void clickingLogin_shouldStartLoginActivity() {
        LoginActivity loginActivity = Robolectric.setupActivity(LoginActivity.class);
        EditText userEditText = (EditText) loginActivity.findViewById(R.id.userLoginText);
        EditText passEditText = (EditText) loginActivity.findViewById(R.id.passLoginText);
        Button loginButton = (Button) loginActivity.findViewById(R.id.login_button);

        Assert.assertTrue(userEditText.isEnabled());
        Assert.assertTrue(passEditText.isEnabled());
        Assert.assertTrue(loginButton.isEnabled());

        ApiManager mockedApiManager = Mockito.mock(ApiManager.class);

        loginActivity.setApiManager(mockedApiManager);

        APIService.OnResponse<LoginModel> mockedResponse = Mockito.mock(APIService.OnResponse.class);
        loginActivity.setOnLoginResponse(mockedResponse);

        userEditText.setText("user1");
        passEditText.setText("pass1");
        loginButton.performClick();

        Mockito.verify(mockedApiManager, Mockito.times(1))
                .login("user1", "pass1", mockedResponse);
    }

}