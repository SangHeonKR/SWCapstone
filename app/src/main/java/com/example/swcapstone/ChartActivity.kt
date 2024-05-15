package com.example.swcapstone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class ChartActivity : AppCompatActivity() {

    private lateinit var chart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 차트 생성
        chart = LineChart(this)

        // 차트 설정
        setupChart()

        // 액티비티에 차트 설정
        setContentView(chart)
    }

    private fun setupChart() {
        val entries = mutableListOf<Entry>()
        entries.add(Entry(1f, 10f))
        entries.add(Entry(2f, 30f))
        entries.add(Entry(3f, 30f))
        entries.add(Entry(4f, 40f))
        entries.add(Entry(5f, 50f))

        val dataSet = LineDataSet(entries, "Label")
        val lineData = LineData(dataSet)

        chart.data = lineData

        // 차트 스타일 및 기타 설정
        val description = Description()
        description.text = "Chart Description"
        chart.description = description

        chart.setDrawGridBackground(false)

        chart.animateX(1500)
        chart.invalidate()

        // 선택된 값에 대한 리스너 설정
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                // 값이 선택됐을 때의 동작 구현
            }

            override fun onNothingSelected() {
                // 아무 값도 선택되지 않았을 때의 동작 구현
            }
        })
    }
}
