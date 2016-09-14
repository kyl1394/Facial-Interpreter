# VictoryRoad - Attendance Tracker

This application was done for SE 329 - Software Management, at Iowa State University.
The goal was to create an application that could interface with a facial recognition api to overlay information about users so that
a professor or other lecturer can more easily learn name of their audience as well as information about them.

# How to set up OpenCV:
1. Add libs\opencv\build\java\opencv2413.jar as a library
 ### In IntelliJ 2016.2.4:
 1. Go to File -> Project Structure.
 2. On the left hand side, go to Libraries
 3. Click the Green Plus on the top of the screen to add a Library
 4. Select Java
 5. Navigate to libs\opencv\build\java and click on opencv2413.jar
 6. Click Okay and after the screen close, click apply

2. Add -Djava.library.path="libs\opencv\build\java\x86" to the VM options in your runtime configurations
 ### In IntelliJ 2016.2.4:
 1. Open Run -> Edit Configurations
 2. Click on Application, then click on Main
 3. Under VM options (under the Configuration tab), add -Djava.library.path="libs\opencv\build\java\x86"
 4. Click Apply
