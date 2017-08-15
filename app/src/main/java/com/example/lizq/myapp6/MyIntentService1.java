package com.example.lizq.myapp6;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.IBinder;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService1 extends IntentService {
    private static final String TAG = "MyIntentService1";
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.lizq.myapp6.action.FOO";
    private static final String ACTION_BAZ = "com.example.lizq.myapp6.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.lizq.myapp6.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.lizq.myapp6.extra.PARAM2";

    public MyIntentService1() {
        super("MyIntentService1");
        Log.i(TAG, "MyIntentService1 thread："+Thread.currentThread().getId());
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService1.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService1.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.i(TAG, "MyIntentService1 onCreate() thread = "+Thread.currentThread().getId());
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "MyIntentService1 onHandleIntent() thread = "+Thread.currentThread().getId());
        if (intent != null) {
            final String action = intent.getAction();
            Log.i(TAG, "action = "+action);
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
            String taskName = intent.getExtras().getString("taskName");
            switch (taskName) {
                case "task1":
                    Log.i(TAG, "do task1");
                    break;
                case "task2":
                    Log.i(TAG, "do task2 "+intent.getExtras().getString("taskId"));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onDestroy() {
        Log.i(TAG, "MyIntentService1 onDestroy() thread = "+Thread.currentThread().getId());
        super.onDestroy();
        Log.i(TAG, "Back to main thread = "+Thread.currentThread().getId());
    }
}
