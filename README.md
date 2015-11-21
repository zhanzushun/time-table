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




