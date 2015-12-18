package com.fyf.parkinglot.activity.chooseSpace;

import android.content.Context;
import android.widget.GridView;
import android.widget.ImageView;

import com.fyf.parkinglot.model.ParkinglotInfoBean;

import java.util.ArrayList;

/**
 * Created by fengyifei on 15/11/25.
 */
public class DrawParkinglot {
    private Context context;
    private GridView gv_gridView;
    private ArrayList<ParkinglotInfoBean> list;
    private GridViewAdapter adapter;
    private int width;

    private final int column = 7;//定义车位显示列数
    private final int padding = 10;//车位间距


    public DrawParkinglot(Context context, GridView gv_gridView, ArrayList<ParkinglotInfoBean> list) {
        this.context = context;
        this.gv_gridView = gv_gridView;
        this.list = list;
    }

    private void calculateWidth() {
        width = (gv_gridView.getWidth() - padding * (column - 1)) / column; // 计算每个小车位的宽度
    }

    private void setGridView() {
        gv_gridView.setNumColumns(column);
        gv_gridView.setColumnWidth(width);
        gv_gridView.setHorizontalSpacing(padding);
        gv_gridView.setVerticalSpacing(padding);
        adapter = new GridViewAdapter(context, list, width);
        gv_gridView.setAdapter(adapter);
    }

    // 设置示例图标的大小
    public void setIcon(ImageView iv_used, ImageView iv_notUsed, ImageView iv_mine) {
        iv_used.setLayoutParams(new android.widget.LinearLayout.LayoutParams(width, width));
        iv_notUsed.setLayoutParams(new android.widget.LinearLayout.LayoutParams(width, width));
        iv_mine.setLayoutParams(new android.widget.LinearLayout.LayoutParams(width, width));
    }

    public void draw() {
        calculateWidth();
        setGridView();
    }

    /**
     * 获取用户选择的车位id
     *
     * @return 未选择时返回-1
     */
    public int getParkId() {
        if (adapter != null) {
            return adapter.getParkId();
        } else {
            return -1;
        }
    }

}
