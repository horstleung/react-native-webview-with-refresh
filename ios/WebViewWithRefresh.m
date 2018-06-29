//  Created by react-native-create-bridge
#import <Foundation/Foundation.h>
#import "WebViewWithRefresh.h"

// import RCTEventDispatcher
#if __has_include(<React/RCTEventDispatcher.h>)
#import <React/RCTEventDispatcher.h>
#elif __has_include(“RCTEventDispatcher.h”)
#import “RCTEventDispatcher.h”
#else
#import “React/RCTEventDispatcher.h” // Required when used as a Pod in a Swift project
#endif


@implementation WebViewWithRefresh : UIView  {
  RCTEventDispatcher *_eventDispatcher;
  UIRefreshControl *_refreshControl;
  UIWebView *_webView;
  NSString *_condition;
}

- (instancetype)initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher
{
  if ((self = [super init])) {
    __weak WebViewWithRefresh *weakSelf = self;
    _eventDispatcher = eventDispatcher;
    _webView = [[UIWebView alloc] init];
    _webView.delegate = weakSelf;
    _refreshControl = [[UIRefreshControl alloc] init];
    [_webView.scrollView addSubview:_refreshControl];
    [_refreshControl addTarget:self action:@selector(refresh) forControlEvents:UIControlEventValueChanged];
  }
  return self;
}

- (void)refresh {
  [_refreshControl endRefreshing];
  [_webView reload];
}

- (void)layoutSubviews {
  [super layoutSubviews];
  _webView.frame = self.bounds;
  [self addSubview:_webView];
}

- (void)goto:(NSString *)url {
  [self setUrl:url];
}


#pragma mark - PropTypes
-(void)setUrl:(NSString *)url {
  dispatch_async(dispatch_get_main_queue(), ^{
   [_webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:[url stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]]]];
  });
}

-(void)setMatchCondition:(NSString *)condition {
  _condition = condition;
}



#pragma mark - UIWebViewDelegate

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType {
  NSString* url = [request.URL absoluteString];
  if(!_onUrlMatch || !_condition){
    return YES;
  }
  
  NSRegularExpression *regEx = [[NSRegularExpression alloc] initWithPattern:_condition options:NSRegularExpressionCaseInsensitive error:nil];
  NSUInteger regExMatches = [regEx numberOfMatchesInString:url options:0 range:NSMakeRange(0, [url length])];
  
  if (regExMatches > 0) {
    _onUrlMatch(@{@"url": url, @"condition": _condition});
    return NO;
  }
  return YES;
}

@end
