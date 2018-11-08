package es.academy.solidgear.surveyx.ui.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;

public class CustomButton extends AppCompatButton {
    private static final String TAG = "CustomButton";

    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context);
    }

    private void setCustomFont(Context ctx) {
        try {
            Typeface tf = Typeface.createFromAsset(ctx.getAssets(), "fonts/KGSecondChancesSketch.ttf");
            setTypeface(tf);
        } catch (Exception e) {
            Log.e(TAG, "Problem setting custom font", e);
        }
    }
}
