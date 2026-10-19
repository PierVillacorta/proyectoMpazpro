package cl.duoc.mpazpro.data.local.dao

import androidx.room.*
import cl.duoc.mpazpro.data.local.entity.Contenido

@Dao
interface ContenidoDao {
    @Insert
    suspend fun insertar(contenido: Contenido)

    @Query("SELECT * FROM contenidos WHERE oaId = :oaId")
    suspend fun porOa(oaId: Int): List<Contenido>
}