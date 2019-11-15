package com.oiranca.pglproject.ui.conection;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class HelperBase extends SQLiteOpenHelper {



    public HelperBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(BaseCreate.SQL_CREATE_ADMIN);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BaseCreate.SQL_DELETE_AD);
        onCreate(db);

    }



}
