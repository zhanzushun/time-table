from django.http import HttpResponse
from timetableapp.models import Schedule
from django.core import serializers

def index(request):
    json_serializer = serializers.get_serializer("json")()
    jsonstr = json_serializer.serialize(Schedule.objects.all(), ensure_ascii=False)
    return HttpResponse(jsonstr, content_type='application/json')
