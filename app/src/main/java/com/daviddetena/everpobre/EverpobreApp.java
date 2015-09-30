package com.daviddetena.everpobre;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by daviddetena on 28/09/15.
 */
public class EverpobreApp extends Application {

    // Creamos referencia weak al contexto
    private static WeakReference<Context> context;

    @Override
    public void onCreate() {
        super.onCreate();

        // Obtenemos el contexto
        final Context c = getApplicationContext();
        context = new WeakReference<Context>(c);

        Log.d(EverpobreApp.class.getCanonicalName(), getString(R.string.log_everpobre_starting));
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(EverpobreApp.class.getCanonicalName(), getString(R.string.log_everpobre_low_memory));
    }
}
