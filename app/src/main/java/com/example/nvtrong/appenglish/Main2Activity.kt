package com.example.nvtrong.appenglish

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.nvtrong.appenglish.listener.OnClickItemListenerFragment
import com.example.nvtrong.appenglish.model.Song
import com.example.nvtrong.appenglish.tab.OneFragment
import com.example.nvtrong.appenglish.tab.TwoFragment
import com.example.nvtrong.appenglish.tab.ViewPagerAdapter
import com.example.nvtrong.appenglish.ultis.Ultis.KEY_TITLE
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.IOException


class Main2Activity : AppCompatActivity(), OnClickItemListenerFragment {

    lateinit var song: String
    lateinit var mediaPlayer: MediaPlayer
    lateinit var thread: Thread
    var isPlay = true
    lateinit var myUri: Uri

    lateinit var oneFragment: OneFragment
    lateinit var twoFragment: TwoFragment

    lateinit var realm: Realm
    var isPause = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        realm = MyApplication.realm
        mediaPlayer = MediaPlayer();
        init()
        onChangeRealm()

    }

    private fun init() {
        song = intent.getStringExtra(MainActivity.PUT_EXTRA_SONG)
        title = song.substring(song.lastIndexOf("/") + 1)
        myUri = Uri.parse("file://$song") // initialize Uri here

        try {
            mediaPlayer.setDataSource(applicationContext, myUri)
        } catch (e: IllegalArgumentException) {
            Toast.makeText(applicationContext, "You might not set the URI correctly!", Toast.LENGTH_LONG).show()
        } catch (e: SecurityException) {
            Toast.makeText(applicationContext, "You might not set the URI correctly!", Toast.LENGTH_LONG).show()
        } catch (e: IllegalStateException) {
            Toast.makeText(applicationContext, "You might not set the URI correctly!", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            mediaPlayer.prepare()
        } catch (e: IllegalStateException) {
            Toast.makeText(applicationContext, "You might not set the URI correctly!", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Toast.makeText(applicationContext, "You might not set the URI correctly!", Toast.LENGTH_LONG).show()
        }
        var handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                timeCurrent.setText(msg.arg1.toString())
                timeHours.setText(msg.obj.toString())
            }
        }
        mediaPlayer.start();
        thread = Thread(object : Runnable {
            override fun run() {
                while (true)
                    while (isPlay) {
                        Log.d("4444444444444444", convertTime(mediaPlayer.currentPosition))
                        Thread.sleep(1000)
                        var message = Message()
                        message.arg1 = mediaPlayer.currentPosition
                        message.obj = convertTime(mediaPlayer.currentPosition)
                        handler.sendMessage(message)
                    }
            }
        })
        thread.start()

        btnSeek.setOnClickListener {
            seekTo(editText.text.toString().toInt())
            oneFragment.addSession("${nameSession.text}", convertTime(editText.text.toString().toInt()), editText.text.toString())

        }
        btnNext.setOnClickListener {
            next5S()
        }
        btnStop.setOnClickListener {
            stopMedia()
        }
        btnPrevious.setOnClickListener {
            previous5S()
        }
        btnPause.setOnClickListener {
            pause()
        }
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length!! > 0)
                    textViewTimeSeek.text = convertTime(p0.toString().toInt())
                else {
                    textViewTimeSeek.text = 0.toString()
                    editText.setText(0.toString())
                    editText.requestFocus()
                    editText.selectAll()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        textViewTimeSeek.text = 0.toString()
        editText.setText(0.toString())

        setupTab()
        setSession()

    }

    fun stopMedia() {
        if (isPlay) {
            mediaPlayer.stop()
            isPlay = false;
            btnStop.text = "Start"
        } else {
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(applicationContext, myUri)
            mediaPlayer.prepare()
            mediaPlayer.start()
            isPlay = true;
            btnStop.text = "Stop"
        }

    }

    private fun previous5S() {
        if (mediaPlayer.currentPosition >= 5000)
            mediaPlayer.seekTo(mediaPlayer.currentPosition - 5000)
    }

    private fun next5S() {
        mediaPlayer.seekTo(mediaPlayer.currentPosition + 5000)
    }

    private fun seekTo(int: Int) {
        mediaPlayer.seekTo(int)
    }

    private fun setSession() {
        btnSet.setOnClickListener {
            addSongToRealm(nameSession.text.toString(), editText.text.toString().toInt())
            twoFragment.notifyDataChanged(this.song)
        }
    }

    var durationInMillisPause = 0
    private fun pause() {
        if (!isPause) {
            btnPause.text = "Resume"
            mediaPlayer.pause()
            isPause = true
            durationInMillisPause = mediaPlayer.currentPosition
        } else {
            btnPause.text = "Pause"
            mediaPlayer.start()
            mediaPlayer.seekTo(durationInMillisPause)
            isPause = false
        }
    }

    fun convertTime(durationInMillis: Int): String {
        val millis = durationInMillis % 1000
        val second = durationInMillis / 1000 % 60
        val minute = durationInMillis / (1000 * 60) % 60
        val hour = durationInMillis / (1000 * 60 * 60) % 24

        val time = String.format("%02d:%02d:%02d.%d", hour, minute, second, millis)
        return "Time: $time - $durationInMillis"
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        isPlay = false;
    }

    private fun setupTab() {
        setupViewPager(viewpager);
        tabs.setupWithViewPager(viewpager)
    }

    private fun setupViewPager(viewpager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        oneFragment = OneFragment()
        twoFragment = TwoFragment()
        oneFragment.addListener(this)
        twoFragment.addListener(this)
        var bundlePath = Bundle()
        bundlePath.putString("pathDir", this.song)
        twoFragment.arguments = bundlePath
        adapter.addFragment(oneFragment, "ONE")
        adapter.addFragment(twoFragment, "TWO")
        viewpager.setAdapter(adapter);
    }

    private fun addSongToRealm(title: String, timeLog: Int) {
        var resultsSong = realm.where(Song::class.java).contains(KEY_TITLE, title).findAll()
        if (resultsSong.size > 0) {
            Toast.makeText(this, "Failed, The title was be available!", Toast.LENGTH_SHORT).show()
        } else {
            realm.beginTransaction()
            var song = realm.createObject(Song::class.java)

            song.title = title
            song.timeLong = timeLog
            song.pathDir = this.song
            realm.commitTransaction()

            Toast.makeText(this, "Add Success!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun updateUI(title: String, time: String) {
        nameSession.setText(title)
        editText.setText(time)
        seekTo(time.toInt())
    }

    fun onChangeRealm() {
        var favourites = realm.where(Song::class.java).contains("pathDir", this.song).findAll()
        favourites.addChangeListener { t, changeSet ->
            twoFragment.notifyDataChanged(this.song)
        }
    }
}
