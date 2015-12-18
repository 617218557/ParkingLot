package com.fyf.parkinglot.activity.chooseSpace;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fyf.parkinglot.R;
import com.fyf.parkinglot.model.ParkinglotInfoBean;

import java.util.ArrayList;

/**
 * Created by fengyifei on 15/11/25.
 */
public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ParkinglotInfoBean> dataList;
    private int width;
    private ArrayList<Integer> flagList = new ArrayList<>(); //维护一个车位状态数据,0表示未占用,1表示已占用,2表示用户选择的

    private final int NOT_USED = 0, USED = 1, USER_USED = 2;

    public GridViewAdapter(Context context, ArrayList<ParkinglotInfoBean> dataList, int width) {
        this.context = context;
        this.dataList = dataList;
        this.width = width;
        initFlagList();
    }

    private void initFlagList() {
        for (ParkinglotInfoBean parkinglotInfoBean : dataList) {
            flagList.add(parkinglotInfoBean.getPark_isUse());
        }
    }

    /**
     * 获取用户选择的车位id
     * @return 未选择时返回-1
     */
    public int getParkId() {
        if (flagList.contains(USER_USED)) {
            return dataList.get(flagList.indexOf(USER_USED)).getPark_id();

        } else {
            return -1;
        }
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //定义一个ImageView,显示在GridView里
        final ImageView iv = new ImageView(context);
        iv.setLayoutParams(new android.widget.AbsListView.LayoutParams(width, width));
        if (flagList.get(position) == USED) {
            // 车位被占用
            iv.setImageDrawable(context.getResources().getDrawable(R.drawable.frame_parking_space_grey));
        } else if (flagList.get(position) == USER_USED) {
            // 用户占用
            iv.setImageDrawable(context.getResources().getDrawable(R.drawable.frame_parking_space_cyan));
        } else {
            // 车位未被占用
            iv.setImageDrawable(context.getResources().getDrawable(R.drawable.frame_parking_space_white));
        }
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagList.get(position) == 0) {
                    // 该处车位可选择
                    if (!flagList.contains(USER_USED)) {
                        // 用户未选择
                        flagList.set(position, USER_USED);
                    } else {
                        // 用户已选择
                        flagList.set(flagList.indexOf(USER_USED), NOT_USED);
                        flagList.set(position, USER_USED);
                    }
                    notifyDataSetChanged();
                }
            }
        });
        return iv;
    }
}

