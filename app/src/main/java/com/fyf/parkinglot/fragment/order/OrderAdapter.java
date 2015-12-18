package com.fyf.parkinglot.fragment.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fyf.parkinglot.R;
import com.fyf.parkinglot.model.ParkinglotInfoBean;

import java.util.ArrayList;

/**
 * Created by fengyifei on 15/11/28.
 */
public class OrderAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<ParkinglotInfoBean> parkList;

    public OrderAdapter(Context context, ArrayList<ParkinglotInfoBean> parkList) {
        this.context = context;
        this.parkList = parkList;
        this.mInflater = LayoutInflater.from(context);
    }

    public void updateList(ArrayList<ParkinglotInfoBean> parkList){
        this.parkList = parkList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return parkList.size();
    }

    @Override
    public Object getItem(int position) {
        return parkList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.fragment_order_list_item, parent, false);
            holder.tv_time = (TextView) convertView.findViewById(R.id.fragment_order_list_item_tv_time);
            holder.tv_parkId = (TextView) convertView.findViewById(R.id.fragment_order_list_item_tv_parkId);
            holder.tv_fee = (TextView) convertView.findViewById(R.id.fragment_order_list_item_tv_fee);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_time.setText(parkList.get(position).getPark_startTime());
        holder.tv_parkId.setText("车位:"+parkList.get(position).getPark_id()+"号");
        holder.tv_fee.setText(parkList.get(position).getPark_fee() + "元/小时");

        return convertView;
    }

    class ViewHolder {
        public TextView tv_time;
        public TextView tv_parkId;
        public TextView tv_fee;
    }
}
