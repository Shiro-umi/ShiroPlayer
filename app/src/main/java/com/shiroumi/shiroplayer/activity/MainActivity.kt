package com.shiroumi.shiroplayer.activity

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.shiroumi.shiroplayer.arch.activity.BaseActivity
import com.shiroumi.shiroplayer.components.PlayMode
import com.shiroumi.shiroplayer.composable.*
import com.shiroumi.shiroplayer.composable.common.navigation.NavHostController
import com.shiroumi.shiroplayer.composable.common.theme.CustomTheme
import com.shiroumi.shiroplayer.composable.common.theme.Theme
import com.shiroumi.shiroplayer.composable.main.Home
import com.shiroumi.shiroplayer.viewmodel.ShiroViewModel
import com.shiroumi.shiroplayer.viewmodel.ThemeViewModel


class MainActivity : BaseActivity() {

    private val shiroViewModel: ShiroViewModel by viewModels()
    private val themeViewModel: ThemeViewModel by viewModels()

    private var safResolver: ((String?) -> Unit)? = null

    private val safLauncher = registerForActivityResult(SafResContract) { result ->
        safResolver?.invoke(result)
    }
    private var mSensorManager: SensorManager? = null
    private var msensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeBinderState { token ->
            shiroViewModel.setBinder(token)
            shiroViewModel.setPlayMode(PlayMode.NORMAL.ordinal)
        }

        setContent {
            val theme by themeViewModel.theme.observeAsState(Theme.Light)
            val navCtrl = NavHostController(rememberNavController())
            CompositionLocalProvider(
                LocalMainActivity provides this@MainActivity,
                LocalShiroViewModel provides shiroViewModel,
                LocalCommonViewModel provides themeViewModel,
                LocalNavCtrl provides navCtrl,
            ) {
                CustomTheme(theme) {
                    Home()
                }
            }
        }

        mSensorManager = this.getSystemService(SENSOR_SERVICE) as SensorManager //获取SensorManager

        msensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_GRAVITY) //获取Sensor

        mSensorManager!!.registerListener(object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            override fun onSensorChanged(event: SensorEvent) { //实现感应检测的监听功能
                if (event.sensor == null) {
                    return
                }
                if (event.sensor.type == Sensor.TYPE_GRAVITY) {
                    val x = event.values[0].toInt()
                    val y = event.values[1].toInt()
                    val z = event.values[2].toInt()
                    themeViewModel.deviceGravity.postValue(DeviceGravity(x, y, z))
                }
            }

        }, msensor, SensorManager.SENSOR_DELAY_GAME) //注册监听器

    }

    fun launchSaf(block: (result: String?) -> Unit) {
        safResolver = block
        safLauncher.launch(null)
    }

    class DeviceGravity(val x: Int = 0, val y: Int = 0, val z: Int = 0)

}



