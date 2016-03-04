package com.fyf.parkinglot.activity.chooseTime;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fyf.parkinglot.R;
import com.fyf.parkinglot.activity.main.MainActivity;
import com.fyf.parkinglot.common.GlobalDefine;
import com.fyf.parkinglot.common.SQLWord;
import com.fyf.parkinglot.common.URLAddress;
import com.fyf.parkinglot.utils.HttpUtils;
import com.fyf.parkinglot.utils.JsonUtils;
import com.fyf.parkinglot.utils.NotifyDataChangeUtils;
import com.fyf.parkinglot.utils.Utils;
import com.fyf.parkinglot.view.CustomPrgressDailog;
import com.fyf.parkinglot.view.CustomToast;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

public class ChooseTimeActivity extends AppCompatActivity {

    private Button btn_back;
    private TextView tv_title;
    private Button btn_next;
    private TimePicker tp_picker;
    private DatePicker dp_picker;

    private int park_id;
    private int car_id;
    private String park_startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);
        findView();
        setListener();
        init();
    }

    private void findView() {
        btn_back = (Button) findViewById(R.id.layout_actionBar_btn_back);
        tv_title = (TextView) findViewById(R.id.layout_actionBar_tv_title);
        btn_next = (Button) findViewById(R.id.layout_actionBar_btn_next);
        dp_picker = (DatePicker) findViewById(R.id.activity_chooseTime_dp_picker);
        tp_picker = (TimePicker) findViewById(R.id.activity_chooseTime_tp_picker);
    }

    private void setListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPickerTime()) {
                    OrderTask orderTask = new OrderTask();
                    orderTask.execute();
                }
            }
        });
    }

    private void init() {
        tv_title.setText(R.string.actionBar_order);
        btn_next.setText(R.string.actionBar_finish);
        tp_picker.setIs24HourView(true);
        Intent intent = getIntent();
        park_id = intent.getIntExtra(SQLWord.PARK_ID, -1);
        car_id = intent.getIntExtra(SQLWord.CAR_ID, -1);
    }

    private boolean checkPickerTime() {
        String year = dp_picker.getYear() + "";
        String month = (dp_picker.getMonth() + 1) + "";
        String day = dp_picker.getDayOfMonth() + "";
        String hour = tp_picker.getCurrentHour() + "";
        String minute = tp_picker.getCurrentMinute() + "";
        // 调整时间字符串格式
        if (dp_picker.getMonth() < 9) {
            month = "0" + (dp_picker.getMonth() + 1);
        }
        if (dp_picker.getDayOfMonth() < 10) {
            day = "0" + dp_picker.getDayOfMonth();
        }
        if (tp_picker.getCurrentHour() < 10) {
            hour = "0" + tp_picker.getCurrentHour();
        }
        if (tp_picker.getCurrentMinute() < 10) {
            minute = "0" + tp_picker.getCurrentMinute();
        }
        // 判断是否在当前时间之前
        if (Utils.comparePointTime(year + "-" + month + "-" + day
                + " " + hour + ":" + minute + ":00")) {
            Log.e("sss", year + "-" + month + "-" + day
                    + " " + hour + ":" + minute + ":00");
            if (Utils.comparePointTimeInPointHour(year + "-" + month + "-" + day
                    + " " + hour + ":" + minute + ":00", GlobalDefine.orderForwardHour)) {
                // 选择时间在当前时间2小时之后
                CustomToast.showToast(getApplicationContext(), "只能预约2小时以内", 1000);
                return false;
            } else {
                // ok
                park_startTime = Utils.getCurrentTime(year + "-" + month + "-" + day
                        + " " + hour + ":" + minute + ":00");
                return true;
            }
        } else {
            CustomToast.showToast(getApplicationContext(), "选择的时间不合适", 1000);
            return false;
        }
    }

    class OrderTask extends AsyncTask {
        CustomPrgressDailog dailog = new CustomPrgressDailog(ChooseTimeActivity.this, R.style.DialogNormal);

        @Override
        protected void onPreExecute() {
            dailog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            RequestBody body = new FormEncodingBuilder()
                    .add(SQLWord.PARK_ID,
                            park_id + "").add(SQLWord.CAR_ID, car_id + "")
                    .add(SQLWord.PARK_STARTTIME, park_startTime).build();
            return HttpUtils.httpPost(URLAddress.orderSPaceURl, body);
        }

        @Override
        protected void onPostExecute(Object o) {
            handleOrderResult(o.toString());
            dailog.dismiss();
            super.onPostExecute(o);
        }
    }

    private void handleOrderResult(String json) {
        if (json == null || json.equals("")) {
            CustomToast.showToast(getApplicationContext(), "加载失败", 1000);
            return;
        }
        if (JsonUtils.getResultCode(json) < 1) {
            // 显示失败原因
            CustomToast.showToast(getApplicationContext(), JsonUtils.getResultMsgString(json), 1000);
        } else {
            // 成功时相关处理
            NotifyDataChangeUtils.REFRESH_TODAY_ORDER = 1;
            new AlertDialog.Builder(ChooseTimeActivity.this).setMessage("预约成功")
                    .setPositiveButton("返回主界面", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(ChooseTimeActivity.this, MainActivity.class));
                        }
                    }).create().show();
        }
    }


}
