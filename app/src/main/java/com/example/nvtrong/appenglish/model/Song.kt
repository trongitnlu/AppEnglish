package com.example.nvtrong.appenglish.model

import io.realm.RealmObject

open class Song(var title: String? = null, var timeLong: Int? = null, var pathDir: String? = null) : RealmObject() {
    override fun toString(): String {
        return "Song(title=$title, timeLong=$timeLong)"
    }
}
