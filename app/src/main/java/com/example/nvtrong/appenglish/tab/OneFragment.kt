package com.example.nvtrong.appenglish.tab

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleAdapter
import com.example.nvtrong.appenglish.R
import com.example.nvtrong.appenglish.listener.OnClickItemListenerFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class OneFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var listener: OnClickItemListenerFragment

    companion object {
        val KEY_TITLE = "title"
        val KEY_TIME = "time"
        val KEY_TIME_LONG = "time_long"
    }

    var arrayList = ArrayList<HashMap<String, String>>()
    lateinit var simpleAdapter: SimpleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
        simpleAdapter = SimpleAdapter(context, arrayList, R.layout.abc_list_menu_item_layout, from, to)
        listSeek.adapter = simpleAdapter
        listSeek.setOnItemClickListener { adapterView, view, i, l ->
            var song = arrayList.get(i)
            Log.d("11111111Song", song.toString())
            var title = (song.getValue(KEY_TITLE))
            var timeLong = (song.getValue(KEY_TIME_LONG))
            listener.updateUI(title, timeLong)
        }
    }

    fun addSession(title: String, time: String, timeLong: String) {
        var hashMap = HashMap<String, String>()
        var flag = false
        hashMap.put(KEY_TITLE, title)
        hashMap.put(KEY_TIME, time)
        hashMap.put(KEY_TIME_LONG, timeLong)
        arrayList.forEach {
            if (it.getValue(KEY_TITLE).equals(title)) {
                Log.d("33333333333333333333334", it.toString())
                it.put(KEY_TIME, time)
                flag = true
                Log.d("33333333333333333333335", it.toString())
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
