package com.fyf.parkinglot.activity.chooseSpace;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fyf.parkinglot.R;
import com.fyf.parkinglot.activity.chooseCar.ChooseCarActivity;
import com.fyf.parkinglot.common.SQLWord;
import com.fyf.parkinglot.common.URLAddress;
import com.fyf.parkinglot.model.ParkinglotInfoBean;
import com.fyf.parkinglot.utils.HttpUtils;
import com.fyf.parkinglot.utils.JsonUtils;
import com.fyf.parkinglot.view.CustomPrgressDailog;
import com.fyf.parkinglot.view.CustomToast;
import com.fyf.parkinglot.view.MyGridView;

import java.util.ArrayList;

public class ChooseSpaceActivity extends AppCompatActivity {

    private Button btn_back;
    private TextView tv_title;
    private Button btn_next;
    private MyGridView gv_gridView;
    private ImageView iv_used, iv_notUsed, iv_mine;


    private ArrayList<ParkinglotInfoBean> spaceList;
    private DrawParkinglot drawParkinglot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_space);
        findView();
        setLisener();
        init();
        AllSpaceInfoTask allSpaceInfoTask = new AllSpaceInfoTask();
        allSpaceInfoTask.execute();
    }

    private void findView() {
        btn_back = (Button) findViewById(R.id.layout_actionBar_btn_back);
        tv_title = (TextView) findViewById(R.id.layout_actionBar_tv_title);
        btn_next = (Button) findViewById(R.id.layout_actionBar_btn_next);
        gv_gridView = (MyGridView) findViewById(R.id.activity_chooseSpace_gv_space);
        iv_used = (ImageView) findViewById(R.id.activity_chooseSpace_iv_used);
        iv_notUsed = (ImageView) findViewById(R.id.activity_chooseSpace_iv_notUsed);
        iv_mine = (ImageView) findViewById(R.id.activity_chooseSpace_iv_mine);
    }

    private void init() {
        tv_title.setText(R.string.actionBar_order);
        btn_next.setText(getString(R.string.actionBar_next));
    }

    private void setLisener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawParkinglot.getParkId() != -1) {
                    Intent intent = new Intent(ChooseSpaceActivity.this, ChooseCarActivity.class);
                    intent.putExtra(SQLWord.PARK_ID,drawParkinglot.getParkId());
                    startActivity(intent);
                } else {
                    CustomToast.showToast(getApplicationContext(),
                            getString(R.string.activity_chooseSpace_notChoose), 1000);
                }
            }
        });
    }

    // 查询所有车位信息
    class AllSpaceInfoTask extends AsyncTask {
        CustomPrgressDailog dailog = new CustomPrgressDailog(ChooseSpaceActivity.this, R.style.DialogNormal);

        @Override
        protected void onPreExecute() {
            dailog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            return HttpUtils.httpGet(URLAddress.allSpaceInfoURl);
        }

        @Override
        protected void onPostExecute(Object o) {
            handleSpaceResult(o.toString());
            dailog.dismiss();
            super.onPostExecute(o);
        }
    }

    // 处理收到停车场状态
    private void handleSpaceResult(String json) {
        if (json == null || json.equals("")) {
            CustomToast.showToast(getApplicationContext(), "加载失败", 1000);
            return;
        }
        if (JsonUtils.getResultCode(json) < 1) {
            // 显示失败原因
            CustomToast.showToast(getApplicationContext(), JsonUtils.getResultMsgString(json), 1000);
        } else {
            // 成功时相关处理
            spaceList = JsonUtils.getParkinglot(JsonUtils.getResultMsgString(json));
            drawParkinglot = new DrawParkinglot(ChooseSpaceActivity.this, gv_gridView, spaceList);
            drawParkinglot.draw();
            drawParkinglot.setIcon(iv_used, iv_notUsed, iv_mine);
        }
    }
}
