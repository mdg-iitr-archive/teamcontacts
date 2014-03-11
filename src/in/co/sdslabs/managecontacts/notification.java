package in.co.sdslabs.managecontacts;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

public class notification extends Activity {
	Button notify;
	String filename = "userInfo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SavePreferences("set", "done");
		AlarmManager mgrAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
		ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
		database1 db = new database1(notification.this);
		db.open();
		int p = db.getCount1();
		int j = 0;
		String[] date = new String[p];
		String[] month = new String[p];
		String[] dob = db.getDOB1();
		while (j < p) {
			String dob1 = dob[j];
			date[j] = dob1.substring(0, 2);
			month[j] = dob1.substring(3, 5);
			j++;
		}
		for (int i = 0; i < p; i++) {
			Intent intent = new Intent(this, Time.class);
			// Loop counter `i` is used as a `requestCode`
			PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i,
					intent, 0);
			Calendar timeOff9 = (Calendar) Calendar.getInstance();
			int date1 = timeOff9.getTime().getDate();
			int month1 = timeOff9.getTime().getMonth();
			int year = timeOff9.getTime().getYear();
			timeOff9.set(year, Integer.parseInt(date[i]),
					Integer.parseInt(month[i]));
			timeOff9.set(Calendar.HOUR_OF_DAY, 0);
			timeOff9.set(Calendar.MINUTE, 0);
			timeOff9.set(Calendar.SECOND, 0);
			Calendar calnow = (Calendar) Calendar.getInstance();
			if (timeOff9.compareTo(calnow) < 0) {
				timeOff9.add(Calendar.DATE, 1);
			}
			mgrAlarm.set(AlarmManager.RTC_WAKEUP, timeOff9.getTimeInMillis(),
					pendingIntent);

			intentArray.add(pendingIntent);
			Intent i1 = new Intent(this, firstsheet.class);
			startActivity(i1);
		}
	}

	private void SavePreferences(String key, String value) {
		SharedPreferences sharedPreferences = getSharedPreferences(filename, 0);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
}
