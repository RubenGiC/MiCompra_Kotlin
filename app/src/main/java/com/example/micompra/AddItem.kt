package com.example.micompra

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.example.micompra.Models.ItemProvider.Companion.addItem
import com.example.micompra.databinding.ActivityAddItemBinding

/**
 * Uso de vinculación de vistas
 *  https://developer.android.com/topic/libraries/view-binding?hl=es-419
 */

class AddItem: AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        //este es una forma automatica de volver atras, pero me interesa una personalizada
        /*supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)*/

        //para volver a la vista anterior
        binding.toolbar.setNavigationOnClickListener { finish() }

        //cuando pulse en añadir el producto
        binding.bAdd.setOnClickListener {
            val producto = binding.etName.text.toString()
            val result = addItem(this, producto)

            //comprobamos que el resultado no es nulo
            if (result != null) {
                if(result == ERROR_EMPTY){
                    Toast.makeText(
                        this,
                        HtmlCompat.fromHtml(
                            "El campo <b>${getString(R.string.name_item)}</b> esta vacio",
                            HtmlCompat.FROM_HTML_MODE_LEGACY),
                        Toast.LENGTH_LONG
                    ).show()
                }else if(result > 0){
                    Toast.makeText(this, "Producto ${producto} añadido", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this, "Ya existe el producto ${producto}", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this, "Error en la Base de Datos", Toast.LENGTH_LONG).show()
            }
        }

    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            androidx.appcompat.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }*/
}
