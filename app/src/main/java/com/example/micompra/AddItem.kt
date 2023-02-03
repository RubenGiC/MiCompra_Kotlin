package com.example.micompra

import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.micompra.databinding.ActivityAddItemBinding
import com.example.micompra.databinding.ActivityAddMarketBinding
import com.example.micompra.databinding.ActivityMainBinding

/**
 * Uso de vinculación de vistas
 *  https://developer.android.com/topic/libraries/view-binding?hl=es-419
 */

class AddItem: AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAddItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            androidx.appcompat.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
