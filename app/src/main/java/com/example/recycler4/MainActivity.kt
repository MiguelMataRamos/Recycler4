package com.example.recycler4

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recycler4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Clicks {
    private lateinit var bind: ActivityMainBinding
    private lateinit var adaptador: AdaptadorTarea
    private lateinit var adaptadorCompletas: AdaptadorTarea
    private lateinit var data: MutableList<Tarea>

    override fun onCreate(savedInstanceState: Bundle?) {
        bind = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        data = mutableListOf()

        adaptador = AdaptadorTarea(data, this)
        adaptadorCompletas = AdaptadorTarea(mutableListOf(), this)

        bind.recycler.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adaptador
        }

        bind.recycler2.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adaptadorCompletas
        }

        bind.botonAdd1.setOnClickListener {
            bind.textotareas.visibility = View.INVISIBLE
            bind.botonAdd1.visibility = View.INVISIBLE
            bind.botonAdd2.visibility = View.VISIBLE
            bind.textInputLayout.visibility = View.VISIBLE
            bind.nuevatarea.requestFocus()
            //que se despliegue el teclado
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(bind.nuevatarea, InputMethodManager.SHOW_IMPLICIT)

        }

        bind.botonAdd2.setOnClickListener {
            addNuevaTarea()
        }

    }

    override fun onStart() {
        super.onStart()
        getData()
    }

    fun getData(){
        var data = mutableListOf<Tarea>(
            Tarea("Tarea 1", false),
            Tarea("Tarea 2", false),
            Tarea("Tarea 3", false),
            Tarea("Tarea 4", false),
            Tarea("Tarea 5", false),
        )

        data.forEach{tarea ->
            addTareaAuto(tarea)
        }
    }

    private fun addTareaAuto(tarea: Tarea) {
        if (tarea.completado) {
            adaptadorCompletas.addTarea(tarea)
        } else {
            adaptador.addTarea(tarea)
        }
    }

    fun addNuevaTarea(){
        if (bind.nuevatarea.text.toString().isNullOrEmpty()) {
            popUp("No se puede añadir una tarea vacia")
        } else {
            var tarea = Tarea(bind.nuevatarea.text.toString(), false)
            data.add(tarea)
            adaptador.notifyDataSetChanged()
            bind.textotareas.visibility = View.VISIBLE
            bind.botonAdd2.visibility = View.INVISIBLE
            bind.botonAdd1.visibility = View.VISIBLE
            bind.textInputLayout.visibility = View.INVISIBLE
            bind.nuevatarea.setText("")
            popUp("Tarea añadida")
            bind.nuevatarea.clearFocus()
            //que se oculte el teclado
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(bind.nuevatarea.windowToken, 0)

        }
    }

    private fun popUp(texto: String) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()
    }

    override fun onlongclick(tarea: Tarea, position: Int): Boolean {
        AlertDialog.Builder(this)
            .setTitle("Eliminar tarea")
            .setMessage("¿Estas seguro de eliminar la tarea ${tarea.nombre.uppercase()}?")
            .setPositiveButton("Si") { _, _ ->
                adaptador.deleteTarea(tarea)
                adaptadorCompletas.deleteTarea(tarea)
                popUp("Tarea eliminada")
                true
            }
            .setNegativeButton("No") { _, _ ->
                false
            }
            .show()
        return true

    }

    override fun onTareaCheck(tarea: Tarea) {
        Handler(Looper.getMainLooper()).post {
            if (tarea.completado) {
                tarea.completado = false
                popUp("Tarea incompleta")
                adaptadorCompletas.deleteTarea(tarea)
                adaptador.addTarea(tarea)
            } else {
                tarea.completado = true
                popUp("Tarea completada")
                adaptador.deleteTarea(tarea)
                adaptadorCompletas.addTarea(tarea)
            }
        }
    }


}