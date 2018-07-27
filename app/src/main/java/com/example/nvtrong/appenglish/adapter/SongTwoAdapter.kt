package com.example.nvtrong.appenglish.adapter

import android.app.Activity
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.annotation.NonNull
import android.support.v7.app.AlertDialog
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

class SongTwoAdapter(@NonNull var context: Context?, @NonNull var list: List<out Map<String, *>>, @LayoutRes var resourceId: Int, var from: Array<String>, var to: IntArray) : BaseAdapter() {
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
        view.findViewById<View>(R.id.btnDelete).setOnClickListener {
            val alert_builder = AlertDialog.Builder(context as Activity)
            alert_builder.setMessage("Do you want delete: ${map[Ultis.KEY_TITLE]}")
            alert_builder.setTitle("Alert")
            alert_builder.setPositiveButton("OK") { dialogInterface, i ->
                var resultsSong = realm.where(Song::class.java).contains(Ultis.KEY_TITLE, map[Ultis.KEY_TITLE].toString()).findAll()
                realm.executeTransaction {
                    Log.d("Delete realm", resultsSong.deleteAllFromRealm().toString())
                    Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
                }
            }

            alert_builder.setNeutralButton("Cancel", null)
            val dialog = alert_builder.create()
            dialog.show()

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
