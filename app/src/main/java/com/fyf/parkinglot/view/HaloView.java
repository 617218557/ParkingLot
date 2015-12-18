package com.fyf.parkinglot.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.fyf.parkinglot.R;
import com.fyf.parkinglot.common.ScreenInfo;

import java.util.Random;

public class HaloView extends View {

	Bitmap mHalo = null;
	MyHalo myHalo[] = new MyHalo[12];
	private Integer[] offsetX;// X轴每次移动像素
	private Integer[] offsetY;// Y轴每次移动像素
	Random r = new Random();
	Matrix m = new Matrix();
	Paint p = new Paint();

	int mW = ScreenInfo.getScreenWidth();// 屏幕宽度
	int mH = ScreenInfo.getScreenHeight();// 屏幕高度
	// 像素密度
	float de = ScreenInfo.getDe();

	public void setOffset() {
		offsetX = new Integer[] { (int) (int) (1 * de), (int) (2 * de), };
		offsetY = new Integer[] { (int) (1 * de), (int) (2 * de), };
	}

	public HaloView(Context context) {
		super(context);
		setOffset();
	}

	public HaloView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOffset();
	}

	public HaloView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOffset();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		for (int i = 0; i < myHalo.length; i++) {
			MyHalo myHaloItem = myHalo[i];

			int t = myHaloItem.t;
			t--;
			if (t <= 0) {

				if (myHaloItem.flagX == 0) {
					myHaloItem.x -= myHaloItem.speedX;
					if (myHaloItem.x < -mW * 0.5)
						myHaloItem.flagX = 1;
				} else {
					myHaloItem.x += myHaloItem.speedX;
					if (myHaloItem.x > mW + mW * 0.5)
						myHaloItem.flagX = 0;
				}
				if (myHaloItem.flagY == 0) {
					myHaloItem.y -= myHaloItem.speedY;
					if (myHaloItem.y < -mH * 0.5)
						myHaloItem.flagY = 1;
				} else {
					myHaloItem.y += myHaloItem.speedY;
					if (myHaloItem.y > mH + mH * 0.5)
						myHaloItem.flagY = 0;
				}

				canvas.save();
				m.reset();
				m.setScale(myHaloItem.s, myHaloItem.s);
				canvas.setMatrix(m);
				p.setAlpha(myHaloItem.alpha);
				canvas.drawBitmap(mHalo, myHaloItem.x, myHaloItem.y, p);
				canvas.restore();
			}
			// 当t过小时，重置t
			if (t == -10000)
				t = -1;
			myHaloItem.t = t;
		}
		super.onDraw(canvas);
	}

	public void loadFlower() {
		Resources r = this.getContext().getResources();
		mHalo = ((BitmapDrawable) r.getDrawable(R.drawable.ic_fog_circle))
				.getBitmap();
	}

	public void recly() {
		if (mHalo != null && !mHalo.isRecycled()) {
			mHalo.recycle();
		}
	}

	public void addRect() {
		for (int i = 0; i < myHalo.length; i++) {
			myHalo[i] = new MyHalo();
		}
	}

	public void inva() {
		invalidate();
	}

	class MyHalo {
		int x;// 距离屏幕左边距离
		int y;// 距离屏幕右边距离
		float s;// 放大倍数
		int alpha;// 透明度
		int t;// 光晕开始运动时间延迟
		int speedX;// X轴动画速度（每次移动的像素）
		int speedY;// Y轴动画速度（每次移动的像素）

		int flagX, flagY;// 记录X,Y轴移动方向(0为向上向左，1为向下向右)

		public void init() {

			this.x = r.nextInt(mW);
			this.y = r.nextInt(mH);

			this.s = r.nextFloat();
			// 防止过小
			if (this.s < 0.1f)
				s += 0.2f;
				// 防止过大
			else if (this.s > 0.5f)
				s -= 0.2f;

			this.alpha = r.nextInt(155) + 100;
			this.t = r.nextInt(80);

			this.speedX = offsetX[r.nextInt(offsetX.length)];
			this.speedY = offsetY[r.nextInt(offsetY.length)];
			// 初始化移动方向
			this.flagX = r.nextInt(2);
			this.flagY = r.nextInt(2);
		}

		public MyHalo() {
			super();
			init();
		}

	}
}
