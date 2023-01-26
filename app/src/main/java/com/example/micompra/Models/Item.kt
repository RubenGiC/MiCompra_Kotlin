package com.example.micompra.Models

//Modelo de articulo
/**
 * Contiene:
 * - id         entero
 * - name       texto
 * - price      Flotante
 * - id_market  entero      (referencia al id Market)
 */
data class Item(val id:Int, var name:String, var price:Float, val id_market:Int)