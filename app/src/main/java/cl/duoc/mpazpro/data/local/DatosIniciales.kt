package cl.duoc.mpazpro.data.local
import cl.duoc.mpazpro.data.local.entity.*
import cl.duoc.mpazpro.data.local.enums.*

suspend fun sembrarDatosSiVacio(db: AppDatabase) {
    // Si ya hay usuarios, los datos ya fueron cargados: no repetir
    if (db.usuarioDao().contar() > 0) return

    // Usuarios 100% ficticios
    db.usuarioDao().insertar(Usuario(nombre = "utp.demo", clave = "1234", rol = Rol.UTP, curso = "-"))
    db.usuarioDao().insertar(Usuario(nombre = "docente.demo", clave = "1234", rol = Rol.DOCENTE, curso = "6.º A"))
    db.usuarioDao().insertar(Usuario(nombre = "alumno1", clave = "1234", rol = Rol.ESTUDIANTE, curso = "6.º A"))
    db.usuarioDao().insertar(Usuario(nombre = "alumno2", clave = "1234", rol = Rol.ESTUDIANTE, curso = "6.º A"))

    // Un objetivo de ejemplo (Matemática)
    val oaId = db.objetivoDao().insertar(
        ObjetivoAprendizaje(
            asignatura = Asignatura.MATEMATICA,
            unidad = "Unidad 1",
            codigo = "OA_POR_VERIFICAR",
            descripcion = "Operar con fracciones y decimales",
            estado = EstadoContenido.AUTORIZADO
        )
    ).toInt()

    db.contenidoDao().insertar(
        Contenido(
            oaId = oaId,
            titulo = "Sumando fracciones",
            explicacion = "Cuando dos fracciones tienen el mismo denominador, se suman los numeradores y el denominador se mantiene.",
            ejemplo = "1/5 + 2/5 = 3/5",
            origen = OrigenMaterial.PROPIO
        )
    )

    db.preguntaDao().insertar(Pregunta(
        oaId = oaId,
        enunciado = "¿Cuánto es 3/4 + 1/4?",
        opcionA = "4/8", opcionB = "1", opcionC = "3/16", opcionD = "4/4 + 1",
        correcta = Opcion.A, dificultad = Dificultad.FACIL
    ))
    db.preguntaDao().insertar(Pregunta(
        oaId = oaId,
        enunciado = "¿Cuál número es mayor?",
        opcionA = "0,25", opcionB = "0,5", opcionC = "0,05", opcionD = "0,2",
        correcta = Opcion.B, dificultad = Dificultad.FACIL
    ))
    db.preguntaDao().insertar(Pregunta(
        oaId = oaId,
        enunciado = "¿Cuánto es el 20 % de 50?",
        opcionA = "5", opcionB = "20", opcionC = "10", opcionD = "25",
        correcta = Opcion.C, dificultad = Dificultad.MEDIA
    ))

    // El docente fija un máximo de 2 intentos
    db.configPruebaDao().guardar(ConfigPrueba(Asignatura.MATEMATICA, "Unidad 1", maxIntentos = 2))
}