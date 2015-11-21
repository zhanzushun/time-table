//
//  OptionsViewController.m
//  timetable5
//
//  Created by cecilia on 7/11/14.
//  Copyright (c) 2014 td. All rights reserved.
//

#import "OptionsViewController.h"
#import "AppDelegate.h"

@implementation OptionsViewController

- (id)initWithNibName: (NSString *)nibNameOrNil
               bundle: (NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil
                           bundle:nibBundleOrNil];
    if (self) {
        UITabBarItem *tbi = [self tabBarItem];
        [tbi setTitle:@"选项"];
        [tbi setImage: [UIImage imageNamed:@"options.png"]];
        
        NSString *path = [[NSBundle mainBundle]  pathForResource:@"areas_20130820_utf8" ofType:@"txt"];
        NSLog(@"path:%@",path);
        NSData *jdata = [[NSData alloc] initWithContentsOfFile:path];
        //NSLog(@"length:%d",[jdata length]);
        NSError *error = nil;
        areas = [NSJSONSerialization JSONObjectWithData:jdata options:kNilOptions error:&error];
        tryLoadFromData = true;
    }
    return self;
}

- (void)viewDidLoad
{
    provincePicker.autoresizingMask = UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;
    provincePicker.dataSource = self;
    provincePicker.delegate = self;
    provincePicker.showsSelectionIndicator = YES;
    NSLog(@"view did load: reload all components for province picker");
    
    roomPicker.autoresizingMask = UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;
    roomPicker.dataSource = self;
    roomPicker.delegate = self;
    roomPicker.showsSelectionIndicator = YES;
    
    [NSTimer scheduledTimerWithTimeInterval:0.2
                                     target:self
                                   selector:@selector(fireTimer:)
                                   userInfo:nil repeats:YES];
}

- (void) fireTimer:(NSTimer*)theTimer
{
    AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    [appDelegate optionsViewLoaded:self];

    [theTimer invalidate];
    theTimer = nil;
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 2;
}

- (NSArray *)getSubareas
{
    NSInteger selectedAreaIndex = [provincePicker selectedRowInComponent:0];
    
    NSArray *subareas = nil;
    if ([areas count] > selectedAreaIndex)
        subareas = [areas[selectedAreaIndex] objectForKey:@"subareas"];
    else {
        NSLog(@"selected index of areas is out of range (index %ld) >= (count %lu)",
              (long)selectedAreaIndex, (unsigned long)[areas count]);
        return nil;
    }
    return subareas;
}

- (NSArray *)getClubs
{
    NSArray *subareas = [self getSubareas];
    if (subareas == nil)
        return nil;
    
    NSInteger selectedSubarea = [provincePicker selectedRowInComponent:1];
    NSArray *clubs = nil;
    if ([subareas count] > selectedSubarea)
        clubs = [subareas[selectedSubarea] objectForKey:@"clubs"];
    else {
        NSLog(@"selected index of subareas is out of range (index %ld) >= (count %lu)",
              (long)selectedSubarea, (unsigned long)[subareas count]);
        return nil;
    }
    return clubs;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView
numberOfRowsInComponent:(NSInteger)component
{
    if (pickerView == roomPicker) {
        if (component == 0) {
            NSArray *clubs = [self getClubs];
            if (clubs == nil)
                return 0;
            return [clubs count];
        }
        else if (component == 1)
            return 3; // rooms
    }
    else {
        if (component == 0)
            return [areas count];
        else if (component == 1) {
            NSArray *subareas = [self getSubareas];
            if (subareas == nil)
                return 0;
            return [subareas count];
        }
    }
    return 0;
}


- (UIView *)pickerView:(UIPickerView *)pickerView
            viewForRow:(NSInteger)row
          forComponent:(NSInteger)component
           reusingView:(UIView *)view
{
    CGFloat w = 100.0;
    NSString *t = nil;
    if (pickerView == roomPicker) {
        if (component == 0) {
            w = 150.0;
            NSArray *clubs = [self getClubs];
            if ([clubs count] > row)
                t = [clubs[row] objectForKey:@"club"];
        }
        else {
            if (row == 0)
                t = @"瑜伽教室";
            else if (row == 1)
                t = @"有氧教室";
            else {
                t = @"单车教室";
            }
        }
    }
    else {
        if (component == 0) {
            if ([areas count] > row)
                t = [areas[row] objectForKey:@"area"];
        }else if (component == 1){
            NSArray *subareas = [self getSubareas];
            if (subareas != nil && [subareas count] > row)
                t = [subareas[row] objectForKey:@"subarea"];
        }
    }
    
    NSLog(@"generate picker view col: %ld, row: %ld, title: %@", (long)component, (long)row, t);
    
    UILabel *myView = nil;
    myView.textAlignment = NSTextAlignmentLeft;
    myView = [[UILabel alloc] initWithFrame:CGRectMake(0.0, 0.0, w, 20)];
    myView.text = t;
    myView.font = [UIFont systemFontOfSize:11];
    myView.backgroundColor = [UIColor clearColor];
    return myView;
}

- (CGFloat)pickerView:(UIPickerView *)pickerView
    widthForComponent:(NSInteger)component
{
    CGFloat componentWidth = 100.0;
    if (pickerView == roomPicker) {
        if (component == 0)
            componentWidth = 150.0;
    }
    return componentWidth;
}

- (CGFloat)pickerView:(UIPickerView *)pickerView
rowHeightForComponent:(NSInteger)component
{
    return 20.0;
}

- (void)
    pickerView:(UIPickerView *)pickerView
  didSelectRow:(NSInteger)row
   inComponent:(NSInteger)component
{
    if (pickerView == roomPicker){
        if (component == 0)
            NSLog(@"club index %ld is selected", (long)row);
        else
            NSLog(@"room index %ld is selected", (long)row);
        return;
    }
    else {
        if (component == 0){
            [provincePicker reloadComponent:1];
            [roomPicker reloadComponent:0];
            NSLog(@"area index %ld is selected", (long)row);
        }
        else {
            [roomPicker reloadComponent:0];
            NSLog(@"subarea index %ld is selected", (long)row);
        }
    }
}

- (BOOL)saveToData:(Data *)data
{
    BOOL isChanged = false;
    
    NSString *areaId = nil;
    NSInteger selectedAreaIndex = [provincePicker selectedRowInComponent:0];
    if ([areas count] > selectedAreaIndex)
        areaId = [areas[selectedAreaIndex] objectForKey:@"area_id"];
    if (![areaId isEqualToString:data.areaId]) {
        data.areaId = areaId;
        isChanged = true;
    }
    
    NSString *subareaId = nil;
    NSInteger selectedSubarea = [provincePicker selectedRowInComponent:1];
    NSArray *subareas = [self getSubareas];
    if ([subareas count] > selectedSubarea) {
        subareaId = [subareas[selectedSubarea] objectForKey:@"subarea_id"];
    }
    if (![subareaId isEqualToString:data.subareaId]) {
        data.subareaId = subareaId;
        isChanged = true;
    }
    
    NSString *clubId = nil;
    NSInteger selectedClub = [roomPicker selectedRowInComponent:0];
    NSArray *clubs = [self getClubs];
    if ([clubs count] > selectedClub) {
        clubId = [clubs[selectedClub] objectForKey:@"club_id"];
    }
    if (![clubId isEqualToString:data.clubId]) {
        data.clubId = clubId;
        data.club = [clubs[selectedClub] objectForKey:@"club"];
        isChanged = true;
    }
    
    NSInteger selectedRoom = [roomPicker selectedRowInComponent:1];
    NSString *roomId = [NSString stringWithFormat:@"%d", (int)selectedRoom + 1];
    if (![roomId isEqualToString:data.roomId]) {
        data.roomId = roomId;
        isChanged = true;
    }
    
    return isChanged;
}

- (int)getAreaIndex: (NSString *)areaId
{
    int i = 0;
    for (NSDictionary *area in areas) {
        if ([areaId isEqualToString:[area objectForKey:@"area_id"]])
            return i;
        i++;
    }
    return -1;
}

- (int)getSubareaIndex: (NSString *)subareaId
{
    NSArray *subareas = [self getSubareas];
    if (!subareas)
        return -1;
    
    int i = 0;
    for (NSDictionary *subarea in subareas) {
        if ([subareaId isEqualToString:[subarea objectForKey:@"subarea_id"]])
            return i;
        i++;
    }
    return -1;
}

- (int)getClubIndex: (NSString *)clubId
{
    NSArray *clubs = [self getClubs];
    if (!clubs)
        return -1;
    
    int i = 0;
    for (NSDictionary *club in clubs) {
        if ([clubId isEqualToString:[club objectForKey:@"club_id"]])
            return i;
        i++;
    }
    return -1;
}

- (int)getRoomIndex: (NSString *)roomId
{
    return (int)[roomId integerValue] - 1;
}

- (void)loadDataToUI:(Data *)data
{
    if (!tryLoadFromData)
        return;

    int expectedAreaIndex = [self getAreaIndex:data.areaId];
    if ([provincePicker selectedRowInComponent:0] != expectedAreaIndex) {
        [provincePicker selectRow:expectedAreaIndex inComponent:0 animated:false];
        NSLog(@"select area: %d", expectedAreaIndex);

        [provincePicker reloadComponent:1];
        NSLog(@"reload col 1");
        
        [roomPicker reloadComponent:0];
    }
    int expectedSubareaIndex = [self getSubareaIndex:data.subareaId];
    if ([provincePicker selectedRowInComponent:1] != expectedSubareaIndex) {
        [provincePicker selectRow:expectedSubareaIndex inComponent:1 animated:false];
        NSLog(@"select subarea: %d", expectedSubareaIndex);
        
        [roomPicker reloadComponent:0];
    }
    int expectedClubIndex = [self getClubIndex:data.clubId];
    if ([roomPicker selectedRowInComponent:0] != expectedClubIndex) {
        [roomPicker selectRow:expectedClubIndex inComponent:0 animated:false];
        NSLog(@"select club: %d", expectedClubIndex);
    }
    int expectedRoomIndex = [self getRoomIndex:data.roomId];
    if ([roomPicker selectedRowInComponent:1] != expectedRoomIndex) {
        [roomPicker selectRow:expectedRoomIndex inComponent:1 animated:false];
    }
    tryLoadFromData = false;
}

- (BOOL)prefersStatusBarHidden
{
    return YES;
}

@end
