package cl.duoc.mpazpro.data.local.entity

import androidx.room.*
import cl.duoc.mpazpro.data.local.enums.*


@Entity(
    tableName = "informes",
    foreignKeys = [
        ForeignKey(entity = Usuario::class, parentColumns = ["id"], childColumns = ["usuarioId"]),
        ForeignKey(entity = ObjetivoAprendizaje::class, parentColumns = ["id"], childColumns = ["oaId"])
    ],
    indices = [Index("usuarioId"), Index("oaId")]
)
data class Informe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioId: Int,
    val oaId: Int,
    val correctas: Int,
    val incorrectas: Int,
    val porcentaje: Int,
    val clasificacion: Clasificacion,
    val fecha: String,
    val estado: EstadoInforme
)