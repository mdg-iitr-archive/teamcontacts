package in.co.sdslabs.managecontacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class massmessage extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);
		LayoutInflater li = LayoutInflater.from(this);
		final View dialogview = li.inflate(R.layout.dialog, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(dialogview);
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Send",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								final EditText edt = (EditText) dialogview
										.findViewById(R.id.editText1);
								String msg = edt.getText().toString();
								if (msg.equals(null)) {
									Toast.makeText(getApplicationContext(),
											"Please enter a valid message.",
											Toast.LENGTH_LONG).show();
								} else {
									int j = 0, p = 0;
									Toast.makeText(
											getApplicationContext(),
											"Please Wait.. The process will take time.. :)",
											Toast.LENGTH_LONG).show();
									database1 db = new database1(
											massmessage.this);
									db.open();
									p = db.getCount1();
									String[] mobilenumber = db.getContact1();
									SmsManager sms = SmsManager.getDefault();
									while (j < p) {

										String SENT = "SMS_SENT";
										String DELIVERED = "SMS_DELIVERED";

										PendingIntent sentPI = PendingIntent
												.getBroadcast(
														getApplicationContext(),
														0, new Intent(SENT), 0);

										PendingIntent deliveredPI = PendingIntent
												.getBroadcast(
														getApplicationContext(),
														0,
														new Intent(DELIVERED),
														0);

										// ---when the SMS has been sent---
										registerReceiver(
												new BroadcastReceiver() {
													@Override
													public void onReceive(
															Context arg0,
															Intent arg1) {
														switch (getResultCode()) {
														case Activity.RESULT_OK:
															// Toast.makeText(getBaseContext(),
															// "SMS sent",
															// Toast.LENGTH_LONG).show();
															break;
														case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
															Toast.makeText(
																	getBaseContext(),
																	"Generic failure",
																	Toast.LENGTH_LONG)
																	.show();
															break;
														case SmsManager.RESULT_ERROR_NO_SERVICE:
															Toast.makeText(
																	getBaseContext(),
																	"No service",
																	Toast.LENGTH_LONG)
																	.show();
															break;
														case SmsManager.RESULT_ERROR_NULL_PDU:
															Toast.makeText(
																	getBaseContext(),
																	"Null PDU",
																	Toast.LENGTH_LONG)
																	.show();
															break;
														case SmsManager.RESULT_ERROR_RADIO_OFF:
															Toast.makeText(
																	getBaseContext(),
																	"Radio off",
																	Toast.LENGTH_LONG)
																	.show();
															break;
														}
													}
												}, new IntentFilter(SENT));

										// ---when the SMS has been delivered---
										registerReceiver(
												new BroadcastReceiver() {
													@Override
													public void onReceive(
															Context arg0,
															Intent arg1) {
														switch (getResultCode()) {
														case Activity.RESULT_OK:
															Toast.makeText(
																	getBaseContext(),
																	"SMS delivered",
																	Toast.LENGTH_LONG)
																	.show();
															break;
														case Activity.RESULT_CANCELED:
															Toast.makeText(
																	getBaseContext(),
																	"SMS not delivered",
																	Toast.LENGTH_LONG)
																	.show();
															break;
														}
													}
												}, new IntentFilter(DELIVERED));

										sms.sendTextMessage(mobilenumber[j],
												null, msg, sentPI, deliveredPI);

										j++;
									}
									db.close();
									Toast.makeText(getApplicationContext(),
											"SMS sending to all people.",
											Toast.LENGTH_LONG).show();
									Intent i = new Intent(massmessage.this,
											firstsheet.class);
									finish();

									startActivity(i);
								}
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent i = new Intent(massmessage.this,
										firstsheet.class);
								finish();

								startActivity(i);
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
}
