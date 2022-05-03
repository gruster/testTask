package com.mycompany.testtask.models

import com.google.gson.annotations.SerializedName

data class Company(
    val name: String,
    @SerializedName("catchPhrase")
    val catchPhrase: String,
    val bs: String
)