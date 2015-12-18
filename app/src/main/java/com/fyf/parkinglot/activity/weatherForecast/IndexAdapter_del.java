package com.fyf.parkinglot.activity.weatherForecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fyf.parkinglot.R;
import com.fyf.parkinglot.model.WeatherForecastInfoBean;

import java.util.List;

/**
 * Created by fengyifei on 15/12/18.
 */
public class IndexAdapter_del extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<WeatherForecastInfoBean.Results.Index> indexList;

    public IndexAdapter_del(Context context, List<WeatherForecastInfoBean.Results.Index> indexList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.indexList = indexList;
    }

    @Override
    public int getCount() {
        return indexList.size();
    }

    @Override
    public Object getItem(int position) {
        return indexList.get(position);
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
                    R.layout.activity_weather_forecast_index_item, parent, false);
            holder.tv_title = (TextView) convertView.findViewById(R.id.activity_weather_forecast_index_item_tv_title);
            holder.tv_zs = (TextView) convertView.findViewById(R.id.activity_weather_forecast_index_item_tv_zs);
            holder.tv_tipt = (TextView) convertView.findViewById(R.id.activity_weather_forecast_index_item_tv_tipt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(indexList.get(position).getTitle() + ":");
        holder.tv_zs.setText(indexList.get(position).getZs());
        holder.tv_tipt.setText(indexList.get(position).getTipt() + ":" + indexList.get(position).getDes());
        return convertView;
    }

    class ViewHolder {
        public TextView tv_title;
        public TextView tv_zs;
        public TextView tv_tipt;
    }
}
