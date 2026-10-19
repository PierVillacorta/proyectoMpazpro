package cl.duoc.mpazpro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cl.duoc.mpazpro.data.local.dto.*
import cl.duoc.mpazpro.data.local.entity.Respuesta
import cl.duoc.mpazpro.data.local.enums.Asignatura

@Dao
interface RespuestaDao {

    @Insert
    suspend fun insertar(respuesta: Respuesta)

    @Insert
    suspend fun insertarTodas(respuestas: List<Respuesta>)

    @Query("""
        SELECT COALESCE(MAX(r.intento), 0)
        FROM respuestas r
        JOIN preguntas p ON p.id = r.preguntaId
        JOIN objetivos o ON o.id = p.oaId
        WHERE r.usuarioId = :usuarioId
          AND o.asignatura = :asignatura
          AND o.unidad = :unidad
    """)
    suspend fun intentosUsados(usuarioId: Int, asignatura: Asignatura, unidad: String): Int

    @Query("""
        SELECT o.id AS oaId, o.codigo AS codigo,
               COUNT(*) AS total,
               SUM(r.esCorrecta) AS correctas
        FROM respuestas r
        JOIN preguntas p ON p.id = r.preguntaId
        JOIN objetivos o ON o.id = p.oaId
        WHERE r.usuarioId = :usuarioId
          AND r.intento = :intento
          AND o.asignatura = :asignatura
          AND o.unidad = :unidad
        GROUP BY o.id
    """)
    suspend fun rendimientoPorOa(
        usuarioId: Int, intento: Int, asignatura: Asignatura, unidad: String
    ): List<RendimientoOa>

    @Query("""
        SELECT p.*, r.opcionElegida AS opcionElegida, r.esCorrecta AS esCorrecta
        FROM respuestas r
        JOIN preguntas p ON p.id = r.preguntaId
        JOIN objetivos o ON o.id = p.oaId
        WHERE r.usuarioId = :usuarioId
          AND r.intento = :intento
          AND o.asignatura = :asignatura
          AND o.unidad = :unidad
        ORDER BY p.id
    """)
    suspend fun detalle(
        usuarioId: Int, intento: Int, asignatura: Asignatura, unidad: String
    ): List<RespuestaDetalle>
}