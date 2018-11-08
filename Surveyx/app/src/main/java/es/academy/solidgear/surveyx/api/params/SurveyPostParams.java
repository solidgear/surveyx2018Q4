package es.academy.solidgear.surveyx.api.params;

import java.util.List;

public class SurveyPostParams {

    private String token;
    private int survey;
    private int question_id;
    private List<Integer> choice;

    public String getToken() {
        return token;
    }

    public int getSurvey() {
        return survey;
    }

    public int getQuestion() {
        return question_id;
    }

    public List<Integer> getChoice() {
        return choice;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSurvey(int survey) {
        this.survey = survey;
    }

    public void setQuestion(int question) {
        this.question_id = question;
    }

    public void setChoice(List<Integer> choice) {
        this.choice = choice;
    }
}
