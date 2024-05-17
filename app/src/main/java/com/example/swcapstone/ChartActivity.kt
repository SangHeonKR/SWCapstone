package com.example.swcapstone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate

class ChartActivity : AppCompatActivity() {

    private lateinit var chart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        // XML에서 정의한 차트를 찾습니다
        chart = findViewById(R.id.chart)

        // 차트 설정
        setupChart()
    }

    private fun setupChart() {
        val entries = mutableListOf<Entry>()
        entries.add(Entry(1f, 40f))
        entries.add(Entry(2f, 15f))
        entries.add(Entry(3f, 30f))
        entries.add(Entry(4f, 10f))
        entries.add(Entry(5f, 5f))

        val dataSet = LineDataSet(entries, "전세계 인종 비율")
        dataSet.colors = ColorTemplate.JOYFUL_COLORS.toList() // 다양한 색상 사용
        dataSet.valueTextSize = 12f // 값의 텍스트 크기 설정

        val lineData = LineData(dataSet)

        if (entries.isNotEmpty()) {
            chart.data = lineData
            chart.description.isEnabled = false // 설명 비활성화
        } else {
            val description = Description()
            description.text = "데이터가 없습니다"
            chart.description = description
        }

        chart.setDrawGridBackground(false)
        chart.animateX(1500) // X축 방향으로 애니메이션 적용
        chart.invalidate() // 차트 갱신

        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                // 선택된 값에 대한 처리
            }

            override fun onNothingSelected() {
                // 선택 해제에 대한 처리
            }
        })
    }
}
