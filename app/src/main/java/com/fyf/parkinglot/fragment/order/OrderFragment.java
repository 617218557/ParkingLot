package com.fyf.parkinglot.fragment.order;

/**
 * Created by fengyifei on 15/11/24.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.fyf.parkinglot.R;
import com.fyf.parkinglot.activity.chooseSpace.ChooseSpaceActivity;
import com.fyf.parkinglot.common.SQLWord;
import com.fyf.parkinglot.common.URLAddress;
import com.fyf.parkinglot.model.ParkinglotInfoBean;
import com.fyf.parkinglot.model.UserInfoInCache;
import com.fyf.parkinglot.utils.HttpUtils;
import com.fyf.parkinglot.utils.JsonUtils;
import com.fyf.parkinglot.utils.NotifyDataChangeUtils;
import com.fyf.parkinglot.utils.Utils;
import com.fyf.parkinglot.view.CustomPrgressDailog;
import com.fyf.parkinglot.view.CustomToast;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.WaveEffect;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class OrderFragment extends Fragment {

    private View rootView;
    private JazzyListView lv_orderList;
    private FloatingActionButton fa_fab; // 悬浮按钮

    private OrderAdapter adapter;
    private ArrayList<ParkinglotInfoBean> parkList;

    public OrderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_order, container, false);
            findView(rootView);
            setListener();
            init();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }


    private void findView(View v) {
        fa_fab = (FloatingActionButton) v.findViewById(R.id.fragment_order_fa_fab);
        lv_orderList = (JazzyListView) v.findViewById(R.id.fragment_order_lv_orderList);
    }

    private void setListener() {
        fa_fab.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          Snackbar.make(view, "是否预约车位?", Snackbar.LENGTH_SHORT)
                                                  .setAction("是", new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View v) {
                                                          startActivity(new Intent(getActivity(), ChooseSpaceActivity.class));
                                                      }
                                                  }).setActionTextColor(getResources().getColor
                                                  (R.color.white_normal)).show();
                                      }
                                  }
        );
    }

    @Override
    public void onResume() {
        if (NotifyDataChangeUtils.REFRESH_TODAY_ORDER == 1) {
            init();
            NotifyDataChangeUtils.REFRESH_TODAY_ORDER = 0;
        }
        super.onResume();
    }

    private void init() {
        GetOrderTask getOrderTask = new GetOrderTask();
        getOrderTask.execute();
    }

    class GetOrderTask extends AsyncTask {
        CustomPrgressDailog dailog = new CustomPrgressDailog(getActivity(), R.style.DialogNormal);

        @Override
        protected void onPreExecute() {
            dailog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            RequestBody body = new FormEncodingBuilder()
                    .add(SQLWord.USER_ID, UserInfoInCache.user_id + "").build();
            return HttpUtils.httpPost(URLAddress.findTodayOrderURl, body);
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
            CustomToast.showToast(getActivity().getApplicationContext(), "加载失败", 1000);
            return;
        }
        if (JsonUtils.getResultCode(json) < 1) {
            // 显示失败原因
            CustomToast.showToast(getActivity().getApplicationContext(), JsonUtils.getResultMsgString(json), 1000);
        } else {
            // 成功时相关处理
            parkList = JsonUtils.getParkinglot(JsonUtils.getResultMsgString(json));
            setList();
        }
    }

    private void setList() {
        lv_orderList.setTransitionEffect(new WaveEffect());
        adapter = new OrderAdapter(getActivity(), parkList);
        lv_orderList.setAdapter(adapter);
        lv_orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // flag为true则取消预约,为false则结束停车
                boolean flag = Utils.comparePointTime(parkList.get(position).getPark_startTime());
                if (flag) {
                    new AlertDialog.Builder(getActivity()).setMessage("是否取消预约")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    CancleOrderTask cancleOrder = new CancleOrderTask(position);
                                    cancleOrder.execute();
                                }
                            }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
                } else {
                    new AlertDialog.Builder(getActivity()).setMessage("是否结束停车")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EndParkingTask endParkingTask = new EndParkingTask(position);
                                    endParkingTask.execute();
                                }
                            }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
                }
            }
        });
    }

    // 取消预约
    class CancleOrderTask extends AsyncTask {
        private CustomPrgressDailog dailog = new CustomPrgressDailog(getActivity(), R.style.DialogNormal);
        private int index;

        public CancleOrderTask(int index) {
            this.index = index;
        }

        @Override
        protected void onPreExecute() {
            dailog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            RequestBody body = new FormEncodingBuilder()
                    .add(SQLWord.PARK_ID, parkList.get(index).getPark_id()+ "").build();
            return HttpUtils.httpPost(URLAddress.cancleOrderURL, body);
        }

        @Override
        protected void onPostExecute(Object o) {
            String json = o.toString();
            if (json == null || json.equals("")) {
                CustomToast.showToast(getActivity().getApplicationContext(), "取消预约失败", 1000);
                return;
            }
            if (JsonUtils.getResultCode(json) < 1) {
                // 显示失败原因
                CustomToast.showToast(getActivity().getApplicationContext(), JsonUtils.getResultMsgString(json), 1000);
            } else {
                // 成功时相关处理
                parkList.remove(index);
                adapter.updateList(parkList);
            }
            dailog.dismiss();
            super.onPostExecute(o);
        }
    }

    // 结束停车
    class EndParkingTask extends AsyncTask {
        private CustomPrgressDailog dailog = new CustomPrgressDailog(getActivity(), R.style.DialogNormal);
        private int index;

        public EndParkingTask(int index) {
            this.index = index;
        }

        @Override
        protected void onPreExecute() {
            dailog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            RequestBody body = new FormEncodingBuilder()
                    .add(SQLWord.PARK_ID, parkList.get(index).getPark_id() + "").build();
            return HttpUtils.httpPost(URLAddress.endParkingURL, body);
        }

        @Override
        protected void onPostExecute(Object o) {
            String json = o.toString();
            if (json == null || json.equals("")) {
                CustomToast.showToast(getActivity().getApplicationContext(), "结束停车失败", 1000);
                return;
            }
            if (JsonUtils.getResultCode(json) < 1) {
                // 显示失败原因
                CustomToast.showToast(getActivity().getApplicationContext(), JsonUtils.getResultMsgString(json), 1000);
            } else {
                // 成功时相关处理
                parkList.remove(index);
                adapter.updateList(parkList);
            }
            dailog.dismiss();
            super.onPostExecute(o);
        }
    }
}