
package com.example.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Date;


public class NewAppWidget extends AppWidgetProvider {


    private static final String SHARED_PREF_FILE = BuildConfig.APPLICATION_ID;
    private static final String COUNT_KEY = "count";


    private void updateAppWidget(Context context,
                                 AppWidgetManager appWidgetManager,
                                 int appWidgetId) {


        SharedPreferences prefs =
                context.getSharedPreferences(SHARED_PREF_FILE, 0);
        int count = prefs.getInt(COUNT_KEY + appWidgetId, 0);
        count++;


        String dateString =
                DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());


        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_id,
                String.valueOf(appWidgetId));
        views.setTextViewText(R.id.appwidget_update,
                context.getResources().getString(
                        R.string.date_count_format, count, dateString));


        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(COUNT_KEY + appWidgetId, count);
        prefEditor.apply();


        Intent intentUpdate = new Intent(context, NewAppWidget.class);


        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);


        int[] idArray = new int[]{appWidgetId};
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);


        PendingIntent pendingUpdate = PendingIntent.getBroadcast(context,
                appWidgetId, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);


        views.setOnClickPendingIntent(R.id.button_update, pendingUpdate);


        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * Override for onUpdate() method, to handle all widget update requests.
     *
     * @param context          The application context.
     * @param appWidgetManager The app widget manager.
     * @param appWidgetIds     An array of the app widget IDs.
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

