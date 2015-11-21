//
//  Data.m
//  timetable5
//
//  Created by cecilia on 7/13/14.
//  Copyright (c) 2014 td. All rights reserved.
//

#import "Data.h"

@implementation Data

- (void)encodeWithCoder:(NSCoder *)aCoder
{
    [aCoder encodeObject:[self areaId] forKey:@"areaId"];
    [aCoder encodeObject:[self subareaId] forKey:@"subareaId"];
    [aCoder encodeObject:[self clubId] forKey:@"clubId"];
    [aCoder encodeObject:[self roomId] forKey:@"roomId"];
    [aCoder encodeObject:[self lessons] forKey:@"lessons"];
    [aCoder encodeObject:[self club] forKey:@"club"];
}

- (id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super init];
    if (self) {
        [self setAreaId:[aDecoder decodeObjectForKey:@"areaId"]];
        [self setSubareaId:[aDecoder decodeObjectForKey:@"subareaId"]];
        [self setClubId:[aDecoder decodeObjectForKey:@"clubId"]];
        [self setRoomId:[aDecoder decodeObjectForKey:@"roomId"]];
        [self setLessons:[aDecoder decodeObjectForKey:@"lessons"]];
        [self setClub:[aDecoder decodeObjectForKey:@"club"]];
    }
    return self;
}

- (BOOL)isValid
{
    return _areaId && _subareaId && _clubId && _roomId;
}

@end