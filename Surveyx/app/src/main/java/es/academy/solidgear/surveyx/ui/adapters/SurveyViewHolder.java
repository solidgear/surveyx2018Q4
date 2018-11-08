package es.academy.solidgear.surveyx.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import es.academy.solidgear.surveyx.R;
import es.academy.solidgear.surveyx.models.SurveyModel;

public class SurveyViewHolder extends RecyclerView.ViewHolder {
    protected TextView titleTextView;
    protected TextView description;
    protected RelativeLayout layout;
    protected ImageView map;
    protected ImageView icon;

    public SurveyViewHolder(View itemView) {
        super(itemView);

        titleTextView = (TextView) itemView.findViewById(R.id.textTitle);
        description = (TextView) itemView.findViewById(R.id.textDescription);
        layout = (RelativeLayout) itemView.findViewById(R.id.layoutSurvey);
        map = (ImageView) itemView.findViewById(R.id.imageViewLocation);
        icon = (ImageView) itemView.findViewById(R.id.imageViewIcon);

        Context ctx = itemView.getContext();

        Typeface titleTypeFace = Typeface.createFromAsset(ctx.getAssets(), "fonts/MuseoSans-900Italic.otf");
        titleTextView.setTypeface(titleTypeFace);

        Typeface descriptionTypeFace = Typeface.createFromAsset(ctx.getAssets(), "fonts/MuseoSans-100.otf");
        description.setTypeface(descriptionTypeFace);
    }

    public void applySurvey(Context ctx, final SurveyModel survey, final OnSelectedSurvey onSelectedSurvey) {
        layout.setContentDescription("surveyCard" + survey.getTitle());
        titleTextView.setText(survey.getTitle());


        description.setText(survey.getDescription());


        if (!survey.hasCoordinates()) {
            map.setVisibility(View.INVISIBLE);
        }

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectedSurvey.onSelectSurvey(survey);
            }
        });

        if (survey.getImage() != null) {
            okhttp3.OkHttpClient okHttp3Client = new okhttp3.OkHttpClient();
            OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(okHttp3Client);
            Picasso picasso = new Picasso.Builder(ctx)
                    .downloader(okHttp3Downloader)
                    .build();
            picasso
                    .load(survey.getImage())
                    .into(icon);
        }
    }
}
