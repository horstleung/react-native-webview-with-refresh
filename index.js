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
  render() {
    return <WebViewWithRefresh {...this.props} onUrlMatch={this._onUrlMatch} />;
  }
}

WebViewWithRefreshView.propTypes = {
  url: PropTypes.string,
  matchCondition: PropTypes.string,
  onUrlMatch: PropTypes.func,
  ...View.propTypes
};

const WebViewWithRefresh = requireNativeComponent(
  "WebViewWithRefresh",
  WebViewWithRefreshView
);

module.exports = WebViewWithRefreshView;
