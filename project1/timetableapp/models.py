from django.db import models

class WeekDay(models.Model):
    name = models.CharField(max_length = 20)
    chinesename = models.CharField(max_length = 20)
    def __unicode__(self):
        return self.name + ',' + self.chinesename
        
class Site(models.Model):
    name = models.CharField(max_length = 50)
    address = models.CharField(max_length = 250)
    def __unicode__(self):
        return self.name + ',' + self.address    

class Teacher(models.Model):
    name = models.CharField(max_length = 50)
    def __unicode__(self):
        return self.name   
                
class Lesson(models.Model):
    name = models.CharField(max_length = 50)
    chinesename = models.CharField(max_length = 50)
    teacher = models.ForeignKey(Teacher)
    site = models.ForeignKey(Site)
    def __unicode__(self):
        return self.name + ',' + self.chinesename + ',' + self.teacher.name + ',' + self.site.name

class Schedule(models.Model):
    weekday = models.ForeignKey(WeekDay)
    starttime = models.TimeField()
    endtime = models.TimeField()
    lesson = models.ForeignKey(Lesson)
    def __unicode__(self):
        return self.weekday.name + ',' + str(self.starttime) + ',' + str(self.endtime) + ',' + self.lesson.name + ',' + self.lesson.teacher.name + ',' + self.lesson.site.name

