package com.example.nvtrong.appenglish.model

import io.realm.RealmObject

open class Song(var title: String? = null, var timeLong: Int? = null) : RealmObject() {
    override fun toString(): String {
        return "Song(title=$title, timeLong=$timeLong)"
    }
}
