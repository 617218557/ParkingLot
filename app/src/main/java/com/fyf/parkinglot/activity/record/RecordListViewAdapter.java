package com.fyf.parkinglot.activity.record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.model.CarInfoBean;
import com.fyf.parkinglot.model.ParkingRecordInfoBean;
import com.fyf.parkinglot.model.UserInfoInCache;
import com.fyf.parkinglot.utils.Utils;

import java.util.ArrayList;

/**
 * Created by fengyifei on 15/11/28.
 */
public class RecordListViewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<ParkingRecordInfoBean> recordList;

    public RecordListViewAdapter(Context context, ArrayList<ParkingRecordInfoBean> recordList) {
        this.context = context;
        this.recordList = recordList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.activity_record_list_item, parent, false);
            holder.iv_car = (SimpleDraweeView) convertView.findViewById(R.id.activity_record_list_item_iv_car);
            holder.tv_time = (TextView) convertView.findViewById(R.id.activity_record_list_item_tv_time);
            holder.tv_fee = (TextView) convertView.findViewById(R.id.activity_record_list_item_tv_fee);
            holder.tv_type = (TextView) convertView.findViewById(R.id.activity_record_list_item_tv_type);
            holder.tv_licenseNum = (TextView) convertView.findViewById(R.id.activity_record_list_item_tv_licenseNum);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            holder.tv_time.setText(recordList.get(position).getRecord_startTime().split(" ")[1]
                    + "-" + recordList.get(position).getRecord_endTime().split(" ")[1]);
        }catch(Exception e){
            holder.tv_time.setText(recordList.get(position).getRecord_startTime() + "-"
                    + recordList.get(position).getRecord_endTime());
            e.printStackTrace();
        }
        holder.tv_fee.setText(recordList.get(position).getRecord_fee() + "元");
        for (CarInfoBean carInfoBean : UserInfoInCache.myCarList) {
            if (carInfoBean.getCar_id() == recordList.get(position).getCar_id())
                holder.tv_type.setText(carInfoBean.getCar_type());
            holder.tv_licenseNum.setText(carInfoBean.getCar_licenseNum());
            Utils.loadImageUtils(holder.iv_car, carInfoBean.getCar_img(), context);
        }
        return convertView;
    }

    class ViewHolder {
        public SimpleDraweeView iv_car;
        public TextView tv_time;
        public TextView tv_fee;
        public TextView tv_type;
        public TextView tv_licenseNum;
    }
}
