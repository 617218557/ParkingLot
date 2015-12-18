package com.fyf.parkinglot.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.fyf.parkinglot.R;

/**
 * Created by fengyifei on 15/11/24.
 */
public class CustomPrgressDailog extends Dialog {

    private Context context;

    private CircularProgressButton btn_circularProgressButton;
    private TextView tv_textView;

    public CustomPrgressDailog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_customdialog);
        findView();
        startProgress();
    }

    private void findView() {
        btn_circularProgressButton = (CircularProgressButton) findViewById(R.id.dialog_custom_btn_loading);
        tv_textView = (TextView) findViewById(R.id.dialog_custom_tv_loading);
    }

    private void startProgress() {
        btn_circularProgressButton.setIndeterminateProgressMode(true);
        btn_circularProgressButton.setProgress(50);
    }


}
