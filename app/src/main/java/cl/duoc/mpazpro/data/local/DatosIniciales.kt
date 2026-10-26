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
            codigo = "MA06-OA-08",
            descripcion = "Resolver problemas rutinarios y no rutinarios que involucren adiciones y sustracciones de fracciones propias, impropias, números mixtos o decimales hasta la milésima",
            estado = EstadoContenido.AUTORIZADO,
            eje = "Números y operaciones",
            textoEscolar = "Texto del Estudiante Matemática 6.º básico (Mineduc)"
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
            codigo = "LE06-OA-01",   // TODO: verificar el código oficial en curriculumnacional.cl
            descripcion = "Ampliar el vocabulario y reconocer sinónimos",
            estado = EstadoContenido.AUTORIZADO,
            eje = "Lectura",
            textoEscolar = "Texto del Estudiante Lenguaje y Comunicación 6.º básico (Mineduc)"
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
            codigo = "CN06-OA-01",   // TODO: cambiar por un OA real de 6.º (ver curriculumnacional.cl)
            descripcion = "Describir la estructura de la Tierra",
            estado = EstadoContenido.EN_REVISION,
            eje = "Ciencias de la Tierra y el Universo",
            textoEscolar = "Texto del Estudiante Ciencias Naturales 6.º básico (Mineduc)"
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

    // ---------- Historia: autorizada, sin asignar ----------
    val oaHis = db.objetivoDao().insertar(
        ObjetivoAprendizaje(
            asignatura = Asignatura.HISTORIA,
            unidad = "Unidad 1",
            codigo = "HI06-OA-18",
            descripcion = "Explicar que los derechos generan deberes y responsabilidades en las personas e instituciones",
            estado = EstadoContenido.AUTORIZADO,
            eje = "Formación ciudadana",
            textoEscolar = "Texto del Estudiante Historia, Geografía y Ciencias Sociales 6.º básico (Mineduc)"
        )
    ).toInt()
    db.contenidoDao().insertar(
        Contenido(
            oaId = oaHis,
            titulo = "Derechos y deberes",
            explicacion = "Los derechos protegen a todas las personas. Los deberes son responsabilidades que ayudan a convivir en sociedad.",
            ejemplo = "Derecho: recibir educación. Deber: respetar a los demás.",
            origen = OrigenMaterial.PROPIO
        )
    )
    db.preguntaDao().insertar(Pregunta(
        oaId = oaHis, enunciado = "¿Cuál de estos es un deber ciudadano?",
        opcionA = "Respetar las normas de convivencia", opcionB = "Recibir educación",
        opcionC = "Opinar libremente", opcionD = "Tener un nombre",
        correcta = Opcion.A, dificultad = Dificultad.FACIL
    ))
    db.preguntaDao().insertar(Pregunta(
        oaId = oaHis, enunciado = "Recibir atención de salud es un...",
        opcionA = "derecho", opcionB = "deber", opcionC = "castigo", opcionD = "favor",
        correcta = Opcion.A, dificultad = Dificultad.FACIL
    ))
    db.configPruebaDao().guardar(ConfigPrueba(Asignatura.HISTORIA, "Unidad 1", maxIntentos = 2))

    // ---------- Actividades y desafíos ----------
    val dao = db.actividadDao()

    dao.insertar(Actividad(
        oaId = oaMat, titulo = "Suma de fracciones", tipo = TipoActividad.ACTIVIDAD,
        enunciado = "¿Cuánto es 2/5 + 1/5?",
        opcionA = "3/10", opcionB = "3/5", opcionC = "2/5", opcionD = "1/5",
        correcta = Opcion.B, dificultad = Dificultad.FACIL
    ))
    dao.insertar(Actividad(
        oaId = oaMat, titulo = "Desafío: decimales", tipo = TipoActividad.DESAFIO,
        enunciado = "Ana corrió 1,5 km y luego 0,75 km. ¿Cuántos kilómetros corrió en total?",
        opcionA = "1,80 km", opcionB = "2,05 km", opcionC = "2,25 km", opcionD = "0,90 km",
        correcta = Opcion.C, dificultad = Dificultad.MEDIA
    ))
    dao.insertar(Actividad(
        oaId = oaLen, titulo = "Sinónimos", tipo = TipoActividad.ACTIVIDAD,
        enunciado = "¿Cuál palabra es sinónimo de \"rápido\"?",
        opcionA = "lento", opcionB = "pesado", opcionC = "tranquilo", opcionD = "veloz",
        correcta = Opcion.D, dificultad = Dificultad.FACIL
    ))
    dao.insertar(Actividad(
        oaId = oaLen, titulo = "Desafío: reemplaza la palabra", tipo = TipoActividad.DESAFIO,
        enunciado = "¿Qué palabra puede reemplazar a \"enorme\" en \"El elefante es enorme\"?",
        opcionA = "pequeño", opcionB = "gigante", opcionC = "delgado", opcionD = "antiguo",
        correcta = Opcion.B, dificultad = Dificultad.MEDIA
    ))
    dao.insertar(Actividad(
        oaId = oaCie, titulo = "Capas de la Tierra", tipo = TipoActividad.ACTIVIDAD,
        enunciado = "¿Cuál capa está formada por roca y es donde vivimos?",
        opcionA = "Núcleo", opcionB = "Manto", opcionC = "Corteza", opcionD = "Océano",
        correcta = Opcion.C, dificultad = Dificultad.FACIL
    ))
    dao.insertar(Actividad(
        oaId = oaCie, titulo = "Desafío: ordena las capas", tipo = TipoActividad.DESAFIO,
        enunciado = "Desde la superficie hacia el centro, ¿cuál es el orden correcto?",
        opcionA = "Núcleo – manto – corteza", opcionB = "Manto – corteza – núcleo",
        opcionC = "Corteza – núcleo – manto", opcionD = "Corteza – manto – núcleo",
        correcta = Opcion.D, dificultad = Dificultad.MEDIA
    ))
    dao.insertar(Actividad(
        oaId = oaHis, titulo = "Derechos y deberes", tipo = TipoActividad.ACTIVIDAD,
        enunciado = "Si tengo derecho a opinar, también tengo el deber de...",
        opcionA = "imponer mi opinión", opcionB = "respetar la opinión de los demás",
        opcionC = "no escuchar a nadie", opcionD = "gritar",
        correcta = Opcion.B, dificultad = Dificultad.FACIL
    ))
    dao.insertar(Actividad(
        oaId = oaHis, titulo = "Desafío: ¿para qué sirven los deberes?", tipo = TipoActividad.DESAFIO,
        enunciado = "¿Por qué es importante cumplir nuestros deberes?",
        opcionA = "Para recibir premios", opcionB = "Porque nadie nos ve",
        opcionC = "Para convivir mejor y respetar los derechos de todos", opcionD = "No es importante",
        correcta = Opcion.C, dificultad = Dificultad.MEDIA
    ))
}