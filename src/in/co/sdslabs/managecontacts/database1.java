package in.co.sdslabs.managecontacts;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class database1 extends Activity {

	public static final String ROWID = "_id";
	public static final String NAME = "_name";
	public static final String BRANCH = "_branch";
	public static final String YEAR = "_year";
	public static final String EMAIL = "_email";
	public static final String CONTACTNO = "_contactno";
	public static final String DOB = "_dob";
	public static final String FLAG = "_flag";
	public static final String SPREADSHTNAME = "spreadsheet_name";
	private static final String WRK_DB1 = "WorkingDB1";
	private static final String TABLE0 = "NAMETABLE";
	private static final String TABLE1 = "FIRSTTABLE";
	private static final int DBVERSION = 1;

	private Dbmaker1 maker;
	private final Context context;
	private SQLiteDatabase database;

	private static class Dbmaker1 extends SQLiteOpenHelper {

		public Dbmaker1(Context context) {
			super(context, WRK_DB1, null, DBVERSION);
			// TODO Auto-generated constructor stub
		}

		String CREATE_TABLE1 = "CREATE TABLE " + TABLE1 + " (" + ROWID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + DOB + " TEXT,"
				+ BRANCH + " TEXT," + YEAR + " TEXT," + EMAIL + " TEXT,"
				+ CONTACTNO + " TEXT," + NAME + " TEXT " + ")";

		String CREATE_TABLE0 = "CREATE TABLE " + TABLE0 + " (" + ROWID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + FLAG + " TEXT,"
				+ SPREADSHTNAME + " TEXT " + ")";

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(CREATE_TABLE1);
			db.execSQL(CREATE_TABLE0);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE1 IF EXISTES" + TABLE1);
			db.execSQL("DROP TABLE1 IF EXISTES" + TABLE0);
			onCreate(db);
		}

	}

	public database1(Context r) {
		context = r;

	}

	public database1 open() {
		maker = new Dbmaker1(context);

		database = maker.getWritableDatabase();
		return this;
	}

	public void close() {
		maker.close();
	}

	public long createEntry1(String dob, String branch, String year,
			String email, String contact, String name) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(DOB, dob);
		cv.put(BRANCH, branch);
		cv.put(YEAR, year);
		cv.put(EMAIL, email);
		cv.put(CONTACTNO, contact);
		cv.put(NAME, name);
		return database.insert(TABLE1, null, cv);
	}

	public long createEntry0(String spreadshtname, String flag) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(SPREADSHTNAME, spreadshtname);
		return database.insert(TABLE0, null, cv);
	}

	public String[] getPriavteSpreadShtName() {
		// TODO Auto-generated method stub
		Cursor c = database.rawQuery("SELECT spreadsheet_name FROM " + TABLE0,
				null);
		String[] result = new String[100];
		int i = 0;
		while (c.moveToNext()) {
			String uname = c.getString(c.getColumnIndex("spreadsheet_name"));
			if (uname == null)
				result[i] = "0";
			else
				result[i] = uname;
			i++;
		}
		return result;
	}

	public String[] getName1() {
		// TODO Auto-generated method stub
		Cursor c = database.rawQuery("SELECT _name FROM " + TABLE1,
				new String[] {});
		String[] result = new String[100];
		int i = 0;
		while (c.moveToNext()) {
			String uname = c.getString(c.getColumnIndex("_name"));
			if (uname == null)
				result[i] = "0";
			else
				result[i] = uname;
			i++;
		}

		return result;
	}

	public String[] getBranch1() {
		// TODO Auto-generated method stub
		Cursor c = database.rawQuery("SELECT _branch FROM " + TABLE1,
				new String[] {});
		String[] result = new String[100];
		int i = 0;
		while (c.moveToNext()) {
			String branch = c.getString(c.getColumnIndex("_branch"));
			if (branch == null)
				result[i] = "0";
			else
				result[i] = branch;
			i++;
		}

		return result;
	}

	public String[] getYear1() {
		// TODO Auto-generated method stub
		Cursor c = database.rawQuery("SELECT _year FROM " + TABLE1,
				new String[] {});
		String[] result = new String[100];
		int i = 0;
		while (c.moveToNext()) {
			String year = c.getString(c.getColumnIndex("_year"));
			if (year == null)
				result[i] = "0";
			else

				result[i] = year;
			i++;
		}

		return result;
	}

	public String[] getEmail1() {
		// TODO Auto-generated method stub
		Cursor c = database.rawQuery("SELECT _email FROM " + TABLE1,
				new String[] {});
		String[] result = new String[100];
		int i = 0;
		while (c.moveToNext()) {
			String email = c.getString(c.getColumnIndex("_email"));
			if (email == null)
				result[i] = "0";
			else

				result[i] = email;
			i++;
		}

		return result;
	}

	public String[] getContact1() {
		// TODO Auto-generated method stub
		Cursor c = database.rawQuery("SELECT _contactno FROM " + TABLE1,
				new String[] {});
		String[] result = new String[100];
		int i = 0;
		while (c.moveToNext()) {
			String contact = c.getString(c.getColumnIndex("_contactno"));
			if (contact == null)
				result[i] = "0";
			else

				result[i] = contact;
			i++;
		}

		return result;
	}

	public String[] getDOB1() {
		// TODO Auto-generated method stub
		Cursor c = database.rawQuery("SELECT _dob FROM " + TABLE1,
				new String[] {});
		String[] result = new String[100];
		int i = 0;
		while (c.moveToNext()) {
			String dob = c.getString(c.getColumnIndex("_dob"));
			if (dob == null)
				result[i] = "00/00/0000";
			else

				result[i] = dob;
			i++;
		}

		return result;
	}

	public int getCount1() {
		// TODO Auto-generated method stub
		Cursor c = database.rawQuery("SELECT _name FROM " + TABLE1,
				new String[] {});
		int i = 0;
		while (c.moveToNext()) {
			i++;
		}
		return i;
	}

	public int getCount0() {
		// TODO Auto-generated method stub
		Cursor c = database.rawQuery("SELECT spreadsheet_name FROM " + TABLE0,
				new String[] {});
		int i = 0;
		while (c.moveToNext()) {
			i++;
		}

		return i;
	}

	public void emptyTABLE1() {
		database.delete(TABLE1, null, null);
	}

	public void emptyTABLE0() {
		database.delete(TABLE0, null, null);
	}
}