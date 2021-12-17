package com.shiroumi.shiroplayer.viewmodel

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.*
import com.shiroumi.shiroplayer.activity.MainActivity
import com.shiroumi.shiroplayer.composable.common.theme.Theme
import com.shiroumi.shiroplayer.composable.themeViewModel

/**
 * ThemeViewModel
 * 通过CompositionLocal隐式传递，用于管理主题和同步主题切换动画状态
 */
class ThemeViewModel : ViewModel() {

    var deviceGravity = MutableLiveData(MainActivity.DeviceGravity())

    // 用于存储当前出题
    private val _theme = MutableLiveData<Theme>(Theme.Dark)
    val theme get() = _theme as LiveData<Theme>
    val currentTheme get() = theme.value ?: Theme.Light

    // 切换主题
    fun light() = _theme.postValue(Theme.Light)
    fun dark() = _theme.postValue(Theme.Dark)

    /**
     * 主题切换动画状态同步系统
     * 用于区分不同Composable代码块中theme和colorPalette相关animateColorAsState动画多次触发问题
     * 参考下面的animateColorAsState扩展方法
     */
    // 每个新key都会使这个变量*2，转换为二进制之后仅1位等于1，用于将一个新key转换为二进制表示
    private var maxObserverInt = 0x1

    // 存储key对应的二进制表示
    private val observerIntStore: HashMap<String, Int> = HashMap()

    // 存储key对应的animating状态observer
    private val observerStore: HashMap<String, MutableList<Observer<*>>> = HashMap()

    // 用于标记多个key对应的动画是否已经完成，主题动画执行的时候该key对应的动画不应该被执行，通过对应位的状态标记
    val isThemeAnimating = MutableLiveData(0x0)

    // 主题切换时触发，将所有key对应的冲突动画标记为执行中
    fun themeAnimStart() = isThemeAnimating.postValue(
        run {
            var res = 0x0
            observerIntStore.values.forEach { value ->
                res = res or value
            }
            res
        }
    )

    // 停止，将key对应位的值恢复为0
    fun themeAnimStop(key: String) = isThemeAnimating.postValue(
        run {
            val inv = observerIntStore[key]?.inv() ?: 0x0
            isThemeAnimating.value?.and(inv) ?: isThemeAnimating.value
        }
    )

    // 监听主题动画状态，包括新key触发对应二进制标记的生成
    private fun observeThemeAnimState(
        owner: LifecycleOwner,
        key: String,
        observer: Observer<Int>
    ) {
        if (observerIntStore[key] == null) {
            maxObserverInt *= 2
            observerIntStore[key] = maxObserverInt
        }
        observerStore[key]?.add(observer)
        isThemeAnimating.observe(owner, observer)
    }

    private fun removeObserver(
        key: String,
        observer: Observer<Int>
    ) {
        observerStore[key]?.clear()
        observerStore.remove(key)
        isThemeAnimating.removeObserver(observer)
    }

    @Composable
    fun LiveData<Int>.observeAsState(key: String, initial: Boolean): State<Boolean> {
        val state = remember { mutableStateOf(initial) }
        val lifecycleOwner = LocalLifecycleOwner.current

        DisposableEffect(this, lifecycleOwner) {
            val observer = Observer<Int> {
                state.value = it and (observerIntStore[key] ?: it) != 0x0
            }
            observeThemeAnimState(lifecycleOwner, key, observer)

            // 触发depose的时候执行removeObserver
            onDispose { removeObserver(key, observer) }
        }
        return state
    }
}

/**
 * 自定义animateColorAsState
 * 在切换主题的时候会导致ColorPalette变化触发recompose
 * 同时commonViewModel.currentTheme也在变化触发recompose
 *
 * 若场景中color需要根据currentTheme从ColorPalette中动态选择，则会不停的触发动画
 * 这里对animateColorAsState做了封装，配合Theme变化使用：
 * 当执行animateColorAsState的时候会向CommonViewModel.themeAnimStarted注册一个监听,
 * 若切换theme，则该监听会收到true，当动画执行完毕的时候会自动将该状态设置为false
 * 当themeAnimStarted状态为true的时候会直接返回默认值的State
 */
@Composable
fun animateColorAsState(
    key: String,
    targetValue: Color,
    animationSpec: AnimationSpec<Color>,
    defaultColor: Color,
    finishedListener: ((Color) -> Unit)? = null
): State<Color> {
    val defaultColorState = rememberUpdatedState(newValue = defaultColor)
    val viewModel = themeViewModel
    val themeAnimating by with(viewModel) { isThemeAnimating.observeAsState(key, false) }
    val targetAnimateColor = androidx.compose.animation.animateColorAsState(
        targetValue = targetValue,
        animationSpec = animationSpec
    ) {
        viewModel.themeAnimStop(key)
        finishedListener?.invoke(it)
    }

    return if (themeAnimating) defaultColorState else targetAnimateColor
}
