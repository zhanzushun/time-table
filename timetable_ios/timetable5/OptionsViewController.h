//
//  OptionsViewController.h
//  timetable5
//
//  Created by cecilia on 7/11/14.
//  Copyright (c) 2014 td. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Data.h"

@interface OptionsViewController : UIViewController <UIPickerViewDelegate, UIPickerViewDataSource>
{
    IBOutlet UIPickerView *provincePicker;
    IBOutlet UIPickerView *cityPicker;
    IBOutlet UIPickerView *clubPicker;
    IBOutlet UIPickerView *roomPicker;
    NSArray *areas;
    BOOL tryLoadFromData;
}
- (BOOL)saveToData: (Data *)data;
- (void)loadDataToUI: (Data *)data;
@end
