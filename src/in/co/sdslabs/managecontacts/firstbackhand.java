package in.co.sdslabs.managecontacts;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Looper;
import android.widget.Toast;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.util.ServiceException;

public class firstbackhand {
	public static final String PREFS_NAME = "MyPrefsFile";
	Context context;

	public firstbackhand(Context context1) {
		this.context = context1;
	}

	public void doit() {
		// TODO Auto-generated method stub
		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		String user = settings.getString("username", null);
		String pass = settings.getString("password", null);
		if (user.equals("") || pass.equals("")) {
			Toast.makeText(context, "Please fill in the details correctly.",
					Toast.LENGTH_SHORT).show();
		} else {
			database1 db = new database1(context);
			db.open();
			db.emptyTABLE1();
			new RetreiveFeedTask().execute(user, pass);
			db.close();
		}
	}

	class RetreiveFeedTask extends AsyncTask<String, String, String> {
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(context, "List:",
					"Please wait while the list of spreadsheets is fetched.",
					true);
			mProgressDialog.setCancelable(true);
		}

		protected String doInBackground(String... user) {

			Looper.prepare();

			database1 db = new database1(context);
			db.open();
			String USERNAME = user[0];
			String PASSWORD = user[1];
			// Google Authorization using clientLogin
			SpreadsheetService service = new SpreadsheetService(
					"MySpreadsheetIntegration-v1");
			try {

				service.setProtocolVersion(SpreadsheetService.Versions.V3);
				service.setUserCredentials(USERNAME, PASSWORD);
			} catch (com.google.gdata.util.AuthenticationException e) {
				// TODO Auto-generated catch block
				mProgressDialog.dismiss();
				Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
			}
			URL metafeedUrl;
			try {

				metafeedUrl = new URL(
						"https://spreadsheets.google.com/feeds/spreadsheets/private/full");
				SpreadsheetFeed spreadsheetFeed = service.getFeed(metafeedUrl,
						SpreadsheetFeed.class);

				List<SpreadsheetEntry> spreadsheets = spreadsheetFeed
						.getEntries();
				db.emptyTABLE0();
				for (SpreadsheetEntry entry : spreadsheets) {
					db.createEntry0(entry.getTitle().getPlainText(), "0");
				}
				Intent intent = new Intent(context, spreadsheeets.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
				android.os.Process.killProcess(android.os.Process.myPid());
			} catch (MalformedURLException e) {

				mProgressDialog.dismiss();
				Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
			} catch (IOException e) {

				mProgressDialog.dismiss();
				Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
			} catch (ServiceException e) {

				mProgressDialog.dismiss();
				Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
			}
			Looper.loop();
			return null;

		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		protected void onPostExecute() {
		}
	}
}
