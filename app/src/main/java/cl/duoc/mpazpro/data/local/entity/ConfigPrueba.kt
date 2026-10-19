package cl.duoc.mpazpro.data.local.entity

import androidx.room.Entity
import cl.duoc.mpazpro.data.local.enums.*


@Entity(
    tableName = "config_prueba",
    primaryKeys = ["asignatura", "unidad"]
)
data class ConfigPrueba(
    val asignatura: Asignatura,
    val unidad: String,
    val maxIntentos: Int
)