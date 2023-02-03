package com.example.micompra

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.micompra.Models.ItemProvider
import com.example.micompra.databinding.ActivityMainBinding

/**
 * IDEAS
 * https://www.geeksforgeeks.org/theming-floating-action-button-with-bottom-navigation-bar-in-android/
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
        binding.addMarketFab.setOnClickListener{view ->
            Toast.makeText(this, "En proceso de creación", Toast.LENGTH_SHORT).show()
        }

        //cuando pulse ira a otra vista para añadir un nuevo producto
        binding.addItemFab.setOnClickListener { view ->
            Toast.makeText(this, "En proceso de creación", Toast.LENGTH_SHORT).show()
        }

        //cuando pulse aparecerá un pop up para añadir un precio de un producto de un supermercado concreto
        binding.addPriceFab.setOnClickListener { view ->
            Toast.makeText(this, "En proceso de creación", Toast.LENGTH_SHORT).show()
        }

        /*binding.buttonFirst.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,AddMarket::class.java)
            startActivity(intent)
        })*/
    }

    /**
     * Crea el contenido del RecyclerView
     */
    fun setUpRecyclerView(){
        val lista = ItemProvider.listItems(this)

        rv_item = binding.rvItems //accedo al recyclerView
        rv_item.layoutManager = GridLayoutManager(applicationContext, 2)
        //rv_item.setHasFixedSize(true)
        //rv_item.layoutManager = LinearLayoutManager(this)
        adapter.AdapterItems(lista, this)//crea el adaptador para el RecyclerView
        rv_item.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        Toast.makeText(this, item.itemId.toString(), Toast.LENGTH_LONG)
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