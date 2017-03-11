package alaviiva.calendarwidget;


import android.appwidget.AppWidgetManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CalendarViewsFactory(getApplicationContext(), intent);
    }
}

class CalendarViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static Context context;
    private int appWidgetId;
    private static Cursor cur;

    public static void update() {
        if (context != null) {
            cur = CalendarReader.read(context);
        }
    }

    public CalendarViewsFactory(Context con, Intent in) {
        context = con;
        appWidgetId = in.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        onDataSetChanged();
    }

    @Override
    public void onDataSetChanged() {
        cur = CalendarReader.read(context);
    }

    @Override
    public int getCount() {
        return cur.getCount();
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.calendar_item);
        cur.moveToPosition(position);

        // date
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cur.getLong(CalendarReader.PROJECTION_BEGIN_INDEX));
        DateFormat formatter = new SimpleDateFormat("dd.MM HH:mm");
        rv.setTextViewText(R.id.date, formatter.format(calendar.getTime()));

        // event name
        rv.setTextViewText(R.id.event, cur.getString(CalendarReader.PROJECTION_TITLE_INDEX));

        // intent to open calendar
        Intent fillIn = new Intent();
        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI,
                getItemId(position));
        fillIn.setData(uri);
        rv.setOnClickFillInIntent(R.id.calendaritem, fillIn);

        return rv;
    }

    @Override
    public long getItemId(int position) {
        cur.moveToPosition(position);
        return cur.getLong(CalendarReader.PROJECTION_ID_INDEX);
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDestroy() {
        cur.close();
    }
}
