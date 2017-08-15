package com.example.lizq.myapp6;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class MediaPlayerActivity  extends Activity implements View.OnClickListener {
    private EditText et_path;
    private MediaPlayer mediaPlayer;
    private Button play, pause, stop, replay;
    private SurfaceView sv;
    private boolean isplaying = false;
    private boolean ispause = false;
    private int position;
    private String path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        et_path = (EditText) this.findViewById(R.id.et_path);
        play = (Button) this.findViewById(R.id.play);
        pause = (Button) this.findViewById(R.id.pause);
        stop = (Button) this.findViewById(R.id.stop);
        replay = (Button) this.findViewById(R.id.replay);
        sv = (SurfaceView) this.findViewById(R.id.sv);

        ViewGroup.LayoutParams lp = sv.getLayoutParams();
        lp.width = 480;
        lp.height =320;
        sv.setLayoutParams(lp);

        sv.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                System.out.println("surfaceview的holder被销毁了");
                //停止视频的播放
                stop();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                System.out.println("surfaceview的holder被创建了");
                //重新开始视频的播放
                //传递进来的参数 holder是新创建出来的 holder对象
                holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


                //从刚才记录的位置地方继续播放视频
                if (position > 0) {
                    play(path, position, holder);
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if(holder != null && mediaPlayer != null){
                    mediaPlayer.setDisplay(holder);
                }
            }
        });


        //指定surfaceview不维护自己的缓冲区,而是等待多媒体播放器mediaplayer 把数据推到surfaceview的缓冲区
        sv.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        replay.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        path = et_path.getText().toString().trim();
        if(path.startsWith("http") ||path.startsWith("rtsp") ||path.startsWith("rtmp") ){

        }else{
            if (TextUtils.isEmpty(path)) {
                Toast.makeText(this, "路径不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            ///mnt/sdcard/m2.mp4
            File file = new File(path);
            if (!file.exists()) {
                Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        switch (v.getId()) {
            case R.id.play:
                play(path, 0, sv.getHolder());

                break;
            case R.id.pause:
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    isplaying = false;

                    pause.setText("继续播放");
                    ispause = true;
                    return;
                }
                if (ispause) {
                    mediaPlayer.start();
                    pause.setText("暂停");
                    ispause = false;
                }
                break;
            case R.id.stop:
                stop();
                break;
            case R.id.replay:
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(0);
                    return;
                }
                play(path, 0, sv.getHolder());
                break;
        }

    }

    /**
     * 需要记录当前播放的位置
     */
    private void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            position = mediaPlayer.getCurrentPosition();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            play.setClickable(true);
            play.setEnabled(true);
        }
    }

    private void play(String path, final int position, SurfaceHolder holder) {
        try {
            if(!isplaying ){
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                //指定播放的视频显示到那个控件里面
                mediaPlayer.setDisplay(holder);
                mediaPlayer.setDataSource(path);
                //mediaPlayer.prepare();
                mediaPlayer.prepareAsync();// 异步的准备
            }

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    mediaPlayer.seekTo(position);
                    isplaying = true;
                    play.setClickable(false);
                    play.setEnabled(false);
                }
            });


            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    //play.setClickable(true);
                    //play.setEnabled(true);
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                    play.setClickable(true);
                    play.setEnabled(true);
                    return false;
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "播放文件失败,格式不支持",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
