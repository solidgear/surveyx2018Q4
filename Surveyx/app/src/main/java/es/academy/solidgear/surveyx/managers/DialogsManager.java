package es.academy.solidgear.surveyx.managers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import es.academy.solidgear.surveyx.ui.activities.BaseActivity;
import es.academy.solidgear.surveyx.ui.fragments.InformationDialogFragment;

public class DialogsManager {

    private static DialogsManager instance = null;

    public static DialogsManager getInstance() {
        if (instance == null) {
            instance = new DialogsManager();
        }
        return instance;
    }

    public void showYesNoDialog(Context ctx,
                    final int title,
                    final int message,
                    final int acceptMessage,
                    final int cancelMessage,
                    DialogInterface.OnClickListener onAccept,
                    DialogInterface.OnClickListener onCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(acceptMessage, onAccept);
        builder.setNegativeButton(cancelMessage, onCancel);
        builder.create().show();
    }

    public InformationDialogFragment showInformationDialog(BaseActivity activity, int message) {
        InformationDialogFragment dialog = InformationDialogFragment.newInstance(message);
        android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
        dialog.show(fragmentManager, "dialog");
        return dialog;
    }
}
