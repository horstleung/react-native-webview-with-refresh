/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from "react";
import { StyleSheet, View } from "react-native";

import WebViewWithRefresh from "react-native-webview-with-refresh";

export default class App extends Component {
  onUrlChange = event => {
    alert(event.url);
    alert(event.condition);
  };

  render() {
    return (
      <View style={styles.container}>
        <WebViewWithRefresh
          url="https://google.com/"
          matchCondition="(http|https)://starfans.info"
          onUrlMatch={this.onUrlChange}
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
