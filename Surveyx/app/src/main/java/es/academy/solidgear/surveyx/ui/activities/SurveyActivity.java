package es.academy.solidgear.surveyx.ui.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import es.academy.solidgear.surveyx.R;
import es.academy.solidgear.surveyx.api.APIService;
import es.academy.solidgear.surveyx.api.APIService.OnResponse;
import es.academy.solidgear.surveyx.managers.ApiManager;
import es.academy.solidgear.surveyx.managers.DialogsManager;
import es.academy.solidgear.surveyx.managers.Utils;
import es.academy.solidgear.surveyx.models.QuestionModel;
import es.academy.solidgear.surveyx.models.SurveyModel;
import es.academy.solidgear.surveyx.ui.fragments.InformationDialogFragment;
import es.academy.solidgear.surveyx.ui.fragments.QuestionFragment;
import es.academy.solidgear.surveyx.ui.views.CustomButton;
import okhttp3.ResponseBody;

public class SurveyActivity extends BaseActivity implements QuestionFragment.OnAnswerChanged{
    private static final String TAG = "SurveyActivity";
    public static final String SURVEY_ID = "surveyId";
    public static final String EXTRA_TOKEN = "token";

    private CustomButton nextButton;
    private TextView currentQuestionTextView;
    private TextView totalTextView;

    private int[] questionIds;
    private int currentQuestionIndex = 0;

    private int surveyId;

    private String token;

    private ApiManager apiManager;
    private final DialogsManager dialogsManager = DialogsManager.getInstance();
    private ArrayList<Integer> selectedAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        initToolbar();

        apiManager = new ApiManager(this);

        Bundle extras = getIntent().getExtras();
        token = extras.getString(EXTRA_TOKEN, null);

        nextButton = (CustomButton) findViewById(R.id.buttonNext);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performNext();
            }
        });
        nextButton.setEnabled(false);

        CustomButton cancelButton = (CustomButton) findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSurvey();
            }
        });

        currentQuestionTextView = (TextView) findViewById(R.id.textViewCurrentQuestion);
        totalTextView = (TextView) findViewById(R.id.textViewTotal);

        surveyId = getIntent().getExtras().getInt(SURVEY_ID);
        getSurvey(surveyId);
    }

    @Override
    public void onBackPressed() {
        cancelSurvey();
    }

    private void getSurvey(int surveyId) {
        final InformationDialogFragment dialog = dialogsManager.showInformationDialog(
                SurveyActivity.this,
                R.string.dialog_getting_survey);

        APIService.OnResponse<SurveyModel> onSurveyResponse = new APIService.OnResponse<SurveyModel>() {
            @Override
            public void onSuccess(SurveyModel survey) {
                questionIds = survey.getQuestions();
                totalTextView.setText(String.valueOf(questionIds.length));
                dialog.dismiss();

                showQuestion();
            }

            @Override
            public void onFailure() {
                dialog.dismiss();
                Utils.showUnexpectedNetworkError(SurveyActivity.this);
            }
        };

        apiManager.fetchSurvey(surveyId, onSurveyResponse);
    }

    private void getQuestion(int questionId) {
        OnResponse<QuestionModel> onFetchQuestionResponse = new OnResponse<QuestionModel>() {
            @Override
            public void onSuccess(QuestionModel question) {
                // Set the question title
                currentQuestionTextView.setText(String.valueOf(SurveyActivity.this.currentQuestionIndex + 1));

                // Update the question fragment
                QuestionFragment questionFragment = new QuestionFragment();
                // Pass the question to the fragment.
                Bundle bundle = new Bundle();
                bundle.putSerializable(QuestionFragment.BUNDLE_QUESTION_KEY, question);
                questionFragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, questionFragment);
                transaction.commit();
            }

            @Override
            public void onFailure() {
                Utils.showUnexpectedNetworkError(SurveyActivity.this);
            }
        };

        apiManager.fetchQuestion(questionId, onFetchQuestionResponse);
    }

    private void showQuestion() {
        getQuestion(this.questionIds[this.currentQuestionIndex]);
    }

    private void cancelSurvey() {

        DialogInterface.OnClickListener onAccept = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SurveyActivity.this.finish();
            }
        };

        DialogInterface.OnClickListener onCancel = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        dialogsManager.showYesNoDialog(
                SurveyActivity.this,
                R.string.global_warning,
                R.string.dialog_finish_survey,
                R.string.global_yes,
                R.string.global_no,
                onAccept,
                onCancel);
    }

    private void setNextButtonEnabledStatus(boolean enabled) {
        nextButton.setEnabled(enabled);
    }

    public void setNextButtonLabel() {
        boolean isLast = currentQuestionIndex + 1 == questionIds.length;
        if (isLast) {
            nextButton.setText(R.string.global_submit);
            nextButton.setContentDescription(getString(R.string.descriptor_survey_submit_button));
        } else {
            nextButton.setText(R.string.global_next);
            nextButton.setContentDescription(getString(R.string.descriptor_survey_next_button));
        }
    }

    private void performNext() {
        sendAnswerToServer();
    }

    private void onAnswerSentToServer() {
        // Go to the next question
        this.currentQuestionIndex++;

        if (this.currentQuestionIndex < this.questionIds.length) {
            showQuestion();
        } else {
            showSocialNetworkPage();
            finish();
        }
    }

    private void showSocialNetworkPage() {
        Intent intent = new Intent(SurveyActivity.this, SocialNetworkActivity.class);
        startActivity(intent);
    }

    private void sendAnswerToServer() {
        int questionId = questionIds[currentQuestionIndex];

        OnResponse<ResponseBody> onSendAnswerResponse= new OnResponse<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody response) {
                Toast.makeText(SurveyActivity.this,
                        R.string.answer_sent_successfully,
                        Toast.LENGTH_SHORT).show();
                onAnswerSentToServer();
            }

            @Override
            public void onFailure() {
                Utils.showUnexpectedNetworkError(SurveyActivity.this);
            }
        };

        apiManager.sendAnswer(questionId, surveyId, selectedAnswers, token, onSendAnswerResponse);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        cancelSurvey();
        return true;
    }

    @Override
    public void onAnswerChange(ArrayList<Integer> selectedAnswers) {
        // Once the question is answered we can continue
        this.selectedAnswers = selectedAnswers;
        setNextButtonEnabledStatus(true);
        setNextButtonLabel();
    }
}
