package com.example.android.shustudenthelper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Surya Gogineni on 11/10/2016.
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // intent.getExtras().getInt("myID");
        //System.out.println( intent.getIntExtra("myID", 0));
        // Log.e(TAG, "onReceive: main");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // if (intent.getIntExtra("myID", 0)==123){
          System.out.println("In  IF condition");
//       //     Log.e(TAG, "onReceive: if");
        String title = intent.getStringExtra("Title");
        System.out.println("In  title condition" + title);
        String name = intent.getStringExtra("Name");
        String dueDate = intent.getStringExtra("Date");
        int Counter = intent.getIntExtra("Counter",0);
        Intent intentMy = new Intent(context, MyCustomHomeActivity.class);
        //   System.out.println("intent for main activity");
        PendingIntent pi = PendingIntent.getActivity(context, Counter, intentMy, 0);
          System.out.println("pending intent for main activity");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        long[] pattern = {0, 300, 0};
        mBuilder.setSmallIcon(R.drawable.shusmalllogo)
                .setContentTitle(title)
                .setContentText(name + " - " + dueDate)
                .setVibrate(pattern)
                .setAutoCancel(true);
        mBuilder.setContentIntent(pi);
        manager.notify(Counter, mBuilder.build());
    }

}
