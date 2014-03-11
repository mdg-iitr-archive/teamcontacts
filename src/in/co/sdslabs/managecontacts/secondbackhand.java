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
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

public class secondbackhand {
	public static final String PREFS_NAME = "MyPrefsFile";
	Context context;

	public secondbackhand(Context context1) {
		this.context = context1;
	}

	public void doit() {
		// TODO Auto-generated method stub
		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		String user = settings.getString("username", null);
		String pass = settings.getString("password", null);
		String title = settings.getString("spreadsheettitle", null);
		Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
		RetreiveFeedTask task = new RetreiveFeedTask();
		task.execute(user, pass, title);
	}

	class RetreiveFeedTask extends AsyncTask<String, String, String> {
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(context, "Fetching Spreadsheet:",
					"Please wait until the spreadsheet is fetched.", false);
		}

		protected String doInBackground(String... user) {

			Looper.prepare();

			String USERNAME = user[0];
			String PASSWORD = user[1];
			String spreadsheetTitle = user[2];
			SpreadsheetService service = new SpreadsheetService(
					"MySpreadsheetIntegration-v1");
			try {

				service.setProtocolVersion(SpreadsheetService.Versions.V3);
				service.setUserCredentials(USERNAME, PASSWORD);
				// service.setReadTimeout(1);
			} catch (com.google.gdata.util.AuthenticationException e) {
				// TODO Auto-generated catch block
				mProgressDialog.dismiss();
				Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
			}

			SpreadsheetEntry spreadsheet = null;
			URL metafeedUrl;
			try {
				Toast.makeText(context, user[2], Toast.LENGTH_SHORT).show();
				metafeedUrl = new URL(
						"https://spreadsheets.google.com/feeds/spreadsheets/private/full");
				SpreadsheetFeed spreadsheetFeed = service.getFeed(metafeedUrl,
						SpreadsheetFeed.class);

				List<SpreadsheetEntry> spreadsheets = spreadsheetFeed
						.getEntries();
				for (SpreadsheetEntry entry : spreadsheets) {
					if (entry.getTitle().getPlainText()
							.equals(spreadsheetTitle)) {
						spreadsheet = entry;
					}
				}
				WorksheetFeed worksheetFeed = service.getFeed(
						spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
				List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
				WorksheetEntry worksheet = worksheets.get(0);
				URL listFeedUrl = worksheet.getListFeedUrl();
				ListFeed feed = service.getFeed(listFeedUrl, ListFeed.class);
				database1 db = new database1(context);
				db.open();
				for (ListEntry row : feed.getEntries()) {
					String[] rowname = new String[100];
					int j = 0;
					for (String tag : row.getCustomElements().getTags()) {
						rowname[j] = row.getCustomElements().getValue(tag);
						j++;
					}
					db.createEntry1(rowname[3], rowname[5], rowname[4],
							rowname[2], rowname[1], rowname[0]);
				}
				db.close();
				mProgressDialog.dismiss();
				Toast.makeText(context, "SpreadSheet Updated.. :)",
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent(context, firstsheet.class);
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
			mProgressDialog.dismiss();
		}
	}
}
