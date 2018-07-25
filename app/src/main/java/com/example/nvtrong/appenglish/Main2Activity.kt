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
import com.example.nvtrong.appenglish.tab.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.IOException
import com.example.nvtrong.appenglish.tab.OneFragment
import com.example.nvtrong.appenglish.tab.TwoFragment


class Main2Activity : AppCompatActivity() {
    lateinit var song: String
    lateinit var mediaPlayer: MediaPlayer
    lateinit var thread: Thread
    var isPlay = true
    lateinit var myUri: Uri
    lateinit var arrayList: ArrayList<HashMap<String, String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        mediaPlayer = MediaPlayer();
        init()
    }

    private fun init() {
        song = intent.getStringExtra(MainActivity.PUT_EXTRA_SONG)
        Log.d("333333", song)
        myUri = Uri.parse("file://$song") // initialize Uri here
        Log.d("URI:11111111111", myUri.toString())

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

    fun convertTime(durationInMillis: Int): String {
        val millis = durationInMillis % 1000
        val second = durationInMillis / 1000 % 60
        val minute = durationInMillis / (1000 * 60) % 60
        val hour = durationInMillis / (1000 * 60 * 60) % 24

        val time = String.format("%02d:%02d:%02d.%d", hour, minute, second, millis)
        return "Time: $time"
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
        adapter.addFragment(OneFragment(), "ONE")
        adapter.addFragment(TwoFragment(), "TWO")
        viewpager.setAdapter(adapter);
    }
}
