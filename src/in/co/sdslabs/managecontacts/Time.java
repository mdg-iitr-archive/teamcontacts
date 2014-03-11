package in.co.sdslabs.managecontacts;

import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Time extends BroadcastReceiver {

	NotificationManager nm;

	@Override
	public void onReceive(Context context, Intent intent) {

		Intent myIntent = new Intent(context, firstsheet.class);
		nm = (NotificationManager) context
				.getSystemService(context.NOTIFICATION_SERVICE);
		CharSequence from = "Birthday";
		Calendar timeOff9 = (Calendar) Calendar.getInstance();
		int date1 = timeOff9.getTime().getDate();
		int month1 = (timeOff9.getTime().getMonth()+1);
		
		database1 db = new database1(context);
		db.open();
		int p = db.getCount1();
		int j = 0;
		String message1="";
		String[] date = new String[p];
		String[] name = db.getName1();
		String[] month = new String[p];
		String[] dob = db.getDOB1();
		while (j < p) {
			String dob1 = dob[j];
			date[j] = dob1.substring(0, 2);
			month[j] = dob1.substring(3, 5);
			if(date1==Integer.parseInt(date[j])&&month1==Integer.parseInt(month[j]))
			{
				message1=""+name[j];
				break;
			}
			j++;
		}
		
		
		CharSequence message = message1;
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				myIntent, 0);
		Notification notif = new Notification(R.drawable.birth_icon,
				"Birthday!!", System.currentTimeMillis());
		notif.flags = Notification.FLAG_INSISTENT;
		notif.setLatestEventInfo(context, from, message, contentIntent);
		notif.flags |= Notification.FLAG_AUTO_CANCEL;
		nm.notify(1, notif);
	}
}
