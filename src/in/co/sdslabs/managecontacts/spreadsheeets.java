package in.co.sdslabs.managecontacts;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class spreadsheeets extends Activity {
	public static final String PREFS_NAME = "MyPrefsFile";
	ListView pr_lv;

	@Override
	protected void onCreate(Bundle a) {
		super.onCreate(a);
		setContentView(R.layout.spreadsheets);
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		final SharedPreferences.Editor editor = settings.edit();
		pr_lv = (ListView) findViewById(R.id.privatelist);
		database1 db = new database1(spreadsheeets.this);
		db.open();
		int i = 0;
		int p = db.getCount0();
		String[] privatesprdsht = db.getPriavteSpreadShtName();
		ArrayList<String> data = new ArrayList<String>();
		while (i <= p) {
			data.add(privatesprdsht[i]);
			i++;
		}
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
				spreadsheeets.this, android.R.layout.simple_list_item_1, data);
		pr_lv.setAdapter(adapter1);
		pr_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String title = (String) (pr_lv.getItemAtPosition(arg2));
				editor.putString("spreadsheettitle", title);
				editor.commit();
				database1 db = new database1(spreadsheeets.this);
				db.open();
				db.emptyTABLE1();
				secondbackhand sb = new secondbackhand(spreadsheeets.this);
				db.close();
				sb.doit();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings: {
			LayoutInflater li = LayoutInflater.from(this);
			View dialogview = li.inflate(R.layout.dialognew, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setView(dialogview);
			final EditText user = (EditText) dialogview
					.findViewById(R.id.editText1);
			final EditText pass = (EditText) dialogview
					.findViewById(R.id.editText2);
			final SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
			final SharedPreferences.Editor editor = pref.edit();
			String username = pref.getString("username", "");
			String password = pref.getString("password", "");
			user.setText(username);
			pass.setText(password);
			alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									String user1 = user.getText().toString();
									String pass1 = pass.getText().toString();
									editor.putString("username", user1);
									editor.putString("password", pass1);
									editor.commit();
									firstbackhand fb = new firstbackhand(
											spreadsheeets.this);
									fb.doit();

									dialog.dismiss();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();

			break;
		}

		default:
			return false;
		}
		return false;
	}

}
