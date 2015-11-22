timetable
=========

This project Timetable was initially created as a cross-platform mobile application, supporting both ios and android.

Project1 is server side written in Python
 - crawler2.py parses the timetable from a website daily
 - downloads all AREA/SUBAREA/CLUB html files into a folder named "tempdata"
 - parses all AREA/SUBAREA/CLUB html files, and generates a json-formatted data file under "data" folder
   , naming "lessons_yyyymmdd.txt"
 - server exposes several urls, the most important one is /lessons/<areaId>_<subAreaId>_<clubId>_<roomId>
 - server returns JSON directly.

timetableapp uses a cross-platform framework - component one, to implement a mobile app in java.
 - main logic is placed in file StateMachine.java
 - it initialises a picture of empty timetable as background
 - user can select area, sub area, club, and room from options dialog
 - According to user selection of location, the main logic sends request to server, and returns lessons
 - According to returned lessons, the main logic generates buttons and places them into time table at the positons dynamically calculated from lessons' time.
 - when user clicks on one of those buttons, the detail information of the lesson will popup.

timetable_ios is an IOS version of app client, because there is performance issue of "component one" version.

![screenshot1](https://github.com/zhanzushun/timetable/blob/master/preview/480x854.jpg)

![screenshot2](https://github.com/zhanzushun/timetable/blob/master/preview/640x960.png)
