package video.hc.com.videodemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ly on 2019/5/27.
 */

public class ToolActivity extends AppCompatActivity {
    @BindView(R.id.webview)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_show);
        ButterKnife.bind(this);
        initwebview();
    }

    private void initwebview() {
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
        finish();
    }
}
