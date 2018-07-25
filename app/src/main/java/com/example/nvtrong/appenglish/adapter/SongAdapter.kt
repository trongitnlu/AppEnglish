package com.example.nvtrong.appenglish.adapter

import android.content.Context
import android.support.annotation.NonNull
import android.support.annotation.RawRes
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class SongAdapter(@NonNull var context: Context, @NonNull var map: HashMap<String, String>, @RawRes var resourceId: Int, var from: Array<String>, var to: Array<Int>) : BaseAdapter() {
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(p0: Int): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemId(p0: Int): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}