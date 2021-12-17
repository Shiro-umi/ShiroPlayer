package com.shiroumi.shiroplayer.arch.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.shiroumi.shiroplayer.IShiroService
import com.shiroumi.shiroplayer.arch.application
import com.shiroumi.shiroplayer.service.ShiroService
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import java.lang.Math.random

@RuntimePermissions
open class BaseActivity : AppCompatActivity() {

    var shiroService = MutableLiveData<IShiroService?>()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            shiroService.value = IShiroService.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("music", "onServiceDisconnected")
        }
    }

    class SafResContract {
        companion object : ActivityResultContract<String?, String?>() {
            override fun createIntent(context: Context, input: String?) =
                Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                    putExtra("android.content.extra.SHOW_ADVANCED", true)
                }

            override fun parseResult(resultCode: Int, intent: Intent?): String? {
                if (resultCode != Activity.RESULT_OK) return null
                persistUriAccess(intent?.data as Uri, intent.flags)
                return intent.data?.toString()
            }

            @SuppressLint("WrongConstant")
            private fun persistUriAccess(uri: Uri?, flags: Int?) {
                uri ?: return
                flags ?: return
                val takeFlags: Int = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION) and flags
                try {
                    application.contentResolver.takePersistableUriPermission(uri, takeFlags)
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
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
            Intent(this, ShiroService::class.java)
        )

        bindService(
            Intent(this, ShiroService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun doOnPermissionDenied() = Toast.makeText(this, "给权限啊老哥", Toast.LENGTH_LONG).show()

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @Suppress("DEPRECATION")
    private fun initializeSystemBars() = window.decorView.run {
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

    protected fun observeBinderState(block: (IShiroService) -> Unit) =
        shiroService.observe(this) { service ->
            service ?: return@observe
            block(service)
        }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}
