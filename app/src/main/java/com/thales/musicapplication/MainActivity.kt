package com.thales.musicapplication

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.thales.musicapplication.listners.SongClickListner
import com.thales.musicapplication.views.CommonViews
import com.thales.musicsdk.musicsdk.MusicProvider
import com.thales.musicsdk.musicsdk.adapters.SongAdapter
import com.thales.musicsdk.musicsdk.models.Song
import com.thales.musicsdk.musicsdk.utils.MusicFilesListner
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),SongClickListner {
    private val TAG = "MainActivity"
    private val RECORD_REQUEST_CODE = 101
    lateinit var songClickListner: SongClickListner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        songClickListner = this
        MusicProvider.getInstance(this,this)


        setupPermissions()
        disableViews()

        startMusic.setOnClickListener({
            MusicProvider.INSTANCE?.playSong(this)
        })
        pauseMusic.setOnClickListener({
            MusicProvider.INSTANCE?.pauseSong(this)
        })

        stopMusic.setOnClickListener({
            MusicProvider.INSTANCE?.stopSong(this)
        })
        getSongs.setOnClickListener({
            MusicProvider.INSTANCE?.getSongs(this, object : MusicFilesListner {
                override fun songLoaded(songs: List<Song>) {
                    Log.d(TAG,"song loaded::"+songs)
                    setUpAdapter(songs)

                }
                override fun onError(error: Throwable) {
                    Log.d(TAG,"song loaded onError::"+error)
                    displayError(getString(R.string.song_loaded_error))

                }

            })
        })

    }

    fun setUpAdapter(songList: List<Song>){
        val adapter = SongAdapter(songClickListner)
        rvMusicList.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        rvMusicList.adapter = adapter
        adapter.setItems(songList as ArrayList<Song>)
    }





    private fun setupPermissions() {
        val permission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()
        } else
            enableViews()
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            RECORD_REQUEST_CODE)
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user")
                    CommonViews.showSnackBar(rootView,getString(R.string.permission_required),"retry",object : View.OnClickListener{
                        override fun onClick(v: View?) {
                            Log.d(TAG,"clicked rety::")
                            makeRequest()
                        }
                    })


                } else {
                    Log.i(TAG, "Permission has been granted by user")
                    MusicProvider.INSTANCE?.startMusicPlayer(this)
                    enableViews()
                }
            }
        }
    }

    //enable the views only when user s accepting the permission
    private fun enableViews() {
        startMusic.isEnabled = true
        stopMusic.isEnabled = true
        pauseMusic.isEnabled = true
        getSongs.isEnabled = true
    }
    private fun disableViews() {
        startMusic.isEnabled = false
        stopMusic.isEnabled = false
        pauseMusic.isEnabled = false
        getSongs.isEnabled = false
    }

    override fun onSongSelected(index: Int) {
        Log.i(TAG, "on Song selected::"+ index)
        MusicProvider.INSTANCE?.selectedSongFromList(this,index)
    }

    private fun displayError(msg:String){
        CommonViews.showSnackBar(rootView,msg,"retry",object : View.OnClickListener{
            override fun onClick(v: View?) {
                Log.d(TAG,"clicked rety::")
            }
        })
    }

}