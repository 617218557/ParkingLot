package com.fyf.parkinglot.activity.chooseCar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.model.UserInfoInCache;
import com.fyf.parkinglot.utils.Utils;

/**
 * Created by fengyifei on 15/11/28.
 */
public class ChooseCarListViewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;

    public ChooseCarListViewAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return UserInfoInCache.myCarList.size();
    }

    @Override
    public Object getItem(int position) {
        return UserInfoInCache.myCarList.get(position);
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
                    R.layout.activity_choose_car_list_item, parent, false);
            holder.iv_car = (SimpleDraweeView) convertView.findViewById(R.id.activity_chooseCar_list_item_iv_car);
            holder.tv_type = (TextView) convertView.findViewById(R.id.activity_chooseCar_list_item_tv_type);
            holder.tv_licenseNum = (TextView) convertView.findViewById(R.id.activity_chooseCar_list_item_tv_licenseNum);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_type.setText(UserInfoInCache.myCarList.get(position).getCar_type());
        holder.tv_licenseNum.setText(UserInfoInCache.myCarList.get(position).getCar_licenseNum());
        Utils.loadImageUtils(holder.iv_car, UserInfoInCache.myCarList.get(position).getCar_img(), context);
        return convertView;
    }

    class ViewHolder {
        public SimpleDraweeView iv_car;
        public TextView tv_type;
        public TextView tv_licenseNum;
    }
}
