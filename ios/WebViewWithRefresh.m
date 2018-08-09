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
  WKWebView *_webView;
  NSString *_condition;
}

- (instancetype)initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher
{
  if ((self = [super init])) {
    __weak WebViewWithRefresh *weakSelf = self;
    _eventDispatcher = eventDispatcher;
    _webView = [[WKWebView alloc] init];
    _webView.navigationDelegate = weakSelf;
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



#pragma mark - WKNavigationDelegate

- (void)webView:(WKWebView *)webView decidePolicyForNavigationAction:(WKNavigationAction *)navigationAction decisionHandler:(void (^)(WKNavigationActionPolicy))decisionHandler {
    
  NSString *url = [[navigationAction.request URL] absoluteString];
  
  if(!_onUrlMatch || !_condition){
    decisionHandler(WKNavigationActionPolicyAllow);
    return;
  }
  NSRegularExpression *regEx = [[NSRegularExpression alloc] initWithPattern:_condition options:NSRegularExpressionCaseInsensitive error:nil];
  NSUInteger regExMatches = [regEx numberOfMatchesInString:url options:0 range:NSMakeRange(0, [url length])];
  NSString* currentURL = webView.URL ? webView.URL.absoluteString : @"";

  if (regExMatches > 0) {
    _onUrlMatch(@{@"url": url, @"condition": _condition, @"currentURL": currentURL});
    decisionHandler(WKNavigationActionPolicyCancel);
    return;
  }
  decisionHandler(WKNavigationActionPolicyAllow);
}

- (void)webView:(WKWebView *)webView didStartProvisionalNavigation:(null_unspecified WKNavigation *)navigation {
    NSLog(@"%s", __FUNCTION__);
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    if(!_onStartLoad){
        return;
    }
    _onStartLoad(@{@"url": webView.URL.absoluteString});
}

- (void)webView:(WKWebView *)webView didCommitNavigation:(WKNavigation *)navigation
{
  NSLog(@"%s", __FUNCTION__);
  [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
  if(!_onFinishLoad){
      return;
  }
  NSString* url = webView.URL.absoluteString ? ([webView.URL absoluteString]) : @"";
  _onFinishLoad(@{@"url": url});
}

- (void)webView:(WKWebView *)webView didFailProvisionalNavigation:(null_unspecified WKNavigation *)navigation withError:(NSError *)error {
  NSLog(@"%s", __FUNCTION__);
  NSLog(@"%s", [error description]);
  [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
  if(!_onFinishLoad){
      return;
  }
  NSString* url = webView.URL ? webView.URL.absoluteString : @"";
  _onFinishLoad(@{@"url": url});
}

- (void)webView:(WKWebView *)webView didReceiveAuthenticationChallenge:(NSURLAuthenticationChallenge *)challenge completionHandler:(void (^)(NSURLSessionAuthChallengeDisposition disposition, NSURLCredential *credential))completionHandler {
    NSLog(@"Allowing all");
    SecTrustRef serverTrust = challenge.protectionSpace.serverTrust;
    CFDataRef exceptions = SecTrustCopyExceptions (serverTrust);
    SecTrustSetExceptions (serverTrust, exceptions);
    CFRelease (exceptions);
    completionHandler (NSURLSessionAuthChallengeUseCredential, [NSURLCredential credentialForTrust:serverTrust]);
}

@end
