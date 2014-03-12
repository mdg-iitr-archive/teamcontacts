package in.co.sdslabs.managecontacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class firstsheet extends Activity {
	public static final String PREFS_NAME = "MyPrefsFile";
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView tv = (TextView) findViewById(R.id.textView1);
		final SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
		String title = pref.getString("spreadsheettitle", null);
		tv.setText(title);
		database1 db = new database1(firstsheet.this);
		db.open();
		int p = db.getCount0();
		if (p == 0) {
			LayoutInflater li = LayoutInflater.from(this);
			View dialogview = li.inflate(R.layout.dialognew, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setView(dialogview);
			final EditText user = (EditText) dialogview
					.findViewById(R.id.editText1);
			final EditText pass = (EditText) dialogview
					.findViewById(R.id.editText2);
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
											firstsheet.this);
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
		} else if (db.getCount1() == 0) {
			Toast.makeText(this, "No spreadsheet selected. Select one.",
					Toast.LENGTH_SHORT).show();
			Intent i = new Intent(firstsheet.this, spreadsheeets.class);
			finish();
			startActivity(i);
		}

		expListView = (ExpandableListView) findViewById(R.id.lvExp);

		prepareListData();

		listAdapter = new ExpandableListAdapter(this, listDataHeader,
				listDataChild);

		expListView.setAdapter(listAdapter);

		expListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return false;
			}
		});
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
			}
		});

		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
			}
		});
		expListView.setOnChildClickListener(new OnChildClickListener() {

			public boolean onChildClick(ExpandableListView parent, View v,
					final int groupPosition, final int childPosition, long id) {
				// TODO Auto-generated method stub
				if (childPosition == 0) {
					String total;
					total = ((listDataChild.get(listDataHeader
							.get(groupPosition)).get(childPosition)));
					StringBuilder str = new StringBuilder(total);
					final String number1 = str.substring(10, 20);
					if (number1.matches(".*\\d.*")) {

						final long number = Long.parseLong(number1);

						int length = String.valueOf(number).length();
						if (length == 10) {
							final Dialog dialog = new Dialog(firstsheet.this);
							dialog.setContentView(R.layout.dialogbox);
							dialog.setTitle(listDataHeader.get(groupPosition));
							ImageButton call = (ImageButton) dialog
									.findViewById(R.id.call);

							call.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent callIntent = new Intent(
											Intent.ACTION_CALL);
									callIntent.setData(Uri.parse("tel:"
											+ number));
									startActivity(callIntent);

								}
							});
							ImageButton message = (ImageButton) dialog
									.findViewById(R.id.msg);
							message.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									try {
										Intent sendIntent = new Intent(
												Intent.ACTION_VIEW);
										sendIntent.putExtra("sms_body", "");
										sendIntent.putExtra("address", number1);
										sendIntent
												.setType("vnd.android-dir/mms-sms");
										startActivity(sendIntent);

									} catch (Exception e) {
										Toast.makeText(
												getApplicationContext(),
												"SMS faild, please try again later!",
												Toast.LENGTH_LONG).show();
										e.printStackTrace();
									}

								}
							});

							dialog.show();
						}
					}
				}
				if (childPosition == 1) {
					String total;
					total = ((listDataChild.get(listDataHeader
							.get(groupPosition)).get(childPosition)));
					StringBuilder str = new StringBuilder(total);
					final String number1 = str.substring(11, str.length());
					if (number1.contains("@")) {
						Intent massemail = new Intent(Intent.ACTION_SEND);

						massemail.putExtra(Intent.EXTRA_EMAIL,
								new String[] { number1 });
						massemail.setType("message/rfc822");
						startActivity(Intent.createChooser(massemail,
								"Choose an Email client :"));
					}
				}
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menus, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		database1 db = new database1(firstsheet.this);
		db.open();
		String[] email = db.getEmail1();
		switch (item.getItemId()) {

		case R.id.massemail: {
			Intent massemail = new Intent(Intent.ACTION_SEND);
			massemail.putExtra(Intent.EXTRA_EMAIL, email);
			massemail.setType("message/rfc822");
			startActivity(Intent.createChooser(massemail,
					"Choose an Email client :"));
			break;
		}
		case R.id.contactimport: {
			AlertDialog.Builder alrt = new AlertDialog.Builder(firstsheet.this);
			alrt.setTitle("Conformation::");
			alrt.setMessage("Are you sure to import all Contacts to your phone..");
			alrt.setPositiveButton("Import",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							add a = new add(firstsheet.this);
							a.doit();
						}
					});
			alrt.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							arg0.dismiss();
						}
					});
			alrt.show();
			break;
		}
		case R.id.notifie: {
			AlertDialog.Builder help = new AlertDialog.Builder(firstsheet.this);
			help.setTitle("Help::");
			help.setMessage("SpreadSheet must be privately linked to your account and the format of your spreadsheet must be fixed..It must have first six columns in the order:: Name,Contact,Email,DOB,Year,Branch");
			help.show();
			break;
		}

		case R.id.changecolumn: {
			LayoutInflater li = LayoutInflater.from(this);
			View dialogview = li.inflate(R.layout.dialog_column, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setView(dialogview);
			final EditText column_3 = (EditText) dialogview
					.findViewById(R.id.editText1);
			final EditText column_4 = (EditText) dialogview
					.findViewById(R.id.editText2);
			final EditText column_5 = (EditText) dialogview
					.findViewById(R.id.editText3);
			final SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
			final SharedPreferences.Editor editor = pref.edit();
			String column3 = pref.getString("column3", "");
			String column4 = pref.getString("column4", "");
			String column5 = pref.getString("column5", "");
			column_3.setText(column3);
			column_4.setText(column4);
			column_5.setText(column5);
			alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									String col3 = column_3.getText().toString();
									String col4 = column_4.getText().toString();
									String col5 = column_5.getText().toString();
									editor.putString("column3", col3);
									editor.putString("column4", col4);
									editor.putString("column5", col5);
									editor.commit();
									dialog.dismiss();
									Intent i = new Intent(firstsheet.this,
											firstsheet.class);
									finish();
									startActivity(i);
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
		case R.id.sprdsheets: {
			Intent i = new Intent(firstsheet.this, spreadsheeets.class);
			finish();
			startActivity(i);
			break;
		}
		default:
			return false;
		}
		return false;
	}

	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		database1 db = new database1(firstsheet.this);
		db.open();
		int p = db.getCount1();
		String[] name = db.getName1();
		String[] branch = db.getBranch1();
		String[] year = db.getYear1();
		String[] email = db.getEmail1();
		String[] mobilenumber = db.getContact1();
		String[] dob = db.getDOB1();

		final SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
		String col3 = pref.getString("column3", "null");
		String col4 = pref.getString("column4", "null");
		String col5 = pref.getString("column5", "null");

		int i = 0;
		while (i < p) {
			listDataHeader.add(name[i]);
			List<String> details = new ArrayList<String>();
			details.add("Contact : " + mobilenumber[i]);
			details.add("Email :    " + email[i]);
			details.add(col3 + " : " + dob[i]);
			details.add(col4 + " : " + year[i]);
			details.add(col5 + " : " + branch[i]);
			listDataChild.put(listDataHeader.get(i), details);
			i++;
		}
	}
}