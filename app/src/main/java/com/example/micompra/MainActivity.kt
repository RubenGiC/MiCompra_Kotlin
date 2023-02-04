package com.example.micompra

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.micompra.Models.Item
import com.example.micompra.Models.ItemProvider
import com.example.micompra.Models.MarketProvider
import com.example.micompra.databinding.ActivityMainBinding

/**
 * IDEAS
 * https://www.geeksforgeeks.org/theming-floating-action-button-with-bottom-navigation-bar-in-android/
 * Pull refresh en el RecyclerView
 *
 * Floatting button (esta ligeramente modificado a como viene)
 * https://www.geeksforgeeks.org/extended-floating-action-button-in-android-with-example/
 *
 */

class MainActivity : AppCompatActivity() {

    //Para facilitar el codigo
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    //Para reducir codigo redundante
    private var hidden = View.GONE
    private var isAllFabsVisible: Boolean? = null //para comprobar si estan visibles o no los floatting button

    /**
     * Inicializamos el RecyclerView y el adaptador
     */
    lateinit var rv_item: RecyclerView
    val adapter:AdapterItems= AdapterItems()
    lateinit var lista: MutableList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setUpRecyclerView()//crea la lista

        //Al iniciar la app por defecto vienen ocultos los floatting buttons
        binding.addItemFab.visibility = hidden
        binding.addMarketFab.visibility = hidden
        binding.addPriceFab.visibility = hidden

        //Al inicio esta a false
        isAllFabsVisible = false

        //comprimimos el floatting button principal
        binding.addFab.shrink()

        //cuando pulse el floatting button principal se desplegara el resto de floatting buttons
        binding.addFab.setOnClickListener { view ->

            //comprobamos que este a false (!) y no nulo (!!)
            isAllFabsVisible = if(!isAllFabsVisible!!){
                //si esta a false se muestran los floatting button ocultos
                binding.addItemFab.show()
                binding.addMarketFab.show()
                binding.addPriceFab.show()

                //y extendemos el floatting button principal
                binding.addFab.setIconResource(R.drawable.ic_baseline_close_24)
                binding.addFab.extend()

                true
            }else{
                //ocultamos los floatting button
                binding.addPriceFab.hide()
                binding.addItemFab.hide()
                binding.addMarketFab.hide()

                //y comprimimos el floatting button principal
                binding.addFab.setIconResource(R.drawable.ic_baseline_add_24)
                binding.addFab.shrink()

                false
            }
        }

        //cuando pulse aparecerá un pop up para añadir un nuevo market
        binding.addMarketFab.setOnClickListener {
            addDialogMarket()
        }

        //cuando pulse ira a otra vista para añadir un nuevo producto
        binding.addItemFab.setOnClickListener {
            startActivity(Intent(this,AddItem::class.java))
        }

        //cuando pulse aparecerá un pop up para añadir un precio de un producto de un supermercado concreto
        binding.addPriceFab.setOnClickListener {
            Toast.makeText(this, "En proceso de creación", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Crea el contenido del RecyclerView
     */
    fun setUpRecyclerView(){
        lista = ItemProvider.listItems(this)

        rv_item = binding.rvItems //accedo al recyclerView
        rv_item.layoutManager = GridLayoutManager(applicationContext, 2)
        //rv_item.setHasFixedSize(true)
        //rv_item.layoutManager = LinearLayoutManager(this)
        adapter.AdapterItems(lista, this)//crea el adaptador para el RecyclerView
        rv_item.adapter = adapter
    }

    /**
     * actualiza la lista al volver a la vista anterior
     * https://stackoverflow.com/questions/5545217/back-button-and-refreshing-previous-activity
     */
    override fun onRestart() {
        super.onRestart()
        update()
    }

    fun update(){
        lista = ItemProvider.listItems(this)
        adapter.AdapterItems(lista, this)//crea el adaptador para el RecyclerView
        adapter!!.notifyDataSetChanged()
    }

    fun addDialogMarket(){
        // Accedemos al layout activity_add_market
        val input = this.layoutInflater.inflate(R.layout.activity_add_market, null)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Añadir supermercado")
            .setView(input)
            .setPositiveButton("Guardar"){_, _ ->

                //accedemos al EditText
                val et_name = input.findViewById<EditText>(R.id.et_name)
                //guardamos el nombre del supermercado
                val name = et_name.text.toString()

                //añadimos el supermercado a la base de datos
                val result = addMarket(name)

                //comprobamos que el resultado no es nulo
                if (result != null) {
                    //si es mayor que 0 se a añadido correctamente
                    if(result > 0) {
                        Toast.makeText(this, "Añadido ${name}", Toast.LENGTH_SHORT).show()

                    // si devuelve -2 es que esta vacio
                    }else if(result == ERROR_EMPTY){
                        // https://stackoverflow.com/questions/37904739/html-fromhtml-deprecated-in-android-n
                        Toast.makeText(this, HtmlCompat.fromHtml("El campo <b>${getString(R.string.name_market)}</b> esta vacio", HtmlCompat.FROM_HTML_MODE_LEGACY), Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this, "Ya existe le superpercado ${name}", Toast.LENGTH_LONG).show()
                    }

                    //si lo es ha habido un error al escribir la base de datos
                }else{
                    Toast.makeText(this, "Error en la Base de Datos", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

    /**
     * Comprueba si esta ya añadido o no y si no esta añadido lo añade a la base de datos
     */
    fun addMarket(name: String): Long? {

        return MarketProvider.addMarket(this, name)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.action_settings -> Toast.makeText(this, "En proceso de creación", Toast.LENGTH_SHORT).show()
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}