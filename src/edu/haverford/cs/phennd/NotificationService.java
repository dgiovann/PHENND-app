package edu.haverford.cs.phennd;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

/**
 * This lovely NotificationService stub class, complete with explanatory comments, is taken from:
 * http://it-ride.blogspot.com/2010/10/android-implementing-notification.html
 * There is additional explanation there that you should also read.
 */
public class NotificationService extends Service {
    
    private WakeLock mWakeLock;
    
    /**
     * Simply return null, since our Service will not be communicating with
     * any other components. It just does its work silently.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    /**
     * This is where we initialize. We call this when onStart/onStartCommand is
     * called by the system. We won't do anything with the intent here, and you
     * probably won't, either.
     */
    private void handleIntent(Intent intent) {
        // obtain the wake lock
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TAG");
        mWakeLock.acquire();
        
        // check the global background data setting
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (!cm.getBackgroundDataSetting()) {
            stopSelf();
            return;
        }
        
        // do the actual work, in a separate thread
        new PollTask().execute();
    }
    
    private class PollTask extends AsyncTask<Void, Void, Void> {
        /**
         * This is where YOU do YOUR work. There's nothing for me to write here
         * you have to fill this in. Make your HTTP request(s) or whatever it is
         * you have to do to get your updates in here, because this is run in a
         * separate thread
         */
    	
    	
        @Override
        protected Void doInBackground(Void... params) {
            // TODO: do stuff!
        	DataManager dm = DataManager.getDataManager(getBaseContext());
        	dm.buildArticles(DataManager.pullData());
            return null;
        }
        
        /**
         * In here you should interpret whatever you fetched in doInBackground
         * and push any notifications you need to the status bar, using the
         * NotificationManager. I will not cover this here, go check the docs on
         * NotificationManager.
         *
         * What you HAVE to do is call stopSelf() after you've pushed your
         * notification(s). This will:
         * 1) Kill the service so it doesn't waste precious resources
         * 2) Call onDestroy() which will release the wake lock, so the device
         *    can go to sleep again and save precious battery.
         */
        @Override
        protected void onPostExecute(Void result) {
        	
			if (DataManager.getUpdatedCount() > 0 ) {
				Context appContext = getBaseContext();
			NotificationManager notificationManager;
			Intent intent = new Intent(appContext, MainActivity.class);
			PendingIntent pIntent = PendingIntent.getActivity(appContext, 0, intent, 0);
			notificationManager = (NotificationManager)appContext.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager = (NotificationManager) appContext
					.getSystemService(Context.NOTIFICATION_SERVICE);
			int icon = R.drawable.ic_launcher;
			Integer countUpdated = Integer.valueOf(DataManager.getUpdatedCount());
			String tickerText = "New PHENND Update Articles";
			long when = System.currentTimeMillis();
			// This is deprecated, but the alternative (Notification Builder) isn't available in most of the API versions we target
			Notification notification = new Notification(icon, tickerText, when); 
			notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
			notification.setLatestEventInfo(appContext, "PHENND Update", countUpdated.toString() + " New Articles Posted", pIntent);
			notificationManager.notify(1, notification);
			DataManager.resetUpdatedCount();
			}
        	// TODO: do some stuff.
            stopSelf();
        }
    }
    
    /**
     * This is deprecated, but you have to implement it if you're planning on
     * supporting devices with an API level lower than 5 (Android 2.0).
     */
    @Override
    public void onStart(Intent intent, int startId) {
        handleIntent(intent);
    }
    
    /**
     * This is called on 2.0+ (API level 5 or higher). Returning
     * START_NOT_STICKY tells the system to not restart the service if it is
     * killed because of poor resource (memory/cpu) conditions.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_NOT_STICKY;
    }
    
    /**
     * In onDestroy() we release our wake lock. This ensures that whenever the
     * Service stops (killed for resources, stopSelf() called, etc.), the wake
     * lock will be released.
     */
    public void onDestroy() {
        super.onDestroy();
        mWakeLock.release();
    }
}
