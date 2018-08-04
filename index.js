import PropTypes from "prop-types";
import React from "react";
import { requireNativeComponent, View } from "react-native";

class WebViewWithRefreshView extends React.Component {
  _onUrlMatch = event => {
    console.log(event.nativeEvent);
    if (!this.props.onUrlMatch) {
      return;
    }
    this.props.onUrlMatch(event.nativeEvent);
  };

  _onStartLoad = event => {
    console.log(event.nativeEvent);
    if (!this.props.onStartLoad) {
      return;
    }
    this.props.onStartLoad(event.nativeEvent);
  };

  _onFinishLoad = event => {
    console.log(event.nativeEvent);
    if (!this.props.onFinishLoad) {
      return;
    }
    this.props.onFinishLoad(event.nativeEvent);
  };

  render() {
    return (
      <WebViewWithRefresh
        {...this.props}
        onUrlMatch={this._onUrlMatch}
        onStartLoad={this._onStartLoad}
        onFinishLoad={this._onFinishLoad}
      />
    );
  }
}

WebViewWithRefreshView.propTypes = {
  url: PropTypes.string,
  matchCondition: PropTypes.string,
  onUrlMatch: PropTypes.func,
  onStartLoad: PropTypes.func,
  onFinishLoad: PropTypes.func,
  ...View.propTypes
};

const WebViewWithRefresh = requireNativeComponent(
  "WebViewWithRefresh",
  WebViewWithRefreshView
);

module.exports = WebViewWithRefreshView;
