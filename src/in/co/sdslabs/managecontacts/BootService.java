package in.co.sdslabs.managecontacts;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class BootService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		String filename = "userInfo";
		SharedPreferences getData;
		String v;
		getData = getSharedPreferences(filename, 0);
		v = getData.getString("set", "");

		if (v.equals("done")) {

			String[] date = null;
			String[] month = null;
			database1 db = new database1(BootService.this);
			db.open();
			int p = db.getCount1();
			int j = 0;
			String[] dob = db.getDOB1();
			while (j < p) {
				String[] row = dob[j].split("/");
				date[j] = row[0];
				month[j] = row[1];
			}
			AlarmManager mgrAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
			ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

			for (int i = 0; i < 3; i++) {
				Intent intent = new Intent(this, Time.class);
				// Loop counter `i` is used as a `requestCode`
				PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
						i, intent, 0);
				// Single alarms in 1, 2, ..., 10 minutes (in `i` minutes)
				Calendar timeOff9 = (Calendar) Calendar.getInstance();
				int year = timeOff9.getTime().getYear();
				timeOff9.set(year, Integer.parseInt(date[i]), Integer.parseInt(month[i]));
				timeOff9.set(Calendar.HOUR_OF_DAY, 0);
				timeOff9.set(Calendar.MINUTE, 0);
				timeOff9.set(Calendar.SECOND, 0);
				mgrAlarm.set(AlarmManager.RTC_WAKEUP,
						timeOff9.getTimeInMillis(), pendingIntent);

				intentArray.add(pendingIntent);
			}
		}
	}
}
