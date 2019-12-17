# CameraX

## 前言
这篇文章只是介绍CameraX的简单使用，其中包含相机预览、拍照、和简单的图片侦分析。

参考自官方文档：https://developer.android.google.cn/training/camerax?hl=zh_CN

本文章Demo代码：https://github.com/L-X-J/JetpackSample/tree/master/cx
## 概览(摘自官方文档)
[![点击跳转到外网](./cxp.png)](https://youtu.be/QYkTXJ2TuiA)
CameraX 是一个 Jetpack 支持库，旨在帮助您简化相机应用的开发工作。它提供一致且易于使用的 API 界面，适用于大多数 Android 设备，并可向后兼容至 Android 5.0（API 级别 21）。

虽然它利用的是 camera2 的功能，但使用的是更为简单且基于用例的方法，该方法具有生命周期感知能力。它还解决了设备兼容性问题，因此您无需在代码库中包含设备专属代码。这些功能减少了将相机功能添加到应用时需要编写的代码量。

最后，借助 CameraX，开发者只需两行代码就能利用与预安装的相机应用相同的相机体验和功能。 [CameraX Extensions](https://developer.android.google.cn/training/camerax/vendor-extensions?hl=zh_CN) 是可选插件，通过该插件，您可以在支持的设备上向自己的应用中添加人像、HDR、夜间模式和美颜等效果。

## 引入依赖
```gradle
    implementation 'androidx.camera:camera-core:1.0.0-alpha06'
    implementation 'androidx.camera:camera-camera2:1.0.0-alpha06'
```
## 实现摄像头预览
### 画布局
`xml`布局文件新增一个`TextureView`且取个id
```xml
    <TextureView
        android:id="@+id/view_finder"
        ...
        android:layout_width="380dp"
        android:layout_height="380dp"/>
```
### 逻辑代码实现
在activity中新增两个方法。
```kotlin
private fun startCamera(){}
private fun updateTransform(){}
```
因为要实现摄像头预览我们需要动态获取权限(`Manifest.permission.CAMERA`)。
获取到权限后们需要调用`startCamera()`函数。如下代码
```kotlin
class MainActivity : AppCompatActivity() ,LifecycleOwner {
    private lateinit var viewFinder : TextureView
    private val executor = Executors.newSingleThreadExecutor()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewFinder = findViewById(R.id.view_finder)
        //自定义函数检查权限是否拥有
        if (checkAllPermissionsGranted()){
            viewFinder.post { startCamera() }
        }else{
            //如果没有权限动态获取
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }
    }
}
```
最后我们实现`startCamera()`、`updateTransform()`函数。
```kotlin
private fun startCamera(){
// 为取景器用例创建配置对象
        val previewConfig = PreviewConfig.Builder().apply {
            setTargetResolution(Size(viewFinder.width,viewFinder.height))
        }.build()

        // 构建取景器用例
        val preview = Preview(previewConfig);

        // 每次取景器更新时，重新计算布局
        preview.setOnPreviewOutputUpdateListener {
            // 要更新SurfaceTexture，我们必须将其删除并重新添加
            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)
            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform()
        }
        //将用例绑定到生命周期
        CameraX.bindToLifecycle(this, preview)
}
 private fun updateTransform() {
        val matrix = Matrix()

        //计算取景器的中心
        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        // 纠正预览输出以适应显示旋转
        val rotationDegrees = when(viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        // 最后，将转换应用于我们的TextureView
        viewFinder.setTransform(matrix)
    }
```
这样我们就实现了摄像头预览，是不是比之前简单很多。

## 拍照
实现拍照功能，我们需要改造一下`startCamera()`函数，同样还是很少的代码。
```kotlin
private fun startCamera(){
...
//在 CameraX.bindToLifecycle(this, preview)前添加以下代码
 // 为图像捕获用例创建配置对象
        val imageCaptureConfig = ImageCaptureConfig.Builder().apply {
            // 我们没有为图像捕获设置分辨率；相反，我们
            //选择一个可以推断出合适的捕获模式
            // 基于宽高比和请求模式的分辨率
                setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
        }.build()

        // 构建图像捕获用例并附加按钮单击侦听器
        val imageCapture = ImageCapture(imageCaptureConfig)
        //拍照按钮
findViewById<ImageButton>(R.id.capture_button).setOnClickListener {
            val file = File(externalMediaDirs.first(),"${System.currentTimeMillis()}.jpg")
            imageCapture.takePicture(file,executor,object : ImageCapture.OnImageSavedListener{
                override fun onImageSaved(file: File) {
                    val msg = "成功: ${file.absolutePath}"
                    Log.d("CameraXApp", msg)
                    viewFinder.post {
                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(
                    imageCaptureError: ImageCapture.ImageCaptureError,
                    message: String,
                    cause: Throwable?
                ) {
                    val msg = "失败: $message"
                    Log.e("CameraXApp", msg, cause)
                    viewFinder.post {
                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    }
                 }

            })
        }
        最后绑定一下生命周期
        
       // CameraX.bindToLifecycle(this, preview)//只是预览
        CameraX.bindToLifecycle(this, preview,imageCapture)//可以预览可以拍照
}
```