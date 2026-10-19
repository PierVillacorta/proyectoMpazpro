package cl.duoc.mpazpro.data.local.entity

import androidx.room.*
import cl.duoc.mpazpro.data.local.enums.*


@Entity(
    tableName = "respuestas",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"]
        ),
        ForeignKey(
            entity = Pregunta::class,
            parentColumns = ["id"],
            childColumns = ["preguntaId"]
        )
    ],
    indices = [Index("usuarioId"), Index("preguntaId")]
)

data class Respuesta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioId: Int,
    val preguntaId: Int,
    val opcionElegida: Opcion,
    val esCorrecta: Boolean,
    val intento: Int
)