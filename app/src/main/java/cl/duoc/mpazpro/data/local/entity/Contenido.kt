package cl.duoc.mpazpro.data.local.entity

import androidx.room.*
import cl.duoc.mpazpro.data.local.enums.*


@Entity(
    tableName = "contenidos",
    foreignKeys = [ForeignKey(
        entity = ObjetivoAprendizaje::class,
        parentColumns = ["id"],
        childColumns = ["oaId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("oaId")]
)
data class Contenido(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val oaId: Int,
    val titulo: String,
    val explicacion: String,
    val ejemplo: String,
    val origen: OrigenMaterial
)