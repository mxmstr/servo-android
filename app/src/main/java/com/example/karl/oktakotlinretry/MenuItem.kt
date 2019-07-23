package com.example.karl.oktakotlinretry

import com.google.gson.annotations.SerializedName

data class MenuItem( val id: Long, val name: String )
data class MenuList (
        //@SerializedName("movies" )
        val items: List<MenuItem>
)
data class MenuEmbedded (
        @SerializedName("_embedded" )
        val list: MenuList
)

