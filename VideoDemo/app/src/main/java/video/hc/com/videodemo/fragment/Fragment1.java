package video.hc.com.videodemo.fragment;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

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

/**
 * Created by ly on 2019/6/6.
 */

public class Fragment1 extends BaseFragment implements View.OnClickListener, HttpService.HttpServiceResult, HttpService.HttpServiceLX {

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
    Button bt_pre;
    @BindView(R.id.bt_next)
    Button bt_next;

    GridViewAdapter gridViewAdapter;
    private int position;
    FragmentCallback callback;
    ArrayList<Map<String, String>> listLXdata = new ArrayList<>();
    ArrayList<Map<String, String>> maplist = new ArrayList<>();

    public interface FragmentCallback {
        void urlCallback(String url);
    }

    @Override
    public View getLayoutView(LayoutInflater inflater, ViewGroup container) {
        Log.d("lylogss", "getLayoutView ");
        return inflater.inflate(R.layout.fragment1, null);
    }

    @Override
    public void initView() {
        Log.d("lylog", "initView");
        listLXdata = HttpService.getInstance().getLXdata(this);

        Map<String, String> map = new HashMap<>();
        map.put("type", 1 + "");
        map.put("curPage", 1 + "");
        HttpService.getInstance().getURLData(map, this);

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
                    if (scrollY == 0) {
                        Log.d("lylog", "顶端");
                    }
                    if ((scrollY + height) == scrollViewMeasuredHeight) {
                        Log.d("lylog", "底端");
                    }
                }
                return false;
            }
        });
        bt_pre.setOnClickListener(this);
        bt_next.setOnClickListener(this);
        gv_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String url = maplist.get(position).get("url");
                Log.d("lylog11", "  position =" + position);
                Log.d("lylog11", "  url =" + url);
                callback.urlCallback(url);
            }
        });
    }

    private void addview(RadioGroup radioGroup) {
        List<String> list = getListSize();
        Log.d("lylog1", " mapLXlist = " + list.toString());
        for (int i = 0; i < list.size(); i++) {
            RadioButton button = new RadioButton(getContext());
            button.setButtonDrawable(0);

            final String s = list.get(i).toString();
            button.setGravity(Gravity.CENTER);
            button.setTextSize(10);
            button.setWidth(206);
            button.setHeight(67);
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
            Log.d("lylog", "w h" + radioButton.getWidth() + "  " + radioButton.getHeight());
        } else {
            radioButton.setTextColor(Color.WHITE);
            radioButton.setBackground(null);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_pre:
                break;
            case R.id.bt_next:
                break;
        }
    }

    @Override
    public void success(Response response) {
        String result = null;
        try {
            result = response.body().string();
            maplist = JsonUtils.getIncetence().getMapData(result);
            Log.d("lylog1", "maplist =" + maplist.size());
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
                    break;
                case RESULT_LX_SUCESS:
                    addview(radioGroup);
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback =(FragmentCallback) getActivity();
    }
}
