package com.example.nvtrong.appenglish.tab

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.nvtrong.appenglish.MyApplication
import com.example.nvtrong.appenglish.R
import com.example.nvtrong.appenglish.adapter.SongAdapter
import com.example.nvtrong.appenglish.listener.OnClickItemListenerFragment
import com.example.nvtrong.appenglish.ultis.Ultis.KEY_TIME
import com.example.nvtrong.appenglish.ultis.Ultis.KEY_TIME_LONG
import com.example.nvtrong.appenglish.ultis.Ultis.KEY_TITLE
import io.realm.Realm

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class OneFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var listener: OnClickItemListenerFragment
    var arrayList = ArrayList<HashMap<String, String>>()
    lateinit var simpleAdapter: SongAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_one, container, false)
        var listSeek: ListView = view.findViewById(R.id.listSeek)
        setupListView(listSeek)
        return view
    }

    fun setupListView(listSeek: ListView) {
        var from = arrayOf(KEY_TITLE, KEY_TIME)
        var to = intArrayOf(R.id.title, R.id.shortcut)
        simpleAdapter = SongAdapter(context, arrayList, R.layout.item_fragment_one, from, to)
        simpleAdapter.addListener(listener)
        listSeek.adapter = simpleAdapter
//        listSeek.setOnItemClickListener { adapterView, view, i, l ->
//            var song = arrayList.get(i)
//            Log.d("11111111Song", song.toString())
//            Log.d("11111111Song111", view.toString())
//            var title = (song.getValue(KEY_TITLE))
//            var timeLong = (song.getValue(KEY_TIME_LONG))
//            listener.updateUI(title, timeLong)
//        }

    }

    fun addSession(title: String, time: String, timeLong: String) {
        var hashMap = HashMap<String, String>()
        var flag = false
        hashMap[KEY_TITLE] = title
        hashMap[KEY_TIME] = time
        hashMap[KEY_TIME_LONG] = timeLong
        arrayList.forEach {
            if (it.getValue(KEY_TITLE).equals(title)) {
                it.put(KEY_TIME, time)
                it.put(KEY_TIME_LONG, timeLong)
                flag = true
            }
        }
        if (!flag) {
            arrayList.add(hashMap)
            arrayList.sortBy {
                it.getValue(KEY_TITLE)
            }
        }
        simpleAdapter.notifyDataSetChanged()
    }

    fun addListener(listener: OnClickItemListenerFragment) {
        this.listener = listener
    }
}
