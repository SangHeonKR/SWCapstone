package com.example.swcapstone

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class CommunityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // URL을 설정하고 Intent를 통해 브라우저를 엽니다.
        val url = "https://m.cafe.naver.com/cantsb"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)

        // 레이아웃을 반환하지 않고 null을 반환합니다.
        return null
    }
}
