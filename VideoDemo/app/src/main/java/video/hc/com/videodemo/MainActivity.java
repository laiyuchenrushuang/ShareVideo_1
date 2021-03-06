package video.hc.com.videodemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;
import video.hc.com.videodemo.adapter.MyPagerAdapter;
import video.hc.com.videodemo.base.BaseFragment;
import video.hc.com.videodemo.base.BeseActivity;
import video.hc.com.videodemo.fragment.Fragment1;
import video.hc.com.videodemo.fragment.Fragment2;
import video.hc.com.videodemo.fragment.Fragment3;
import video.hc.com.videodemo.upload.HttpService;
//import video.hc.com.videodemo.utils.SurfaceViewOutlineProviderUtil;
import video.hc.com.videodemo.utils.SurfaceViewOutlineProviderUtil;
import video.hc.com.videodemo.utils.TimeUtils;
import video.hc.com.videodemo.utils.Utils;
import video.hc.com.videodemo.view.MySurfaceView;

public class MainActivity extends BeseActivity implements Fragment1.FragmentCallback, View.OnClickListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, MySurfaceView.GestrueListener, HttpService.HttpAllCallback {
    @BindView(R.id.video)
    MySurfaceView mSurfaceView;
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

    @BindView(R.id.iv_publicity)
    TextView iv_publicity;
    @BindView(R.id.iv_experience)
    TextView iv_experience;

    @BindView(R.id.viewpager)
    ViewPager vp_video;
    @BindView(R.id.scan_progress)
    LinearLayout scan_progress;

    @BindView(R.id.framelaout)
    FrameLayout fragment;

    private static int currentPosition = 0;
    private SurfaceHolder surfaceHolder;
    MediaPlayer mediaPlayer;
//    ArrayList<Map<String, String>> urlList = new ArrayList();
    int[] ivProgresslist = {R.mipmap.progress1, R.mipmap.progress2, R.mipmap.progress3, R.mipmap.progress4, R.mipmap.progress5};
    int ivProgressFlag = 0;
    private static int mPrecent = 0;
    private static boolean lunboFlag = false;
    private static int mapPosition = 0;
    private MyPagerAdapter vpAdapter;
    public static ArrayList<Map<String, String>> listdata;

    String uid,token;
    ArrayList<String> urlist = new ArrayList<>();

    private static String beifenTrueUrl = null;
    private boolean progressFlag = true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        urlList = HttpService.getUrlList();
        HttpService.getInstance().getAllurlonline(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mSurfaceView.registerListener(this);

        initView();
        initEvent();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        mediaPlayer = new MediaPlayer();
        surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.i("lylog", "SurfaceHolder 创建");
//                playVideo(urlList.get(mapPosition).get("url"));
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
                    mediaPlayer.pause();
                }
            }
        });
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        image_progress.setImageResource(ivProgresslist[ivProgressFlag]);

        mSurfaceView.setOutlineProvider(new SurfaceViewOutlineProviderUtil(30));
        mSurfaceView.setClipToOutline(true);

        initSeekBarColor();
        FragmentManager fm = getSupportFragmentManager();
        ArrayList<Fragment> listf = new ArrayList<>();
        Fragment1 f1 = new Fragment1();
        Fragment3 f3 = new Fragment3();
        listf.add(f1);
        listf.add(f3);

        vpAdapter = new MyPagerAdapter(fm, listf);
        vp_video.setAdapter(vpAdapter);

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
        iv_publicity.setOnClickListener(this);
        iv_experience.setOnClickListener(this);

        vp_video.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    showbuttonBgPublicity();
                } else if (position == 1) {
                    showbuttonBgExperience();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
                Utils.canPlay = true;
                Log.i("lylog1"," beifen ="+beifenTrueUrl);
                playVideo(beifenTrueUrl);
                break;
            case R.id.video:
                //doshowProgressState();
                break;
            case R.id.button_pause:
                bt_pause.setVisibility(View.INVISIBLE);
                bt_play.setVisibility(View.VISIBLE);
                currentPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
                break;
            case R.id.button:
                mediaPlayer.stop();
                lunboFlag = false;
                loadThirdWeb();
                break;
            case R.id.iv_error:
                HttpService.getInstance().getTrueUrl(mHandler,getApplicationContext(),uid,token,urlist.get(mapPosition).replace("\"",""));
                mediaPlayer.seekTo(currentPosition);
                break;
            case R.id.iv_experience:
                showbuttonBgExperience();

                vp_video.setCurrentItem(1);
                break;
            case R.id.iv_publicity:
                showbuttonBgPublicity();
                vp_video.setCurrentItem(0);
                break;
        }
    }

    private void showbuttonBgPublicity() {
        iv_publicity.setBackground(getResources().getDrawable(R.mipmap.bt_clicked));
        iv_experience.setBackground(getResources().getDrawable(R.mipmap.bt_click));
    }

    private void showbuttonBgExperience() {
        iv_experience.setBackground(getResources().getDrawable(R.mipmap.bt_clicked));
        iv_publicity.setBackground(getResources().getDrawable(R.mipmap.bt_click));
    }

    private void loadThirdWeb() {
        Intent intent = new Intent();
        intent.setClass(this, NewActivity.class);
        intent.putExtra("url", "http://jtzzlm.cdjg.chengdu.gov.cn/cdjj/newcwtapi/newindex?id=888&code=oSthY1d1ccDM1JPaQrLDsihxLbZ&state=STATE");
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
            //播完一集，下一集从0开始
            if (!mp.isPlaying()) {
                currentPosition = 0;
            }

            ivProgressFlag++;
            image_progress.setImageResource(ivProgresslist[ivProgressFlag]);//轮询视频进度，然而卵用的功能
            if (ivProgressFlag == 4) {
                image_progress.setImageResource(ivProgresslist[ivProgressFlag]);
                ivProgressFlag = -1;
            }
            mapPosition++;
            if (urlist.size() > mapPosition && null != urlist.get(mapPosition).toString()) {
                lunboFlag = true;
                Log.d("lylog", "onCompletion mapPosition =" + mapPosition);
                HttpService.getInstance().getTrueUrl(mHandler,getApplicationContext(),uid,token,urlist.get(mapPosition).replace("\"",""));
//                playVideo(urlList.get(mapPosition).get("url"));
            } else {
                mapPosition = 0;
                HttpService.getInstance().getTrueUrl(mHandler,getApplicationContext(),uid,token,urlist.get(mapPosition).replace("\"",""));
//                bt_play.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Utils.canPlay = true;
        gif_networkwait.setVisibility(View.INVISIBLE);
        iv_error.setVisibility(View.GONE);
        scan_progress.setVisibility(View.INVISIBLE);//++
        int duration2 = mediaPlayer.getDuration() / 1000;
        seekBar.setMax(duration2);
        //bt_play.setVisibility(View.VISIBLE);
        Log.i("lylog", " onPrepared");
        getVideoTime();
        resizeSurfaceView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressFlag){
                    int position = mediaPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(position);
                }

            }
        }).start();
//        if (lunboFlag) {
//            bt_play.setVisibility(View.INVISIBLE);//轮播自动播放不需要播放按钮
//        mp.start();
//        }
        play(currentPosition);
        Log.i("lylog11", "onPrepared canPlay =" + Utils.canPlay);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.i("lylog11", "onError = " + what + " extra = " + extra);
        Utils.canPlay = false;
        if(extra == -2147483648){
            Message msg = new Message();
            msg.what = Utils.VIDEO_ERROR;
            msg.arg1 = extra;
            mHandler.sendMessage(msg);
        }
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (mp.isPlaying()) {
            iv_error.setVisibility(View.GONE);
        }
//        mPrecent = percent;
//        seekBar.setProgress(percent);
//        int currentTime = mp.getCurrentPosition();
//        Log.d("lylog", " percent = " + percent + " currentTime = " + currentTime + " total = " + mediaPlayer.getDuration());
//        String time = TimeUtils.calculateTime(currentTime / 1000);
//        startTime.setText(time);
    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                mediaPlayer.seekTo(progress * 1000);
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
        Log.i("lylog1", "onDestroy");
        mediaPlayer.release();
        lunboFlag = false;
        currentPosition = 0;
        mapPosition = 0;
        mPrecent = 0;
        progressFlag = false;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d("lylog1", " onPause ");
        currentPosition = mediaPlayer.getCurrentPosition();
        mediaPlayer.stop();
        progressFlag = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bt_play.setVisibility(View.VISIBLE);
        Log.i("lylog1", " onResume mPrecent =" + mPrecent);
       // playVideo(beifenTrueUrl);
        seekBar.setProgress(mPrecent);
        progressFlag = true;
        mediaPlayer.seekTo(currentPosition);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Utils.VIDEO_ERROR:
                    iv_error.setVisibility(View.VISIBLE);
                    bt_play.setVisibility(View.INVISIBLE);
                    gif_networkwait.setVisibility(View.INVISIBLE);
                    break;
                case Utils.VIDEO_PRE:
                    Log.d("lylogsss", " VIDEO_PRE");
                    if (mapPosition > 0) {
                        mapPosition--;
                    } else {
                        mapPosition = 0;
                    }
                    HttpService.getInstance().getTrueUrl(mHandler,getApplicationContext(),uid,token,urlist.get(mapPosition).replace("\"",""));
                    if (ivProgressFlag > 0) {
                        ivProgressFlag--;
                    } else {
                        ivProgressFlag = 0;
                    }
                    image_progress.setImageResource(ivProgresslist[ivProgressFlag]);
                    break;
                case Utils.VIDEO_NEXT:
                    Log.d("lylogsss", " VIDEO_NEXT");
                    if (mapPosition < urlist.size() - 1) {
                        mapPosition++;
                    } else {
                        mapPosition = urlist.size() - 1;
                    }
                    HttpService.getInstance().getTrueUrl(mHandler,getApplicationContext(),uid,token,urlist.get(mapPosition).replace("\"",""));

                    if (ivProgressFlag == 4) {
                        ivProgressFlag = 0;
                    } else {
                        ivProgressFlag++;
                    }
                    image_progress.setImageResource(ivProgresslist[ivProgressFlag]);
                    break;
                case Utils.VIDEO_ONCLICK:
                    doshowProgressState();
                    break;
                case Utils.FRAGMENT_CALLBACK:
                    String url = (String) msg.obj;
                    playVideo(url);
                    break;
                case  Utils.URL_SUCCESS:
                    String trueurl = (String) msg.obj;
                    beifenTrueUrl = trueurl;
                    Log.i("lylogsss", " URL_SUCCESS trueurl ="+trueurl);
                    playVideo(trueurl);
            }

        }
    };

    private void doshowProgressState() {
        Log.d("lylogsss", " VIDEO_ONCLICK");
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
        } else if (Utils.canPlay) {
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
        } else {
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
    }

    @Override
    public void gestrueDerection(int firstX, int endX) {
        if (Math.abs((firstX - endX)) > Utils.SLIDE_SCALE && firstX < endX) {
            Message msg = new Message();
            msg.what = Utils.VIDEO_PRE;
            mHandler.sendMessage(msg);
        } else if (Math.abs((firstX - endX)) > Utils.SLIDE_SCALE && firstX > endX) {
            Message msg = new Message();
            msg.what = Utils.VIDEO_NEXT;
            mHandler.sendMessage(msg);
        } else if (Utils.ONCLICK_SCALE_MIN < Math.abs((firstX - endX)) && Math.abs((firstX - endX)) < Utils.ONCLICK_SCALE_MAX) {
            Message msg = new Message();
            msg.what = Utils.VIDEO_ONCLICK;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void urlCallback(String url) {
        currentPosition = 0;
        iv_error.setVisibility(View.GONE);
        Message msg = new Message();
        msg.obj = url;
        msg.what = Utils.FRAGMENT_CALLBACK;
        mHandler.sendMessage(msg);
    }

    @Override
    public void tokenCallback(String uid, String token, ArrayList<String> url) {
        this.uid = uid;
        this.token = token;
        this.urlist = url;
        HttpService.getInstance().getTrueUrl(mHandler,getApplicationContext(),uid,token,urlist.get(mapPosition).replace("\"",""));
    }
}
