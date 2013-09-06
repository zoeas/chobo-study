package com.example.puzztest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PuzzDB extends SQLiteOpenHelper{
	
	private static final String DB_NAME="PuzzDB";
	private static final int DB_VERSION=1;

	public PuzzDB(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String[][] monster={{"C","slame"},{"A","susano"},{"B","magic"},{"AA","rucifer"},{"A","neptuene"},{"C","golem"}};
		db.execSQL("CREATE TABLE eggs(_id INTEGER NOT NULL PRIMARY KEY ASC autoincrement ,grade VARCHAR(10),name VARCHAR(10))");
		for(int i=0;i<6;i++){
			db.execSQL("INSERT INTO eggs(grade,name) VALUES('"+monster[i][0]+"','"+monster[i][1]+"')");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
