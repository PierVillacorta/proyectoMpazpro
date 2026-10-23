package cl.duoc.mpazpro.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "asignaciones",
    primaryKeys = ["oaId", "curso"],
    foreignKeys = [
        ForeignKey(
            entity = ObjetivoAprendizaje::class,
            parentColumns = ["id"],
            childColumns = ["oaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Asignacion(
    val oaId: Int,
    val curso: String
)