//
//  AppDelegate.m
//  timetable5
//
//  Created by cecilia on 7/11/14.
//  Copyright (c) 2014 td. All rights reserved.
//

#import "AppDelegate.h"
#import "ScrollViewController.h"
#import "OptionsViewController.h"
#import "MyTabBarController.h"

@implementation AppDelegate

- (void)refreshScrollView: (ScrollViewController *)svc
{
    NSString *room = nil;
    if ([data.roomId isEqualToString: @"1"])
        room = @"瑜伽教室";
    else if ([data.roomId isEqualToString: @"2"])
        room = @"有氧教室";
    else if ([data.roomId isEqualToString: @"3"])
        room = @"单车教室";
    [svc refreshLessons:data.lessons
                  title:[NSString stringWithFormat:@"%@ - %@", data.club, room]];
}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    // Override point for customization after application launch.
 
    showOptionsViewAtLaunch = false;
    data = [NSKeyedUnarchiver unarchiveObjectWithFile:[self getDataFilePath]];
    if (!data){
        data = [[Data alloc] init];
        showOptionsViewAtLaunch = true;
    }
    
    ScrollViewController *svc = [[ScrollViewController alloc] init];
    OptionsViewController *ovc = [[OptionsViewController alloc] init];
    MyTabBarController *tbc = [[MyTabBarController alloc] init];
    tbc.delegate = self;
    
    NSArray *viewControllers = [NSArray arrayWithObjects:svc,ovc,nil];
    [tbc setViewControllers:viewControllers];

    [[self window] setRootViewController:tbc];
    
    if ([data isValid]) {
        [self loadLessonsFromWeb];
        [self refreshScrollView:svc];
    }
    else
        showOptionsViewAtLaunch = true;
    
    if (showOptionsViewAtLaunch)
        tbc.selectedViewController = ovc;
    
    self.window.backgroundColor = [UIColor whiteColor];
    [self.window makeKeyAndVisible];
    
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    BOOL success = [NSKeyedArchiver archiveRootObject:data toFile:[self getDataFilePath]];
    if (success)
        NSLog(@"data is saved");
    else
        NSLog(@"failed to save data");
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

- (void)tabBarController:(UITabBarController *)tabBarController
 didSelectViewController:(UIViewController *)viewController
{
    if (selectedViewController == viewController)
        return;
    selectedViewController = viewController;
        
    if (viewController == tabBarController.viewControllers[0]) {
        NSLog(@"timetable view is selected");
        BOOL isChanged = [(OptionsViewController*)(tabBarController.viewControllers[1]) saveToData:data];
        if (![data isValid]){
            UIAlertView *alert = [[UIAlertView alloc]
                                  initWithTitle:@"提示"
                                  message:@"请选择一兆韦德俱乐部和教室"
                                  delegate:self
                                  cancelButtonTitle:@"确定"
                                  otherButtonTitles:nil];
            [alert show];
        }
        else if (isChanged && [data isValid]){
            [self loadLessonsFromWeb];
            [self refreshScrollView:(ScrollViewController *)viewController];
        }
    }
}

- (NSString *)getDataFilePath
{
    NSArray *documentDirs = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentDir = [documentDirs objectAtIndex:0];
    NSLog(@"document dir:%@", documentDir);
    return [documentDir stringByAppendingPathComponent:@"data.archive"];
}

- (void)loadLessonsFromWeb
{
    NSString *urlStr = [NSString stringWithFormat: @"http://timetable.sinaapp.com/lessons/%@_%@_%@_%@",
                        data.areaId, data.subareaId, data.clubId, data.roomId];
    NSURLRequest *request = [NSURLRequest requestWithURL: [NSURL URLWithString:urlStr]];
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    
    NSError *error = nil;
    NSArray *lessons = [NSJSONSerialization JSONObjectWithData:response options:kNilOptions error:&error];
    NSLog(@"load lessons from web: %@", lessons);
    
    data.lessons = lessons;
}

- (void)optionsViewLoaded:(UIViewController *)ovc
{
    if (!showOptionsViewAtLaunch) {
        [(OptionsViewController *)ovc loadDataToUI:data];
    }
}

@end