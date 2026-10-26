package cl.duoc.mpazpro.data.local.dao

import androidx.room.*
import cl.duoc.mpazpro.data.local.entity.*
import cl.duoc.mpazpro.data.local.enums.Asignatura

@Dao
interface ActividadDao {
    @Insert
    suspend fun insertar(actividad: Actividad): Long

    // Actividades: siempre visibles. Desafíos: solo si el docente los asignó al curso.
    @Query("""
        SELECT a.* FROM actividades a
        JOIN objetivos o ON o.id = a.oaId
        JOIN asignaciones s ON s.oaId = o.id
        JOIN usuarios u ON u.curso = s.curso
        WHERE u.id = :usuarioId AND o.asignatura = :asignatura AND o.unidad = :unidad
          AND o.estado IN ('AUTORIZADO', 'PUBLICADO')
          AND (a.tipo = 'ACTIVIDAD' OR EXISTS (
                SELECT 1 FROM asignaciones_desafio d
                WHERE d.actividadId = a.id AND d.curso = u.curso))
        ORDER BY a.tipo, a.id
    """)
    suspend fun visibles(usuarioId: Int, asignatura: Asignatura, unidad: String): List<Actividad>

    @Query("SELECT * FROM progreso_actividad WHERE usuarioId = :usuarioId")
    suspend fun progresoDe(usuarioId: Int): List<ProgresoActividad>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarProgreso(progreso: ProgresoActividad)

    @Query("SELECT * FROM actividades WHERE tipo = 'DESAFIO' ORDER BY id")
    suspend fun desafios(): List<Actividad>

    @Query("SELECT actividadId FROM asignaciones_desafio WHERE curso = :curso")
    suspend fun desafiosAsignados(curso: String): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun asignarDesafio(asignacion: AsignacionDesafio)

    @Query("DELETE FROM asignaciones_desafio WHERE actividadId = :actividadId AND curso = :curso")
    suspend fun quitarDesafio(actividadId: Int, curso: String)
}