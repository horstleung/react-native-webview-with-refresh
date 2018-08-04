//  Created by react-native-create-bridge

package com.reactlibrary;

import android.support.annotation.Nullable;
import android.view.View;

import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import com.facebook.react.uimanager.annotations.ReactProp;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Map;

public class WebViewWithRefreshManager extends SimpleViewManager<View> {
    public static final String REACT_CLASS = "WebViewWithRefresh";

    @Override
    public String getName() {
        // Tell React the name of the module
        // https://facebook.github.io/react-native/docs/native-components-android.html#1-create-the-viewmanager-subclass
        return REACT_CLASS;
    }

    @Override
    public WebViewWithRefresh createViewInstance(ThemedReactContext context){
        // Create a view here
        // https://facebook.github.io/react-native/docs/native-components-android.html#2-implement-method-createviewinstance
        WebViewWithRefresh wv = new WebViewWithRefresh(context);
        return wv;
    }

    @ReactProp(name = "url")
    public void setUrl(WebViewWithRefresh view, String prop) {
        // Set properties from React onto your native component via a setter method
        // https://facebook.github.io/react-native/docs/native-components-android.html#3-expose-view-property-setters-using-reactprop-or-reactpropgroup-annotation
        view.goTo(prop);
    }

    @ReactProp(name = "matchCondition")
    public void setMatchCondition(WebViewWithRefresh view, String condition) {
        view.setCondition(condition);
    }

    @Override
    public @Nullable
    Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
            .put("onUrlMatch",
                    MapBuilder.of("registrationName", "onUrlMatch"))
            .put("onStartLoad",
                    MapBuilder.of("registrationName", "onStartLoad"))
            .put("onFinishLoad",
                    MapBuilder.of("registrationName", "onFinishLoad"))
            .build();
    }



}
