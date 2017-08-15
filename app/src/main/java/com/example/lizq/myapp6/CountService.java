package com.example.lizq.myapp6;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class CountService extends Service {
    public CountService() {
    }
    /** 创建参数 */
    boolean threadDisable;
    int count;

    public IBinder onBind(Intent intent) {
        Log.i("CountService", "onBind()");

        return new ServiceBinder();
    }

    public void onCreate() {
        Log.i("CountService", "onCreate() thread = "+Thread.currentThread().getId());
        super.onCreate();
        /** 创建一个线程，每秒计数器加一，并在控制台进行Log输出 */
        new Thread(new Runnable() {
            public void run() {
                Log.i("CountService", "new Thread  = "+Thread.currentThread().getId());
                while (!threadDisable) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {

                    }
                    count++;
                    Log.i("CountService", "Count is " + count);
                }
                Log.i("CountService", "exit new Thread  = "+Thread.currentThread().getId());
            }
        }).start();
    }

    public void onDestroy() {
        super.onDestroy();
        /** 服务停止时，终止计数进程 */
        Log.i("CountService", "CountService onDestroy() ");
        this.threadDisable = true;
    }

    public int getConunt() {
        return count;
    }

//此方法是为了可以在Acitity中获得服务的实例
    class ServiceBinder extends Binder {
        public CountService getService() {
            return CountService.this;
        }
    }

}
