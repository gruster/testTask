package com.mycompany.testtask.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mycompany.testtask.data.ObjectTypeConverter

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int? = null,
    @field:TypeConverters(ObjectTypeConverter::class)
    val address: Address? = null,
    @field:TypeConverters(ObjectTypeConverter::class)
    val company: Company? = null,
    val email: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val username: String? = null,
    val website: String? = null
)