package cl.duoc.mpazpro.data.local.dao

import androidx.room.*

import cl.duoc.mpazpro.data.local.entity.ConfigPrueba
import cl.duoc.mpazpro.data.local.enums.*

@Dao
interface ConfigPruebaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(config: ConfigPrueba)

    @Query("SELECT * FROM config_prueba WHERE asignatura = :asignatura AND unidad = :unidad LIMIT 1")
    suspend fun obtener(asignatura: Asignatura, unidad: String): ConfigPrueba?
}