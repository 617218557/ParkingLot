package com.fyf.parkinglot.activity.groupChat;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.util.PathUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.model.UserInfoInCache;
import com.fyf.parkinglot.utils.Utils;
import com.fyf.parkinglot.view.CustomToast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by fengyifei on 15/12/19.
 */
public class GroupChatListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<EMMessage> msgs;

    private MediaPlayer mPlayer;

    public GroupChatListAdapter(Context context, List<EMMessage> msgs) {
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
                    R.layout.activity_group_chat_list_item, parent, false);
            holder.tv_messageTime = (TextView) convertView.findViewById(R.id.activity_group_chat_list_item_tv_messageTime);
            holder.tv_message = (TextView) convertView.findViewById(R.id.activity_group_chat_list_item_tv_message);
            holder.iv_image = (SimpleDraweeView) convertView.findViewById(R.id.activity_group_chat_list_item_iv_image);
            holder.iv_voice = (ImageView) convertView.findViewById(R.id.activity_group_chat_list_item_iv_voice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 显示信息时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date dt = new Date(msgs.get(position).getMsgTime());
        holder.tv_messageTime.setText(sdf.format(dt));//得到精确到秒的表示
        if (msgs.get(position).getType() == EMMessage.Type.TXT) {
            // 文字信息
            showText(holder);
            TextMessageBody txtBody = (TextMessageBody) msgs.get(position).getBody();
            if (msgs.get(position).getFrom().equals(UserInfoInCache.user_phoneNum)) {
                holder.tv_message.setText("我: " + txtBody.getMessage());
            } else {
                holder.tv_message.setText(msgs.get(position).getFrom() + ":" + txtBody.getMessage());
            }
        } else if (msgs.get(position).getType() == EMMessage.Type.IMAGE) {
            showImage(holder);
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
                Utils.loadImageUtils(holder.iv_image, remotePath, context);
            } else {
                // 发送方的消息
                String filePath = imageBody.getLocalUrl();
                if (filePath != null) {
                    Utils.loadImageUtils(holder.iv_image, "file://" + filePath, context);
                }
            }
        } else if (msgs.get(position).getType() == EMMessage.Type.VOICE) {
            showVoice(holder);
            final int posi = position;
            // 语音消息
            holder.iv_voice.setVisibility(View.VISIBLE);
            if (msgs.get(position).getFrom().equals(UserInfoInCache.user_phoneNum)) {
                holder.tv_message.setText("我: ");
            } else {
                holder.tv_message.setText(msgs.get(position).getFrom() + ":");
            }
            holder.iv_voice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (msgs.get(posi).direct == EMMessage.Direct.RECEIVE) {
                        // 接收方向的消息
                        VoiceMessageBody voiceMessageBody = (VoiceMessageBody) msgs.get(posi).getBody();
                        String path;
                        if (TextUtils.isEmpty(voiceMessageBody.getLocalUrl())) {
                            path = voiceMessageBody.getRemoteUrl();
                        } else {
                            path = voiceMessageBody.getLocalUrl();
                        }
                        playVoice(path, (ImageView) v);
                    } else {
                        // 发送方的消息
                        VoiceMessageBody voiceMessageBody = (VoiceMessageBody) msgs.get(posi).getBody();
                        String path;
                        if (TextUtils.isEmpty(voiceMessageBody.getLocalUrl())) {
                            path = voiceMessageBody.getRemoteUrl();
                        } else {
                            path = voiceMessageBody.getLocalUrl();
                        }
                        playVoice(path, (ImageView) v);
                    }
                }
            });
        }
        return convertView;
    }


    class ViewHolder {
        public TextView tv_messageTime;
        public TextView tv_message;
        public SimpleDraweeView iv_image;
        public ImageView iv_voice;
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

    private void playVoice(String voiceFilePath, final ImageView iv) {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();

        }
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                iv.setBackgroundResource(R.drawable.ic_voice);
            }
        });
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                iv.setBackgroundResource(R.drawable.ic_voice_green);
            }
        });
        try {
            mPlayer.reset();
            mPlayer.setDataSource(voiceFilePath);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            CustomToast.showToast(context, "播放失败:" + e.getMessage(), 1000);
        }
    }

    private void showText(ViewHolder holder) {
        holder.tv_message.setVisibility(View.VISIBLE);
        holder.iv_image.setVisibility(View.GONE);
        holder.iv_voice.setVisibility(View.GONE);
    }

    private void showImage(ViewHolder holder) {
        holder.tv_message.setVisibility(View.GONE);
        holder.iv_image.setVisibility(View.VISIBLE);
        holder.iv_voice.setVisibility(View.GONE);
    }

    private void showVoice(ViewHolder holder) {
        holder.tv_message.setVisibility(View.GONE);
        holder.iv_image.setVisibility(View.GONE);
        holder.iv_voice.setVisibility(View.VISIBLE);
    }

    public void onDestory() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}
