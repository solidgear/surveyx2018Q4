package es.academy.solidgear.surveyx.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import es.academy.solidgear.surveyx.R;

public class InformationDialogFragment extends DialogFragment {
    private TextView textViewInformation;
    private int messageId;

    public static InformationDialogFragment newInstance(int messageId) {
        InformationDialogFragment fragment = new InformationDialogFragment();
        fragment.messageId = messageId;
        return fragment;
    }

    public InformationDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }

        getDialog().getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                                          RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // titleTextView by default, but your custom layout might not need it. So here you can
        // remove the dialog titleTextView, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setCancelable(false);
        View view = inflater.inflate(R.layout.fragment_information_dialog, container, false);
        textViewInformation = (TextView) view.findViewById(R.id.textViewMessage);
        textViewInformation.setText(messageId);
        return view;
    }

    public void setMessage(String message) {
        textViewInformation.setText(message);
    }
}
