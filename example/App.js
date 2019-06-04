/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from "react";
import { StyleSheet, View, NativeModules, findNodeHandle } from "react-native";
const WebViewFunctions = NativeModules.WebViewWithRefreshView;

import WebViewWithRefresh from "react-native-webview-with-refresh";

export default class App extends Component {
  onUrlChange = event => {
    console.warn("onUrlChange: %s", event.url);
  };

  onStartLoad = event => {
    console.warn("onStartLoad: %s", event.url);
  };

  onFinishLoad = async event => {
    console.warn("onFinishLoad: %s", event.url);
    var res = await WebViewFunctions.backPressed(findNodeHandle(this.refs.webview));
    console.warn(res);
  };

  render() {
    return (
      <View style={styles.container}>
        <WebViewWithRefresh
          url="http://mobileapi.metroradio.com.hk/MetroMobile/mobile/default.aspx"
          matchCondition="(http|https)://starfans.info"
          onUrlMatch={this.onUrlChange}
          onStartLoad={this.onStartLoad}
          onFinishLoad={this.onFinishLoad}
          style={styles.webview}
          ref="webview"
        />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#F5FCFF"
  },
  welcome: {
    fontSize: 20,
    textAlign: "center",
    margin: 10
  },
  instructions: {
    textAlign: "center",
    color: "#333333",
    marginBottom: 5
  },
  webview: {
    flex: 1,
    backgroundColor: "#000000",
    alignSelf: "stretch"
  }
});
