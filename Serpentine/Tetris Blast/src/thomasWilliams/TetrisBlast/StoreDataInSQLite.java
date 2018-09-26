package thomasWilliams.TetrisBlast;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StoreDataInSQLite {

	public static final String KEY_GRIDID = "GridID";
	public static final String KEY_ROWID = "ID";

	private static final String TAG = "HistoryTable";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_NAME = "BlockStructure";
	private static final String DATABASE_TABLE_GridID = "tblGridID";
	private static final String DATABASE_TABLE_GridIDOffset = "tblGridIDOffset";
	private static final int DATABASE_VERSION = 3;

	/**
	 * Database creation sql statement
	 */
	private static final String CREATE_TABLE_GRIDID = "create table " + DATABASE_TABLE_GridID + " (" + KEY_ROWID + " integer primary key autoincrement, " + KEY_GRIDID + " text not null);";
	private static final String CREATE_TABLE_GRIDIDOFFSET = "create table " + DATABASE_TABLE_GridIDOffset + " (" + KEY_ROWID + " integer primary key autoincrement, " + KEY_GRIDID + " text not null);";

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(TAG, "Creating DataBase: " + CREATE_TABLE_GRIDID);
			Log.i(TAG, "Creating DataBase: " + CREATE_TABLE_GRIDIDOFFSET);
			db.execSQL(CREATE_TABLE_GRIDID);
			db.execSQL(CREATE_TABLE_GRIDIDOFFSET);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_GridID);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_GridIDOffset);
			onCreate(db);
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public StoreDataInSQLite(Context ctx) {
		this.mCtx = ctx;
	}

	public StoreDataInSQLite open() throws SQLException {
		Log.i(TAG, "OPening DataBase Connection....");
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long createRow(String strGridID, String DATABASE_TABLE) {
		Log.i(TAG, "Inserting record...");
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_GRIDID, strGridID);

		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deleteRow(long rowId, String DATABASE_TABLE) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean ClearAll(String DATABASE_TABLE) {

		return mDb.delete(DATABASE_TABLE, null, null) > 0;
	}

	public Cursor fetchAllData(String DATABASE_TABLE) {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_GRIDID }, null, null, null, null, null);
	}

	public Cursor fetchData(long empId, String DATABASE_TABLE) throws SQLException {

		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_GRIDID }, KEY_ROWID + "=" + empId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public Cursor fetchRow(String strWhereClause, String DATABASE_TABLE) throws SQLException {
		String strQuery = "select * from " + DATABASE_TABLE + " where " + strWhereClause;
		Cursor mCursor = mDb.rawQuery(strQuery, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// public String fetchID(int intGridSize, int intLevel) throws SQLException {
	// String strQuery = "select * from " + DATABASE_TABLE + " where " + KEY_GRIDSIZE + " = " + intGridSize + " and " + KEY_LEVEL + " = " + intLevel;
	// Cursor mCursor = mDb.rawQuery(strQuery, null);
	// if (mCursor.getCount() > 0) {
	// mCursor.moveToFirst();
	// return mCursor.getString(mCursor.getColumnIndex(KEY_ROWID));
	// }
	// return null;
	// }

	public long TotalCount(String DATABASE_TABLE) {
		Cursor dataCount = mDb.rawQuery("SELECT  * FROM " + DATABASE_TABLE, null);
		return dataCount.getCount();
	}

	public boolean updateHistory(int empId, String strGridSize, String DATABASE_TABLE) {
		ContentValues args = new ContentValues();
		args.put(KEY_GRIDID, strGridSize);

		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + empId, null) > 0;
	}
}