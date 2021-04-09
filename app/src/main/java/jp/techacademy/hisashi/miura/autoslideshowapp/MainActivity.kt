package jp.techacademy.hisashi.miura.autoslideshowapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
//import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.content.Intent
import android.database.Cursor

class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE = 100
    private var mTimer: Timer? = null
    private var mHandler = Handler()
    var cursor: Cursor?= null//★1　

    //onrequestpermissionresult定義が必要　レッスン５の７章と８章の復習が必要



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resolver = contentResolver//★2



        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                // 許可されている
                Log.d("slideshowapp", "android6パーミッション有り")

                cursor=resolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                    null, // 項目（null = 全項目）
                    null, // フィルタ条件（null = フィルタなし）
                    null, // フィルタ用パラメータ
                    null // ソート (nullソートなし）
                )
                if (cursor!!.moveToFirst()) {
                    val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor!!.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    imageView.setImageURI(imageUri)
                }
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_CODE
                )
                Log.d("slideshowapp", "android6パーミッション無し")
                timer.text="画像の読み込みを許可して、アプリを再起動してください。"
            }
            // Android 5系以下の場合
        } else {
            Log.d("slideshowapp", "android5")//カーソル読み込み処理ないので、落ちる
        }

        start_button.setOnClickListener {//自動スライド処理
            if (mTimer == null) {
                mTimer = Timer()
                mTimer!!.schedule(object : TimerTask() {
                    override fun run() {
                        susumu_button.isClickable = false
                        modoru_button.isClickable = false



                        if (cursor!!.moveToNext()) {
                            // indexからIDを取得し、そのIDから画像のURIを取得する
                            val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                            val id = cursor!!.getLong(fieldIndex)
                            val imageUri = ContentUris.withAppendedId(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                id
                            )
                            Log.d("slideshowapp", "タイマー起動中")

                            mHandler.post {
                                imageView.setImageURI(imageUri)

                            }
                        } else {
                            cursor!!.moveToFirst()
                            val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                            val id = cursor!!.getLong(fieldIndex)
                            val imageUri = ContentUris.withAppendedId(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                id
                            )

                            mHandler.post {
                                imageView.setImageURI(imageUri)

                            }
                        }
                    }
                }, 2000, 2000) // 最初に始動させるまで2000ミリ秒、ループの間隔を2000ミリ秒 に設定
            } else {
                if (mTimer != null) {
                    susumu_button.isClickable = true//ボタン無効化処理
                    modoru_button.isClickable = true
                    mTimer!!.cancel()
                    mTimer = null
                }
            }
        }

        susumu_button.setOnClickListener {//進むボタン処理
            if (cursor!!.moveToNext()) {
                val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor!!.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)
            } else {
                cursor!!.moveToFirst()
                val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor!!.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)
            }
        }


        modoru_button.setOnClickListener {//戻るボタン処理
            if (cursor!!.moveToPrevious()) {
                // indexからIDを取得し、そのIDから画像のURIを取得する
                val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor!!.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)
            } else {
                cursor!!.moveToLast()
                val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor!!.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)

            }
        }
    }
}

