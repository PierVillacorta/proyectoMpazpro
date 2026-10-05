package cl.duoc.mpazpro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "objetivos")
data class ObjetivoAprendizaje(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val asignatura: String,
    val unidad: String,
    val codigo: String,
    val descripcion: String,
    val estado: String = "BORRADOR"
)