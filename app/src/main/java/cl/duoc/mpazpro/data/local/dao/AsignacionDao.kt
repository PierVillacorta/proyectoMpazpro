package cl.duoc.mpazpro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cl.duoc.mpazpro.data.local.entity.Asignacion

@Dao
interface AsignacionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun asignar(asignacion: Asignacion)

    @Query("DELETE FROM asignaciones WHERE oaId = :oaId AND curso = :curso")
    suspend fun quitar(oaId: Int, curso: String)

    @Query("SELECT oaId FROM asignaciones WHERE curso = :curso")
    suspend fun idsAsignados(curso: String): List<Int>
}