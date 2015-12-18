package com.fyf.parkinglot.activity.myCar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.fyf.parkinglot.R;
import com.fyf.parkinglot.activity.addCar.AddCarActivity;
import com.fyf.parkinglot.activity.updateCar.UpdateCarActivity;
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

/**
 * Created by fengyifei on 15/12/1.
 */
public class MyCarActivity extends AppCompatActivity {

    private Button btn_back;
    private TextView tv_title;
    private Button btn_next;
    private JazzyListView lv_car;
    private CarListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car);
        findView();
        setListener();
        init();
    }

    private void findView() {
        btn_back = (Button) findViewById(R.id.layout_actionBar_btn_back);
        tv_title = (TextView) findViewById(R.id.layout_actionBar_tv_title);
        btn_next = (Button) findViewById(R.id.layout_actionBar_btn_next);
        lv_car = (JazzyListView) findViewById(R.id.activity_my_car_lv_car);
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
                startActivity(new Intent(MyCarActivity.this, AddCarActivity.class));
            }
        });
    }

    private void init() {
        tv_title.setText(R.string.actionBar_myCar);
        btn_next.setText(R.string.actionBar_add);
        if (UserInfoInCache.myCarList == null || UserInfoInCache.myCarList.size() == 0) {
            GetCarAsyncTask getCarAsyncTask = new GetCarAsyncTask();
            getCarAsyncTask.execute();
        } else {
            initList();
        }
    }

    // 查询用户车辆
    class GetCarAsyncTask extends AsyncTask {
        CustomPrgressDailog dailog = new CustomPrgressDailog(MyCarActivity.this, R.style.DialogNormal);

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
        adapter = new CarListViewAdapter(MyCarActivity.this);
        lv_car.setAdapter(adapter);
        lv_car.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyCarActivity.this, UpdateCarActivity.class);
                intent.putExtra("index", position);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        super.onResume();
    }
}
