package es.academy.solidgear.surveyx.models;

import java.util.List;

public class SurveysModel {
    private int count;
    private List<SurveyModel> results;

    public List<SurveyModel> getSurveys() {
        return results;
    }

    public void setSurveys(List<SurveyModel> surveys) {
        this.results = surveys;
    }

    public int getTotal() {
        return count;
    }

    public void setTotal(int total) {
        this.count = total;
    }
}
