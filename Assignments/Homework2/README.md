# Homework 2 - Camera App

A Camera App that takes, displays, store and crops photos.

## Note

- Implemented with Android Camera2 API.
- Use only front (default) or back camera.
- Camera real-time preview starts as soon as camera opens.
- Taking photos freezes preview for 3 seconds.
- Previewing and Taking photo use independent CaptureSession.
- Photos are saved under ./images in external storage.
- Displayed images are down-sampled.
- Cropped images will overwrite the original files if saved.
- Undo cropping will not save and overwrite the originals.

## Libraries

- Android Image Cropper Library for cropping.
- Android Glide for imaging loading and caching.
