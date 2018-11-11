# Homework 2 -  Running Dairy App

## Description
A Running Dairy App that :
* Allows user to input distance and time to calculate pace and speed.
* Shows real-time running route/direction on map.
* Captures the distance and time per run.
* Saves and displays history of runs with weekly averages
* Provides access to Music player


## Note
* Implemented with Android Camera2 API.
* Use only front (default) or back camera.
* Camera real-time preview starts as soon as camera opens.
* Taking photos freezes preview for 3 seconds.
* Previewing and Taking photo use independent CaptureSession.
* Photos are saved under ./images in external storage.
* Displayed images are down-sampled.
* Cropped images will overwrite the original files if saved.
* Undo cropping will not save and overwrite the originals.


## Libraries
* Android Image Cropper Library for cropping.
* Android Glide for imaging loading and caching.
