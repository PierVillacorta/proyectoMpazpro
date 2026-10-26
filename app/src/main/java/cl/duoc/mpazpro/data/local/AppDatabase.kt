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
        Informe::class,
        Asignacion::class,
        Actividad::class,
        ProgresoActividad::class,
        AsignacionDesafio::class// ← nueva
    ],
    version = 2              // ← era 3
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun objetivoDao(): ObjetivoDao
    abstract fun preguntaDao(): PreguntaDao
    abstract fun respuestaDao(): RespuestaDao
    abstract fun contenidoDao(): ContenidoDao
    abstract fun configPruebaDao(): ConfigPruebaDao
    abstract fun informeDao(): InformeDao
    abstract fun asignacionDao(): AsignacionDao

    abstract fun actividadDao(): ActividadDao


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