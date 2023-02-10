package com.example.micompra

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.micompra.models.AdapterItem

/**
 * Clase que rellenara la listview de forma personalizada
 * https://cursokotlin.com/capitulo-15-recyclerview-kotlin/
 */
class AdapterItems: RecyclerView.Adapter<AdapterItems.ItemViewHolder>(){

    var productos:MutableList<AdapterItem> = ArrayList()
    lateinit var context: Context

    /**
     * Inicialización de los datos
     */
    fun AdapterItems(productos: MutableList<AdapterItem>, context: Context){
        this.productos = productos
        this.context = context
    }

    /**
     * Recoge las posiciones de la lista items y las pasa a la clase ItemViewHolder
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val producto = productos.get(position)

        holder.bind(producto)
    }

    /**
     * Para modificar la lista
     */
    fun setItemList(productos: List<AdapterItem>){
        this.productos = productos.toMutableList()
        notifyDataSetChanged()// notifica del cambio
    }

    /**
     * Función Get que devuelve el tamaño de la lista
     */
    override fun getItemCount(): Int { return productos.size }

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
        fun bind(producto:AdapterItem){//, context: Context
            titulo.text = producto.item.name
            market.text = producto.market.name
            precio.text = producto.price.toString()+" €"

            //NO FUNCIONA
            /*itemView.setOnClickListener(View.OnClickListener {
                Toast.makeText(context,item.toString(), Toast.LENGTH_LONG)
            })*/
        }
    }
}
