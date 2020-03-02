package com.oiranca.pglproject;


import com.google.firebase.database.FirebaseDatabase;

public class Persistence extends android.app.Application {

/*Es la clase que nos permite que Firebase se actualic después de recuperar la conexión a internet*/
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }

}
