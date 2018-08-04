//  Created by react-native-create-bridge

// import UIKit so you can subclass off UIView
#import <UIKit/UIKit.h>
#import <React/RCTComponent.h>
#import <WebKit/WebKit.h>
@class RCTEventDispatcher;

@interface WebViewWithRefresh : UIView<WKNavigationDelegate>
  // Define view properties here with @property
  @property (nonatomic, assign) NSString *url;
  @property (nonatomic, assign) NSString *matchCondition;
  @property (nonatomic, copy) RCTDirectEventBlock onUrlMatch;
  @property (nonatomic, copy) RCTDirectEventBlock onStartLoad;
  @property (nonatomic, copy) RCTDirectEventBlock onFinishLoad;


  // Initializing with the event dispatcher allows us to communicate with JS
  - (instancetype)initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher NS_DESIGNATED_INITIALIZER;
  - (void) goto: (NSString*) url;
@end
