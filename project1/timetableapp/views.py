from django.http import HttpResponse
from timetableapp.models import Schedule
import json
from timetable.settings import DATA_DIR
import os.path

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

def areasVersion(request):
    l = []
    d = {}
    d['version'] = '2'
    l.append(d)
    return HttpResponse(json.dumps(l), content_type='application/json')
    
def areas(request):
    with open(os.path.join(DATA_DIR, 'areas_20130820.txt')) as areasFile:
        l = json.loads(areasFile.read().decode('utf8'))
        return HttpResponse(json.dumps(l), content_type='application/json')
    
def lessonsVersion(request, areaId, subAreaId, clubId, room):
    l = []
    d = {}
    d['version'] = '2'
    l.append(d)
    return HttpResponse(json.dumps(l), content_type='application/json')
        
def lessons(request, areaId, subAreaId, clubId, room):
    with open(os.path.join(DATA_DIR, 'lessons_demo.txt')) as areasFile:
        l = json.loads(areasFile.read().decode('utf8'))
        return HttpResponse(json.dumps(l), content_type='application/json')
