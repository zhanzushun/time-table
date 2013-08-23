#!/usr/bin/python
# -*- coding: utf-8 -*-

from django.http import HttpResponse
from timetableapp.models import Schedule
import json
from timetable.settings import DATA_DIR
import os.path
import glob

def index(request):
    l = []
    for obj in Schedule.objects.all():
        d = {}
        d['weekday'] = obj.weekday.name
        d['weekday_chinese'] = obj.weekday.chinesename
        d['start_time'] = str(obj.starttime)
        d['end_time'] = str(obj.endtime)
        d['lesson'] = obj.lesson.name
        d['lesson_chinese'] = obj.lesson.chinesename
        d['site'] = obj.lesson.site.name
        d['site_address'] = obj.lesson.site.address
        d['teacher'] = obj.lesson.teacher.name
        l.append(d)
    #json_serializer = serializers.get_serializer("json")()
    #jsonstr = json_serializer.serialize(Schedule.objects.all(), ensure_ascii=False)
    return HttpResponse(json.dumps(l), content_type='application/json')

def getMaxDateInFileName(pattern):
    fileList = glob.glob(os.path.join(DATA_DIR, '{0}_*.txt'.format(pattern)))
    sample = '20130820.txt'
    result = 10
    for f in fileList:
        date = int(f[-len(sample):-4])
        if (date > result):
            result = date
    return str(result)

def areasVersion(request):
    l = []
    d = {}
    d['version'] = getMaxDateInFileName('areas')
    l.append(d)
    return HttpResponse(json.dumps(l), content_type='application/json')
    
def areas(request):
    date = getMaxDateInFileName('areas')
    with open(os.path.join(DATA_DIR, 'areas_{0}.txt'.format(date))) as areasFile:
        l = json.loads(areasFile.read().decode('utf8'))
        return HttpResponse(json.dumps(l), content_type='application/json')

def getRoom(roomId):
    if roomId == '1':
        room = u'瑜伽教室'
    elif roomId == '2':
        room = u'有氧教室'
    elif roomId == '3':
        room = u'单车教室'
    else:
        room = ''
    return room   

def findClub(clubId, roomId):
    date = getMaxDateInFileName('json')
    with open(os.path.join(DATA_DIR, 'json_{0}.txt'.format(date))) as lessonsFile:
        clubList = json.loads(lessonsFile.read().decode('utf8'))
        for club in clubList:
              if club.get('club_id') == clubId and club.get('room') == getRoom(roomId):
                  return club
    return {}

def lessonsVersion(request, areaId, subAreaId, clubId, roomId):
    l = []
    d = {}
    ver = findClub(clubId, roomId).get('version')
    if ver:
        d['version'] = ver
    else:
        d['version'] = '10' 
    l.append(d)
    return HttpResponse(json.dumps(l), content_type='application/json')
        
def lessons(request, areaId, subAreaId, clubId, roomId):
    l = findClub(clubId, roomId).get('lessons')
    return HttpResponse(json.dumps(l), content_type='application/json')
