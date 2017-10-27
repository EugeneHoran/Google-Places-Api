package exercise.noteworth.com;

import android.app.Application;

import io.realm.Realm;

public class PlacesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
