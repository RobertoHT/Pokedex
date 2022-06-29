package com.architect.coders.pokedex.framework.network

import com.google.gson.annotations.SerializedName

data class TypeR(
    @SerializedName("slot")
    val slot: Int,
    @SerializedName("type")
    val type: TypeX
)