//
//  AppDelegate.h
//  timetable5
//
//  Created by cecilia on 7/11/14.
//  Copyright (c) 2014 td. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Data.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate, UITabBarControllerDelegate>
{
    Data *data;
    BOOL showOptionsViewAtLaunch;
    UIViewController* selectedViewController;
}
@property (strong, nonatomic) UIWindow *window;
- (void)optionsViewLoaded: (UIViewController *)ovc;
@end

