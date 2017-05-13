package cn.ilell.manage.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.ilell.manage.R;

/**
 * Created by WSL
 */
@SuppressLint("HandlerLeak")
public class BandBufferDialog {
    private Dialog mDialog;
    private TextView titleView;
    private ProgressBar progressBar;

    public BandBufferDialog(Activity activity, String title) {
        mDialog = new Dialog(activity, R.style.dialog_translucent);
        mDialog.setContentView(R.layout.activity_dialogr);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        titleView = (TextView) mDialog.findViewById(R.id.buffer_text_title);
        progressBar = (ProgressBar) mDialog.findViewById(R.id.pb_dialog);
        titleView.setText(title);
    }

    public BandBufferDialog(Context context, int id) {
        mDialog = new Dialog(context, R.style.dialog_translucent);
        mDialog.setContentView(id);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        titleView = (TextView) mDialog.findViewById(R.id.buffer_text_title);
        progressBar = (ProgressBar) mDialog.findViewById(R.id.pb_dialog);
    }

    public boolean isShow() {
        return mDialog.isShowing();
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setVisibility() {
        progressBar.setVisibility(View.GONE);
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public void show() {
        try {
            mDialog.show();
        } catch (Exception e) {
        }
    }
}
