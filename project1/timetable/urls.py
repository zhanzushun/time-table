from django.conf.urls import patterns, include, url
import timetableapp.views

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'timetable.views.home', name='home'),
    # url(r'^timetable/', include('timetable.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    url(r'^admin/', include(admin.site.urls)),
    url(r'^$', timetableapp.views.index, name='index'),
    url(r'^areas/version', timetableapp.views.areasVersion),
    url(r'^areas', timetableapp.views.areas),
    url(r'^lessons/version/(?P<areaId>\d+)_(?P<subAreaId>\d+)_(?P<clubId>\d+)_(?P<roomId>.+)', timetableapp.views.lessonsVersion),
    url(r'^lessons/(?P<areaId>\d+)_(?P<subAreaId>\d+)_(?P<clubId>\d+)_(?P<roomId>.+)', timetableapp.views.lessons),
)
