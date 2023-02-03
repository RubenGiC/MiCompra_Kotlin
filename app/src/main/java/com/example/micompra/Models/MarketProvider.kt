package com.example.micompra.Models

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.widget.Toast
import com.example.micompra.FeedReaderContract
import com.example.micompra.FeedReaderDbHelper

/**
 * Basado en los papers oficiales
 * https://developer.android.com/training/data-storage/sqlite?hl=es-419#WriteDbRow
 */

class MarketProvider {

    companion object{
        /**
         * Comprueba si esta el supermercado ya o no
         */
        fun isMarket(context: Context, name: String): Int{

            //Accedemos a la base de datos
            val dbHelper = FeedReaderDbHelper(context)

            //indicamos que queremos leer los datos de la base de datos
            val db = dbHelper.readableDatabase

            //indicamos las columnas a devolver
            val projection = arrayOf(BaseColumns._ID, FeedReaderContract.FeedEntry.COLUMN_NAME)

            //filtramos por el nombre
            val selection = "${FeedReaderContract.FeedEntry.COLUMN_NAME} = ?"
            val selectionArgs = arrayOf(name)

            //creamos la query
            val cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_MARKET,  // tabla a acceder
                projection,                                 // columnas a devolver
                selection,                                  // indicamos las columnas para filtrar
                selectionArgs,                              // indicamos los argumentos de dichas columnas a filtrar
                null,                               // para grupos de columnas
                null,                                // para filtrar por grupos de columnas
                null                                // para ordenar los valores a devolver
            )

            //guardamos el numero de entradas
            val entradas = cursor.count

            //cerramos el cursor
            cursor.close()

            return entradas
        }

        /**
         * Añade un nuevo supermercado
         */
        fun addMarket(context: Context, name: String): Long? {

            //lo ponemos en minuscula para que la comprobación de si ya existe el supermercado
            val name_min = name.lowercase()

            //comprobamos si existe el supermercado
            val existe = isMarket(context, name_min)

            //si no existe
            if(existe == 0){

                // Accedemos a la base de datos
                val dbHelper = FeedReaderDbHelper(context)

                //indicamos que vamos a escribir en la base de datos
                val db = dbHelper.writableDatabase

                //Creamos el mapa de valores, donde contiene el key (nombre de la columna) y el valor
                val values = ContentValues().apply {
                    put(FeedReaderContract.FeedEntry.COLUMN_NAME, name_min)
                }

                //Insertamos los nuevos valores
                val newRowId = db?.insert(FeedReaderContract.FeedEntry.TABLE_MARKET, null, values)

                return newRowId
            }

            return -1
        }
    }
}