from django.contrib import admin
from timetableapp.models import WeekDay, Teacher, Site, Lesson, Schedule

admin.site.register(WeekDay)
admin.site.register(Teacher)
admin.site.register(Site)
admin.site.register(Lesson)
admin.site.register(Schedule)