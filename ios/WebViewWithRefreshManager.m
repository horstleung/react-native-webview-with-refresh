//  Created by react-native-create-bridge

#import <Foundation/Foundation.h>
#import "WebViewWithRefresh.h"
#import "WebViewWithRefreshManager.h"

// import RCTBridge
#if __has_include(<React/RCTBridge.h>)
#import <React/RCTBridge.h>
#elif __has_include(“RCTBridge.h”)
#import “RCTBridge.h”
#else
#import “React/RCTBridge.h” // Required when used as a Pod in a Swift project
#endif


@implementation WebViewWithRefreshManager {
  WebViewWithRefresh *_wv;
}

@synthesize bridge = _bridge;

// Export a native module
// https://facebook.github.io/react-native/docs/native-modules-ios.html
RCT_EXPORT_MODULE();

// Return the native view that represents your React component
- (UIView *)view
{
  _wv = [[WebViewWithRefresh alloc] initWithEventDispatcher:self.bridge.eventDispatcher];
  return _wv;
}
//
RCT_EXPORT_VIEW_PROPERTY(url, NSString)
RCT_EXPORT_VIEW_PROPERTY(matchCondition, NSString)
RCT_EXPORT_VIEW_PROPERTY(onUrlMatch, RCTBubblingEventBlock)
RCT_EXPORT_METHOD(goTo:(NSString*)url) {
  [_wv goto:url];
}

@end




