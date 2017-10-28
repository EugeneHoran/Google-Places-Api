# Noteworth

- Download Link: https://rink.hockeyapp.net/apps/de2f4ddb0d4142c69efdf7212bc0d5fc/app_versions/3
- YouTube Video: https://youtu.be/wSCCk9iEgwU


## Problem
> Using Places API, display a list of restaurants based on food type and location. 
User should be able to sort the list by Best Match, Distance, and Most Reviewed.
A user should be taken to a details page of the restaurant based on the restaurant they select. 
There should be a separation between the data and view layer. The app should be extensible.
## Solution
- Google Places API
  > After going through the documentation I realized that there were different ways to implement the search.
  I decided to use Lat and Lon for the query parameters. 
  To get the Lat and Lon, I used last known location(which might not work in emulators if no apps requested the users location) 
  and the option to search(also the fallback if last known location is null) for locations.  
 - Sort the list by Best Match, Distance, and Most Reviewed
   > The [Places API Search](https://developers.google.com/places/web-service/search) only provided the option to sort the list (paramater "rankby") by "prominence (default Best Matched) and distance." 
   Although, I tried to filter it by Most Reviewed, the data would have been inaccurate. 
    The user can filter via Spinner.
- Details Page
  > Using [Place Details API](https://developers.google.com/places/web-service/details) I pulled the data and presented it similar to how google presents their data in Google Maps.
- Separation of Data and View Layer
  > All the data is handled inside of ViewModel classes(Retains data on screen rotation). 
  The views are handled via DataBinding and LiveData.
   Originally, I was using MVP architecture but decided to use Google's latest Architecture Library. 
   The ViewModel retains data on screen rotation. The LiveData to handle any data changes. 

# Architectural Reasons
- Network and Response handling
  - I used Retrofit, RxJava, GSON and okHTTP to handle connection and data request. Why? In my opinion, they are the most robust libraries and easy to test with. 
- Cache
  - I used Realm to cache the users location's lat, lon, and name. Typically I would just use shared preferences for small amounts of data. 
- Data Handling
  - All data is handled inside of ViewModels. ViewModels are one of Googles latest libraries. I believe ViewModels are going to be the future of Android Architecture. Due to that I will be moving away from MVP. 
- View Handling
  - All views are handled via DataBinding(with few exceptions). 
  - DataBinding eliminates the need for method calls “findViewById” and “setText...” It also decreases chances of leaking views. 
  


### Photos of Restaurant Search Results and Filter Options  
<p float="top">
<img src="https://github.com/EugeneHoran/Noteworth/blob/master/images/device-2017-10-28-130332.png" width="220" />
<img src="https://github.com/EugeneHoran/Noteworth/blob/master/images/device-2017-10-28-131031.png" width="220"  />
</p>

### Photos of Restaurant details 
<p float="top">
<img src="https://github.com/EugeneHoran/Noteworth/blob/master/images/device-2017-10-28-130427.png" width="220" />
<img src="https://github.com/EugeneHoran/Noteworth/blob/master/images/device-2017-10-28-130439.png" width="220"  />
</p>

### Libraries

```
   // Play
    compile 'com.google.android.gms:play-services-places:11.4.2'
    compile 'com.google.android.gms:play-services-location:11.4.2'
    // Network
    compile 'com.squareup.retrofit2:retrofit:2.3.0'
    compile 'com.squareup.retrofit2:converter-gson:2.3.0'
    compile 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
    compile 'com.squareup.okhttp3:logging-interceptor:3.8.0'
    // Rx
    compile 'io.reactivex.rxjava2:rxjava:2.1.3'
    compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
    // Support
    compile "com.android.support:appcompat-v7:${android_support_lib_version}"
    compile "com.android.support:design:${android_support_lib_version}"
    compile "com.android.support:recyclerview-v7:${android_support_lib_version}"
    compile "com.android.support:cardview-v7:${android_support_lib_version}"
    compile 'com.android.support.constraint:constraint-layout:1.0.2'
    // Permission
    compile 'pub.devrel:easypermissions:0.1.5'
    // Glide
    compile 'com.github.bumptech.glide:glide:4.2.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.2.0'
    // ViewPager Indicator
    compile 'com.github.vivchar:ViewPagerIndicator:v1.0.1'
    // Arch
    compile 'android.arch.lifecycle:extensions:1.0.0-rc1'
```
