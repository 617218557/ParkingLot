package com.fyf.parkinglot.activity.chooseSpace;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fyf.parkinglot.R;
import com.fyf.parkinglot.model.ParkinglotInfoBean;

import java.util.ArrayList;

/**
 * Created by fengyifei on 15/11/25.
 * @deprecated 有问题
 * 绘制停车场
 */
public class DrawParkinglot1 {

    private Context context;
    private FrameLayout layout;
    private ArrayList<ParkinglotInfoBean> list;

    private final int column = 8;//定义车位显示列数
    private final int padding = 10;//车位间距

    private int chooseIndex = -1;
    private ArrayList<ImageView> viewList = new ArrayList<>();

    public DrawParkinglot1(Context context, FrameLayout layout, ArrayList<ParkinglotInfoBean> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    public void draw() {
        int len = list.size();
        int width = (layout.getWidth() - padding * (column - 1)) / column; // 计算每个小车位的宽度
        for (int i = 0; i < len; i++) {
            ParkinglotInfoBean parkinglotInfoBean = list.get(i);
            final ImageView iv = new ImageView(context);
            int leftPadding = (width + padding) * (i % column); // 计算左边距
            int topPadding = i / column * (width + padding); // 计算高度
            iv.layout(leftPadding, topPadding, 0, 0);
            iv.setLayoutParams(new android.widget.FrameLayout.LayoutParams(width, width));

            iv.setTag(i);

            if (parkinglotInfoBean.getPark_isUse() == 1) {
                // 车位被占用
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.frame_parking_space_grey));
            } else {
                // 车位未被占用
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.frame_parking_space_white));
            }
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chooseIndex == -1) {
                        // 未选择时
                        iv.setImageDrawable(context.getResources().getDrawable(R.drawable.frame_parking_space_cyan));
                        chooseIndex = (int) iv.getTag();
                    } else if (chooseIndex == (int) iv.getTag()) {
                        // 点中自身
                        iv.setImageDrawable(context.getResources().getDrawable(R.drawable.frame_parking_space_white));
                        chooseIndex = -1;
                    } else {
                        //未点中自身
                        viewList.get(chooseIndex).setImageDrawable(context.getResources().getDrawable(R.drawable.frame_parking_space_white));
                        iv.setImageDrawable(context.getResources().getDrawable(R.drawable.frame_parking_space_grey));
                        chooseIndex = (int) iv.getTag();
                    }
                }
            });
            layout.addView(iv);
            viewList.add(iv);
        }
    }


}
