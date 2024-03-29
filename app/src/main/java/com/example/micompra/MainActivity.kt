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
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.micompra.models.ItemProvider
import com.example.micompra.models.MarketProvider
import com.example.micompra.databinding.ActivityMainBinding
import com.example.micompra.models.AdapterItem
import com.example.micompra.models.MarketProvider.Companion.addMarket
import com.example.micompra.models.PriceProvider.Companion.addPrice
import com.example.micompra.models.PriceProvider.Companion.isPrice
import com.example.micompra.models.ProvideAdapterItem.Companion.listItemsAdapter
import com.google.android.material.textfield.TextInputLayout

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
    private lateinit var rv_item: RecyclerView
    private val adapter:AdapterItems= AdapterItems()
    private lateinit var lista: MutableList<AdapterItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setUpRecyclerView()//crea el recyclerView

        //Al iniciar la app por defecto vienen ocultos los floatting buttons
        binding.addItemFab.visibility = hidden
        binding.addMarketFab.visibility = hidden
        binding.addPriceFab.visibility = hidden

        //Al inicio esta a false
        isAllFabsVisible = false

        //comprimimos el floatting button principal
        binding.addFab.shrink()

        //cuando pulse el floatting button principal se desplegara el resto de floatting buttons
        binding.addFab.setOnClickListener {

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
            addDialogPrice()
        }
    }

    /**
     * Crea el contenido del RecyclerView
     */
    private fun setUpRecyclerView(){

        //obtengo la lista de los productos
        lista = listItemsAdapter(this)

        rv_item = binding.rvItems //accedo al recyclerView
        //modo Grid
        rv_item.layoutManager = GridLayoutManager(applicationContext, 2)
        //rv_item.setHasFixedSize(true)
        //rv_item.layoutManager = LinearLayoutManager(this)

        adapter.AdapterItems(lista, this)//crea el adaptador para el RecyclerView
        rv_item.adapter = adapter // y se lo añado al recyclerView
    }

    /**
     * actualiza la lista al volver a la vista anterior
     * https://stackoverflow.com/questions/5545217/back-button-and-refreshing-previous-activity
     */
    override fun onRestart() {
        super.onRestart()
        update()
    }

    /**
     * actualiza la lista y el adaptador del recyclerView
     */
    private fun update(){
        lista = listItemsAdapter(this)
        adapter.setItemList(lista)//actualiza la lista del adaptador

        //adapter.notifyDataSetChanged() // notifica del cambio
        //adapter.notifyItemInserted(lista.size-1)
    }

    /**
     * Crea el dialog para añadir un supermercado
     */
    private fun addDialogMarket(){
        // Accedemos al layout activity_add_market
        val input = this.layoutInflater.inflate(R.layout.activity_add_market, null)

        //accdedemos al campo TextInputLayout
        val til_name = input.findViewById<TextInputLayout>(R.id.til_name)

        //creamos el dialogo
        val dialog = createDialog(input, "Añadir supermercado")

        //acción con los botones (guardar/cancelar)
        dialog.setOnShowListener {
            //accedemos al boton guardar
            val acept: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            //cuando pulse guardar
            acept.setOnClickListener {
                //accedemos al EditText
                val et_name = input.findViewById<EditText>(R.id.et_name)
                //guardamos el nombre del supermercado
                val name = et_name.text.toString()

                //añadimos el supermercado a la base de datos
                val result = addMarket(this, name)

                //comprobamos que se ha añadido correctamente
                if(result > 0) {
                    Toast.makeText(this, HtmlCompat.fromHtml("Añadido <b>${name}</b>", HtmlCompat.FROM_HTML_MODE_LEGACY), Toast.LENGTH_SHORT).show()
                    dialog.dismiss()

                    // si esta vacio, muestra un mensaje de error
                }else if(result == ERROR_EMPTY){
                    til_name.error = "Está vacío"
                }
                else{
                    // https://stackoverflow.com/questions/37904739/html-fromhtml-deprecated-in-android-n
                    til_name.error = HtmlCompat.fromHtml("Ya existe <b>${name}</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                }
            }
        }
        //mostramos el dialogo
        dialog.show()
    }

    /**
     * Crea un dialog para añadir un precio a un producto y supermercado concreto
     *
     * https://universoandroidhn.blogspot.com/2020/03/como-crea-un-spinner-utilizando-sqlite.html
     */
    private fun addDialogPrice(){
        // Accedemos al layout activity_add_market
        val input = this.layoutInflater.inflate(R.layout.activity_add_price, null)

        //obtenemos la lista de productos
        val items = ItemProvider.listItemsSort(this, 1)

        //generamos una lista de strings de la anterior lista
        val list_items_string: MutableList<String> = mutableListOf()

        for( i in items){
            list_items_string.add(i.name)
        }

        //creamos el adapter para el spinner
        val spItemsAdapter:ArrayAdapter<String> = ArrayAdapter(this, androidx.transition.R.layout.support_simple_spinner_dropdown_item,list_items_string)

        //accdedemos al spinner de productos
        val spItems = input.findViewById<Spinner>(R.id.s_productos)

        //le añadimos el adaptador
        spItems.adapter = spItemsAdapter

        //obtenemos la lista de supermercados
        val markets = MarketProvider.listMarkets(this)

        //generamos una lista de strings de la anterior lista
        val list_markets_string: MutableList<String> = mutableListOf()

        for( i in markets){
            list_markets_string.add(i.name)
        }

        //creamos el adapter para el spinner
        val spMarketsAdapter:ArrayAdapter<String> = ArrayAdapter(this, androidx.transition.R.layout.support_simple_spinner_dropdown_item,list_markets_string)

        //accedemos al spinner de supermercados
        val spMarkets = input.findViewById<Spinner>(R.id.s_market)

        //le añadimos el adaptador
        spMarkets.adapter = spMarketsAdapter

        //accdedemos al campo TextInputLayout de los 3 campos
        val til_price = input.findViewById<TextInputLayout>(R.id.til_price)
        val til_market = input.findViewById<TextInputLayout>(R.id.til_market)
        val til_producto = input.findViewById<TextInputLayout>(R.id.til_producto)

        //creamos el dialogo
        val dialog = createDialog(input, "Añadir supermercado")

        //acción con los botones (guardar/cancelar)
        dialog.setOnShowListener {
            //accedemos al boton guardar
            val acept: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            //cuando pulse guardar
            acept.setOnClickListener {

                //guardamos los ids de ambos spinners
                val id_item = spItems.selectedItemId.toInt()
                val id_market = spMarkets.selectedItemId.toInt()

                //accedemos al EditText del precio
                val et_price = input.findViewById<EditText>(R.id.et_price)
                //guardamos el precio
                val price = et_price.text.toString().trim()

                val exist = isPrice(this, items[id_item], markets[id_market])

                //añadimos el precio a la base de datos
                val result = addPrice(this, items[id_item], markets[id_market], price)

                //comprobamos que el resultado no es nulo
                if(result > 0) {
                    Toast.makeText(this, "Precio Añadido correctamente", Toast.LENGTH_SHORT).show()

                    //actualizamos la lista de productos
                    update()

                    //cerramos el dialog
                    dialog.dismiss()

                // en caso contrario
                }else{

                    // si esta vacio, muestra un mensaje de error
                    if(result == ERROR_EMPTY){
                        til_price.error = "Está vacío"

                    // si el valor del campo es 0
                    }else if(result == ERROR_0){
                        til_price.error = HtmlCompat.fromHtml("El valor tiene que ser <b>mayor a 0</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)

                    // si el campo es correcto
                    }else{
                        til_price.error = null
                    }

                    //si ya tiene un precio ese producto en ese supermercado
                    if(exist > 0){
                        // https://stackoverflow.com/questions/37904739/html-fromhtml-deprecated-in-android-n
                        til_producto.error = HtmlCompat.fromHtml("Ya tiene un precio el producto <b>${items[id_item].name}</b> en <b>${markets[id_market].name}</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                        til_market.error = HtmlCompat.fromHtml("Ya tiene un precio el producto <b>${items[id_item].name}</b> en <b>${markets[id_market].name}</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)

                        //en caso contrario no tiene precio ese producto en ese supermercado
                    }else{
                        til_producto.error = null
                        til_market.error = null
                    }
                }
            }
        }
        //mostramos el dialogo
        dialog.show()
    }

    /**
     * Función que crea los dialog (para optimizar codigo)
     */
    fun createDialog(view: View, title: String):AlertDialog{
        //creamos el dialogo
        return AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setCancelable(false)
            .setPositiveButton("Guardar", null)
            .setNegativeButton("Cancelar", null)
            .create()
    }

    /**
     * menu de opciones del banner
     */
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