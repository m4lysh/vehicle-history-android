package io.vehiclehistory.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static io.vehiclehistory.database.DbConstants.COLUMN_LABEL;
import static io.vehiclehistory.database.DbConstants.COLUMN_REGISTRATION_DATE;
import static io.vehiclehistory.database.DbConstants.COLUMN_REGISTRATION_NUMBER;
import static io.vehiclehistory.database.DbConstants.COLUMN_TIMESTAMP;
import static io.vehiclehistory.database.DbConstants.COLUMN_VIN;
import static io.vehiclehistory.database.DbConstants.DATABASE_NAME;
import static io.vehiclehistory.database.DbConstants.TABLE_NAME;

public class HistoryDbOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    private static final String SQL_TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_TIMESTAMP + " LONG, " +
                    COLUMN_LABEL + " TEXT, " +
                    COLUMN_REGISTRATION_NUMBER + " TEXT, " +
                    COLUMN_VIN + " TEXT, " +
                    COLUMN_REGISTRATION_DATE + " TEXT);";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public HistoryDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
