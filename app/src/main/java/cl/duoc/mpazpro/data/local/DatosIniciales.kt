package cl.duoc.mpazpro.data.local

import cl.duoc.mpazpro.data.local.entity.*
import cl.duoc.mpazpro.data.local.enums.*

suspend fun sembrarDatosSiVacio(db: AppDatabase) {
    if (db.usuarioDao().contar() > 0) return

    db.usuarioDao().insertar(Usuario(nombre = "utp.demo", clave = "1234", rol = Rol.UTP, curso = "-"))
    db.usuarioDao().insertar(Usuario(nombre = "docente.demo", clave = "1234", rol = Rol.DOCENTE, curso = "6.º A"))
    db.usuarioDao().insertar(Usuario(nombre = "alumno1", clave = "1234", rol = Rol.ESTUDIANTE, curso = "6.º A"))
    db.usuarioDao().insertar(Usuario(nombre = "alumno2", clave = "1234", rol = Rol.ESTUDIANTE, curso = "6.º A"))

    // ---------- Matemática: autorizada y asignada ----------
    val oaMat = db.objetivoDao().insertar(
        ObjetivoAprendizaje(
            asignatura = Asignatura.MATEMATICA,
            unidad = "Unidad 1",
            codigo = "MA06-OA-01",
            descripcion = "Operar con fracciones y decimales",
            estado = EstadoContenido.AUTORIZADO
        )
    ).toInt()

    db.contenidoDao().insertar(
        Contenido(
            oaId = oaMat,
            titulo = "Sumando fracciones",
            explicacion = "Cuando dos fracciones tienen el mismo denominador, se suman los numeradores y el denominador se mantiene.",
            ejemplo = "1/5 + 2/5 = 3/5",
            origen = OrigenMaterial.PROPIO
        )
    )
    db.preguntaDao().insertar(Pregunta(
        oaId = oaMat, enunciado = "¿Cuánto es 3/4 + 1/4?",
        opcionA = "4/8", opcionB = "1", opcionC = "3/16", opcionD = "4/4 + 1",
        correcta = Opcion.B, dificultad = Dificultad.FACIL
    ))
    db.preguntaDao().insertar(Pregunta(
        oaId = oaMat, enunciado = "¿Cuál número es mayor?",
        opcionA = "0,25", opcionB = "0,5", opcionC = "0,05", opcionD = "0,2",
        correcta = Opcion.B, dificultad = Dificultad.FACIL
    ))
    db.preguntaDao().insertar(Pregunta(
        oaId = oaMat, enunciado = "¿Cuánto es el 20 % de 50?",
        opcionA = "5", opcionB = "20", opcionC = "10", opcionD = "25",
        correcta = Opcion.C, dificultad = Dificultad.MEDIA
    ))
    db.configPruebaDao().guardar(ConfigPrueba(Asignatura.MATEMATICA, "Unidad 1", maxIntentos = 2))
    db.asignacionDao().asignar(Asignacion(oaMat, "6.º A"))

    // ---------- Lenguaje: autorizada pero SIN asignar ----------
    val oaLen = db.objetivoDao().insertar(
        ObjetivoAprendizaje(
            asignatura = Asignatura.LENGUAJE,
            unidad = "Unidad 1",
            codigo = "LE06-OA-01",
            descripcion = "Ampliar el vocabulario y reconocer sinónimos",
            estado = EstadoContenido.AUTORIZADO
        )
    ).toInt()

    db.contenidoDao().insertar(
        Contenido(
            oaId = oaLen,
            titulo = "Los sinónimos",
            explicacion = "Los sinónimos son palabras diferentes que tienen un significado parecido.",
            ejemplo = "bonito – hermoso",
            origen = OrigenMaterial.PROPIO
        )
    )
    db.preguntaDao().insertar(Pregunta(
        oaId = oaLen, enunciado = "¿Cuál es un sinónimo de \"alegre\"?",
        opcionA = "triste", opcionB = "contento", opcionC = "enojado", opcionD = "cansado",
        correcta = Opcion.B, dificultad = Dificultad.FACIL
    ))
    db.preguntaDao().insertar(Pregunta(
        oaId = oaLen, enunciado = "En \"El perro ladra fuerte\", ¿cuál es el verbo?",
        opcionA = "El", opcionB = "perro", opcionC = "ladra", opcionD = "fuerte",
        correcta = Opcion.C, dificultad = Dificultad.FACIL
    ))
    // ---------- Ciencias: EN REVISIÓN (UTP debe autorizarla) ----------
    val oaCie = db.objetivoDao().insertar(
        ObjetivoAprendizaje(
            asignatura = Asignatura.CIENCIAS,
            unidad = "Unidad 1",
            codigo = "CN06-OA-01",
            descripcion = "Describir la estructura de la Tierra",
            estado = EstadoContenido.EN_REVISION
        )
    ).toInt()

    db.contenidoDao().insertar(
        Contenido(
            oaId = oaCie,
            titulo = "Las capas de la Tierra",
            explicacion = "La Tierra está formada por tres capas principales: la corteza, el manto y el núcleo.",
            ejemplo = "Vivimos sobre la corteza, que es la capa más externa.",
            origen = OrigenMaterial.PROPIO
        )
    )
    db.preguntaDao().insertar(Pregunta(
        oaId = oaCie, enunciado = "¿Cuál es la capa más externa de la Tierra?",
        opcionA = "Núcleo", opcionB = "Manto", opcionC = "Corteza", opcionD = "Magma",
        correcta = Opcion.C, dificultad = Dificultad.FACIL
    ))
    db.preguntaDao().insertar(Pregunta(
        oaId = oaCie, enunciado = "¿Qué capa de la Tierra está en el centro?",
        opcionA = "Corteza", opcionB = "Manto", opcionC = "Núcleo", opcionD = "Océano",
        correcta = Opcion.C, dificultad = Dificultad.FACIL
    ))
}