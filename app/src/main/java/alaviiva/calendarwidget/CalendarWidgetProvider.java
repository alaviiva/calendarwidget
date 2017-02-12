package alaviiva.calendarwidget;

import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CalendarWidgetProvider extends AppWidgetProvider {
    private final CalendarReader cr = new CalendarReader();

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Cursor cur = cr.read(context);
        String msg = "";

        while (cur.moveToNext()) {
            String title = null;
            long beginVal = 0;

            beginVal = cur.getLong(CalendarReader.PROJECTION_BEGIN_INDEX);
            title = cur.getString(CalendarReader.PROJECTION_TITLE_INDEX);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(beginVal);
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            msg += formatter.format(calendar.getTime());
            msg += "\n" + title + "\n";
        }
        Log.d("calendar", msg);
        //View.findViewById(R.id.twiew).setText(msg);

    }

}
