package com.reactlibrary;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.graphics.Bitmap; 

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.reactlibrary.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// The callback interface
interface MyWebViewClientCallback {
    void webClientCallback(String url, String condition);
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
            delegate.webClientCallback(url, condition);
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
    private FrameLayout customViewContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private View mCustomView;
    private MyWebChromeClient mWebChromeClient;

    public WebViewWithRefresh(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public void init() {
        inflate(this.context, R.layout.web_view_with_refresh, this);
         wv = (WebView) findViewById(R.id.webView);
        customViewContainer = (FrameLayout) findViewById(R.id.customViewContainer);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDisplayZoomControls(false);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.getSettings().setPluginState(WebSettings.PluginState.ON);
        wv.getSettings().setMediaPlaybackRequiresUserGesture(false);
        wv.setWebChromeClient(new MyWebChromeClient());
        webClient = new MyWebViewClient();
        webClient.delegate = new MyWebViewClientCallback() {
            @Override
            public void webClientCallback(String url, String condition) {
                WritableMap event = Arguments.createMap();
                event.putString("url", url);
                event.putString("condition", condition);
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

    public Boolean backPressed(){

        new AlertDialog.Builder(this.getContext())
                .setTitle("Your Alert")
                .setMessage("Your Message")
                .setCancelable(true).show();
        return true;
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
            onShowCustomView(view, callback);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void onShowCustomView(View view,CustomViewCallback callback) {

            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            wv.setVisibility(View.GONE);
            customViewContainer.setVisibility(View.VISIBLE);
            customViewContainer.addView(view);
            customViewCallback = callback;
        }


        @Override
        public void onHideCustomView() {
            super.onHideCustomView();    //To change body of overridden methods use File | Settings | File Templates.
            if (mCustomView == null)
                return;

            wv.setVisibility(View.VISIBLE);
            customViewContainer.setVisibility(View.GONE);

            // Hide the custom view.
            mCustomView.setVisibility(View.GONE);

            // Remove the custom view from its container.
            customViewContainer.removeView(mCustomView);
            customViewCallback.onCustomViewHidden();

            mCustomView = null;
        }
    }


}