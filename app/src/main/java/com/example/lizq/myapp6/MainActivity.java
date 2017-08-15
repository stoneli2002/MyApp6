package com.example.lizq.myapp6;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.lizq.myapp6.MESSAGE";
    CountService countService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.i(MainActivity.class.getName(),"MainActivity thread=" + Thread.currentThread().getId());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //1 start service
        Intent i =new Intent(this,MyIntentService1.class);
        Bundle bundle = new Bundle();
        bundle.putString("taskName", "task1");
        i.putExtras(bundle);
        startService(i);
        //2 bind service
        Intent intent3 = new Intent(MainActivity.this, CountService.class);
        bindService(intent3, conn, Context.BIND_AUTO_CREATE);
        //不能在这里调用countServie的方法，没有产生
//        while (true){
//            Log.i(MainActivity.class.getName(),"Service ="+countService);
//            if(countService != null){
//                Log.i(MainActivity.class.getName(),"Service count="+countService.getConunt());
//                break;
//            }
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        //3 Handler
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.i(MainActivity.class.getName(),id+"=");
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id== R.id.action_search){
           // openSearch();
            return true;
        }
        Log.i(MainActivity.class.getName(),item.toString());
        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(View view) {
        //start service1
        Intent i2 = new Intent(this,MyIntentService1.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString("taskName", "task2");
        bundle2.putString("taskId", "101");
        i2.putExtras(bundle2);
        startService(i2);
        //startService(i);

        //New activity
        Intent intent = new Intent(this, Main2Activity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString() + countService.getConunt();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }


    public void new_webview(View view){
        Intent intent = new Intent(this, WebViewActivity.class);
        startActivity(intent);
    }

    public void new_mediaPlayer(View view){
        Intent intent = new Intent(this, MediaPlayerActivity.class);
        startActivity(intent);
    }

    public void newView(View view) {
        Intent intent = new Intent(this, DemoSurfaceActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString() + countService.getConunt();
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass
        this.unbindService(conn);
        // Stop method tracing that the activity started during onCreate()
        android.os.Debug.stopMethodTracing();
        Log.i(MainActivity.class.getName(),"main acitivty destroy");
    }

    //bind service
    private ServiceConnection conn = new ServiceConnection() {
        /** 获取服务对象时的操作 */
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            Log.i(MainActivity.class.getName(),"conn.onServiceConnected() "+countService);
            countService = ((CountService.ServiceBinder) service).getService();
            Log.i(MainActivity.class.getName(),"conn.onServiceConnected(), get bind service ="+countService);
        }
        /** 无法获取到服务对象时的操作 */
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            countService = null;
        }
    };
    //Handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //获得刚才发送的Message对象，然后在这里进行UI操作
            Log.i("Main1Activity","------------> msg.what = " + msg.what);
        }
    };

    //开启一个线程模拟处理耗时的操作
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    SystemClock.sleep(5000);
                    Log.i("Main1Activity","new Thread send message " + Thread.currentThread().getId());
                    //通过Handler发送一个消息切换回主线程（mHandler所在的线程）
                    mHandler.sendEmptyMessage(0);
                }
            }
        }).start();
    }
}
