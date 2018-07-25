package com.example.nvtrong.appenglish

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.AdapterView
import android.widget.SimpleAdapter
import android.widget.Toast
import com.example.nvtrong.appenglish.model.SongsManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        const val PUT_EXTRA_SONG: String = "song"
    }

    lateinit var songsManager: SongsManager
    lateinit var listSong: ArrayList<HashMap<String, String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        songsManager = SongsManager(this)
        init()
    }

    private fun init() {
        listSong = songsManager.playList
        val from = arrayOf("songTitle", "songPath")
        val to = intArrayOf(R.id.title, R.id.shortcut)
        listView.adapter = SimpleAdapter(baseContext, listSong, R.layout.abc_list_menu_item_layout, from, to)
        listView.setOnItemClickListener({ adapterView, view, i, l ->
            Log.d("22222222", adapterView.toString() + "---" + view.toString() + "---" + i + "--" + l)
            val intent = Intent(this, Main2Activity::class.java)
            intent.putExtra(PUT_EXTRA_SONG, listSong.get(i).get("songPath"))
            startActivity(intent)
        })
    }

    private fun print(array: ArrayList<HashMap<String, String>>) {
        array.forEach {
            Log.d("11111111111: " + it.keys, it.values.toString())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SongsManager.RUNTIME_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                init()
                Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "FAILED!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
