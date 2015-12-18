package com.fyf.parkinglot.activity.weatherForecast;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fyf.parkinglot.R;
import com.fyf.parkinglot.model.WeatherForecastInfoBean;
import com.fyf.parkinglot.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by fengyifei on 15/12/18.
 */
public class WeatherDataAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private DisplayImageOptions options;
    private List<WeatherForecastInfoBean.Results.WeatherData> weatherList;

    public WeatherDataAdapter(Context context, List<WeatherForecastInfoBean.Results.WeatherData> weatherList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.weatherList = weatherList;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public int getCount() {
        return weatherList.size();
    }

    @Override
    public Object getItem(int position) {
        return weatherList.get(position);
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
                    R.layout.activity_weather_forecast_data_item, parent, false);
            holder.iv_weather = (ImageView) convertView.findViewById(R.id.activity_weather_forecast_data_item_iv_weather);
            holder.tv_date = (TextView) convertView.findViewById(R.id.activity_weather_forecast_data_item_tv_date);
            holder.tv_weather = (TextView) convertView.findViewById(R.id.activity_weather_forecast_data_item_tv_weather);
            holder.tv_wind = (TextView) convertView.findViewById(R.id.activity_weather_forecast_data_item_tv_wind);
            holder.tv_temperature = (TextView) convertView.findViewById(R.id.activity_weather_forecast_data_item_tv_temperature);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_date.setText(weatherList.get(position).getDate() + ":");
        holder.tv_weather.setText(weatherList.get(position).getWeather());
        holder.tv_wind.setText(weatherList.get(position).getWind() + ":");
        holder.tv_temperature.setText(weatherList.get(position).getTemperature());
        // 判断白天晚上,加载不同的图片
        ImageLoader.getInstance().displayImage(
                Utils.isNight() == false ?
                        weatherList.get(position).getDayPictureUrl() :
                        weatherList.get(position).getNightPictureUrl(),
                holder.iv_weather, options);
        return convertView;
    }

    class ViewHolder {
        public ImageView iv_weather;
        public TextView tv_date;
        public TextView tv_weather;
        public TextView tv_wind;
        public TextView tv_temperature;
    }
}
