object AndroidConfig {
    /**
     * SDK编译版本
     * compileSdkVersion 告诉 Gradle 用哪个 Android SDK 版本编译你的应用。
     * 使用任何新添加的 API 就需要使用对应 Level 的 Android SDK。
     */
    const val compileSdkVersion = 29
    /**
     *  android构建工具的版本,在SDK Manager中安装选择版本，
     *  buildToolsVersion的版本需要>=CompileSdkVersion;
     *  高版本的build-tools 可以构建低版本编译的android程序；
     */
    const val buildToolsVersion = "29.0.2"
    /**
     * 最小的SDK版本
     */
    const val minSdkVersion = 24
    /**
     * targetSdkVersion 是 Android 提供向前兼容的主要依据，
     * 在应用的 targetSdkVersion 没有更新之前系统不会应用最新的行为变化。这允许你在适应新的行为变化之前就可以使用新的 API
     */
    const val targetSdkVersion = 29
}

object Libs {
    val kotlin = KotlinLibs()
    val lifecycle = Lifecycle()
    val androidX = AndroidX()
    val room = Room()
    val flycoDialog = FlycoDialog()
    val materialDialog = MaterialDialog()
    val junit = Junit()
    val http = Http()
    val glide = Glide()
    val others = Others()
    val navigation = Navigation()
}

