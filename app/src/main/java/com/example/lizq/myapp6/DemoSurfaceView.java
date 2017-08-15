package com.example.lizq.myapp6;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class DemoSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    LoopThread thread;

    public DemoSurfaceView(Context context) {
        super(context);
        init(); //初始化,设置生命周期回调方法

    }

    private void init(){
        SurfaceHolder holder = getHolder();
        holder.addCallback(this); //设置Surface生命周期回调created, changed, destroyed,
        thread = new LoopThread(holder, getContext());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.isRunning = true;
        thread.start();
    }
    /**
     * 执行绘制的绘制线程
     * @author Administrator
     *
     */
    class LoopThread extends Thread{
        SurfaceHolder surfaceHolder;
        Context context;
        boolean isRunning;
        float radius = 10f;
        Paint paint;

        public LoopThread(SurfaceHolder surfaceHolder,Context context){
            this.surfaceHolder = surfaceHolder;
            this.context = context;
            isRunning = false;

            paint = new Paint();
            paint.setColor(Color.YELLOW);
            paint.setStyle(Paint.Style.STROKE);
        }

        @Override
        public void run() {
            Canvas c = null;
            while(isRunning){
                try{
                    synchronized (surfaceHolder) {
                        c = surfaceHolder.lockCanvas(null);
                        if(c != null)
                        doDraw(c);
                        //通过它来控制帧数执行一次绘制后休息50ms
                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if(c!=null)
                        surfaceHolder.unlockCanvasAndPost(c);
                }

            }

        }

        public void doDraw(Canvas c){

            //这个很重要，清屏操作，清楚掉上次绘制的残留图像
            c.drawColor(Color.BLACK);

            c.translate(200, 200);
            c.drawCircle(0,0, radius++, paint);

            if(radius > 100){
                radius = 10f;
            }

        }

    }

}
