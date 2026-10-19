package cl.duoc.mpazpro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import cl.duoc.mpazpro.data.local.enums.*

@Entity(tableName = "objetivos")
data class ObjetivoAprendizaje(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val asignatura: Asignatura,
    val unidad: String,
    val codigo: String,
    val descripcion: String,
    val estado: EstadoContenido = EstadoContenido.BORRADOR
)