# Masterpieces Hall

This Capstone project is part of [**Android Developer Nanodegree Scholarship Program**](https://www.udacity.com/google-scholarships) by
**Udacity** and **Google**. The app showcases art works from the [**Rijksmuseum Collection**](https://www.rijksmuseum.nl/en) as provided from its REST API. Users can explore some of the most famous paintings and figures of this collection, read the work description and get more details about the technique used, the dimensions, work period etc.

## Features
* App lists artworks from painting and figures categories
* User can create a personal collection locally
* Provides details view (activity) with more info for each art object
* App offers sharing functionality with some info about any art object.
* Search for the future events of Rijksmuseum by picking a date in DatePickerDialog (events are retrieved through AsyncTask)
* Provides offline app usage through Retrofit cashing
* Zoomable and pannable artwork image in details view
* Homescreen widget displays last saved artwork (or default image if database empty). Clicking on widget takes to the details view (activity) of that artwork.
* Test banner and interstitial ads are displayed from Google's Mobile AdMob
* Firebase Analytics is used to track events and user engagement

## Screenshots

![Masterpieces Hall  Phone](https://raw.githubusercontent.com/SamuelaAnastasi/MasterpiecesHall/master/previews/preview.jpg)  

## Instructions
Please ensure you have a valid API KEY from [**Rijksmuseum**](https://www.rijksmuseum.nl/en) to use this app. The API KEY can be obtained through the advanced settings of your Rijksmuseum user account. Full info at REST API [**documentation**](https://www.rijksmuseum.nl/en/api). A valid key will need to be entered in gradle.properties file:

```
    API_KEY="your_api_key"
```


## Libraries
The project uses the following libraries:
* [Retrofit](http://square.github.io/retrofit/) and [GSON](https://github.com/google/gson) converter
* [Butter Knife](http://jakewharton.github.io/butterknife/)
* [Picasso](http://square.github.io/picasso/)
* [PhotoView](https://github.com/chrisbanes/PhotoView)

### Project License
This project is licensed under the [MIT Licence](https://opensource.org/licenses/MIT)

Copyright &copy; 2018 - Samuela Anastasi
