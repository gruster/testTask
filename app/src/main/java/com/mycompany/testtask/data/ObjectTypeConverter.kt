package com.mycompany.testtask.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mycompany.testtask.models.Address
import com.mycompany.testtask.models.Company

class ObjectTypeConverter {

    private val gson: Gson by lazy { Gson() }

    @TypeConverter
    fun fromAddress(address: Address): String?{
        return gson.toJson(address)
    }

    @TypeConverter
    fun toAddress(value: String): Address {
        val arrayTutorialType = object : TypeToken<Address>() {}.type
        return gson.fromJson(value, arrayTutorialType) as Address
    }

    @TypeConverter
    fun fromCompany(company: Company?): String?{
        return gson.toJson(company)
    }

    @TypeConverter
    fun toCompany(value: String): Company {
        val arrayTutorialType = object : TypeToken<Company>() {}.type
        return gson.fromJson(value, arrayTutorialType) as Company
    }
}