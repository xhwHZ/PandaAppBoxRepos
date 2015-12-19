package com.xhw.panda.app.box.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.xhw.panda.app.box.utils.ThreadUtils;
import com.xhw.panda.app.box.utils.UIUtils;
/**
 * �Զ��������
 * @author itcast
 *
 */
public class ProgressArc extends View {
	public final static int PROGRESS_STYLE_NO_PROGRESS = -1;
	public final static int PROGRESS_STYLE_DOWNLOADING = 0;
	public final static int PROGRESS_STYLE_WAITING = 1;
	private final static int START_PROGRESS = -90;
	private static final float RATIO = 360;
	
	
	
	private Paint mPaint;
	private RectF mArcRect;// ���ڻ�Բ�ε�����
	private boolean mUserCenter;  // �����Ƿ����Բ��
	private Drawable mDrawableForegroud; // ��ʾ��ͼƬ
	private int mDrawableForegroudResId;//
	private int mArcDiameter;//Բ��ֱ��
	private int mProgressColor;// ���ȵ���ɫ
	private  int mStyle=PROGRESS_STYLE_NO_PROGRESS;
	private float mProgress;
	//private float mCurrentProgress;
	//private float mStartProgress;// ��������ʼ����
	private float mSweep=0;
	
	
	public void setStyle(int mStyle) {
		this.mStyle = mStyle;
		if(mStyle==PROGRESS_STYLE_WAITING){
			invalidateSafe();
		}
	}
	public ProgressArc(Context context) {
		super(context);
		int strokeWidth=UIUtils.dp2px(1);
		
		mPaint=new Paint();
		mPaint.setAntiAlias(true);// �������
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(strokeWidth);
		
		mUserCenter=false;
		
		mArcRect=new RectF();
		
	}
	public void seForegroundResource(int resId) {
		if (mDrawableForegroudResId == resId) {
			return;
		}
		mDrawableForegroudResId = resId;
		mDrawableForegroud = UIUtils.getDrawable(mDrawableForegroudResId);
		invalidateSafe();  //ˢ�½���
	}
	private void invalidateSafe() {
		if (ThreadUtils.isRunOnMainThread()) {
			postInvalidate();
		} else {
			invalidate();
		}
	}

	/** ����ֱ�� */
	public void setArcDiameter(int diameter) {
		mArcDiameter = diameter;
	}
	/** ���ý���������ɫ */
	public void setProgressColor(int progressColor) {
		mProgressColor = progressColor;
		mPaint.setColor(progressColor);
	}
	// ����
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = 0;
		int height = 0;

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthMode == MeasureSpec.EXACTLY) {//����Ǿ�ȷ��
			width = widthSize;
		} else {//����ͼƬ�Ĵ�С
			width = mDrawableForegroud == null ? 0 : mDrawableForegroud.getIntrinsicWidth();
			if (widthMode == MeasureSpec.AT_MOST) {
				width = Math.min(width, widthSize);
			}
		}

		if (heightMode == MeasureSpec.EXACTLY) {//����Ǿ�ȷ��
			height = heightSize;
		} else {//����ͼƬ�Ĵ�С
			height = mDrawableForegroud == null ? 0 : mDrawableForegroud.getIntrinsicHeight();
			if (heightMode == MeasureSpec.AT_MOST) {
				height = Math.min(height, heightSize);
			}
		}
		//�����������������
		mArcRect.left = (width - mArcDiameter) * 0.5f;
		mArcRect.top = (height - mArcDiameter) * 0.5f;
		mArcRect.right = (width + mArcDiameter) * 0.5f;
		mArcRect.bottom = (height + mArcDiameter) * 0.5f;

		setMeasuredDimension(width, height);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		if(mDrawableForegroud!=null){
			// ���ñ߾�
			mDrawableForegroud.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
			mDrawableForegroud.draw(canvas);
		}
		//���ƽ���
		drawArc(canvas);
	}
	public void setProgress(float progress,boolean smooth){
		mProgress =progress;
//		if(mProgress==0){
//			mCurrentProgress=0;// ��ʼ����
//		}
//		mStartProgress=mCurrentProgress;
		invalidateSafe();
	}
	
	
	private void drawArc(Canvas canvas) {
		if(mStyle==PROGRESS_STYLE_DOWNLOADING||mStyle==PROGRESS_STYLE_WAITING){
			mPaint.setColor(mProgressColor);
			mSweep=mProgress*RATIO;
			// ���Ƶ�����
			canvas.drawArc(mArcRect, START_PROGRESS, mSweep, mUserCenter, mPaint);
			
		}
	}
}
