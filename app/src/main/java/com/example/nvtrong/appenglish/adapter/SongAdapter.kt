package com.example.nvtrong.appenglish.adapter

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.annotation.NonNull
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.nvtrong.appenglish.MyApplication
import com.example.nvtrong.appenglish.R
import com.example.nvtrong.appenglish.listener.OnClickItemListenerFragment
import com.example.nvtrong.appenglish.model.Song
import com.example.nvtrong.appenglish.ultis.Ultis
import io.realm.Realm

class SongAdapter(@NonNull var context: Context?, @NonNull var list: List<out Map<String, *>>, @LayoutRes var resourceId: Int, var from: Array<String>, var to: IntArray) : BaseAdapter() {
    lateinit var listenerFragment: OnClickItemListenerFragment
    lateinit var realm: Realm

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        realm = MyApplication.realm
        val viewHolder: ViewHolder
        val view: View
        if (p1 == null) {
            view = View.inflate(context, resourceId, null)
            viewHolder = ViewHolder()
        } else {
            view = p1
//            viewHolder = p1.tag as ViewHolder
        }
        var map = getItem(p0) as Map<String, *>
        for (i in 0..(from.size - 1)) {
            var value = map.getValue(from[i])
            var textView = view.findViewById<TextView>(to[i])
            textView.text = value.toString()
        }
        view.findViewById<View>(R.id.btnAdd).setOnClickListener {
            var resultsSong = realm.where(Song::class.java).contains(Ultis.KEY_TITLE, map[Ultis.KEY_TITLE].toString()).findAll()
            if (resultsSong.size > 0) {
                Toast.makeText(context, "Failed, The title was be available!", Toast.LENGTH_SHORT).show()
            } else {
                realm.beginTransaction()
                var song = realm.createObject(Song::class.java)

                song.title = map[Ultis.KEY_TITLE].toString()
                song.timeLong = map[Ultis.KEY_TIME_LONG].toString().toInt()
                realm.commitTransaction()

                var results = realm.where(Song::class.java).findAll()
                results.forEach {
                    Log.d("333333333333333333", it.toString())
                }
                Toast.makeText(context, "Add Success!", Toast.LENGTH_SHORT).show()
            }
        }
        view.setOnClickListener {
            listenerFragment.updateUI(map.getValue(Ultis.KEY_TITLE).toString(), map.getValue(Ultis.KEY_TIME_LONG).toString())
            Toast.makeText(context, map.getValue(Ultis.KEY_TITLE).toString(), Toast.LENGTH_SHORT).show()
        }

        return view
    }

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    fun addListener(listener: OnClickItemListenerFragment) {
        this.listenerFragment = listener
    }

    class ViewHolder {
        lateinit var btnAdd: ImageButton
    }

}
