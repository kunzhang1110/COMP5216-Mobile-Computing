# Easy Diet - COMP5216 Mobile Computing
**University of Sydney**

*Semester 2, 2018*

## Description
An Android App that makes it easy for people to loose fat (not just weight!)...

## Usage
1. Download the app source code, or clone it to your directory, with
```
git clone https://github.com/kunzhang1110/COMP5216-Mobile-Computing/tree/master/Project/Easy_Diet
```
1. Open Android Studio - Open file from your directory
1. After Gradle successfully built, the app should be able to run in an Android Virtual Device compatible with the Testing Device, or on a compatible Android Device.


### Testing Environment
- Device: NOKIA 6 TA-1033
    - Display Resolution: 1080 x 1920 pixels, 16:9 ratio (~403 ppi density)
    - Android OS: version 7.1.1
- API: Android API 28
- Java: Java 8
- IDE: Android Studio 3.2.1

### Android Studio
Android Studio 3.2.1
- Using any previous version needs to locate build.gradle (Module.app) and replace ``` implementation 'com.android.support:appcompat-v7:28.0.0'```
to ```implementation 'com.android.support:appcompat-v7:28.0.0-alpha1'
```, to be able to preview layout.
- Layout Preview uses Nexus 4 as a default device

## Library Used
- com.android.volley:volley:1.1.0
- com.jjoe64:graphview:4.2.2
- joda-time:joda-time
- android.arch.persistence.room

## Info
written by Kun Zhang (kunzhang1110@gmail.com), Juan Huang (hj0726j@gmail.com), Juan Pablo Molina and Zane Li.
