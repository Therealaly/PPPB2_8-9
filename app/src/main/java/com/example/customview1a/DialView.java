package com.example.customview1a;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class DialView extends View {

    private static int SELECTION_COUNT = 4;
    private float mWidth;
    private float mHeight;
    private Paint mTextPaint;
    private Paint mDialPaint;
    private float mRadius;
    private int mActiveSelection;
    private final StringBuffer mTempLabel = new StringBuffer(8);
    private final float[] mTempResult = new float[2];

    private void init(){
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(40f);

        mDialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDialPaint.setColor(Color.GRAY);

        mActiveSelection = 0;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mActiveSelection = (mActiveSelection + 1) % SELECTION_COUNT;
                //bisa geser karena mActiveSelection selalu ditambah 1

                if (mActiveSelection >= 1) {
                    mDialPaint.setColor(Color.GREEN);
                }
                else {
                    mDialPaint.setColor(Color.GRAY);
                }

                // agar bisa berjalan harus diberi invalidate
                invalidate();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = (float) (Math.min(mWidth, mHeight/2*0.8));
    }

    private float [] computeXYZForPosition(final int pos, final float radius) {
        float[] result = mTempResult;
        Double startAngle = Math.PI * (9/8d);
        Double angle = startAngle + (pos * (Math.PI/4));
        result[0] = (float) (radius * Math.cos(angle)) + (mWidth/2);
        result[1] = (float) (radius * Math.sin(angle)) + (mHeight/2);
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // gambar circle besar
        canvas.drawCircle(mWidth/2, mHeight/2, mRadius, mDialPaint);

        //draw text
        final float labelRadius = mRadius + 20;
        StringBuffer label = mTempLabel;

        // buat label angka maks 4 karena diawal diset SELECTION_COUNT = 4
        for (int i = 0; i< SELECTION_COUNT; i++){
            float [] xyData = computeXYZForPosition(i, labelRadius);
            float x = xyData[0];
            float y = xyData[1];
            label.setLength(0);
            label.append(i);
            // peritah menaruh text sesuai radius
            canvas.drawText(label, 0, label.length(), x, y, mTextPaint);
        }

        //draw mark circle
        final float markerRadus = mRadius - 35;
        float[] xyData = computeXYZForPosition(mActiveSelection, markerRadus);
        float x = xyData[0];
        float y = xyData[1];

        canvas.drawCircle(x, y, 20, mTextPaint);
    }

    public DialView(Context context) {
        super(context);
        init();
    }

    public DialView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
}
