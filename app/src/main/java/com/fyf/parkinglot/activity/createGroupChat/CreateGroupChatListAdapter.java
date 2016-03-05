package com.fyf.parkinglot.activity.createGroupChat;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fyf.parkinglot.R;
import com.fyf.parkinglot.view.CustomToast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengyifei on 15/11/28.
 */
public class CreateGroupChatListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<String> imAccountList;
    private DisplayImageOptions options;
    private ImageSize mImageSize;

    private List<Boolean> userCheckedList;// 用户选取群聊成员列表,false为未选中,true为选中

    public CreateGroupChatListAdapter(Context context, List<String> imAccountList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.imAccountList = imAccountList;
        initCheckedList();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        mImageSize = new ImageSize(60, 60);
    }

    //初始化选择列表
    private void initCheckedList() {
        userCheckedList = new ArrayList<>();
        if(imAccountList == null){
            CustomToast.showToast(context,"好友列表初始化错误",1000);
        }else {
            int size = imAccountList.size();
            for (int i = 0; i < size; i++) {
                userCheckedList.add(false);
            }
        }
    }

    // 获取选取用户的列表,未选取时返回null
    public String[] getCheckedAccount() {
        List<String> tmp = new ArrayList<>();
        for (int i = 0; i < userCheckedList.size(); i++) {
            if (userCheckedList.get(i) == true) {
                tmp.add(imAccountList.get(i));
            }
        }
        if (tmp.size() == 0) {
            return null;
        }
        String[] accounts = new String[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            accounts[i] = tmp.get(i);
        }
        return accounts;
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
                    R.layout.activity_create_group_chat_list_adapter, parent, false);
            holder.ll_total = (RelativeLayout) convertView.findViewById
                    (R.id.activity_create_group_chat_lv_item_ll_total);
            holder.tv_imAccount = (TextView) convertView.findViewById
                    (R.id.activity_create_group_chat_lv_item_tv_imAccount);
            holder.tv_checked = (TextView) convertView.findViewById
                    (R.id.activity_create_group_chat_lv_item_tv_checked);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_imAccount.setText(imAccountList.get(position));
        holder.tv_checked.setVisibility(userCheckedList.get(position) == true ? View.VISIBLE : View.INVISIBLE);
        holder.ll_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userCheckedList.get(position) == false) {
                    // 未选中时,变为选中
                    userCheckedList.set(position, true);
                } else {
                    // 选中时,变为未选中
                    userCheckedList.set(position, false);
                }
                holder.tv_checked.setVisibility(userCheckedList.get(position) == true ? View.VISIBLE : View.INVISIBLE);
            }
        });

        return convertView;
    }

    class ViewHolder {
        public RelativeLayout ll_total;
        public TextView tv_imAccount;
        public TextView tv_checked;
    }
}
