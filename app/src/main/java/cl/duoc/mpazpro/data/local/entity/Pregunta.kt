package cl.duoc.mpazpro.data.local.entity

import androidx.room.*
import cl.duoc.mpazpro.data.local.enums.*


@Entity(tableName = "preguntas", foreignKeys = [  ForeignKey(
    entity = ObjetivoAprendizaje::class,
    parentColumns = ["id"],
    childColumns = ["oaId"],
    onDelete = ForeignKey.CASCADE
)])
data class Pregunta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val oaId: Int,
    val enunciado: String,
    val opcionA: String,
    val opcionB: String,
    val opcionC: String,
    val opcionD: String,
    val correcta: Opcion,
    val dificultad: Dificultad
)