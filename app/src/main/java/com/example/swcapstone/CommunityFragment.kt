package com.example.swcapstone

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class CommunityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 레이아웃 인플레이트 및 뷰 초기화
        val view = inflater.inflate(R.layout.activity_community, container, false)

        // 네이버 카페 이미지 뷰 클릭 이벤트 처리
        view.findViewById<ImageView>(R.id.naverCafeImageView).setOnClickListener {
            openUrl("https://cafe.naver.com/pyurion/243666?art=ZXh0ZXJuYWwtc2VydmljZS1uYXZlci1zZWFyY2gtY2FmZS1wcg.eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjYWZlVHlwZSI6IkNBRkVfVVJMIiwiY2FmZVVybCI6InB5dXJpb24iLCJhcnRpY2xlSWQiOjI0MzY2NiwiaXNzdWVkQXQiOjE3MTU5NzcxOTkzMDV9.TKh3JIE5FlyH5Xve3XVLVVbG1XCeg1KSJIcK7YaVtcg")
        }

        // 디스코드 이미지 뷰 클릭 이벤트 처리
        view.findViewById<ImageView>(R.id.discordImageView).setOnClickListener {
            openUrl("https://discord.gg/CWSKuxhV")
        }

        return view
    }

    // URL을 열기 위한 메서드
    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}
