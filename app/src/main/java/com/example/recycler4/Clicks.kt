package com.example.recycler4

interface Clicks {
    fun onlongclick(tarea: Tarea, position: Int): Boolean

    fun onTareaCheck(tarea: Tarea)
}