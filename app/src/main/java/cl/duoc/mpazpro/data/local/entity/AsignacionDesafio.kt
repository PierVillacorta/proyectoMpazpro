package cl.duoc.mpazpro.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "asignaciones_desafio",
    primaryKeys = ["actividadId", "curso"],
    foreignKeys = [ForeignKey(
        entity = Actividad::class,
        parentColumns = ["id"],
        childColumns = ["actividadId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class AsignacionDesafio(
    val actividadId: Int,
    val curso: String
)