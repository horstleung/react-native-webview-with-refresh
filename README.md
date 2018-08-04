# react-native-webview-with-refresh

## Getting started

`$ npm install react-native-webview-with-refresh --save`

### Mostly automatic installation

`$ react-native link react-native-webview-with-refresh`

### Manual installation

#### iOS

1.  In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2.  Go to `node_modules` ➜ `react-native-webview-with-refresh` and add `RNWebviewWithRefresh.xcodeproj`
3.  In XCode, in the project navigator, select your project. Add `libRNWebviewWithRefresh.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4.  Run your project (`Cmd+R`)<

#### Android

1.  Open up `android/app/src/main/java/[...]/MainActivity.java`

- Add `import com.reactlibrary.RNWebviewWithRefreshPackage;` to the imports at the top of the file
- Add `new RNWebviewWithRefreshPackage()` to the list returned by the `getPackages()` method

2.  Append the following lines to `android/settings.gradle`:
    ```
    include ':react-native-webview-with-refresh'
    project(':react-native-webview-with-refresh').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-webview-with-refresh/android')
    ```
3.  Insert the following lines inside the dependencies block in `android/app/build.gradle`:

    ```
      compile project(':react-native-webview-with-refresh')
    ```

## Parameters

| Parameter      | Usage                                                       |
| -------------- | ----------------------------------------------------------- |
| url            | the url loaded                                              |
| matchCondition | regex; would call onUrlMatch if the url meet this condition |
| onUrlMatch     | the callback when the url meet the condition                |
| onStartLoad    | the callback when the url start to load                     |
| OnFinishLoad   | the callback when the url loading is committed              |

## Usage

```javascript
import RNWebviewWithRefresh from "react-native-webview-with-refresh";
export default class App extends Component {
  onUrlChange = event => {
    alert(event.url);
    alert(event.condition);
  };

  onStartLoad = event => {
    console.warn("onStartLoad: %s", event.url);
  };

  onFinishLoad = event => {
    console.warn("onFinishLoad: %s", event.url);
  };

  render() {
    return (
      <View style={styles.container}>
        <WebViewWithRefresh
          url="https://www.google.com/"
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
```

## Warnning

For iOS, this lib accept all SSL cert as the WKWebview seems being rude to https://google.com/

## ChangeLog

1.1 switch to WKWebview for better performance; add onStartLoad and onFinishLoad callbacks.
