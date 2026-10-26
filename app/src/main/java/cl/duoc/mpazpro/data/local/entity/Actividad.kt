package cl.duoc.mpazpro.data.local.entity

import androidx.room.*
import cl.duoc.mpazpro.data.local.enums.*

@Entity(
    tableName = "actividades",
    foreignKeys = [ForeignKey(
        entity = ObjetivoAprendizaje::class,
        parentColumns = ["id"],
        childColumns = ["oaId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("oaId")]
)
data class Actividad(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val oaId: Int,
    val titulo: String,
    val tipo: TipoActividad,
    val enunciado: String,
    val opcionA: String,
    val opcionB: String,
    val opcionC: String,
    val opcionD: String,
    val correcta: Opcion,
    val dificultad: Dificultad
)