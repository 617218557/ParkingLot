package com.fyf.parkinglot.activity.chooseCar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.fyf.parkinglot.R;
import com.fyf.parkinglot.activity.chooseTime.ChooseTimeActivity;
import com.fyf.parkinglot.common.SQLWord;
import com.fyf.parkinglot.common.URLAddress;
import com.fyf.parkinglot.model.UserInfoInCache;
import com.fyf.parkinglot.utils.HttpUtils;
import com.fyf.parkinglot.utils.JsonUtils;
import com.fyf.parkinglot.view.CustomPrgressDailog;
import com.fyf.parkinglot.view.CustomToast;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.WaveEffect;

/**
 * Created by fengyifei on 15/11/28.
 */
public class ChooseCarActivity extends AppCompatActivity {

    private Button btn_back;
    private TextView tv_title;
    private Button btn_next;

    private JazzyListView lv_listView;

    private int park_id;
    private ChooseCarListViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_car);
        findView();
        setListener();
        init();

    }

    private void findView() {
        btn_back = (Button) findViewById(R.id.layout_actionBar_btn_back);
        tv_title = (TextView) findViewById(R.id.layout_actionBar_tv_title);
        btn_next = (Button) findViewById(R.id.layout_actionBar_btn_next);

        lv_listView = (JazzyListView) findViewById(R.id.activity_chooseCar_lv_listView);
    }

    private void setListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        tv_title.setText(R.string.actionBar_order);
        btn_next.setVisibility(View.INVISIBLE);
        park_id = getIntent().getIntExtra(SQLWord.PARK_ID, -1);
        if (UserInfoInCache.myCarList == null || UserInfoInCache.myCarList.size() == 0) {
            GetCarAsyncTask getCarAsyncTask = new GetCarAsyncTask();
            getCarAsyncTask.execute();
        } else {
            initList();
        }
    }

    // 查询用户车辆
    class GetCarAsyncTask extends AsyncTask {
        CustomPrgressDailog dailog = new CustomPrgressDailog(ChooseCarActivity.this, R.style.DialogNormal);

        @Override
        protected void onPreExecute() {
            dailog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            RequestBody body = new FormEncodingBuilder()
                    .add(SQLWord.USER_ID, UserInfoInCache.user_id + "").build();
            return HttpUtils.httpPost(URLAddress.findUserCarURL, body);
        }

        @Override
        protected void onPostExecute(Object o) {
            handleCarResult(o.toString());
            dailog.dismiss();
            super.onPostExecute(o);
        }
    }


    // 处理用户车辆信息
    private void handleCarResult(String json) {
        if (json == null || json.equals("")) {
            CustomToast.showToast(getApplicationContext(), "加载失败", 1000);
            return;
        }
        if (JsonUtils.getResultCode(json) < 1) {
            // 显示失败原因
            CustomToast.showToast(getApplicationContext(), JsonUtils.getResultMsgString(json), 1000);
        } else {
            // 成功时相关处理
            UserInfoInCache.myCarList = JsonUtils.getCarlist(JsonUtils.getResultMsgString(json));
            initList();
        }
    }

    private void initList() {
        lv_listView.setTransitionEffect(new WaveEffect());
        adapter = new ChooseCarListViewAdapter(ChooseCarActivity.this);
        lv_listView.setAdapter(adapter);
        lv_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ChooseCarActivity.this, ChooseTimeActivity.class);
                intent.putExtra(SQLWord.PARK_ID, park_id);
                intent.putExtra(SQLWord.CAR_ID, UserInfoInCache.myCarList.get(position).getCar_id());
                startActivity(intent);
            }
        });
    }
}
