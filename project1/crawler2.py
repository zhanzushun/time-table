#!/usr/bin/python
# -*- coding: utf-8 -*-

import urllib
import urllib2
import re
import json
import os.path
import datetime

def main():
    dir = r'c:\temp'
    errFileName = os.path.join(dir, 'err.log')
    areasFileName = os.path.join(dir, 'areas.txt')
    lessonsFileName = os.path.join(dir, 'lessons.txt')
    htmlDir = os.path.join(dir, 'html_2013-08-26')
    
    with open(errFileName, 'w') as errFile:
        areas = getAreas(False, htmlDir, errFile)
        with open(areasFileName, 'w') as areasFile:
            areasFile.write(json.dumps(areas, indent = 4, ensure_ascii = False))
        with open(lessonsFileName, 'w') as lessonsFile:
            lessonList = []
            for area in areas:
                for subArea in area['subareas']:
                    for club in subArea['clubs']:
                        clubId = club['club_id']
                        htmlFileName = os.path.join(htmlDir, '{0}.htm'.format(clubId))
                        with open(htmlFileName, 'r') as htmlFile:
                            club_obj = {}
                            club_obj['club_id'] = clubId
                            club_obj['rooms'] = []
                            lessonList.append(club_obj)
                            parseClub(clubId, htmlFile.read(), club_obj['rooms'], errFile)
            lessonsFile.write(json.dumps(lessonList, indent = 4, ensure_ascii = False))
    
def getAreas(downloadHtmlFiles, htmlDir, errFile):
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
            print >>errFile, "!!!!!!!!", area, area_url
        
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
                print >>errFile, "!!!!!!!!", area, subAreaName, subAreaUrl
                
            clubIndex = 0
            for clubId in clubList:
                clubName = clubNameList[clubIndex]
                clubIndex += 1
                
                club_obj = {}
                club_obj['club'] = clubName.encode('utf8')
                club_obj['club_id'] = clubId
                subArea_obj['clubs'].append(club_obj)
                if not downloadHtmlFiles:
                    continue
                downloadCount = 0
                while not downloadClub(clubId, htmlDir, errFile):
                    err = "download club({0}) failed:".format(clubId)
                    print err
                    downloadCount += 1
                    if (downloadCount >= 3):
                        print >>errFile, "!!!!!!!!", err
                        break
    return areas_result
    
def downloadClub(clubId, htmlDir, errFile):
    try:
        filename = os.path.join(htmlDir, r'{0}.htm'.format(clubId))
        if (os.path.exists(filename)):
            return True
        print "download", filename
        url = 'http://www.1012china.com/Download.aspx?clubid={0}'.format(clubId)
        clubContent = urllib2.urlopen(url).read()
        if not clubContent:
            return False
        with open(filename, 'w') as htmlFile:
            htmlFile.write(clubContent)
    except:
        errStr = "download club({0}) failed".format(clubId)
        print errStr
        print >>errFile, errStr
        return False
    return True
        
def testParseClub():
    with open(r'c:\temp\err.log', 'w') as errFile:
        with open(r'c:\temp\html_2013-08-26\1404.htm', 'r') as htmlFile:
            clubLessonList = []
            parseClub('1404', htmlFile.read(), clubLessonList, errFile)
            with open(r'c:\temp\1404.txt', 'w') as lessonsFile:
                lessonsFile.write(json.dumps(clubLessonList, indent = 4, ensure_ascii = False))
                
def parseClub(clubId, fileContent, resultList, errFile):
    token = r'<td colspan="8" style="text-align:left; border:0; font-family:Verdana, Arial, Helvetica, sans-serif; padding-left:0; color:#922f7b; font-size:14px; font-weight:bold;">'
    tokenList = fileContent.split(token)
    if len(tokenList) < 2:
        err = "parse club({0}): No table found".format(clubId)
        print err
        print >>errFile, err
        return
    tableList = tokenList[1:]
    for tableContent in tableList:
        roomFound = False
        pos = tableContent.find('</td>')
        if pos >= 0:
            room = tableContent[:pos].strip()
            if room and room.find('<') == -1:
                roomFound = True
        if not roomFound:
            err = 'parse club({0}): get room error'.format(clubId)
            print err
            print >>errFile, err
        parseTable(room, tableContent, resultList, errFile)
        
def parseTable(room, tableContent, resultList, errFile):
    timeList = re.findall(r'<tr>\s*<td bgcolor="#dcdcdf" style="color:#922f7b;">\s*\d{2}:\d{2}', tableContent)
    reo_trList = re.compile(r'<tr>\s*<td bgcolor="#dcdcdf" style="color:#922f7b;">\s*\d{2}:\d{2}.*?/tr>', re.S)
    trList = reo_trList.findall(tableContent)
    if len(timeList) != len(trList):
        err = 'parseTable room({0}): time count error'.format(room)
        print err
        print >>errFile, err
        return
    
    room_obj = {}
    room_obj['room'] = room
    room_obj['lessons'] = []
    resultList.append(room_obj)
    
    timeIndex = -1
    for timeTemp in timeList:
        timeIndex += 1
        try:
            time = re.findall(r'\d{2}:\d{2}', timeTemp)[0]
            timeTrContent = trList[timeIndex]
        except:
            err = 'parseTable room({0}): time count error 2'.format(room)
            print err
            print >>errFile, err
            continue
        parseTimeTr(time, timeTrContent, room_obj['lessons'], errFile)
        
def parseTimeTr(time, timeTrContent, lessonList, errFile):
    reo_lesson = re.compile(r'<span class="f10">[^<]*</span><br />', re.S)
    reo_lesson_chinese = re.compile(r'</span><br />[^<]*<br />', re.S)
    reo_teacher = re.compile(r'<br />[^<]*<br /><span', re.S)
    reo_duration = re.compile(r'<span class="f10">[^<]*</span>\s*</a></div>', re.S)
    
    tdContentList = timeTrContent.split('<td width="110" height="80" bgcolor="#efeff0">')
    
    weekday = -2
    for tdContent in tdContentList:
        weekday += 1
        try:
            if weekday == -1:
                continue
            lessonName = [e.strip('<span class="f10">').strip('</span><br />') for e in reo_lesson.findall(tdContent)]
            if (len(lessonName) <= 0):
                continue
            if (len(lessonName) != 1):
                err = 'parseTimeTr: lesson error,' + tdContent
                print err
                print >>errFike, err
                continue
            lesson = {}
            lesson['weekday'] = str(weekday)
            lesson['time'] = time
            lesson['lesson'] = lessonName[0]
            lesson['duration'] = [e.strip('</a></div>').strip().strip('</span>').strip('<span class="f10">') for e in reo_duration.findall(tdContent)][0]
            lesson['lesson_chinese'] = [e.strip('</span>').strip('<br />') for e in reo_lesson_chinese.findall(tdContent)][0]
            lesson['teacher'] = [e.strip('<span').strip('<br />') for e in reo_teacher.findall(tdContent)][0]
            lessonList.append(lesson)
        except:
            err = 'parseTimeTr: unknown error,' + tdContent
            print err
            print >>errFile, err
                
if __name__ == "__main__":
    main()
