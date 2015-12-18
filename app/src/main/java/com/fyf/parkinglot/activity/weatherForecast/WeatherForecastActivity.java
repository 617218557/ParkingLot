package com.fyf.parkinglot.activity.weatherForecast;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fyf.parkinglot.R;
import com.fyf.parkinglot.common.GlobalDefine;
import com.fyf.parkinglot.common.URLAddress;
import com.fyf.parkinglot.model.WeatherForecastInfoBean;
import com.fyf.parkinglot.utils.HttpUtils;
import com.fyf.parkinglot.utils.JsonUtils;
import com.fyf.parkinglot.view.CustomPrgressDailog;
import com.fyf.parkinglot.view.CustomToast;
import com.twotoasters.jazzylistview.JazzyListView;

public class WeatherForecastActivity extends AppCompatActivity {

    private Button btn_back;
    private TextView tv_title;
    private Button btn_next;

    private TextView tv_city, tv_pm;
    private RecyclerView rv_index;
    private JazzyListView lv_day;

    private WeatherDataAdapter weatherDataAdapter;
    private IndexAdapter indexAdapter;

    private WeatherForecastInfoBean weatherForecastInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        findView();
        setListener();
        init();
    }

    private void findView() {
        btn_back = (Button) findViewById(R.id.layout_actionBar_btn_back);
        tv_title = (TextView) findViewById(R.id.layout_actionBar_tv_title);
        btn_next = (Button) findViewById(R.id.layout_actionBar_btn_next);
        tv_city = (TextView) findViewById(R.id.activity_weather_forecast_tv_city);
        tv_pm = (TextView) findViewById(R.id.activity_weather_forecast_tv_pm);
        rv_index = (RecyclerView) findViewById(R.id.activity_weather_forecast_rv_index);
        lv_day = (JazzyListView) findViewById(R.id.activity_weather_forecast_lv_day);
    }

    private void setListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        tv_title.setText(R.string.activity_weather_forecast_forecast);
        btn_next.setVisibility(View.INVISIBLE);
        GetWeatherForecastTask getWeatherForecastTask = new GetWeatherForecastTask();
        getWeatherForecastTask.execute();
    }

    class GetWeatherForecastTask extends AsyncTask {

        CustomPrgressDailog dailog = new CustomPrgressDailog(WeatherForecastActivity.this, R.style.DialogNormal);

        @Override
        protected void onPreExecute() {
            dailog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            return HttpUtils.httpGetWithOutBase64(URLAddress.weatherForcastURL + "?location=西安&output=json&ak="
                    + GlobalDefine.CAR_API_AK
                    + "&mcode=43:05:6B:FA:9E:39:B0:2B:C6:27:10:DA:6A:79:B2:FF:59:B8:32:6B;com.fyf.parkinglot");
        }

        @Override
        protected void onPostExecute(Object o) {
            handleForecast(o.toString());
            dailog.dismiss();
            super.onPostExecute(o);
        }
    }

    private void handleForecast(String json) {
        weatherForecastInfoBean = JsonUtils.getWeatherForecast(json);
        if (weatherForecastInfoBean != null || weatherForecastInfoBean.getError() == 0) {
            // 查询天气成功
            tv_city.setText(weatherForecastInfoBean.getResults().get(0).getCurrentCity());
            tv_pm.setText("PM2.5: " + weatherForecastInfoBean.getResults().get(0).getPm25());

            indexAdapter = new IndexAdapter(this,
                    weatherForecastInfoBean.getResults().get(0).getIndex());
            LinearLayoutManager layout = new LinearLayoutManager(WeatherForecastActivity.this);
            layout.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv_index.setLayoutManager(layout);
            rv_index.setAdapter(indexAdapter);

            weatherDataAdapter = new WeatherDataAdapter
                    (this, weatherForecastInfoBean.getResults().get(0).getWeather_data());
            lv_day.setAdapter(weatherDataAdapter);
        } else {
            // 查询天气失败
            CustomToast.showToast(getApplicationContext(), "查询天气失败", 1000);
        }
    }
}
