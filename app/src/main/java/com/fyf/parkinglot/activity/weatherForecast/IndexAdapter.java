package com.fyf.parkinglot.activity.weatherForecast;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fyf.parkinglot.R;
import com.fyf.parkinglot.model.WeatherForecastInfoBean;

import java.util.List;

public class IndexAdapter extends
        RecyclerView.Adapter<IndexAdapter.ViewHolder> {
    private List<WeatherForecastInfoBean.Results.Index> indexList;

    public IndexAdapter(List<WeatherForecastInfoBean.Results.Index> indexList) {
        this.indexList = indexList;
    }

    @Override
    public int getItemCount() {
        return indexList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        viewHolder.tv_title.setText(indexList.get(position).getTitle() + ":");
        viewHolder.tv_zs.setText(indexList.get(position).getZs());
        viewHolder.tv_tipt.setText(indexList.get(position).getTipt() + ":" + indexList.get(position).getDes());
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_weather_forecast_index_item, parent, false);
        return new ViewHolder(v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_title;
        public TextView tv_zs;
        public TextView tv_tipt;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.activity_weather_forecast_index_item_tv_title);
            tv_zs = (TextView) itemView.findViewById(R.id.activity_weather_forecast_index_item_tv_zs);
            tv_tipt = (TextView) itemView.findViewById(R.id.activity_weather_forecast_index_item_tv_tipt);
        }

    }

}
