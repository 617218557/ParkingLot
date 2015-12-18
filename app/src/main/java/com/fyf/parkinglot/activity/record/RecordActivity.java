package com.fyf.parkinglot.activity.record;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fyf.parkinglot.R;
import com.fyf.parkinglot.common.SQLWord;
import com.fyf.parkinglot.common.URLAddress;
import com.fyf.parkinglot.model.ParkingRecordInfoBean;
import com.fyf.parkinglot.model.UserInfoInCache;
import com.fyf.parkinglot.utils.HttpUtils;
import com.fyf.parkinglot.utils.JsonUtils;
import com.fyf.parkinglot.view.CustomPrgressDailog;
import com.fyf.parkinglot.view.CustomToast;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.twotoasters.jazzylistview.JazzyListView;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {

    private Button btn_back;
    private TextView tv_title;
    private Button btn_next;

    private JazzyListView lv_record;

    private ArrayList<ParkingRecordInfoBean> recordList;
    private RecordListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        findView();
        setListener();
        init();
    }

    private void findView(){
        btn_back = (Button) findViewById(R.id.layout_actionBar_btn_back);
        tv_title = (TextView) findViewById(R.id.layout_actionBar_tv_title);
        btn_next = (Button) findViewById(R.id.layout_actionBar_btn_next);
        lv_record = (JazzyListView) findViewById(R.id.activity_record_lv_record);
    }

    private void setListener(){
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private  void init(){
        tv_title.setText(R.string.actionBar_record);
        btn_next.setVisibility(View.INVISIBLE);
        GetRecordTask getRecordTask = new GetRecordTask();
        getRecordTask.execute();
    }

    class GetRecordTask extends AsyncTask {
        CustomPrgressDailog dailog = new CustomPrgressDailog(RecordActivity.this, R.style.DialogNormal);
        @Override
        protected void onPreExecute() {
            dailog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
                RequestBody body = new FormEncodingBuilder()
                        .add(SQLWord.USER_ID, UserInfoInCache.user_id + "").build();
                return HttpUtils.httpPost(URLAddress.findRecordByUserIdURL, body);
        }

        @Override
        protected void onPostExecute(Object o) {
            handleRecord(o.toString());
            dailog.dismiss();
            super.onPostExecute(o);
        }
    }

    private void handleRecord(String json){
        if (json == null || json.equals("")) {
            CustomToast.showToast(getApplicationContext(), "加载失败", 1000);
            return;
        }
        if (JsonUtils.getResultCode(json) < 1) {
            // 显示失败原因
            CustomToast.showToast(getApplicationContext(), JsonUtils.getResultMsgString(json), 1000);
        } else {
            // 成功时相关处理
            ArrayList<ParkingRecordInfoBean> recordList = JsonUtils.getRecordList(JsonUtils.getResultMsgString(json));
            adapter = new RecordListViewAdapter(RecordActivity.this,recordList);
            lv_record.setAdapter(adapter);
        }
    }
}
