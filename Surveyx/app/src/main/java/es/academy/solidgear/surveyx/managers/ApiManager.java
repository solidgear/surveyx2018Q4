package es.academy.solidgear.surveyx.managers;

import android.content.Context;

import java.util.ArrayList;

import es.academy.solidgear.surveyx.api.APIService;
import es.academy.solidgear.surveyx.api.params.SurveyPostParams;
import es.academy.solidgear.surveyx.models.LoginModel;
import es.academy.solidgear.surveyx.models.QuestionModel;
import es.academy.solidgear.surveyx.models.SurveyModel;
import es.academy.solidgear.surveyx.models.SurveysModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiManager {

    private final APIService apiService;
    private Context context;

    public ApiManager(Context context) {
        this.context = context;
        this.apiService = NetworkManager.getInstance().getAPI();
    }

    public void login(String username,
                      String password,
                      APIService.OnResponse<LoginModel> onResponse) {
        apiService
                .login(username, password)
                .enqueue(new RetrofitCallback<>(context, onResponse));
    }

    public void fetchQuestion(int questionId,
                              APIService.OnResponse<QuestionModel> onResponse) {
        apiService
                .getQuestion(questionId)
                .enqueue(new RetrofitCallback<>(context, onResponse));
    }

    public void fetchSurveys(String token,
                             APIService.OnResponse<SurveysModel> onResponse) {
        apiService
                .getAllSurveys(token)
                .enqueue(new RetrofitCallback<SurveysModel>(context, onResponse));
    }

    public void fetchSurvey(int surveyId,
                            APIService.OnResponse<SurveyModel> onResponse) {
        apiService
                .getSurvey(surveyId)
                .enqueue(new RetrofitCallback<SurveyModel>(context, onResponse));
    }

    public void sendAnswer(int questionId,
                           int surveyId,
                           ArrayList<Integer> responseSelected,
                           String token,
                           APIService.OnResponse<ResponseBody> onResponse) {
        SurveyPostParams surveyPostParams = new SurveyPostParams();
        surveyPostParams.setToken(token);
        surveyPostParams.setSurvey(surveyId);
        surveyPostParams.setQuestion(questionId);
        surveyPostParams.setChoice(responseSelected);

        apiService
                .sendResponseToQuestion(surveyPostParams)
                .enqueue(new RetrofitCallback<ResponseBody>(context, onResponse));
    }

    private class RetrofitCallback<T> implements Callback<T> {

        private final APIService.OnResponse<T> onResponse;
        private Context context;

        public RetrofitCallback(Context ctx, APIService.OnResponse<T> onResponse) {
            this.context = ctx;
            this.onResponse = onResponse;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (response.isSuccessful()) {
                onResponse.onSuccess(response.body());
            } else {
                onResponse.onFailure();
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            Utils.showUnexpectedNetworkError(this.context);
        }
    }

}
