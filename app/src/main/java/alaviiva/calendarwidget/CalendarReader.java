package alaviiva.calendarwidget;

import java.util.Calendar;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract.Instances;
import android.database.Cursor;
import android.content.ContentResolver;


public class CalendarReader {
    public static final String[] INSTANCE_PROJECTION = new String[] {
            Instances.EVENT_ID,      // 0
            Instances.BEGIN,         // 1
            Instances.TITLE          // 2
    };
    // The indices for the projection array above.
    public static final int PROJECTION_ID_INDEX = 0;
    public static final int PROJECTION_BEGIN_INDEX = 1;
    public static final int PROJECTION_TITLE_INDEX = 2;


    public static Cursor read(Context context) {

        Calendar beginTime = Calendar.getInstance();
        long beginms = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.roll(Calendar.MONTH, 1);
        long endms = endTime.getTimeInMillis();

        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();

        Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, beginms);
        ContentUris.appendId(builder, endms);

        cur = cr.query(builder.build(), INSTANCE_PROJECTION, null, null, Instances.BEGIN);

        return cur;
        /*
        while (cur.moveToNext()) {
            String title = null;
            long eventID = 0;
            long beginVal = 0;

            // Get the field values
            eventID = cur.getLong(PROJECTION_ID_INDEX);
            beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
            title = cur.getString(PROJECTION_TITLE_INDEX);

            // Do something with the values.
            Log.i("calendar", "Event:  " + title);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(beginVal);
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Log.i("calendar", "Date: " + formatter.format(calendar.getTime()));
        }
        */
    }


}
