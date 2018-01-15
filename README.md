# Google Places API 

- Download Link: https://rink.hockeyapp.net/apps/de2f4ddb0d4142c69efdf7212bc0d5fc/app_versions/3
- YouTube Video: https://youtu.be/wSCCk9iEgwU


## Goal
> Using Places API, display a list of restaurants based on food type and location. 
User should be able to sort the list by Best Match, Distance, and Most Reviewed.
A user should be taken to a details page of the restaurant based on the restaurant they select. 
There should be a separation between the data and view layer. The app should be extensible.


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
