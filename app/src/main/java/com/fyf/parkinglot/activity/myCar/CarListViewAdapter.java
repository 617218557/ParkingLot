package com.fyf.parkinglot.activity.myCar;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fyf.parkinglot.R;
import com.fyf.parkinglot.model.UserInfoInCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by fengyifei on 15/11/28.
 */
public class CarListViewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private DisplayImageOptions options;
    private ImageSize mImageSize;

    public CarListViewAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        mImageSize = new ImageSize(60, 60);
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.activity_my_car_list_item, parent, false);
            holder.iv_car = (ImageView) convertView.findViewById(R.id.activity_my_car_list_item_iv_car);
            holder.tv_type = (TextView) convertView.findViewById(R.id.activity_my_car_list_item_tv_type);
            holder.tv_licenseNum = (TextView) convertView.findViewById(R.id.activity_my_car_list_item_tv_licenseNum);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_type.setText(UserInfoInCache.myCarList.get(position).getCar_type());
        holder.tv_licenseNum.setText(UserInfoInCache.myCarList.get(position).getCar_licenseNum());
        ImageLoader.getInstance().loadImage(UserInfoInCache.myCarList.get(position).getCar_img(), mImageSize, options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String arg0,
                                                  View arg1, Bitmap bitmap) {
                        holder.iv_car.setImageBitmap(bitmap);
                    }
                });
        return convertView;
    }

    class ViewHolder {
        public ImageView iv_car;
        public TextView tv_type;
        public TextView tv_licenseNum;
    }
}
