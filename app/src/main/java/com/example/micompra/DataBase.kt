package com.example.micompra

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

// https://developer.android.com/training/data-storage/sqlite?hl=es-419

object FeedReaderContract{
    object FeedEntry : BaseColumns{
        // nombre de las tablas
        const val TABLE_ITEMS = "miitem"
        const val TABLE_MARKET = "mimarket"
        const val TABLE_PRICE = "miprice"

        //nombre de los campos de la tabla
        const val COLUMN_NAME = "name"
        const val COLUMN_PATH = "path"
        const val COLUMN_ITEM = "item"
        const val COLUMN_MARKET = "market"
        const val COLUMN_PRICE = "price"
    }

    internal const val SQL_CREATE_ITEMS = "CREATE TABLE ${FeedEntry.TABLE_ITEMS}(" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedEntry.COLUMN_NAME} TEXT)"

    internal const val SQL_CREATE_MARKET = "CREATE TABLE ${FeedEntry.TABLE_MARKET}(" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedEntry.COLUMN_NAME} TEXT)"

    internal val SQL_DELETE_ITEMS = "DROP TABLE IF EXISTS ${FeedEntry.TABLE_ITEMS}"
    internal val SQL_DELETE_MARKETS = "DROP TABLE IF EXISTS ${FeedEntry.TABLE_MARKET}"
}

class FeedReaderDbHelper(context: Context): SQLiteOpenHelper(context, DATABAASE_NAME, null, DATABASE_VERSION) {
    // crea la base de datos
    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null) {//POR SI ESTA A NULL
            db.execSQL(FeedReaderContract.SQL_CREATE_ITEMS)
            db.execSQL(FeedReaderContract.SQL_CREATE_MARKET)
        }
    }

    //actualiza la base de datos
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL(FeedReaderContract.SQL_DELETE_ITEMS)
            db.execSQL(FeedReaderContract.SQL_DELETE_MARKETS)
            onCreate(db)
        }
    }


    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object{
        const val DATABAASE_NAME = "MiCompra.db" // nombre de la base de datos
        const val DATABASE_VERSION = 1 // versión
    }
}