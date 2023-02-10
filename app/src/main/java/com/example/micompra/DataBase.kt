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

    //creación de las tablas
    internal const val SQL_CREATE_ITEMS = "CREATE TABLE ${FeedEntry.TABLE_ITEMS}(" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedEntry.COLUMN_NAME} TEXT)"

    internal const val SQL_CREATE_MARKET = "CREATE TABLE ${FeedEntry.TABLE_MARKET}(" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedEntry.COLUMN_NAME} TEXT)"

    internal const val SQL_CREATE_PRICE = "CREATE TABLE ${FeedEntry.TABLE_PRICE}(" +
            "${FeedEntry.COLUMN_ITEM} INTEGER NOT NULL," +
            "${FeedEntry.COLUMN_MARKET} INTEGER NOT NULL," +
            "${FeedEntry.COLUMN_PRICE} REAL," +
            "PRIMARY KEY (${FeedEntry.COLUMN_ITEM}, ${FeedEntry.COLUMN_MARKET}))"

    //borra las tablas
    internal const val SQL_DELETE_ITEMS = "DROP TABLE IF EXISTS ${FeedEntry.TABLE_ITEMS}"
    internal const val SQL_DELETE_MARKETS = "DROP TABLE IF EXISTS ${FeedEntry.TABLE_MARKET}"
    internal const val SQL_DELETE_PRICES = "DROP TABLE IF EXISTS ${FeedEntry.TABLE_PRICE}"
}

class FeedReaderDbHelper(context: Context): SQLiteOpenHelper(context, DATABAASE_NAME, null, DATABASE_VERSION) {
    // crea la base de datos
    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null) {//POR SI ESTA A NULL
            db.execSQL(FeedReaderContract.SQL_CREATE_ITEMS)
            db.execSQL(FeedReaderContract.SQL_CREATE_MARKET)
            db.execSQL(FeedReaderContract.SQL_CREATE_PRICE)
        }
    }

    //actualiza la base de datos
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            //borra las tablas existentes
            db.execSQL(FeedReaderContract.SQL_DELETE_ITEMS)
            db.execSQL(FeedReaderContract.SQL_DELETE_MARKETS)
            db.execSQL(FeedReaderContract.SQL_DELETE_PRICES)
            //y las crea
            onCreate(db)
        }
    }


    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object{
        const val DATABAASE_NAME = "MiCompra.db" // nombre de la base de datos
        const val DATABASE_VERSION = 10 // versión
    }
}