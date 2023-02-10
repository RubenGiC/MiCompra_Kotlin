package com.example.micompra.models

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import com.example.micompra.ERROR_EMPTY
import com.example.micompra.ERROR_EXIST
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

            //recorremos los resultados
            with(cursor){
                while (moveToNext()) {

                    //extraemos el id y el nombre
                    val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                    val name = getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME))
                    //generamos un obejto Item y lo añadimos a la lista de items
                    lista.add(Item(id, name.replaceFirstChar(Char::titlecase)))
                    //NOTA: ponemos el primer caracter del string mayuscula
                }
            }
            //cerramos el cursor
            cursor.close()

            return lista
        }

        /**
         * Genera la lista de items ordenada:
         * - ord: 0 is DESC and 1 is ASC
         */
        fun listItemsSort(context: Context, ord: Int): MutableList<Item>{

            //accedemos a la base de datos
            val dbHelper = FeedReaderDbHelper(context)

            // indicamos que queremos leer la base de datos
            val db = dbHelper.readableDatabase

            // definimos las columnas que nos interesan leer de la base de datos
            val projection = arrayOf(BaseColumns._ID, FeedReaderContract.FeedEntry.COLUMN_NAME)

            //tipo de ordenación
            var order = "ASC"
            if(ord == 0){
                order = "DESC"
            }

            //indicamos que queremos ordenar los valores a devolver por el nombre descendentemente
            val sortOrd = "${FeedReaderContract.FeedEntry.COLUMN_NAME} $order"

            //creamos la query que nos devolvera un cursor
            val cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_ITEMS,   // nombre de la tabla a acceder
                projection,                                 // columnas a devolver
                null,                               // las columnas para el WHERE clause
                null,                            // los valores para el WHERE clause
                null,                                // para grupos de columnas
                null,                                 // para filtrar por grupos de columnas
                sortOrd                                 // para ordenar los valores a devolver
            )

            // lista donde almacenara los valores de los items
            val lista:MutableList<Item> = mutableListOf()

            //recorremos los resultados
            with(cursor){
                while (moveToNext()) {

                    //obtenemos el id y el nombre
                    val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                    val name = getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME))

                    //creamos el objeto Item y lo añadimos a la lista
                    lista.add(Item(id, name.replaceFirstChar(Char::titlecase)))
                }
            }
            cursor.close()// cerramos el cursor

            return lista
        }

        /**
         * Comprueba si existe o no el producto:
         * - name: nombre del producto
         */
        fun isItem(context: Context, name: String): Int{

            // Accedemos a la base de  datos
            val dbHelper = FeedReaderDbHelper(context)

            // Idicamos que queremos leer la base de datos
            val db = dbHelper.readableDatabase

            //indicamos las columnas a devolver
            val projection = arrayOf(BaseColumns._ID, FeedReaderContract.FeedEntry.COLUMN_NAME)

            // Filtramos por el nombre
            val selection = "${FeedReaderContract.FeedEntry.COLUMN_NAME} = ?"
            val selectionArgs = arrayOf(name)

            //creamos la query
            val cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_ITEMS,   // Tabla a acceder
                projection,                                 // columnas a devolver
                selection,                                  // columnas a filtrar
                selectionArgs,                              // Argumentos de dichas columnas a filtrar
                null,                                       // para grupos de columnas
                null,                                       // para filtrar por grupos de columnas
                null,                               // para ordenar los valores a devolver
            )

            //guardamos el numero de coincidencias
            val entradas = cursor.count

            //cerramos el cursor
            cursor.close()

            return entradas
        }

        /**
         * Añade un nuevo producto
         */
        fun addItem(context: Context, name: String): Long{

            //comprobamos que el campo no este vacio
            if(name.isNotEmpty()) {

                //se pone en minuscular para una comprobación más facil
                //y eliminamos los espacios del principio y final
                val name_min = name.lowercase().trim()

                //comprobamos si existe
                val exist = isItem(context, name_min)

                //si no existe se añade
                if (exist == 0) {

                    // Accedemos a la base de datos
                    val dbHelper = FeedReaderDbHelper(context)

                    // Indicamos que vamos a modificar la base de datos
                    val db = dbHelper.writableDatabase

                    // creamos el mapa de valores, key = combre de la columna, valor
                    val values = ContentValues().apply {
                        put(FeedReaderContract.FeedEntry.COLUMN_NAME, name_min)
                    }

                    // Insertamos en la tabla, devolviendo el id donde se encuentra o un error
                    return db.insert(FeedReaderContract.FeedEntry.TABLE_ITEMS, null, values)

                }
            }else{
                //si esta vacio
                return ERROR_EMPTY
            }

            //en caso contrario existe
            return ERROR_EXIST
        }
    }
}