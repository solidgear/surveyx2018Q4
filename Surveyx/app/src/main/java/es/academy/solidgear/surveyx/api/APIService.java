package es.academy.solidgear.surveyx.api;

import es.academy.solidgear.surveyx.api.params.SurveyPostParams;
import es.academy.solidgear.surveyx.models.LoginModel;
import es.academy.solidgear.surveyx.models.QuestionModel;
import es.academy.solidgear.surveyx.models.SurveyModel;
import es.academy.solidgear.surveyx.models.SurveysModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @GET("/surveys")
    Call<SurveysModel> getAllSurveys(@Query("token") String token);

    @GET("/surveys/{surveyId}")
    Call<SurveyModel> getSurvey(@Path("surveyId") int surveyId);

    @GET("/questions/{questionId}")
    Call<QuestionModel> getQuestion(@Path("questionId") int questionId);

    @POST("/responses")
    Call<ResponseBody> sendResponseToQuestion(@Body SurveyPostParams surveyPostParams);

    @GET("/users")
    Call<LoginModel> login(@Query("username") String username, @Query("password") String password);

    interface OnResponse<T> {

        void onSuccess(T obj);

        void onFailure();

    }

}
