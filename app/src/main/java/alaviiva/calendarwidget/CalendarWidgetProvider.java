package alaviiva.calendarwidget;

import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.content.Context;


public class CalendarWidgetProvider extends AppWidgetProvider {
    private final CalenrarReader cr = new CalenrarReader();

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        cr.read(context);

    }

}
