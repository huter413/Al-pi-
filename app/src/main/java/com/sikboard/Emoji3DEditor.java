package com.sikboard;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public final class Emoji3DEditor implements View.OnTouchListener {
    private float rotationX, rotationY, zoom=1f, lastX, lastY;
    private final ScaleGestureDetector scale;
    public Emoji3DEditor(Context context){
        scale=new ScaleGestureDetector(context,new ScaleGestureDetector.SimpleOnScaleGestureListener(){
            public boolean onScale(ScaleGestureDetector d){ zoom=Math.max(.5f,Math.min(3f,zoom*d.getScaleFactor())); return true; }
        });
    }
    public boolean onTouch(View v, MotionEvent e){
        scale.onTouchEvent(e);
        if(e.getPointerCount()==1 && e.getActionMasked()==MotionEvent.ACTION_MOVE){ rotationY+=e.getX()-lastX; rotationX+=e.getY()-lastY; }
        lastX=e.getX(); lastY=e.getY(); return true;
    }
    public float getRotationX(){return rotationX;} public float getRotationY(){return rotationY;} public float getZoom(){return zoom;}
}
