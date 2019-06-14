package video.hc.com.videodemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import video.hc.com.videodemo.base.BeseActivity;

/**
 * Created by ly on 2019/5/27.
 */

public class ToolActivity extends BeseActivity implements View.OnClickListener {
    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_show);
        ButterKnife.bind(this);
        initwebview();
        initEvent();
    }

    private void initEvent() {

        findViewById(R.id.bt_back).setOnClickListener(this);
        findViewById(R.id.bt_cancel).setOnClickListener(this);
    }

    private void initwebview() {
        String url = getIntent().getStringExtra("url");
//        webView.loadUrl(url);
//        finish();
        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        webview.loadUrl(url);
        //设置Web视图
        webview.setWebViewClient(new webViewClient());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_back:
                finish();
               break;
            case R.id.bt_cancel:
                if (webview.canGoBack()) {
                    webview.goBack();
                }else{
                    finish();
                }
                break;
        }
    }

    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        finish();//结束退出程序
        return false;
    }
}
