package in.co.sdslabs.managecontacts;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

public class add {
	Context context;

	public add(Context context1) {
		this.context = context1;
	}

	public void doit() {
		// TODO Auto-generated method stub
		new RetreiveFeedTask().execute();
	}

	class RetreiveFeedTask extends AsyncTask<String, String, String> {
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = ProgressDialog
					.show(context,
							"Import::",
							"Please wait while the Contacts are being imported..",
							true);
		}

		protected String doInBackground(String... user) {

			Looper.prepare();
			database1 db = new database1(context);
			db.open();
			int p = db.getCount1();
			int j = 0;
			while (j < p) {

				String[] name = db.getName1();
				String[] Number = db.getContact1();
				String[] email = db.getEmail1();
				String DisplayName = name[j];
				String MobileNumber = Number[j];
				String emailID = email[j];
				int length = String.valueOf(Number[j]).length();
				if (length == 10) {
					ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

					ops.add(ContentProviderOperation
							.newInsert(ContactsContract.RawContacts.CONTENT_URI)
							.withValue(
									ContactsContract.RawContacts.ACCOUNT_TYPE,
									null)
							.withValue(
									ContactsContract.RawContacts.ACCOUNT_NAME,
									null).build());

					if (DisplayName != null) {
						ops.add(ContentProviderOperation
								.newInsert(ContactsContract.Data.CONTENT_URI)
								.withValueBackReference(
										ContactsContract.Data.RAW_CONTACT_ID, 0)
								.withValue(
										ContactsContract.Data.MIMETYPE,
										ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
								.withValue(
										ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
										DisplayName).build());
					}

					if (MobileNumber != null) {
						ops.add(ContentProviderOperation
								.newInsert(ContactsContract.Data.CONTENT_URI)
								.withValueBackReference(
										ContactsContract.Data.RAW_CONTACT_ID, 0)
								.withValue(
										ContactsContract.Data.MIMETYPE,
										ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
								.withValue(
										ContactsContract.CommonDataKinds.Phone.NUMBER,
										MobileNumber)
								.withValue(
										ContactsContract.CommonDataKinds.Phone.TYPE,
										ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
								.build());
					}

					if (emailID != null) {
						ops.add(ContentProviderOperation
								.newInsert(ContactsContract.Data.CONTENT_URI)
								.withValueBackReference(
										ContactsContract.Data.RAW_CONTACT_ID, 0)
								.withValue(
										ContactsContract.Data.MIMETYPE,
										ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
								.withValue(
										ContactsContract.CommonDataKinds.Email.DATA,
										emailID)
								.withValue(
										ContactsContract.CommonDataKinds.Email.TYPE,
										ContactsContract.CommonDataKinds.Email.TYPE_WORK)
								.build());
					}

					try {
						context.getContentResolver().applyBatch(
								ContactsContract.AUTHORITY, ops);

					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(context, "Exception: " + e.getMessage(),
								Toast.LENGTH_SHORT).show();
					}
					j++;
				} else {
					Toast.makeText(context, "Improper Number " + Number[j],
							100).show();
					j++;
				}
			}
			mProgressDialog.dismiss();
			Toast.makeText(context, p + " contacts created.", Toast.LENGTH_LONG)
					.show();
			Looper.loop();
			return null;

		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
		}

		protected void onPostExecute() {
		}
	}
}
