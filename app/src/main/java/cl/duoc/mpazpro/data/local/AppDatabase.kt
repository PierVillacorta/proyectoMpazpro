package cl.duoc.mpazpro.data.local

import android.content.Context
import androidx.room.*
import cl.duoc.mpazpro.data.local.dao.*
import cl.duoc.mpazpro.data.local.entity.*

@Database(
    entities = [
        Usuario::class,
        ObjetivoAprendizaje::class,
        Pregunta::class,
        Respuesta::class,
        Contenido::class,
        ConfigPrueba::class,
        Informe::class
    ],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun objetivoDao(): ObjetivoDao
    abstract fun preguntaDao(): PreguntaDao
    abstract fun respuestaDao(): RespuestaDao
    abstract fun contenidoDao(): ContenidoDao
    abstract fun configPruebaDao(): ConfigPruebaDao
    abstract fun informeDao(): InformeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mpaz.db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { INSTANCE = it }
            }
    }
}