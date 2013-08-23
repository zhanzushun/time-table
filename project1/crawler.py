#!/usr/bin/python
# -*- coding: utf-8 -*-

import urllib
import urllib2
import re
import json

def main():
    (areas, results) = mainImpl(True)
    out = r'c:\temp\areas.txt'
    outFile = open(out, 'w')
    outFile.write (json.dumps(areas, indent = 4, ensure_ascii = False))

    out = r'c:\temp\results.txt'
    outFile = open(out, 'w')
    outFile.write (json.dumps(results, indent = 4, ensure_ascii = False))
    
def mainImpl(getDetail):
    f1=open(r'c:\temp\crawler.log', 'w+')
    
    results = []
    areas_result = []
    
    areas = {'上海市' : '121',
        '北京市' : '122',
        '天津市' : '141',
        '辽宁省' : '142',
        '浙江省' : '143',
        '河南省' : '144',
        '湖北省' : '145',
        '江苏省' : '146',
        '吉林省' : '147',
        '山东省' : '148',
        '广东省' : '149',
        }
    
    for area in areas:
        subAreaList = []
        try:
            area_id = areas[area]
            area_url = 'http://www.1012china.com/area.aspx?id={0}'.format(area_id)
            area_content = urllib2.urlopen(area_url).read()
            subAreaList = [e.strip().strip('"') for e in re.findall(r"(?<=id=)\d+", area_content)]
            #find non-number like '">xxxx/a>'
            subAreaNameList = [e[2:][0:-4].strip().decode('utf-8') for e in re.findall(r"\">\D*?/a>", area_content)]
            assert(len(subAreaList) == len(subAreaNameList))
            
            area_obj = {}
            area_obj['area'] = area
            area_obj['area_id'] = area_id
            area_obj['subareas'] = []
            areas_result.append(area_obj)
        except:
            print >>f1, "!!!!!!!!", area, area_url
        
        subAreaIndex = 0
        for subAreaId in subAreaList:
            clubList = []
            try:
                subAreaName = subAreaNameList[subAreaIndex]
                subAreaIndex += 1
                subAreaUrl = 'http://www.1012china.com/area_club.aspx?id={0}'.format(subAreaId)
                subAreaContent = urllib2.urlopen(subAreaUrl).read()
                
                clubList = [e.strip() for e in re.findall(r'(?<=value=")\d+', subAreaContent)]
                #find non-number like '">xxxx</a>'
                clubNameList = [e[2:][0:-4].strip().decode('utf-8') for e in re.findall(r'">[^<]*?</a>', subAreaContent)]
                
                subArea_obj = {}
                subArea_obj['subarea'] = subAreaName.encode('utf8')
                subArea_obj['subarea_id'] = subAreaId
                subArea_obj['clubs'] = []
                area_obj['subareas'].append(subArea_obj)
            except:
                print >>f1, "!!!!!!!!", area, subAreaName, subAreaUrl
                
            clubIndex = 0
            for clubId in clubList:
                try:
                    clubName = clubNameList[clubIndex]
                    clubIndex += 1
                    
                    club_obj = {}
                    club_obj['club'] = clubName.encode('utf8')
                    club_obj['club_id'] = clubId
                    subArea_obj['clubs'].append(club_obj)
                    if not getDetail:
                        continue
                    
                    result = {}
                    room = '瑜伽教室'
                    result['area'] = area
                    result['area_id'] = area_id
                    result['sub_area'] = subAreaName.encode('utf8')
                    result['sub_area_id'] = subAreaId
                    result['club'] = clubName.encode('utf8')
                    result['club_id'] = clubId
                    result['room'] = room
                    result['lessons'] = []
                    
                    url = 'http://www.1012china.com/CustomDetail.aspx?typeid=1&id={0}'.format(clubId)
                    clubContent = urllib2.urlopen(url).read()
                    parse(clubContent, result['lessons'], f1)
                    print "============================"
                    print json.dumps(result, indent = 4)
                    results.append(result)
                except:
                    print >>f1, "room1!!!!!!!!", url

                try:                
                    result2 = {}
                    room2 = '有氧教室'
                    result2['area'] = area
                    result2['area_id'] = area_id
                    result2['sub_area'] = subAreaName.encode('utf8')
                    result2['sub_area_id'] = subAreaId
                    result2['club'] = clubName.encode('utf8')
                    result2['club_id'] = clubId
                    result2['room'] = room2
                    result2['lessons'] = []
                    
                    postData2 = urllib.urlencode({'btnSite' : room2})
                    req2 = urllib2.Request(url = url, data = postData2)
                    clubContent2 = urllib2.urlopen(req2).read()
                    parse(clubContent2, result2['lessons'], f1)
                    print "============================"
                    print json.dumps(result2, indent = 4)
                    results.append(result2)
                except:
                    print >>f1, "room2!!!!!!!!", url

                try:
                    result3 = {}
                    room3 = '单车教室'
                    result3['area'] = area
                    result3['area_id'] = area_id
                    result3['sub_area'] = subAreaName.encode('utf8')
                    result3['sub_area_id'] = subAreaId
                    result3['club'] = clubName.encode('utf8')
                    result3['club_id'] = clubId
                    result3['room'] = room3
                    result3['lessons'] = []
                    
                    postData3 = urllib.urlencode({'btnSite' : room3})
                    req3 = urllib2.Request(url = url, data = postData3)
                    clubContent3 = urllib2.urlopen(req3).read()
                    parse(clubContent3, result3['lessons'], f1)
                    print "============================"
                    print json.dumps(result3, indent = 4)
                    results.append(result3)
                except:
                    print >>f1, "room3!!!!!!!!", url
                
    return (areas_result, results)
                
def parse(fileContent, resultList, f1):
    timeList = []
    try:
        timeList = re.findall(r'<tr>\s*<td align="center">\s*<br />\s*\d{2}:\d{2}', fileContent)
        reo = re.compile(r'<tr>\s*<td align="center">\s*<br />\s*\d{2}:\d{2}.*?/tr>', re.S)
        trList = reo.findall(fileContent)
        
        reo_lesson = re.compile(r'<span class="f10">[^<]*', re.S)
        reo_lesson_chinese = re.compile(r'</span>\s*<br />[^<]*<br />', re.S)
        reo_teacher = re.compile(r'<br />[^<]*<br />\s*<span', re.S)
    except:
        print >>f1, "timeList:!!!!!!!!", timeList
        print >>f1, "trList:!!!!!!!!", trList
    
    timeIndex = 0
    for time in timeList:
        tdContentList = []
        try:
            time = re.findall(r'\d{2}:\d{2}', time)[0]
            timeContent = trList[timeIndex]
            timeIndex += 1
            
            tdContentList = timeContent.split('<td class="lessons">')
            weekday = -1
        except:
            print >>f1, "time:!!!!!!!!", time
            print >>f1, "timeContent:!!!!!!!!", timeContent
        
        for tdContent in tdContentList:
            try:
                if weekday == -1:
                    weekday += 1
                    continue
                    
                lessonAndDuration = [''.join(e.split()).strip('<spanclass="f10">') for e in reo_lesson.findall(tdContent)]
                if (len(lessonAndDuration) <= 0):
                    weekday += 1
                    continue
                if (len(lessonAndDuration) != 2):
                    print "lessonAndDuration:!!!!!!!!", lessonAndDuration
                    continue
                
                lesson = {}
                lesson['weekday'] = str(weekday)
                lesson['time'] = time
                lesson['lesson'] = lessonAndDuration[0]
                lesson['duration'] = lessonAndDuration[1]
                lesson['lesson_chinese'] = [''.join(e.split()).strip('</span>').strip('<br />') for e in reo_lesson_chinese.findall(tdContent)][0]
                lesson['teacher'] = [''.join(e.split()).strip('<span').strip('<br />') for e in reo_teacher.findall(tdContent)][0]
                resultList.append(lesson)
                
                weekday += 1
            except:
                print >>f1, "tdContent:!!!!!!!!", tdContent
                
if __name__ == "__main__":
    main()
