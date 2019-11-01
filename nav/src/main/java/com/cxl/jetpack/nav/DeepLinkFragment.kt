package com.cxl.jetpack.nav

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

/**
 * 文件描述：DeepLink
 * 文件创建人：ChenXinLei
 * 文件创建人联系方式：502616659(QQ)
 * 创建时间：2019 年 10月 29 日
 * 文件版本：v1.0
 * 版本描述：创建文件
 */
class DeepLinkFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.deep_link_dest,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args : DeepLinkFragmentArgs by navArgs()
        view.findViewById<TextView>(R.id.dlTv).run {
            text = args.dlValue
            setOnClickListener {
                val bundle = Bundle()
                bundle.putString("dlValue","通知跳转")
                val deepLink = findNavController()
                    .createDeepLink()
                    .setArguments(bundle)
                    .setDestination(R.id.deep_link_dest)
                    .createPendingIntent()
                val notificationManager =
                    context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(NotificationChannel(
                        "Deep Link", "Deep Links", NotificationManager.IMPORTANCE_HIGH)
                    )
                }
                val builder = NotificationCompat.Builder(
                    context!!, "Deep Link")
                    .setContentTitle("通知")
                    .setContentText("跳转到Deep Link")
                    .setSmallIcon(R.drawable.ic_android)
                    .setContentIntent(deepLink)
                    .setAutoCancel(true)
                notificationManager.notify(0, builder.build())
            }
        }

    }
}