package cl.duoc.mpazpro.data.local.dao

import androidx.room.*
import cl.duoc.mpazpro.data.local.entity.ObjetivoAprendizaje
import cl.duoc.mpazpro.data.local.enums.*

@Dao
interface ObjetivoDao {
    @Insert
    suspend fun insertar(objetivo: ObjetivoAprendizaje): Long

    @Query("SELECT * FROM objetivos WHERE asignatura = :asignatura")
    suspend fun porAsignatura(asignatura: Asignatura): List<ObjetivoAprendizaje>

    @Query("SELECT * FROM objetivos WHERE estado IN ('AUTORIZADO', 'PUBLICADO')")
    suspend fun disponibles(): List<ObjetivoAprendizaje>

    @Query("UPDATE objetivos SET estado = :estado WHERE id = :id")
    suspend fun cambiarEstado(id: Int, estado: EstadoContenido)

    @Query("""
    SELECT * FROM objetivos
    WHERE asignatura = :asignatura AND unidad = :unidad
      AND estado IN ('AUTORIZADO', 'PUBLICADO')
""")
    suspend fun disponiblesPorUnidad(asignatura: Asignatura, unidad: String): List<ObjetivoAprendizaje>

    @Query("""
    SELECT o.* FROM objetivos o
    JOIN asignaciones a ON a.oaId = o.id
    JOIN usuarios u ON u.curso = a.curso
    WHERE u.id = :usuarioId AND o.estado IN ('AUTORIZADO', 'PUBLICADO')
""")
    suspend fun asignadosA(usuarioId: Int): List<ObjetivoAprendizaje>

    @Query("""
    SELECT o.* FROM objetivos o
    JOIN asignaciones a ON a.oaId = o.id
    JOIN usuarios u ON u.curso = a.curso
    WHERE u.id = :usuarioId AND o.asignatura = :asignatura AND o.unidad = :unidad
      AND o.estado IN ('AUTORIZADO', 'PUBLICADO')
""")
    suspend fun asignadosAPorUnidad(
        usuarioId: Int, asignatura: Asignatura, unidad: String
    ): List<ObjetivoAprendizaje>

    @Query("SELECT * FROM objetivos ORDER BY asignatura, unidad, id")
    suspend fun todos(): List<ObjetivoAprendizaje>
}