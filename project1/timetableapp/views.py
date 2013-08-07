from django.http import HttpResponse
from timetableapp.models import Schedule
import json

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
