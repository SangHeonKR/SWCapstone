package com.example.swcapstone

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class MyHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_mypage, container, false)

        // 'imageViewKcal' 클릭 시 NumberSettingActivity 시작
        view.findViewById<ImageView>(R.id.imageViewKcal).setOnClickListener {
            openNumberSettingActivity()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        updateCaloriesDisplay() // 화면이 다시 보일 때마다 칼로리 정보 업데이트
    }

    private fun openNumberSettingActivity() {
        val intent = android.content.Intent(activity, NumberSettingActivity::class.java)
        startActivity(intent)
    }

    private fun updateCaloriesDisplay() {
        val prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val lastNumber = prefs.getInt("lastNumber", 0)  // 최신 저장된 섭취 칼로리 값 불러오기
        val goalCalories = prefs.getInt("goalCalories", 2000)  // 최신 저장된 목표 칼로리 값 불러오기
        val recentMealInfo = view?.findViewById<TextView>(R.id.recentMealInfo)
        recentMealInfo?.text = "오늘의 섭취 칼로리: $lastNumber / $goalCalories"
    }
}
