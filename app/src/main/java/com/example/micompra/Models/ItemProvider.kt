package com.example.micompra.Models

import kotlin.random.Random

class ItemProvider {
    companion object{
        /**
         * Genera la lista de prueba
         */
        fun listItems(): MutableList<Item>{
            val ids = listOf(0,1,2,3,4,5,6)
            val names = listOf("Huevos", "Leche", "Pescado", "Carne", "Zumos", "Cereales","Patatas")
            val prices = List(7){ Random.nextFloat()}
            val id_markets = List(7){ Random.nextInt(0,3)}

            val lista:MutableList<Item> = mutableListOf()

            for (i in ids.indices){
                lista.add(Item(ids[i],names[i], prices[i], id_markets[i]))
            }

            return lista
        }
    }
}