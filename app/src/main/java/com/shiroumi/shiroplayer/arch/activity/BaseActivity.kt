package com.shiroumi.shiroplayer.arch.activity

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.DocumentsContract
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.shiroumi.shiroplayer.IMusicService
import com.shiroumi.shiroplayer.service.MusicService
import permissions.dispatcher.*

@RuntimePermissions
open class BaseActivity : AppCompatActivity() {
    private var musicService = MutableLiveData<IMusicService?>()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            musicService.value = IMusicService.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("music", "onServiceDisconnected")
        }
    }

    class SafResContract : ActivityResultContract<String?, String?>() {
        override fun createIntent(context: Context, input: String?): Intent {
            return Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                putExtra("android.content.extra.SHOW_ADVANCED", true)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            return intent?.data?.run {
                toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeSystemBars()
        doStartServiceWithPermissionCheck()
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun doStartService() {
        startService(
            Intent(this, MusicService::class.java)
        )

        bindService(
            Intent(this, MusicService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun doOnPermissionDenied() {
        Toast.makeText(this, "给权限啊老哥", Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @Suppress("DEPRECATION")
    private fun initializeSystemBars() {
        window.decorView.apply {
            if (Build.VERSION.SDK_INT >= 30) {
                windowInsetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.statusBarColor = Color.rgb(255, 255, 255)
            window.navigationBarColor = Color.TRANSPARENT
        }
    }

    fun observeBinderState(block: (IMusicService) -> Unit) {
        musicService.observe(this) { service ->
            service ?: return@observe
            block(service)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}
