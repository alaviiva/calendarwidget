package alaviiva.calendarwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CalendarWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        CalendarViewsFactory.update();
        Log.d("calendar", "Updating widget");

        for (int i = 0; i < appWidgetIds.length; i++) {
            Intent in = new Intent(context, CalendarWidgetService.class);
            in.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            in.setData(Uri.parse(in.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.calendarwidget);
            rv.setRemoteAdapter(R.id.listv, in);

            // update date
            Calendar calendar = Calendar.getInstance();
            DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            rv.setTextViewText(R.id.buttn, formatter.format(calendar.getTime()));

            // clicking date opens calendar
            Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
            builder.appendPath("time");
            ContentUris.appendId(builder, calendar.getTimeInMillis());
            Intent calendarIn = new Intent();
            calendarIn.setAction(Intent.ACTION_VIEW);
            calendarIn.setData(builder.build());
            PendingIntent pendingCalendar = PendingIntent.getActivity(context, 0, calendarIn, 0);
            rv.setOnClickPendingIntent(R.id.buttn, pendingCalendar);

            // clicking event opens calendar
            Intent eventIn = new Intent();
            eventIn.setAction(Intent.ACTION_VIEW);
            PendingIntent pendingEvent = PendingIntent.getActivity(context, 0, eventIn, 0);
            rv.setPendingIntentTemplate(R.id.listv, pendingEvent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}
