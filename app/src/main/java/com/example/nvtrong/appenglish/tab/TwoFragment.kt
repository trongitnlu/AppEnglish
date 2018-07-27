package com.example.nvtrong.appenglish.tab


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleAdapter
import com.example.nvtrong.appenglish.MyApplication
import com.example.nvtrong.appenglish.R
import com.example.nvtrong.appenglish.adapter.SongTwoAdapter
import com.example.nvtrong.appenglish.listener.OnClickItemListenerFragment
import com.example.nvtrong.appenglish.model.Song
import com.example.nvtrong.appenglish.ultis.Ultis.KEY_TIME
import com.example.nvtrong.appenglish.ultis.Ultis.KEY_TIME_LONG
import com.example.nvtrong.appenglish.ultis.Ultis.KEY_TITLE
import com.example.nvtrong.appenglish.ultis.Ultis.convertTime
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_one.*

class TwoFragment : Fragment() {
    lateinit var arrayList: ArrayList<HashMap<String, String>>
    lateinit var realm: Realm
    lateinit var simpleAdapter: SongTwoAdapter
    lateinit var listener: OnClickItemListenerFragment
    lateinit var listSeek: ListView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_two, container, false)
        listSeek = view.findViewById(R.id.listSeek)
        setupListView()
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = MyApplication.realm
        arrayList = getListSong()
    }

    private fun getListSong(): ArrayList<HashMap<String, String>> {
        var arrayList = ArrayList<HashMap<String, String>>()
        var resultsSong = realm.where(Song::class.java).findAll()
        resultsSong.forEach {
            var hashMap = HashMap<String, String>()

            hashMap.put(KEY_TITLE, it.title!!)
            hashMap.put(KEY_TIME, convertTime(it.timeLong!!))
            hashMap.put(KEY_TIME_LONG, it.timeLong!!.toString())

            arrayList.add(hashMap)
        }
        return arrayList
    }


    private fun setupListView() {
        var from = arrayOf(KEY_TITLE, KEY_TIME)
        var to = intArrayOf(R.id.title, R.id.shortcut)
        simpleAdapter = SongTwoAdapter(context, arrayList, R.layout.item_fragment_two, from, to)
        simpleAdapter.addListener(listener)
        listSeek.adapter = simpleAdapter
        listSeek.setOnItemClickListener { adapterView, view, i, l ->
            var song = arrayList.get(i)
            var title = (song.getValue(KEY_TITLE))
            var timeLong = (song.getValue(KEY_TIME_LONG))
            listener.updateUI(title, timeLong)
        }
    }

    fun addListener(listener: OnClickItemListenerFragment) {
        this.listener = listener
    }

    fun notifyDataChanged() {
        arrayList = getListSong()
        setupListView()
    }
}
