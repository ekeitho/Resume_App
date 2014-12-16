package com.ekeitho.resume.github.database;

/**
 * Created by Keithmaynn on 12/16/14.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GithubSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_REPOS = "repos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_REPO_NAME = "repo_name";

    private static final String DATABASE_NAME = "gitrepo.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_REPOS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_REPO_NAME + " text not null);";

    public GithubSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(GithubSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPOS);
        onCreate(db);
    }

}
