package alaviiva.calendarwidget;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
    private Context context;
    private int appWidgetId;
    private Cursor cur;

    public CalendarViewsFactory(Context con, Intent in) {
        context = con;
        appWidgetId = in.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager
                .INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        cur = CalendarReader.read(context);

    }

    @Override
    public int getCount() {
        return cur.getCount();
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.calendar_item);
        cur.moveToPosition(position);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cur.getLong(CalendarReader.PROJECTION_BEGIN_INDEX));
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        rv.setTextViewText(R.id.date, formatter.format(calendar.getTime()));

        rv.setTextViewText(R.id.event, cur.getString(CalendarReader.PROJECTION_TITLE_INDEX));

        return rv;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }



    @Override
    public void onDestroy() {
        cur.close();
    }
}
