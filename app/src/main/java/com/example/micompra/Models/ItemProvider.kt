package com.example.micompra.Models

import android.content.Context
import android.provider.BaseColumns
import com.example.micompra.FeedReaderContract
import com.example.micompra.FeedReaderDbHelper

class ItemProvider {
    companion object{
        /**
         * Genera la lista de items de la base de datos
         */
        fun listItems(context: Context): MutableList<Item>{

            //accedemos a la base de datos
            val dbHelper = FeedReaderDbHelper(context)

            // indicamos que queremos leer la base de datos
            val db = dbHelper.readableDatabase

            // definimos las columnas que nos interesan leer de la base de datos
            val projection = arrayOf(BaseColumns._ID, FeedReaderContract.FeedEntry.COLUMN_NAME)

            //creamos la query que nos devolvera un cursor
            val cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_ITEMS,   // nombre de la tabla a acceder
                projection,                                 // columnas a devolver
                null,                               // las columnas para el WHERE clause
                null,                            // los valores para el WHERE clause
                null,                                // para grupos de columnas
                null,                                 // para filtrar por grupos de columnas
                null                                 // para ordenar los valores a devolver
            )

            // lista donde almacenara los valores de los items
            val lista:MutableList<Item> = mutableListOf()

            with(cursor){
                while (moveToNext()) {

                    val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                    val name = getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME))
                    lista.add(Item(id, name))
                }
            }
            cursor.close()

//            //indicamos que queremos insertar datos en la base de datos
//            val db = dbHelper.writableDatabase
//
//            //creamos el nuevo mapa de valores, donde estara el nombre de la columna y el valor a guardar
//            val valores = ContentValues().apply {
//                put(FeedReaderContract.FeedEntry.COLUMN_NAME, "Huevos")
//            }
//
//            // insertamos la nueva columna de datos en la tabla de la base de datos
//            val newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_ITEMS, null, valores)
//
//            if(newRowId>0){
//                Toast.makeText(context, "Añadido: ${newRowId}", Toast.LENGTH_LONG).show()
//            }

            return lista
        }
    }
}