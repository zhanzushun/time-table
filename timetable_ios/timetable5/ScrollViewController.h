//
//  ScrollViewController.h
//  timetable5
//
//  Created by cecilia on 7/11/14.
//  Copyright (c) 2014 td. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ScrollViewController : UIViewController
{
    NSMutableDictionary *btntitleToData;
    UILabel *labelTitle;
}
- (void)refreshLessons: (NSArray *)lessons title: (NSString *)aTitle;
@end
