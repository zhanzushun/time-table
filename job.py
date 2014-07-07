#!/user/bin/python
# -*- coding: utf-8 -*-
import subprocess

if __name__ == '__main__':
    try:
        print subprocess.check_output('svn delete lessons*.txt', shell=True, cwd='/home/ubuntu/timetable/1/data')
        print subprocess.check_output('python /home/ubuntu/timetable/1/crawler2.py', shell=True)
        print subprocess.check_output('svn add lessons*.txt', shell=True, cwd='/home/ubuntu/timetable/1/data')
        print subprocess.check_output('svn commit -m "data"', shell=True, cwd='/home/ubuntu/timetable/1/data')
    except subprocess.CalledProcessError:
        print subprocess.CalledProcessError.message
        
