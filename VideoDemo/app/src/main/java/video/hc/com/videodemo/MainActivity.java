package video.hc.com.videodemo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;
import video.hc.com.videodemo.base.BeseActivity;
import video.hc.com.videodemo.upload.HttpService;
import video.hc.com.videodemo.utils.SurfaceViewOutlineProviderUtil;
import video.hc.com.videodemo.utils.TimeUtils;
import video.hc.com.videodemo.utils.Utils;

public class MainActivity extends BeseActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener {
    @BindView(R.id.video)
    SurfaceView mSurfaceView;
    @BindView(R.id.button_play)
    ImageView bt_play;
    @BindView(R.id.button_pause)
    ImageView bt_pause;
    @BindView(R.id.video_progress)
    SeekBar seekBar;
    @BindView(R.id.video_time)
    TextView videoTime;
    @BindView(R.id.start_time)
    TextView startTime;
    @BindView(R.id.button)
    ImageView button;

    @BindView(R.id.image_progress)
    ImageView image_progress;
    @BindView(R.id.gif_networkwait)
    GifImageView gif_networkwait;
    @BindView(R.id.iv_error)
    LinearLayout iv_error;
    @BindView(R.id.txt_error)
    TextView txt_error;

    @BindView(R.id.scan_progress)
    LinearLayout scan_progress;
    private static int currentPosition = 0;
    private SurfaceHolder surfaceHolder;
    MediaPlayer mediaPlayer;
    ArrayList<Map<String, String>> urlList = new ArrayList();
    int[] ivProgresslist = {R.mipmap.progress1, R.mipmap.progress2, R.mipmap.progress3, R.mipmap.progress4, R.mipmap.progress5};
    int ivProgressFlag = 0;
    private static int mPrecent = 0;
    private static boolean lunboFlag = false;
    private static int mapPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        urlList = HttpService.getUrlList();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initView();
        initEvent();
    }

    private void initView() {
//        setDataIVProgress();
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
                mediaPlayer.setDisplay(surfaceHolder);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.i("lylog", "SurfaceHolder 被销毁");
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    currentPosition = mediaPlayer.getCurrentPosition();
                    mediaPlayer.stop();
                }
            }
        });
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        image_progress.setImageResource(ivProgresslist[ivProgressFlag]);
        mSurfaceView.setOutlineProvider(new SurfaceViewOutlineProviderUtil(30));
        mSurfaceView.setClipToOutline(true);
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
        iv_error.setOnClickListener(this);
        //mediaPlayer.setOnVideoSizeChangedListener(this);
    }

    private void playVideo(String url) {
        // 重置mediaPaly,建议在初始滑mediaplay立即调用。
        mediaPlayer.reset();
        gif_networkwait.setVisibility(View.VISIBLE);
        bt_play.setVisibility(View.INVISIBLE);
        iv_error.setVisibility(View.INVISIBLE);
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
                    scan_progress.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        bt_pause.setVisibility(View.INVISIBLE);
                                        scan_progress.setVisibility(View.INVISIBLE);
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else if (Utils.canPlay){
                    bt_play.setVisibility(View.VISIBLE);
                    scan_progress.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        bt_play.setVisibility(View.INVISIBLE);
                                        scan_progress.setVisibility(View.INVISIBLE);
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else {
                    scan_progress.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        scan_progress.setVisibility(View.INVISIBLE);
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
                mediaPlayer.stop();
                loadThirdWeb();
                break;
            case R.id.iv_error:
                playVideo(urlList.get(mapPosition).get("url"));
                mediaPlayer.seekTo(currentPosition);
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
        scan_progress.setVisibility(View.INVISIBLE);
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
        mSurfaceView.getHolder().setFixedSize(screenW, screenH);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        if (Utils.canPlay) {
            ivProgressFlag++;
            image_progress.setImageResource(ivProgresslist[ivProgressFlag]);//轮询视频进度，然而卵用的功能
            if (ivProgressFlag == 4) {
                image_progress.setImageResource(ivProgresslist[ivProgressFlag]);
                ivProgressFlag = -1;
            }

            mapPosition++;
            if (urlList.size() > mapPosition) {
                lunboFlag = true;
                Log.d("lylog", "onCompletion mapPosition =" + mapPosition);
                playVideo(urlList.get(mapPosition).get("url"));
            } else {
                mapPosition = 0;
                playVideo(urlList.get(mapPosition).get("url"));
//                mediaPlayer.pause();
                bt_play.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        gif_networkwait.setVisibility(View.INVISIBLE);
        iv_error.setVisibility(View.INVISIBLE);
        bt_play.setVisibility(View.VISIBLE);
        Log.d("lylog", " onPrepared");
        getVideoTime();
        resizeSurfaceView();
        if (lunboFlag) {
            bt_play.setVisibility(View.INVISIBLE);//轮播自动播放不需要播放按钮
            mp.start();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.i("lylog", "onError = " + what + " extra = " + extra);
        Utils.canPlay = false;
        Message msg = new Message();
        msg.what = what;
        msg.arg1 = extra;
        mHandler.sendMessage(msg);
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        mPrecent = percent;
        seekBar.setProgress(percent);
        int currentTime = mp.getCurrentPosition();
        String time = TimeUtils.calculateTime(currentTime/1000);
        startTime.setText(time);
    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {

                int duration2 = mediaPlayer.getDuration();
                currentPosition = (int) (duration2 * ((float) progress) / 100);
                mediaPlayer.seekTo(currentPosition);
                bt_play.setVisibility(View.INVISIBLE);
                mediaPlayer.start();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    };

    public void getVideoTime() {
        int duration2 = mediaPlayer.getDuration() / 1000;
        videoTime.setText(TimeUtils.calculateTime(duration2));
    }

    @Override
    protected void onDestroy() {
        Log.d("lylog1", "onDestroy");
        mediaPlayer.release();
        lunboFlag = false;
        currentPosition = 0;
        mapPosition = 0;
        mPrecent = 0;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d("lylog1", " onPause ");
        currentPosition = mediaPlayer.getCurrentPosition();
        mediaPlayer.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bt_play.setVisibility(View.VISIBLE);
        Log.d("lylog1", " onResume mPrecent =" + mPrecent);
        seekBar.setProgress(mPrecent);
        mediaPlayer.seekTo(currentPosition);
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            iv_error.setVisibility(View.VISIBLE);
            bt_play.setVisibility(View.INVISIBLE);
            gif_networkwait.setVisibility(View.INVISIBLE);
        }
    };
}
