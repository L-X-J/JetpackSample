package com.cxl.jetpack.nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs

/**
 * 文件描述：TwoFragment
 * 文件创建人：ChenXinLei
 * 文件创建人联系方式：502616659(QQ)
 * 创建时间：2019 年 10月 28 日
 * 文件版本：v1.0
 * 版本描述：创建文件
 */
class TwoFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.flow_step_two_dest,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg : TwoFragmentArgs by navArgs()
        view.findViewById<TextView>(R.id.twoTxt).text=arg.text
        view.findViewById<Button>(R.id.flow).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_tow_dest_to_home_dest))
    }
}