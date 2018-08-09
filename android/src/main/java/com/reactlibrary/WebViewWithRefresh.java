package com.reactlibrary;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.graphics.Bitmap; 

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.reactlibrary.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// The callback interface
interface MyWebViewClientCallback {
    void webClientCallback(String url, String condition, String currentURL);
    void onStartLoad(String url);
    void onFinishLoad(String url);
}
class MyWebViewClient extends WebViewClient {
    public String condition;
    MyWebViewClientCallback delegate;

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if(TextUtils.isEmpty(url) || TextUtils.isEmpty(condition) ) {
            return super.shouldOverrideUrlLoading(view, url);
        }
        Pattern mPattern = Pattern.compile(condition);

        Matcher matcher = mPattern.matcher(url);
        if(matcher.find())
        {
            delegate.webClientCallback(url, condition, view.getUrl());
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

   @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        delegate.onStartLoad(url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        delegate.onFinishLoad(url);
    }
}

public class WebViewWithRefresh extends RelativeLayout {

    private Context context;

    WebView wv;
    SwipeRefreshLayout swipe;
    MyWebViewClient webClient;
    public WebViewWithRefresh(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public void init() {
        inflate(this.context, R.layout.web_view_with_refresh, this);
         wv = (WebView) findViewById(R.id.webView);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDisplayZoomControls(false);
        webClient = new MyWebViewClient();
        webClient.delegate = new MyWebViewClientCallback() {
            @Override
            public void webClientCallback(String url, String condition, String currentURL) {
                WritableMap event = Arguments.createMap();
                event.putString("url", url);
                event.putString("condition", condition);
                event.putString("currentURL", currentURL);
                ReactContext reactContext = (ReactContext)getContext();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                        getId(),
                        "onUrlMatch",
                        event);
            }

            @Override
            public void onStartLoad(String url) {
                WritableMap event = Arguments.createMap();
                event.putString("url", url);
                ReactContext reactContext = (ReactContext)getContext();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                        getId(),
                        "onStartLoad",
                        event);
            }

            @Override
            public void onFinishLoad(String url) {
                WritableMap event = Arguments.createMap();
                event.putString("url", url);
                ReactContext reactContext = (ReactContext)getContext();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                        getId(),
                        "onFinishLoad",
                        event);
            }
        };
        wv.setWebViewClient(webClient);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        wv.reload();
                        swipe.setRefreshing(false);
                    }
                }
        );
    }

    public void setCondition(String condition) {
        webClient.condition = condition;
    }

    public void goTo(String url) {
        WebView wv = (WebView) findViewById(R.id.webView);
        wv.loadUrl(url);
    }


}