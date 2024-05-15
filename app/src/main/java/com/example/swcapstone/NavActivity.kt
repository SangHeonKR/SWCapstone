package com.example.swcapstone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.FrameLayout

class NavActivity : AppCompatActivity() {

    private lateinit var fragmentControl: FrameLayout
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // 뷰 바인딩
        fragmentControl = findViewById(R.id.fragment_control)
        bottomNav = findViewById(R.id.nav)

        // BottomNavigationView에 메뉴 리소스 로드
        bottomNav.inflateMenu(R.menu.bottom_nav_menu)

        val inflater = LayoutInflater.from(this)

        // 초기 레이아웃 설정
        inflater.inflate(R.layout.activity_main, fragmentControl, true)

        bottomNav.setOnNavigationItemSelectedListener { item ->
            fragmentControl.removeAllViews() // 기존 뷰 삭제
            when (item.itemId) {
                R.id.navigation_search -> inflater.inflate(R.layout.activity_main, fragmentControl, true)
                R.id.navigation_community -> inflater.inflate(R.layout.activity_main, fragmentControl, true)
                R.id.navigation_myhome -> inflater.inflate(R.layout.activity_mypage, fragmentControl, true)
                R.id.navigation_record -> inflater.inflate(R.layout.activity_record, fragmentControl, true)
            }
            true // 선택된 아이템을 하이라이트
        }
    }
}
