package com.fyf.parkinglot.activity.singleChat;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.PathUtil;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.model.UserInfoInCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by fengyifei on 15/12/19.
 */
public class SingleChatListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<EMMessage> msgs;
    private DisplayImageOptions options;

    public SingleChatListAdapter(Context context, List<EMMessage> msgs) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.msgs = msgs;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
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
            holder.tv_messageTime = (TextView) convertView.findViewById(R.id.activity_single_chat_list_item_tv_messageTime);
            holder.tv_message = (TextView) convertView.findViewById(R.id.activity_single_chat_list_item_tv_message);
            holder.iv_image = (ImageView) convertView.findViewById(R.id.activity_single_chat_list_item_iv_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 显示信息时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        java.util.Date dt = new Date(msgs.get(position).getMsgTime());
        holder.tv_messageTime.setText(sdf.format(dt));//得到精确到秒的表示
        // 清空控件服用缓存
        holder.iv_image.setImageDrawable(null);
        if (msgs.get(position).getType() == EMMessage.Type.TXT) {
            // 文字信息
            TextMessageBody txtBody = (TextMessageBody) msgs.get(position).getBody();
            if (msgs.get(position).getFrom().equals(UserInfoInCache.user_phoneNum)) {
                holder.tv_message.setText("我: " + txtBody.getMessage());
            } else {
                holder.tv_message.setText(msgs.get(position).getFrom() + ":" + txtBody.getMessage());
            }
        } else if (msgs.get(position).getType() == EMMessage.Type.IMAGE) {
            if (msgs.get(position).getFrom().equals(UserInfoInCache.user_phoneNum)) {
                holder.tv_message.setText("我: ");
            } else {
                holder.tv_message.setText(msgs.get(position).getFrom() + ":");
            }
            // 图片信息
            ImageMessageBody imageBody = (ImageMessageBody) msgs.get(position).getBody();
            if (msgs.get(position).direct == EMMessage.Direct.RECEIVE) {
                // 接收方向的消息
                String remotePath = imageBody.getRemoteUrl();
                String filePath = getImagePath(remotePath);
                String thumbRemoteUrl = imageBody.getThumbnailUrl();
                String thumbnailPath = getThumbnailImagePath(thumbRemoteUrl);
                ImageLoader.getInstance().displayImage(remotePath, holder.iv_image, options);
            } else {
                // 发送方的消息
                String filePath = imageBody.getLocalUrl();
                if (filePath != null) {
                    ImageLoader.getInstance().displayImage("file://" + filePath, holder.iv_image, options);
                }
            }

        }

        return convertView;
    }


    class ViewHolder {
        public TextView tv_messageTime;
        public TextView tv_message;
        public ImageView iv_image;
    }

    public static String getImagePath(String remoteUrl) {
        String imageName = remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1, remoteUrl.length());
        String path = PathUtil.getInstance().getImagePath() + "/" + imageName;
        return path;

    }

    public static String getThumbnailImagePath(String thumbRemoteUrl) {
        String thumbImageName = thumbRemoteUrl.substring(thumbRemoteUrl.lastIndexOf("/") + 1, thumbRemoteUrl.length());
        String path = PathUtil.getInstance().getImagePath() + "/" + "th" + thumbImageName;
        return path;
    }
}
