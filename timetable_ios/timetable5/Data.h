//
//  Data.h
//  timetable5
//
//  Created by cecilia on 7/13/14.
//  Copyright (c) 2014 td. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Data : NSObject <NSCoding>
{
}
@property (strong, nonatomic) NSString *areaId;
@property (strong, nonatomic) NSString *subareaId;
@property (strong, nonatomic) NSString *clubId;
@property (strong, nonatomic) NSString *roomId;
@property (strong, nonatomic) NSArray *lessons;
@property (strong, nonatomic) NSString *club;
- (BOOL) isValid;
@end
