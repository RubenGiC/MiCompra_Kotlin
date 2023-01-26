package com.example.micompra

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.micompra.Models.Item

/**
 * Clase que rellenara la listview de forma personalizada
 * https://cursokotlin.com/capitulo-15-recyclerview-kotlin/
 */
class AdapterItems: RecyclerView.Adapter<AdapterItems.ItemViewHolder>(){

    var items:MutableList<Item> = ArrayList()
    lateinit var context: Context

    fun AdapterItems(items: MutableList<Item>, context: Context){
        this.items = items
        this.context = context
    }

    /**
     * Recoge las posiciones de la lista items y las pasa a la clase ItemViewHolder
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items.get(position)

        holder.bind(item, context)
    }

    fun setItemList(items: List<Item>){
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    /**
     * Función Get que devuelve el tamaño de la lista
     */
    override fun getItemCount(): Int { return items.size }

    /**
     * Crea el objeto ViewHolder para el RecyclerView
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater.inflate(R.layout.row_item, parent, false))
    }

    /**
     * ViewHolder personalizado haciendo referencia a los items de la vista que recibe
     */
    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        //accedo a los elementos del layout
        private val titulo = view.findViewById(R.id.t_name) as TextView
        private val precio = view.findViewById<TextView>(R.id.t_price)
        private val market = view.findViewById<TextView>(R.id.t_market)

        /**
         * Rellena los datos
         */
        fun bind(item:Item, context: Context){
            titulo.text = item.name
            precio.text = item.price.toString()
            market.text = item.id_market.toString()
            //NO FUNCIONA
            /*itemView.setOnClickListener(View.OnClickListener {
                Toast.makeText(context,item.toString(), Toast.LENGTH_LONG)
            })*/
        }
    }
}
