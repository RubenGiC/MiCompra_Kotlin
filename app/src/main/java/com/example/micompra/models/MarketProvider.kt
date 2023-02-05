package com.example.micompra.models

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import com.example.micompra.FeedReaderContract
import com.example.micompra.FeedReaderDbHelper

/**
 * Basado en los papers oficiales
 * https://developer.android.com/training/data-storage/sqlite?hl=es-419#WriteDbRow
 */

class MarketProvider {

    companion object{

        /**
         * Genera la lista de supermercados que hay
         */
        fun listMarkets(context: Context): MutableList<Market>{

            //accedemos a la base de datos
            val dbHelper = FeedReaderDbHelper(context)

            //indicamos que vamos a leer los datos de la base de datos
            val db = dbHelper.readableDatabase

            //indicamos las columnas a devolver
            val projection = arrayOf(BaseColumns._ID, FeedReaderContract.FeedEntry.COLUMN_NAME)

            //indicamos que queremos ordenar los valores a devolver por el nombre descendentemente
            val sortOrd = "${FeedReaderContract.FeedEntry.COLUMN_NAME} ASC"

            //creamos la query
            val cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_MARKET,  // Tabla a acceder
                projection,                                 // Columnas a devolver
                null,                               // Filtramos por columnas indicadas
                null,                            // Indicamos los argumentos de dichas columnas a filtrar
                null,                               // Para grupo de columnas
                null,                                // Para filtrar por grupo de columnas
                sortOrd                                    // Ordenar por columna los valores a devolver
            )

            //creamos la lista mutable
            val lista:MutableList<Market> = mutableListOf()

            //si hay elementos en el cursor
            with(cursor){
                //recorremos los elementos del cursor
                while (moveToNext()){
                    //accedemos al id y al nombre del supermercado
                    val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                    val name = getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME))

                    //añadimos el objeto Market a la lista
                    lista.add(Market(id, name.replaceFirstChar(Char::titlecase)))
                }
            }

            return lista
        }

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
        fun addMarket(context: Context, name: String): Long {

            //comprobamos que el campo no este vacio
            if(name.isNotEmpty()) {
                //lo ponemos en minuscula para que la comprobación de si ya existe el supermercado
                //y eliminamos los espacios del principio y final
                val name_min = name.lowercase().trim()

                //comprobamos si existe el supermercado
                val existe = isMarket(context, name_min)

                //si no existe
                if (existe == 0) {

                    // Accedemos a la base de datos
                    val dbHelper = FeedReaderDbHelper(context)

                    //indicamos que vamos a escribir en la base de datos
                    val db = dbHelper.writableDatabase

                    //Creamos el mapa de valores, donde contiene el key (nombre de la columna) y el valor
                    val values = ContentValues().apply {
                        put(FeedReaderContract.FeedEntry.COLUMN_NAME, name_min)
                    }

                    //Insertamos los nuevos valores, devolviendonos el id o un error si se ha insertado mal
                    return db.insert(FeedReaderContract.FeedEntry.TABLE_MARKET, null, values)
                }
            }else{
                return -2
            }

            return -1
        }
    }
}