package cl.duoc.mpazpro.data.local.dao

import androidx.room.*

import cl.duoc.mpazpro.data.local.entity.Pregunta
import cl.duoc.mpazpro.data.local.enums.Asignatura

@Dao
interface PreguntaDao {
    @Insert
    suspend fun insertar(pregunta: Pregunta)

    @Query("SELECT * FROM preguntas WHERE oaId = :oaId")
    suspend fun porOa(oaId: Int): List<Pregunta>

    @Query("""
    SELECT p.* FROM preguntas p
    JOIN objetivos o ON o.id = p.oaId
    WHERE o.asignatura = :asignatura AND o.unidad = :unidad
    ORDER BY p.id
""")
    suspend fun porUnidad(asignatura: Asignatura, unidad: String): List<Pregunta>
}