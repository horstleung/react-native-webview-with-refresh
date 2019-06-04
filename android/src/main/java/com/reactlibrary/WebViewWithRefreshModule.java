package com.reactlibrary;

import android.util.Log;
import android.view.View;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.NativeViewHierarchyManager;
import com.facebook.react.uimanager.UIBlock;
import com.facebook.react.uimanager.UIManagerModule;

interface MyViewHandler {
    void handle(WebViewWithRefresh view);
}

public class WebViewWithRefreshModule extends ReactContextBaseJavaModule {
    public static final String TAG = WebViewWithRefreshModule.class.getSimpleName();

    public WebViewWithRefreshModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "WebViewWithRefreshView";
    }

    @ReactMethod
    public void backPressed(final int viewId, final Promise promise) {
        withMyView(viewId, promise, new MyViewHandler() {
            @Override
            public void handle(WebViewWithRefresh view) {
                Boolean value = view.backPressed();
                promise.resolve(value);
            }
        });
    }

    private void withMyView(final int viewId, final Promise promise, final MyViewHandler handler) {
        UIManagerModule uiManager = getReactApplicationContext().getNativeModule(UIManagerModule.class);
        uiManager.addUIBlock(new UIBlock() {
            @Override
            public void execute(NativeViewHierarchyManager nativeViewHierarchyManager) {
                View view = nativeViewHierarchyManager.resolveView(viewId);
                if (view instanceof WebViewWithRefresh) {
                    WebViewWithRefresh myView = (WebViewWithRefresh) view;
                    handler.handle(myView);
                }
                else {
                    Log.e(TAG, "Expected view to be instance of MyView, but found: " + view);
                    promise.reject("my_view", "Unexpected view type");
                }
            }
        });
    }
}
