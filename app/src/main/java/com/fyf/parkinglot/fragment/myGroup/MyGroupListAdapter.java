package com.fyf.parkinglot.fragment.myGroup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.fyf.parkinglot.R;

import java.util.List;

/**
 * Created by fengyifei on 15/11/28.
 */
public class MyGroupListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<EMGroup> groupList;

    public MyGroupListAdapter(Context context, List<EMGroup> groupList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.groupList = groupList;
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Object getItem(int position) {
        return groupList.get(position);
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
                    R.layout.fragment_my_group_list_adapter, parent, false);
            holder.tv_groupName = (TextView) convertView.findViewById
                    (R.id.fragment_my_group_lv_item_tv_groupName);
            holder.tv_newMessage = (TextView) convertView.findViewById
                    (R.id.fragment_my_group_lv_item_tv_newMessage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_groupName.setText(groupList.get(position).getGroupName());

        // 设置未读消息数量
        EMConversation conversation = EMChatManager.getInstance().getConversation(groupList.get(position).getGroupId());
        int unReadCount = conversation.getUnreadMsgCount();
        if (unReadCount == 0) {
            holder.tv_newMessage.setVisibility(View.INVISIBLE);
        } else {
            holder.tv_newMessage.setVisibility(View.VISIBLE);
            holder.tv_newMessage.setText(unReadCount + "");
        }
        return convertView;
    }

    class ViewHolder {
        public TextView tv_groupName;
        public TextView tv_newMessage;
    }
}
