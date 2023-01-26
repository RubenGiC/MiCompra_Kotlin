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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.micompra.Models.ItemProvider
import com.example.micompra.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //Para facilitar el codigo
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

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

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
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
        val lista = ItemProvider.listItems()

        rv_item = binding.rvItems //accedo al recyclerView
        rv_item.setHasFixedSize(true)
        rv_item.layoutManager = LinearLayoutManager(this)
        adapter.AdapterItems(lista, this)//crea el adaptador para el RecyclerView
        rv_item.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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