package cl.duoc.mpazpro.data.local.entity

import cl.duoc.mpazpro.data.local.enums.*
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "usuarios")
data class Usuario (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val clave: String,
    val rol: Rol,
    val curso: String
)