# Homework 3 - Running Dairy App

A Running Dairy App that :

- shows real-time running route/direction on map,
- captures the distance and time per run,
- saves and displays history of runs with weekly averages,
- allows user to input distance and time to calculate pace and speed, and
- provides access to default music player.

## Note

- Use FINE_LOCATION for geo-locations.
- Request location information every 2 seconds.
- Routes are displayed as polylines connecting the locations.
- Map's camera moves according to the latest location.
- User can pause current tracking with a toggle Start/Pause button.
- Switching to other apps (e.g. music player) will not stop tracking.
- Log entries are displayed as expandable rows, grouped by weeks with weekly info shown.
- UI: Tabs swiping are implemented with ViewPager.
- Data saved locally (SQLite) using ROOM.

## Libraries

- Google Mobile Service Maps.
- Google Mobile Service Location.
- Google Maps utils.
- Joda, to manipulate date/time.
