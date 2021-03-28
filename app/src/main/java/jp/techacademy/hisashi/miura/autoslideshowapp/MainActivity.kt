package jp.techacademy.hisashi.miura.autoslideshowapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var mTimer: Timer? = null



    private var mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val images = arrayOf<String>("a","b","c","d","e","f","g","h","i")
        var imagenumber:Int = 1
        var maxnumber:Int = 9

        timer.text = imagenumber.toString()

        start_button.setOnClickListener {
            if (mTimer == null){
                mTimer = Timer()
                mTimer!!.schedule(object : TimerTask() {
                    override fun run() {
                        susumu_button.isClickable = false
                        modoru_button.isClickable = false
                                                imagenumber += 1
                        mHandler.post {
                            timer.text = imagenumber.toString()
                        }
                    }
                }, 2000, 2000) // 最初に始動させるまで100ミリ秒、ループの間隔を100ミリ秒 に設定
            }else {
                if (mTimer != null){
                    susumu_button.isClickable = true
                    modoru_button.isClickable = true
                    mTimer!!.cancel()
                    mTimer = null
                }
            }
        }

        susumu_button.setOnClickListener {
            if (imagenumber < maxnumber){
                imagenumber += 1
                timer.text = imagenumber.toString()

            }else {
                imagenumber = 1
                timer.text = imagenumber.toString()
            }
        }

        modoru_button.setOnClickListener {
            if (imagenumber > 1){
                imagenumber -= 1
                timer.text = imagenumber.toString()

            }else {
                imagenumber = maxnumber
                timer.text = imagenumber.toString()
            }
        }
    }
}