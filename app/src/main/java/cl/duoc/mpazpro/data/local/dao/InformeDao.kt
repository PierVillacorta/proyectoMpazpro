package cl.duoc.mpazpro.data.local.dao

import androidx.room.*
import cl.duoc.mpazpro.data.local.dto.InformeDetalle
import cl.duoc.mpazpro.data.local.entity.Informe
import cl.duoc.mpazpro.data.local.enums.EstadoInforme

@Dao
interface InformeDao {
    @Insert
    suspend fun insertar(informe: Informe)

    @Insert
    suspend fun insertarTodos(informes: List<Informe>)
    @Query("SELECT * FROM informes WHERE usuarioId = :usuarioId")
    suspend fun porUsuario(usuarioId: Int): List<Informe>

    @Query("SELECT * FROM informes WHERE estado = 'PENDIENTE_VALIDACION'")
    suspend fun pendientes(): List<Informe>

    @Query("UPDATE informes SET estado = :estado WHERE id = :id")
    suspend fun cambiarEstado(id: Int, estado: EstadoInforme)

    @Query("""
    SELECT i.id AS id, u.nombre AS alumno, o.asignatura AS asignatura, o.unidad AS unidad,
           o.codigo AS codigoOa, i.correctas AS correctas, i.incorrectas AS incorrectas,
           i.porcentaje AS porcentaje, i.clasificacion AS clasificacion,
           i.fecha AS fecha, i.estado AS estado, i.docenteResponsable AS docenteResponsable
    FROM informes i
    JOIN usuarios u ON u.id = i.usuarioId
    JOIN objetivos o ON o.id = i.oaId
    WHERE u.curso = :curso
    ORDER BY i.id DESC
""")
    suspend fun delCurso(curso: String): List<InformeDetalle>

    @Query("UPDATE informes SET estado = 'VALIDADO', docenteResponsable = :docente WHERE id = :id")
    suspend fun validar(id: Int, docente: String)
}