package cl.duoc.mpazpro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
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
}