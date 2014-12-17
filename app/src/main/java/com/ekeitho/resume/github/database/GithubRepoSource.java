package com.ekeitho.resume.github.database;

/**
 * Created by Keithmaynn on 12/16/14.
 */

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ekeitho.resume.github.Repo;

public class GithubRepoSource {

    // Database fields
    private SQLiteDatabase database;
    private GithubSQLiteHelper dbHelper;
    private String[] allColumns = {GithubSQLiteHelper.COLUMN_ID,
            GithubSQLiteHelper.COLUMN_GIT_ID,
            GithubSQLiteHelper.COLUMN_REPO_NAME};

    public GithubRepoSource(Context context) {
        dbHelper = new GithubSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean doesDatabaseHaveData() {
        Cursor cursor = database.query(GithubSQLiteHelper.TABLE_REPOS, allColumns, null, null, null, null, null);
        return cursor.moveToFirst();
    }

    // return true, if the database needs to be added
    // return false, if every id is already in the database, so we don't add copies.
    public boolean updateAnyChanges(ArrayList<Repo> repositories) {

        for (int i = 0; i < repositories.size(); i++) {

            // @Todo : what if user updates and i delete a repository, must implement way to recognize this
            // if the repo git id isn't in the database, then add it
            if(!database.query(GithubSQLiteHelper.TABLE_REPOS, allColumns,
                    GithubSQLiteHelper.COLUMN_GIT_ID + " = " + repositories.get(i).getGitId(), null,
                    null, null, null).moveToFirst()){

                ContentValues values = new ContentValues();
                values.put(GithubSQLiteHelper.COLUMN_REPO_NAME, repositories.get(i).getRepoName());
                values.put(GithubSQLiteHelper.COLUMN_GIT_ID, repositories.get(i).getGitId());
                database.insert(GithubSQLiteHelper.TABLE_REPOS,null, values);
                return true;
            }
        }

        return false;
    }



    public Repo createRepo(String name) {
        ContentValues values = new ContentValues();
        values.put(GithubSQLiteHelper.COLUMN_REPO_NAME, name);
        long insertId = database.insert(GithubSQLiteHelper.TABLE_REPOS, null,
                values);
        Cursor cursor = database.query(GithubSQLiteHelper.TABLE_REPOS,
                allColumns, GithubSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        Repo newRepo = cursorToNextRepo(cursor);
        cursor.close();
        return newRepo;
    }

    public ArrayList<Repo> getAllRepos() {
        ArrayList<Repo> repositories = new ArrayList<Repo>();

        Cursor cursor = database.query(GithubSQLiteHelper.TABLE_REPOS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Repo repo = cursorToNextRepo(cursor);
            repositories.add(repo);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return repositories;
    }

    private Repo cursorToNextRepo(Cursor cursor) {
        Repo repo = new Repo(cursor.getLong(1), cursor.getString(2));
        return repo;
    }
}
