package video.hc.com.videodemo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Response;
import video.hc.com.videodemo.MainActivity;
import video.hc.com.videodemo.R;
import video.hc.com.videodemo.adapter.GridViewAdapter;
import video.hc.com.videodemo.base.BaseFragment;
import video.hc.com.videodemo.upload.HttpService;
import video.hc.com.videodemo.utils.JsonUtils;
import video.hc.com.videodemo.utils.Utils;

/**
 * Created by ly on 2019/6/6.
 */

public class Fragment1 extends BaseFragment implements View.OnClickListener, HttpService.HttpServiceResult, HttpService.HttpServiceLX, HttpService.HttpTokenCallback {

    private static final int RESULT_SUCESS = 1;
    private static final int RESULT_LX_SUCESS = 2;
    @BindView(R.id.gv_video)
    GridView gv_video;
    @BindView(R.id.rb_video_lx)
    RadioGroup radioGroup;
    @BindView(R.id.sv_leixing)
    ScrollView sv_leixing;

    @BindView(R.id.tv_page_number)
    TextView tv_page_number;
    @BindView(R.id.bt_pre)
    ImageView bt_pre;
    @BindView(R.id.bt_next)
    ImageView bt_next;
    @BindView(R.id.up)
    ImageView iv_up;
    @BindView(R.id.down)
    ImageView iv_down;

    @BindView(R.id.fragment1)
    LinearLayout fragment1;

    GridViewAdapter gridViewAdapter;
    private int position;
    FragmentCallback callback;
    int count;
    int pageNo;
    ArrayList<Map<String, String>> listLXdata = new ArrayList<>();
    ArrayList<Map<String, String>> maplist = new ArrayList<>();
    String uid, token;

    ArrayList<String> urlList = new ArrayList<>();

    @Override
    public void tokenCallback(String uid, String token) {
        this.uid = uid;
        this.token = token;
        Map<String, String> map = new HashMap<>();
        map.put("type", 1 + "");
        map.put("curPage", 1 + "");
        HttpService.getInstance().getURLData(map, this);
    }

    @Override
    public void tokenError(String result) {

    }

    public interface FragmentCallback {
        void urlCallback(String url);
    }

    @Override
    public View getLayoutView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment1, null);
    }

    @Override
    public void initView() {
        listLXdata = HttpService.getInstance().getLXdata(this);
        HttpService.getInstance().getTokenData(this, getContext());

//        HttpService.getInstance().getPostData();
//        addview(radioGroup);
        initEvent();
    }

    private void initEvent() {
        sv_leixing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    int scrollY = view.getScrollY();
                    int height = view.getHeight();
                    int scrollViewMeasuredHeight = sv_leixing.getChildAt(0).getMeasuredHeight();
                    if (scrollY < 100) {
                        iv_up.setVisibility(View.INVISIBLE);
                        iv_down.setVisibility(View.VISIBLE);
                        Log.d("lylog", "顶端");
                    }
                    if ((scrollY + height) == scrollViewMeasuredHeight) {
                        iv_down.setVisibility(View.INVISIBLE);
                        iv_up.setVisibility(View.VISIBLE);
                        Log.d("lylog", "底端");
                    }
                    if ((scrollY + height) < scrollViewMeasuredHeight && scrollY > 100) {
                        iv_down.setVisibility(View.VISIBLE);
                        iv_up.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        });
        bt_pre.setOnClickListener(this);
        bt_next.setOnClickListener(this);
        iv_down.setOnClickListener(this);
        iv_up.setOnClickListener(this);
        gv_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("lylog"," onItemClick");
                String maplistURL = maplist.get(position).get("url");
                HttpService.getInstance().getTrueUrl(mHandler, getContext(), uid, token, maplistURL);
//               String url = urlList.get(position);
//               callback.urlCallback(url);
            }
        });
    }

    private void addview(RadioGroup radioGroup) {
        List<String> list = getListSize();

        Log.d("lylog1", " mapLXlist = " + list.toString());
        if (list.size() < 6) {
            iv_down.setVisibility(View.GONE);
            iv_up.setVisibility(View.GONE);
        }
        for (int i = 0; i < list.size(); i++) {
            RadioButton button = new RadioButton(getContext());
            button.setButtonDrawable(0);

            final String s = list.get(i).toString();
            button.setGravity(Gravity.CENTER);
            button.setTextSize(10);
            button.setBackground(getResources().getDrawable(R.mipmap.bg_lx_unclick));
            button.setTextColor(Color.parseColor("#00ffff"));
            button.setText(s);

            if (i == 0) {
                setBackground(true, button);
            }
            radioGroup.addView(button);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int position) {
                    maplist.clear();
                    Log.d("lylog", "onCheckedChanged i = " + position);
                    setIndex(position);
                    Map<String, String> map = new HashMap<>();
                    map.put("type", position + "");
                    map.put("curPage", 1 + "");
                    HttpService.getInstance().getURLData(map, Fragment1.this);

                    RadioButton radioButton = (RadioButton) (radioGroup.getChildAt(0));
                    if (position != 1) {
                        setBackground(false, radioButton);
                    }

                }
            });


            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        setBackground(true, (RadioButton) compoundButton);
                    } else {
                        setBackground(false, (RadioButton) compoundButton);
                    }
                }
            });
        }

    }

    private void setBackground(boolean flag, RadioButton radioButton) {
        if (flag) {
            radioButton.setTextColor(Color.YELLOW);
            radioButton.setBackground(getResources().getDrawable(R.mipmap.bg_lx_clicked));
        } else {
            radioButton.setTextColor(Color.parseColor("#00ffff"));
            radioButton.setBackground(getResources().getDrawable(R.mipmap.bg_lx_unclick));
        }
    }

    private void setIndex(int position) {
        this.position = position;
    }

    private int getIndex() {
        return this.position;
    }

    private List<String> getListSize() {
        List<String> list = new ArrayList<String>();
        for (Map<String, String> map : listLXdata) {
            list.add(map.get("lx"));
        }
        return list;
    }

    int time = 1;

    @Override
    public void onClick(View v) {
        int i = Math.abs(radioGroup.getCheckedRadioButtonId());
        switch (v.getId()) {
            case R.id.bt_pre:
                if (time > 1) {
                    time--;
                } else {
                    time = 1;
                }
                Map<String, String> map2 = new HashMap();

                map2.put("type", i + "");
                map2.put("curPage", time + "");
                HttpService.getInstance().getURLData(map2, this);
                break;
            case R.id.bt_next:
                if (time < count / 4) {
                    time++;
                } else {
                    time = (count + pageNo - 1) / 4;
                }
                Map<String, String> map1 = new HashMap();
                map1.put("type", i + "");
                map1.put("curPage", time + "");
                HttpService.getInstance().getURLData(map1, this);
                break;
            case R.id.up:
                sv_leixing.fullScroll(ScrollView.FOCUS_UP);
                iv_up.setVisibility(View.INVISIBLE);
                iv_down.setVisibility(View.VISIBLE);
                break;
            case R.id.down:
                sv_leixing.fullScroll(ScrollView.FOCUS_DOWN);
                iv_down.setVisibility(View.INVISIBLE);
                iv_up.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void success(Response response) {
        maplist.clear();
        String result = null;
        try {
            result = response.body().string();
            maplist = JsonUtils.getIncetence().getMapData(result);
            if (maplist.size() > 0) {
                count = Integer.valueOf(maplist.get(0).get("count"));
                pageNo = Integer.valueOf(maplist.get(0).get("pageNo"));

            } else {
                count = pageNo = 0;
            }

            Message msg = new Message();
            msg.what = RESULT_SUCESS;
            mHandler.sendMessage(msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void isOK(Response response) {
        String result = null;
        try {
            result = response.body().string();
            listLXdata = JsonUtils.getIncetence().getLXMapData(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message msg = new Message();
        msg.what = RESULT_LX_SUCESS;
        mHandler.sendMessage(msg);

    }

    @Override
    public void isNO(String result) {

    }

    @Override
    public void failed(String result) {

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RESULT_SUCESS:
                    gridViewAdapter = new GridViewAdapter(getContext(), maplist);
                    gv_video.setAdapter(gridViewAdapter);

                    if (0 < count && count < 10) {
                        count = 1;
                        tv_page_number.setText(pageNo + "/" + count);
                    } else if (count == 0) {
                        count = 0;
                        tv_page_number.setText(pageNo + "/" + count);
                    } else {
                        tv_page_number.setText(pageNo + "/" + (count + pageNo - 1) / 10);
                    }

                    break;
                case RESULT_LX_SUCESS:
                    addview(radioGroup);
                    break;
                case Utils.URL_SUCCESS:
                    String url = (String) msg.obj;
                    callback.urlCallback(url);
                    break;
                case Utils.URL_FAILED:
                    HttpService.getInstance().getTokenData(Fragment1.this, getContext());//再次请求正确的token
                    Utils.showToast(getContext(),"请求失败 继续请求");
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (FragmentCallback) getActivity();
    }
}
