package com.fyf.parkinglot.activity.singleChat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.model.UserInfoInCache;

import java.util.List;

/**
 * Created by fengyifei on 15/12/19.
 */
public class SingleChatListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<EMMessage> msgs;

    public SingleChatListAdapter(Context context, List<EMMessage> msgs) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.msgs = msgs;
    }

    public void updateData(List<EMMessage> msgs) {
        this.msgs = msgs;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return msgs.size();
    }

    @Override
    public Object getItem(int position) {
        return msgs.get(position);
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
                    R.layout.activity_single_chat_list_item, parent, false);
            holder.tv_message = (TextView) convertView.findViewById(R.id.activity_single_chat_list_item_tv_message);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TextMessageBody txtBody = (TextMessageBody) msgs.get(position).getBody();
        if (msgs.get(position).getFrom().equals(UserInfoInCache.user_phoneNum)) {
            holder.tv_message.setText("我: " + txtBody.getMessage());
        } else {
            holder.tv_message.setText(msgs.get(position).getFrom() + ":" + txtBody.getMessage());
        }

        return convertView;
    }

    class ViewHolder {
        public TextView tv_message;
    }
}
