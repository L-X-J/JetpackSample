package com.cxl.jetpack.cx

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.nio.ByteBuffer
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private const val REQUEST_CODE_PERMISSIONS = 10
private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

class MainActivity : AppCompatActivity(), LifecycleOwner {
    private lateinit var viewFinder: TextureView
    private val executor = Executors.newSingleThreadExecutor()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewFinder = findViewById(R.id.view_finder)
        if (checkAllPermissionsGranted()) {
            viewFinder.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }
    }

    private fun startCamera() {
        // 为取景器用例创建配置对象
        val previewConfig = PreviewConfig.Builder().apply {
            setTargetResolution(Size(viewFinder.width, viewFinder.height))
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
        // 为图像捕获用例创建配置对象
        val imageCaptureConfig = ImageCaptureConfig.Builder().apply {
            // 我们没有为图像捕获设置分辨率；相反，我们
            //选择一个可以推断出合适的捕获模式
            // 基于宽高比和请求模式的分辨率
            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
        }.build()

        // 构建图像捕获用例并附加按钮单击侦听器
        val imageCapture = ImageCapture(imageCaptureConfig)
        findViewById<ImageButton>(R.id.capture_button).setOnClickListener {
            val file = File(externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")
            imageCapture.takePicture(file, executor, object : ImageCapture.OnImageSavedListener {
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

        //设置图像分析管道，以计算平均像素亮度
        val analyzerConfig = ImageAnalysisConfig.Builder().apply {
            // 在我们的分析中，我们更关注最新图片而不是
            //分析*每张*图像
            setImageReaderMode(
                ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE
            )
        }.build()

        // 构建图像分析用例并实例化我们的分析器
        val analyzerUseCase = ImageAnalysis(analyzerConfig).apply {
            setAnalyzer(executor, LuminosityAnalyzer())
        }

        //将用例绑定到生命周期
        // 如果Android Studio抱怨“此”不是LifecycleOwner
        // 尝试重建项目或将appcompat依赖项更新为
        // 1.1.0或更高版本。
        // CameraX.bindToLifecycle(this, preview)//只是预览
        //CameraX.bindToLifecycle(this, preview,imageCapture)//可以预览可以拍照
        CameraX.bindToLifecycle(this, preview, imageCapture, analyzerUseCase)//可以预览、拍照、分析图片亮度
    }

    private fun updateTransform() {
        val matrix = Matrix()

        //计算取景器的中心
        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        // 纠正预览输出以适应显示旋转
        val rotationDegrees = when (viewFinder.display.rotation) {
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (checkAllPermissionsGranted()) {
                viewFinder.post { startCamera() }
            } else {
                Toast.makeText(applicationContext, "您没有同意权限", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 检查所有权限
     */
    private fun checkAllPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private class LuminosityAnalyzer : ImageAnalysis.Analyzer {
        private var lastAnalyzedTimestamp = 0L

        /**
         * Helper扩展功能，用于从内存中提取字节数组
         * 图像平面缓冲区
         */
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // 将缓冲区倒回零
            val data = ByteArray(remaining())
            get(data)   // 将缓冲区复制到字节数组
            return data // 返回字节数组
        }

        override fun analyze(image: ImageProxy, rotationDegrees: Int) {
            val currentTimestamp = System.currentTimeMillis()
            // 计算平均亮度的频率不超过每秒
            if (currentTimestamp - lastAnalyzedTimestamp >=
                TimeUnit.SECONDS.toMillis(1)
            ) {
                // 由于ImageAnalysis中的格式为YUV，因此image.planes [0]
                // 包含Y（亮度）平面
                val buffer = image.planes[0].buffer
                // 从回调对象中提取图像数据
                val data = buffer.toByteArray()
                // 将数据转换为像素值数组
                val pixels = data.map { it.toInt() and 0xFF }
                //计算图像的平均亮度
                val luma = pixels.average()
                //记录新的亮度值
                Log.e("CameraXApp", "Average luminosity: $luma")
                // 更新最后分析帧的时间戳
                lastAnalyzedTimestamp = currentTimestamp
            }
        }
    }
}
