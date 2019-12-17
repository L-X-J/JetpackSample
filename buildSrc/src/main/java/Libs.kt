/**
 * 文件描述：依赖包
 * 文件创建人：ChenXinLei
 * 文件创建人联系方式：502616659(QQ)
 * 创建时间：2019 年 12月 10 日
 * 文件版本：v1.0
 * 版本描述：创建文件
 */
class KotlinLibs {
    val std = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.Kotlin.kotlinVersion}"
    val coroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Kotlin.kotlinCoroutinesVersion}"
    val ankoCommons = "org.jetbrains.anko:anko-commons:${Versions.Kotlin.kotlinVersion}"
    val ankoSQLite = "org.jetbrains.anko:anko-sqlite:${Versions.Kotlin.kotlinVersion}"
}

class AndroidX {
    val appCompat = "androidx.appcompat:appcompat:${Versions.Androidx.appcompatVersion}"
    val corektx = "androidx.core:core-ktx:${Versions.Androidx.coreKtxVersion}"
    val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.Androidx.constraintLayoutVersion}"
    val cardView = "androidx.cardview:cardview:${Versions.Androidx.cardViewVersion}"
    val material = "com.google.android.material:material:${Versions.Androidx.materialVersion}"
    val recyclerView = "androidx.recyclerview:recyclerview:${Versions.Androidx.recyclerViewVersion}"
    val multiDex = "androidx.multidex:multidex:${Versions.Androidx.multiDexVersion}"
    val coordinatorLayout =
        "androidx.coordinatorlayout:coordinatorlayout:${Versions.Androidx.coordinatorLayoutVersion}"
}

/**
 * 使用说明和依赖详见：https://developer.android.com/jetpack/androidx/releases/lifecycle#declaring_dependencies
 */
class Lifecycle {
    //viewModel 和 LiveData 合体版
    val viewModelAndLiveData =
        "androidx.lifecycle:lifecycle-extensions:${Versions.Androidx.lifecycleVersion}"
    //只包含ViewModel
    val viewModel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.Androidx.lifecycleVersion}"
    //只包含LiveData
    val liveData = "androidx.lifecycle:lifecycle-livedata:${Versions.Androidx.lifecycleVersion}"
    //只包含lifecycle不包含viewModel和LiveData
    val lifecycleRuntime =
        "androidx.lifecycle:lifecycle-runtime:${Versions.Androidx.lifecycleVersion}"
}

/**
 * 使用说明和依赖引用详见：https://developer.android.google.cn/jetpack/androidx/releases/room?hl=en
 */
class Room {
    //基础包
    val roomRuntime = "androidx.room:room-runtime:${Versions.Androidx.roomVersion}"
    //对于Kotlin，请使用kapt代替注解处理器
    val roomCompiler = "androidx.room:room-compiler:${Versions.Androidx.roomVersion}"
    //可选-Kotlin扩展和协程对Room的支持
    val roomKtx = "androidx.room:room-ktx:${Versions.Androidx.roomVersion}"
    //可选-RxJava对Room的支持
    val roomRxJava2 = "androidx.room:room-rxjava2:${Versions.Androidx.roomVersion}"
    //可选-guava对Room的支持，包括Optional和ListenableFuture
    val roomGuava = "androidx.room:room-guava:${Versions.Androidx.roomVersion}"
    //测试助手
    val roomTesting = "androidx.room:room-testing:${Versions.Androidx.roomVersion}"
}

/**
 *FlycoDialog_Master(Dialog):https://github.com/H07000223/FlycoDialog_Master
 */
class FlycoDialog {
    val flycoDialog = "com.flyco.dialog:FlycoDialog_Lib:1.2.2@aar"
    val flycoDialogAnimation = "com.flyco.animation:FlycoAnimation_Lib:1.0.0@aar"
    val nineoldAndroids = "com.nineoldandroids:library:2.4.0"
}

/**
 * Material Dialogs: https://github.com/afollestad/material-dialogs
 */
class MaterialDialog {
    val materialDialogsCore =
        "com.afollestad.material-dialogs:core:${Versions.materialDialogsVersion}"
    val materialDialogsInput =
        "com.afollestad.material-dialogs:input:${Versions.materialDialogsVersion}"
    val materialDialogsFiles =
        "com.afollestad.material-dialogs:files:${Versions.materialDialogsVersion}"
    val materialDialogsColor =
        "com.afollestad.material-dialogs:color:${Versions.materialDialogsVersion}"
    val materialDialogsDateTime =
        "com.afollestad.material-dialogs:datetime:${Versions.materialDialogsVersion}"
    val materialDialogsBottomSheets =
        "com.afollestad.material-dialogs:bottomsheets:${Versions.materialDialogsVersion}"
    val materialDialogsLifecycle =
        "com.afollestad.material-dialogs:lifecycle:${Versions.materialDialogsVersion}"
}

/**
 * 引入示例
 * <code>
 *     dependencies {
 *     ...
 *      testImplementation junit
 *      androidTestImplementation androidTestJunit
 *      androidTestImplementation androidTestEspresso
 *     }
 * </code>
 */

class Junit {
    val junit = "junit:junit:${Versions.Test.junitVersion}"
    val androidTestJunit = "androidx.test.ext:junit:${Versions.Test.androidTestJunitVersion}"
    val androidTestEspresso =
        "androidx.test.espresso:espresso-core:${Versions.Test.androidTestEspresso}"
}

class Http {
    val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okhttpVersion}"
}

class Glide {
    val glide = "com.github.bumptech.glide:glide:${Versions.Glide.glideVersion}"
    //需要使用注解引入kapt
    val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.Glide.glideCompilerVersion}"
}

class Others {
    //https://github.com/CymChad/BaseRecyclerViewAdapterHelper
    val brvh = "com.github.CymChad:BaseRecyclerViewAdapterHelper:${Versions.brvhVersion}"
    val gson = "com.google.code.gson:gson:${Versions.gsonVersion}"
    //https://github.com/greenrobot/EventBus
    val eventBus = "org.greenrobot:eventbus:3.1.1"
    //https://github.com/JessYanCoding/AndroidAutoSize
    val autoSize = "me.jessyan:autosize:1.1.2"
    //轻量级对象存储框架https://github.com/orhanobut/hawk
    val hawk = "com.orhanobut:hawk:2.0.1"
    //https://github.com/amitshekhariitbhu/Android-Debug-Database
    val deBugDB = "com.amitshekhar.android:debug-db:1.0.6"
    val glideOkHttp = "com.github.bumptech.glide:okhttp3-integration:1.4.0@aar"
    //https://github.com/cshzhang/largeimageview
    val largeImageView = "com.shizhefei:LargeImageView:1.1.0"
    //https://github.com/Clans/FloatingActionButton
    val fab = "com.github.clans:fab:1.6.4"
}

/**
 * 使用说明：https://developer.android.com/guide/navigation
 */
class Navigation {
    val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigationVersion}"
    val uiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigationVersion}"
}

//使用说明：https://developer.android.google.cn/training/camerax?hl=zh_CN
class CameraX {
    val core = "androidx.camera:camera-core:${Versions.cameraXVersion}"
    val camera2 = "androidx.camera:camera-camera2:${Versions.cameraXVersion}"
}