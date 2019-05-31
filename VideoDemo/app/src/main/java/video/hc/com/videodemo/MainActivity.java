package video.hc.com.videodemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import video.hc.com.videodemo.upload.HttpService;
import video.hc.com.videodemo.utils.TimeUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnVideoSizeChangedListener {
    @BindView(R.id.video)
    SurfaceView mSurfaceView;
    @BindView(R.id.button_play)
    Button bt_play;
    @BindView(R.id.button_pause)
    Button bt_pause;
    @BindView(R.id.video_progress)
    SeekBar seekBar;
    @BindView(R.id.video_time)
    TextView videoTime;
    @BindView(R.id.start_time)
    TextView startTime;
    @BindView(R.id.button)
    Button button;

    private static int currentPosition = 0;
    private SurfaceHolder surfaceHolder;
    private String[] mPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private int surfaceWidth, surfaceHeight;
    MediaPlayer mediaPlayer;
    ArrayList<Map<String, String>> urlList = new ArrayList();
    private static int mPrecent = 0;
    private static boolean lunboFlag = false;
    private static int mapPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, mPermissions, 10);
        }
        urlList = HttpService.getUrlList();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initView();
        initEvent();
    }

    private void initView() {
        mediaPlayer = new MediaPlayer();
        surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.i("lylog", "SurfaceHolder 创建");
                playVideo(urlList.get(mapPosition).get("url"));
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.i("lylog", "SurfaceHolder 变化时");
                surfaceWidth = width;
                surfaceHeight = height;
                mediaPlayer.setDisplay(surfaceHolder);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.i("lylog", "SurfaceHolder 被销毁");
//                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                    currentPosition = mediaPlayer.getCurrentPosition();
//                    mediaPlayer.pause();
//                }
            }
        });
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        initSeekBarColor();
    }

    private void initSeekBarColor() {
        LayerDrawable layerDrawable = (LayerDrawable)
                seekBar.getProgressDrawable();
        Drawable dra = layerDrawable.getDrawable(2);
        dra.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC);
        seekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
    }

    private void initEvent() {
        bt_play.setOnClickListener(this);
        bt_pause.setOnClickListener(this);
        button.setOnClickListener(this);

        mSurfaceView.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        mediaPlayer.setOnVideoSizeChangedListener(this);
    }

    private void playVideo(String url) {
        // 重置mediaPaly,建议在初始滑mediaplay立即调用。
        mediaPlayer.reset();
        // 设置声音效果
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // 设置播放完成监听
        mediaPlayer.setOnCompletionListener(this);

        // 设置媒体加载完成以后回调函数。
        mediaPlayer.setOnPreparedListener(this);
        // 错误监听回调函数
        mediaPlayer.setOnErrorListener(this);
        // 设置缓存变化监听
        mediaPlayer.setOnBufferingUpdateListener(this);
        //网络请求：
        try {
            mediaPlayer.setDataSource(url);
//            mediaPlayer.setDataSource(path);
            // 设置异步加载视频，包括两种方式 prepare()同步，prepareAsync()异步
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_play:
                playButtonClick();
                break;
            case R.id.video:
                if (mediaPlayer.isPlaying()) {
                    bt_pause.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.VISIBLE);
                    videoTime.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        bt_pause.setVisibility(View.INVISIBLE);
                                        seekBar.setVisibility(View.INVISIBLE);
                                        videoTime.setVisibility(View.INVISIBLE);
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    bt_play.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.VISIBLE);
                    videoTime.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        bt_play.setVisibility(View.INVISIBLE);
                                        seekBar.setVisibility(View.INVISIBLE);
                                        videoTime.setVisibility(View.INVISIBLE);
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                break;
            case R.id.button_pause:
                bt_pause.setVisibility(View.INVISIBLE);
                bt_play.setVisibility(View.VISIBLE);
                currentPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
                break;
            case R.id.button:
                loadThirdWeb();
                break;
        }
    }

    private void loadThirdWeb() {
        Intent intent = new Intent();
        intent.setClass(this, ToolActivity.class);
        intent.putExtra("url", "http://www.baidu.com");
        startActivity(intent);
    }

    private void playButtonClick() {
        bt_play.setVisibility(View.INVISIBLE);
        seekBar.setVisibility(View.INVISIBLE);
        videoTime.setVisibility(View.INVISIBLE);
        play(currentPosition);
    }

    private void play(int currentPosition) {
        Log.i("lylog", "currentPosition =  " + currentPosition);
        mediaPlayer.seekTo(currentPosition);
        mediaPlayer.start();
    }

    private void resizeSurfaceView() {
        int w = mediaPlayer.getVideoWidth();
        int h = mediaPlayer.getVideoHeight();
        Display display = getWindowManager().getDefaultDisplay();
        int screenW = display.getWidth();
        int screenH = (int) (screenW * (((float) h) / w));
        Log.i("lylog", " media w = " + w + "  h =" + h + " screeb w = " + screenW + " h = " + screenH);
        mSurfaceView.getHolder().setFixedSize(screenW, screenH);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mapPosition++;
        if (urlList.size() > mapPosition) {
            lunboFlag = true;
            playVideo(urlList.get(mapPosition).get("url"));
        } else {
            mapPosition = 0;
            playVideo(urlList.get(mapPosition).get("url"));
            mediaPlayer.pause();
            bt_play.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d("lylog", " onPrepared");
        getVideoTime();
        resizeSurfaceView();
        if (lunboFlag) {
            mp.start();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.d("lylog", "percent = " + percent);
        mPrecent = percent;
        seekBar.setProgress(percent);
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        // changeVideoSize();
    }

    public void changeVideoSize() {
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();

        //根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。
        float max;
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            //竖屏模式下按视频宽度计算放大倍数值
            max = Math.max((float) videoWidth / (float) surfaceWidth, (float) videoHeight / (float) surfaceHeight);
        } else {
            //横屏模式下按视频高度计算放大倍数值
            max = Math.max(((float) videoWidth / (float) surfaceHeight), (float) videoHeight / (float) surfaceWidth);
        }

        //视频宽高分别/最大倍数值 计算出放大后的视频尺寸
        videoWidth = (int) Math.ceil((float) videoWidth / max);
        videoHeight = (int) Math.ceil((float) videoHeight / max);

        //无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。
        mSurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(videoWidth, videoHeight));
    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Log.d("lylog"," progress111 = "+progress);

            if (fromUser) {

                int duration2 = mediaPlayer.getDuration();
                currentPosition = (int) (duration2 * ((float) progress) / 100);
                mediaPlayer.seekTo(currentPosition);
                bt_play.setVisibility(View.INVISIBLE);
                mediaPlayer.start();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    public void getVideoTime() {
        int duration2 = mediaPlayer.getDuration() / 1000;
        videoTime.setText(TimeUtils.calculateTime(duration2));
    }

    @Override
    protected void onDestroy() {
        Log.d("lylog", "onDestroy");
        mediaPlayer.release();
        lunboFlag = false;
        currentPosition = 0;
        mapPosition = 0;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mediaPlayer.pause();
        currentPosition = mediaPlayer.getCurrentPosition();
        super.onPause();
    }

    @Override
    protected void onResume() {
        bt_play.setVisibility(View.VISIBLE);
        Log.d("lylog", " mPrecent =" + mPrecent);

        seekBar.setProgress(mPrecent);
        mediaPlayer.seekTo(currentPosition);
        super.onResume();
    }
}
