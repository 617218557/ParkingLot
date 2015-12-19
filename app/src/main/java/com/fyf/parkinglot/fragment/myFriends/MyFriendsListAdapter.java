package com.fyf.parkinglot.fragment.myFriends;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.fyf.parkinglot.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.List;

/**
 * Created by fengyifei on 15/11/28.
 */
public class MyFriendsListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<String> imAccountList;
    private DisplayImageOptions options;
    private ImageSize mImageSize;

    public MyFriendsListAdapter(Context context, List<String> imAccountList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.imAccountList = imAccountList;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        mImageSize = new ImageSize(60, 60);
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
                    R.layout.fragment_my_friends_list_adapter, parent, false);
            holder.tv_imAccount = (TextView) convertView.findViewById
                    (R.id.fragment_my_friends_lv_item_tv_imAccount);
            holder.tv_newMessage = (TextView) convertView.findViewById
                    (R.id.fragment_my_friends_lv_item_tv_newMessage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_imAccount.setText(imAccountList.get(position));

        // 设置未读消息数量
        EMConversation conversation = EMChatManager.getInstance().getConversation(imAccountList.get(position));
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
        public TextView tv_imAccount;
        public TextView tv_newMessage;
    }
}
