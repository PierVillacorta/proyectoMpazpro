package cl.duoc.mpazpro.data.local.entity

import androidx.room.*
import cl.duoc.mpazpro.data.local.enums.Opcion

@Entity(
    tableName = "progreso_actividad",
    primaryKeys = ["usuarioId", "actividadId"],
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Actividad::class,
            parentColumns = ["id"],
            childColumns = ["actividadId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("actividadId")]
)
data class ProgresoActividad(
    val usuarioId: Int,
    val actividadId: Int,
    val opcionElegida: Opcion,
    val esCorrecta: Boolean
)