package com.fyf.parkinglot.activity.groupInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fyf.parkinglot.R;

import java.util.List;

/**
 * Created by fengyifei on 15/11/28.
 */
public class GroupMembersListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<String> imAccountList;

    public GroupMembersListAdapter(Context context, List<String> imAccountList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.imAccountList = imAccountList;
    }

    @Override
    public int getCount() {
        return imAccountList.size();
    }

    @Override
    public Object getItem(int position) {
        return imAccountList.get(position);
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
                    R.layout.activity_group_info_members_list_adapter, parent, false);
            holder.tv_imAccount = (TextView) convertView.findViewById
                    (R.id.activity_grouo_info_members_lv_item_tv_imAccount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_imAccount.setText(imAccountList.get(position));

        return convertView;
    }

    class ViewHolder {
        public TextView tv_imAccount;
    }
}
