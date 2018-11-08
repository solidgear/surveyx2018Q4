package es.academy.solidgear.surveyx.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import es.academy.solidgear.surveyx.R;
import es.academy.solidgear.surveyx.managers.Utils;
import es.academy.solidgear.surveyx.models.OptionModel;
import es.academy.solidgear.surveyx.models.QuestionModel;
import es.academy.solidgear.surveyx.ui.views.AnswerRadioButton;

public class QuestionFragment extends Fragment {
    private static final int UNCHECKED_VALUE = -1;
    public static final String BUNDLE_QUESTION_KEY = "question";

    private OnAnswerChanged onAnswerChanged;

    private QuestionModel question;
    private RadioGroup answersOutlet;

    public interface OnAnswerChanged {
        void onAnswerChange(ArrayList<Integer> selectedAnswers);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int paddingInPixels = (int) Utils.dimenToPixels(getActivity(), TypedValue.COMPLEX_UNIT_DIP, 10);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                paddingInPixels);

        int count = answersOutlet.getChildCount();
        ArrayList<AnswerRadioButton> listOfRadioButtons = new ArrayList<>();
        for (int i=0; i<count; i++) {
            View o = answersOutlet.getChildAt(i);
            if (o instanceof AnswerRadioButton) {
                o.setPadding(0, paddingInPixels, 0, paddingInPixels);
            }
        }


        answersOutlet.setPadding(0, paddingInPixels, 0, paddingInPixels);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            onAnswerChanged = (OnAnswerChanged) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnAnswerChanged ");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get data sent by the activity.
        Bundle bundle = getArguments();
        question = (QuestionModel) bundle.getSerializable(BUNDLE_QUESTION_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_survey, container, false);

        TextView questionTextView = (TextView) root.findViewById(R.id.question_text);
        questionTextView.setText(question.getText());

        answersOutlet = (RadioGroup) root.findViewById(R.id.answers_outlet);

        for (OptionModel option : question.getChoices()) {

            // Create radio button with answer
            AnswerRadioButton radioButton = new AnswerRadioButton(getActivity(), option.getText());
            radioButton.setTag(option.getId());
            answersOutlet.addView(radioButton);
        }

        RadioGroup.OnCheckedChangeListener onAnswerChecked = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean enabled = checkedId != UNCHECKED_VALUE;
                ArrayList<Integer> responseSelected = new ArrayList<>();
                if (enabled) {
                    responseSelected.clear();
                    View radioButton = group.findViewById(group.getCheckedRadioButtonId());
                    responseSelected.add((int) radioButton.getTag());
                }
                QuestionFragment.this.onAnswerChanged.onAnswerChange(responseSelected);
            }
        };

        answersOutlet.setOnCheckedChangeListener(onAnswerChecked);

        return root;
    }

}
