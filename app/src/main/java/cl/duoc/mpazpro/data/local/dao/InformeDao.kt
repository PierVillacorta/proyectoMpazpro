package cl.duoc.mpazpro.data.local.dao

import androidx.room.*
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
}