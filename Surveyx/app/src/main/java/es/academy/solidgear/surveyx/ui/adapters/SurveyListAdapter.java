package es.academy.solidgear.surveyx.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import es.academy.solidgear.surveyx.R;
import es.academy.solidgear.surveyx.models.SurveyModel;

public class SurveyListAdapter extends RecyclerView.Adapter<SurveyViewHolder> {
    private final List<SurveyModel> surveys;

    private OnSelectedSurvey onSelectedSurvey;

    private Context context;

    public SurveyListAdapter(Context ctx, List<SurveyModel> surveys, OnSelectedSurvey onSelectedSurvey) {
        this.context = ctx;
        this.surveys = surveys;
        this.onSelectedSurvey = onSelectedSurvey;
    }

    @Override
    public int getItemCount() {
        return surveys.size();
    }

    @Override
    public void onBindViewHolder(final SurveyViewHolder surveyViewHolder, int i) {
        final SurveyModel survey = surveys.get(i);
        surveyViewHolder.applySurvey(context, survey, onSelectedSurvey);
    }

    @Override
    public SurveyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_survey, viewGroup, false);

        return new SurveyViewHolder(itemView);
    }
}
