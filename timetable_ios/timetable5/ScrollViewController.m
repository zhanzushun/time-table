//
//  ScrollViewController.m
//  timetable5
//
//  Created by cecilia on 7/11/14.
//  Copyright (c) 2014 td. All rights reserved.
//

#import "ScrollViewController.h"

@implementation ScrollViewController

- (id)initWithNibName: (NSString *)nibNameOrNil
               bundle: (NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nil
                           bundle:nil];
    if (self) {
        UITabBarItem *tbi = [self tabBarItem];
        [tbi setTitle:@"课表"];
        [tbi setImage:[UIImage imageNamed:@"table.png"]];
    }
    return self;
}

- (CGFloat)getTitleHeight
{
    return 25;
}

- (CGFloat)getRetinaFactor
{
    return 2;
}

- (void)loadView
{
    //[super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    CGRect rect_screen = [[UIScreen mainScreen] bounds];
    CGSize size_screen = rect_screen.size;
    CGFloat titleHeight = [self getTitleHeight];
    CGFloat titleFontSize = 12;
    UIColor *bgclr =
        //[UIColor colorWithRed:219.0/256.0 green:235.0/256.0 blue:255.0/256.0 alpha:1];
        [UIColor colorWithRed:236.0/256.0 green:244.0/256.0 blue:255.0/256.0 alpha:1];
        //[UIColor colorWithRed:168.0/256.0 green:207.0/256.0 blue:255.0/256.0 alpha:1];
    
    UIScrollView *scrollView = [[UIScrollView alloc] init];
    scrollView.frame = CGRectMake(0, titleHeight, size_screen.width, size_screen.height - titleHeight);
    scrollView.backgroundColor = bgclr;
    
    UIView *wrapperView = [[UIView alloc] init];
    wrapperView.frame = CGRectMake(0, 0, size_screen.width, size_screen.height);
    [self setView: wrapperView];

    labelTitle = [[UILabel alloc] init];
    labelTitle.frame = CGRectMake(0, 0, self.view.frame.size.width, titleHeight);
    labelTitle.font = [UIFont systemFontOfSize:titleFontSize];
    labelTitle.textAlignment = NSTextAlignmentCenter;
    labelTitle.backgroundColor = bgclr;
    labelTitle.textColor = [UIColor blackColor];

    [wrapperView addSubview:scrollView];
    [wrapperView addSubview:labelTitle];
    
    UIImageView *imageView = [[UIImageView alloc] init];
    imageView.image = [UIImage imageNamed: @"timetable_1000.png"];
    CGFloat imgW = imageView.image.size.width;
    CGFloat imgH = imageView.image.size.height;
    imageView.frame = CGRectMake(0, 0, imgW, imgH);
    
    //NSLog(@"view1 frame:%@--------view1 bounds:%@",
    //      NSStringFromCGRect(imageView.frame),
    //      NSStringFromCGRect(imageView.bounds));
    
    CGFloat scrollW = imgW;
    CGFloat scrollH = imgH + 60;
    scrollView.contentSize = CGSizeMake(scrollW, scrollH);
    
    [scrollView addSubview:imageView];
}

-(void)buttonAction:(id)sender
{
    NSArray *info = [btntitleToData objectForKey:NSStringFromCGRect([sender frame])];
    if ([info count] < 6)
        return;
    
    NSString *weekday = info[0];
    if ([weekday isEqualToString:@"0"])
        weekday = @"星期一";
    else if ([weekday isEqualToString:@"1"])
        weekday = @"星期二";
    else if ([weekday isEqualToString:@"2"])
        weekday = @"星期三";
    else if ([weekday isEqualToString:@"3"])
        weekday = @"星期四";
    else if ([weekday isEqualToString:@"4"])
        weekday = @"星期五";
    else if ([weekday isEqualToString:@"5"])
        weekday = @"星期六";
    else if ([weekday isEqualToString:@"6"])
        weekday = @"星期日";
    
    NSString *starttime = info[1];
    NSString *durationMin = info[2];
    //NSString *lesson = info[3];
    NSString *lessonChinese = info[4];
    NSString *teacher = info[5];
    
    UIAlertView *alert = [[UIAlertView alloc]
                          initWithTitle:lessonChinese
                          message:[NSString stringWithFormat:@"%@ %@\n%@\n%@",weekday,starttime,durationMin,teacher]
                          delegate:self
                          cancelButtonTitle:@"确定"
                          otherButtonTitles:nil];
    [alert show];
}

- (NSUInteger) strIndexOf: (NSString *)str
         subStr: (NSString *)aSubStr
{
    NSRange range = [str rangeOfString:aSubStr];
    if ( range.length > 0 ) {
        return range.location;
    } else {
        return -1;
    }
}

- (CGFloat)getRow:(NSString *)time
{
    NSUInteger index = [self strIndexOf:time subStr:@":"];
    if (index == -1)
        return 0;
    long s_h = [[time substringToIndex:index] integerValue];
    
    double result = 0;
    NSString *min_sec = [time substringFromIndex:(index + 1)];
    if (min_sec) {
        NSUInteger index2 = [self strIndexOf:min_sec subStr:@":"];
        if (index2 == -1){
            result = s_h + [min_sec integerValue] / 60.0;
        }
        else {
            long s_m = [[min_sec substringToIndex:index2] integerValue];
            result = s_h + s_m / 60.0;
        }
    }
    else
        result = s_h;
    return (result - 9) / 13.0 /*hours*/ * 13.0 /*rows*/; // 13 rows present 13 hours starting from 9:00AM
}

- (CGRect)calculatePosition: (NSString *)weekday
                  startTime: (NSString *)aStartTime
                   duration: (NSString *)aDuration
{
    //int w=1235;
    //int h=1000;
    int t=66 + 1;
    int l=154 + 1;
    int cellw=154 - 3;
    int sepw=3;
    int cellh=67 - 3;
    int seph=3;
    //int cols=7;
    //int rows=13;
    
    int x = (int) (l + [weekday integerValue] * (cellw + sepw));
    int y1 = (int) (t + [self getRow:aStartTime] * (cellh + seph));
    //int height = (int) ((Integer.parseInt(duration) / 60.0) * cellh);
    int height = cellh;
    
    return CGRectMake(x/[self getRetinaFactor], y1/[self getRetinaFactor],
                      cellw/[self getRetinaFactor], height/[self getRetinaFactor]);
}

- (void)refreshLessons:(NSArray *)lessons
                 title:(NSString *)aTitle
{
    btntitleToData = nil;
    for (UIView *sub in [self.view.subviews[0] subviews]) {
        if ([sub isKindOfClass:[UIButton class]])
            [sub removeFromSuperview];
    }
    
    [labelTitle setText:aTitle];
    
    btntitleToData = [[NSMutableDictionary alloc] init];
    for (NSDictionary *dict in lessons) {
        
        NSString *weekday = [dict objectForKey:@"weekday"];
        NSString *starttime = [dict objectForKey:@"time"];
        NSString *durationMin = [dict objectForKey:@"duration"];
        NSString *duration = [durationMin substringToIndex:[durationMin length] - [@"MIN" length]];
        
        NSString *lesson = [dict objectForKey:@"lesson"];
        NSString *lessonChinese = [dict objectForKey:@"lesson_chinese"];
        NSString *teacher = [dict objectForKey:@"teacher"];

        UIButton *btn = [UIButton buttonWithType:UIButtonTypeSystem];
        
        btn.frame = [self calculatePosition:weekday startTime:starttime duration:duration];
        btn.titleLabel.font = [UIFont systemFontOfSize:12];
        btn.backgroundColor = [UIColor colorWithRed:0 green:153.0/256.0 blue:255.0/256.0 alpha:1];
        btn.titleLabel.textAlignment = NSTextAlignmentCenter;
        [btn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        CALayer *btnLayer = [btn layer];
        [btnLayer setMasksToBounds:YES];
        [btnLayer setCornerRadius:5.0f];

        [btn setTitle:lessonChinese forState:UIControlStateNormal];
        
        NSArray *btnData = [[NSArray alloc] initWithObjects:weekday,starttime,durationMin,lesson,lessonChinese,teacher, nil];
        [btntitleToData setObject:btnData forKey:NSStringFromCGRect(btn.frame)];
        
        [btn addTarget:self action:@selector(buttonAction:)
            forControlEvents:UIControlEventTouchUpInside];
    
        [self.view.subviews[0] addSubview:btn];
    }
}

- (BOOL)prefersStatusBarHidden
{
    return YES;
}

- (void)viewDidUnload
{
    labelTitle = nil;
}

@end

