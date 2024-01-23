package com.example.recycler4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recycler4.databinding.ItemTareaBinding

class AdaptadorTarea(private var listatareas: MutableList<Tarea>, private var listener: Clicks) :
    RecyclerView.Adapter<AdaptadorTarea.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bind =
            ItemTareaBinding.bind(view)//esta linea es la que nos permite acceder a los elementos del layout

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarea, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listatareas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var tarea = listatareas[position]

        holder.bind.Tarea.setOnCheckedChangeListener(null)
        holder.bind.Tarea.text = tarea.nombre
        holder.bind.Tarea.isChecked = tarea.completado

        holder.bind.Tarea.setOnLongClickListener {
            listener.onlongclick(tarea, position)
        }

        holder.bind.Tarea.setOnCheckedChangeListener { _, isChecked ->

            listener.onTareaCheck(tarea)

        }


    }

    fun addTarea(tarea: Tarea) {
        listatareas.add(tarea)
        notifyDataSetChanged()
    }

    fun deleteTarea(tarea: Tarea) {
        listatareas.remove(tarea)
        notifyDataSetChanged()
    }


}