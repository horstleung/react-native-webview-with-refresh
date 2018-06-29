//  Created by react-native-create-bridge

// import UIKit so you can subclass off UIView
#import <UIKit/UIKit.h>
#import <React/RCTComponent.h>

@class RCTEventDispatcher;

@interface WebViewWithRefresh : UIView<UIWebViewDelegate>
  // Define view properties here with @property
  @property (nonatomic, assign) NSString *url;
  @property (nonatomic, assign) NSString *matchCondition;
  @property (nonatomic, copy) RCTDirectEventBlock onUrlMatch;

  // Initializing with the event dispatcher allows us to communicate with JS
  - (instancetype)initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher NS_DESIGNATED_INITIALIZER;
  - (void) goto: (NSString*) url;
@end
