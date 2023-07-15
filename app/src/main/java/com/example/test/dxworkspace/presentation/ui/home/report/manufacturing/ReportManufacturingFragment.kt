package com.example.test.dxworkspace.presentation.ui.home.report.manufacturing

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.QualityGoodsCompare
import com.example.test.dxworkspace.data.entity.report.ReportRequestModel
import com.example.test.dxworkspace.databinding.FragmentReportManufacturingBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.model.menu.CompareModel
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.report.ReportViewModel
import com.example.test.dxworkspace.presentation.ui.home.report.adapter.DetailFinancialAdapter
import com.example.test.dxworkspace.presentation.ui.timepicker.RangeTimeSelectFragment
import com.example.test.dxworkspace.presentation.utils.Marker
import com.example.test.dxworkspace.presentation.utils.MarkerHorizontal
import com.example.test.dxworkspace.presentation.utils.VariantChartFormat
import com.example.test.dxworkspace.presentation.utils.common.Constants
import com.example.test.dxworkspace.presentation.utils.common.formatMoney
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.common.roundPercent
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import javax.inject.Inject

class ReportManufacturingFragment : BaseFragment<FragmentReportManufacturingBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_report_manufacturing
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModel: ReportViewModel

    @Inject
    lateinit var configRepository: ConfigRepository
    var listChart = mutableListOf<QualityGoodsCompare>()
    val adapter by lazy { DetailFinancialAdapter() }
    private var mkRate: Marker? = null
    var currentFilter = "percent"

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    fun onBusEvent(event: EventUpdate) {
        when (event.type) {
            EventUpdate.UPDATE_DASHBOARD_MANUFACTURING -> {
                binding?.tvRangeTime?.text = homeViewModel.fromDate + " - " + homeViewModel.toDate
                getReportData()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory) {
            observe(failure) {
                showToast(EventToast(R.string.error_notification))
                binding?.pullToRefresh?.isRefreshing = false
            }
            observe(isSuccess) {
                binding?.pullToRefresh?.isRefreshing = false
                setDataChart()
//                setDataRcv()
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            ivBack.setOnClickListener { onBackPress() }
            pullToRefresh.setOnRefreshListener {
                getReportData()
            }
            binding?.tvRangeTime?.text = homeViewModel.fromDate + " - " + homeViewModel.toDate
            tvRangeTime.setOnClickListener {
                postNormal(EventNextHome(RangeTimeSelectFragment::class.java))
            }
//            rcvSale.adapter = adapter

        }
        setupChart()
        getReportData()
    }

    fun getReportData() {
        viewModel.getNumberPlanCompletedOnSchedule(
            ReportRequestModel(
                homeViewModel.fromDate,
                homeViewModel.toDate,
                homeViewModel.fromDateCompare,
                homeViewModel.toDateCompare
            )
        )
        viewModel.getReportGoodsQuality(
            configRepository.getCurrentRole().id,
            null,
            homeViewModel.fromDate,
            homeViewModel.toDate,
            homeViewModel.fromDateCompare,
            homeViewModel.toDateCompare
        )
    }


    fun setupChart() {
        val chart = binding!!.chart
        chart.isDragEnabled = false
        chart.description = null
        chart.setPinchZoom(false)
        chart.isDoubleTapToZoomEnabled = false
        chart.setDrawBarShadow(false)
        chart.setDrawGridBackground(false)
        chart.setDrawBorders(false)
        chart.setScaleEnabled(false)


        val xl = chart.xAxis
        xl.position = XAxis.XAxisPosition.BOTTOM
        xl.setDrawGridLines(false)
        xl.gridLineWidth = 0.5f
        xl.axisLineColor = Color.argb(150, 95, 95, 95)
        xl.gridColor = Color.argb(88, 218, 218, 218)
        xl.setDrawLimitLinesBehindData(false)
        xl.textSize = 10f
        xl.granularity = 5f
        xl.isGranularityEnabled = true
        xl.setCenterAxisLabels(true)
        xl.setAxisMinimum(0f);
        xl.setGranularity(1f);
        xl.setAxisMaximum(2f);
        xl.setLabelCount(2)
        val rightAxisVariant: YAxis = chart.axisRight
        rightAxisVariant.isEnabled = false

        val leftAxisVariant: YAxis = chart.axisLeft
        leftAxisVariant.labelCount = 6
        leftAxisVariant.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxisVariant.spaceTop = 15f
        leftAxisVariant.axisLineColor = Color.argb(150, 218, 218, 218)
        leftAxisVariant.setDrawGridLines(true)
        leftAxisVariant.gridColor = Color.argb(88, 218, 218, 218)
        leftAxisVariant.setDrawLabels(true)
        leftAxisVariant.textSize = 12f
        leftAxisVariant.axisMinimum = 0f
        val vcFormat = VariantChartFormat()
        leftAxisVariant.valueFormatter = object : IAxisValueFormatter {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                return  String.format("%.2f", value) + "%"
            }

        }
        val legend: Legend = chart.legend
        legend.isEnabled = true
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.form = Legend.LegendForm.SQUARE
        legend.formSize = 9f
        legend.textSize = 11f
        legend.isWordWrapEnabled = true
        legend.xEntrySpace = 4f
    }

    fun setDataChart() {
        val chart = binding!!.chart
        val dataPlanNow = viewModel.planData.value!!
        val dataPlanPre = viewModel.planDataCompare.value!!
        val barEntriesRevenue = mutableListOf<BarEntry>()
        barEntriesRevenue.add(
            BarEntry(
                1f,
                roundPercent(
                    dataPlanPre.numberPlanCompleted ?: 0,
                    dataPlanPre.numberPlanNeedCompleted ?: 0
                ).toFloat()
            )
        )
        barEntriesRevenue.add(
            BarEntry(
                2f,
                roundPercent(
                    dataPlanNow.numberPlanCompleted ?: 0,
                    dataPlanNow.numberPlanNeedCompleted ?: 0
                ).toFloat()
            )
        )


        val listGoodsCompare = viewModel.listGoods.value ?: listOf<QualityGoodsCompare>()
        listChart =
            listGoodsCompare.filter {
                (it.numberOfProducts ?: 0) != 0 || (it.numberOfWaste ?: 0) != 0
            }
                .sortedBy { it.qualityPercent }.toMutableList()
//        for (i in 0..10) listChart.add(t)
        var totalP = 0.0
        var totalC = 0
        for (i in 0 until listChart.size) {
            val s = listChart[i]
            totalP += ((s.numberOfProducts ?: 0) + (s.numberOfWaste ?: 0)) * (s.qualityPercent
                ?: 0.0)
            totalC += (s.numberOfProducts ?: 0) + (s.numberOfWaste ?: 0)
        }
        val percentAverage = if (totalC != 0) ((totalP / totalC) * 100.toLong()) / 100 else 0.0
        // average compare
        listChart =
            listGoodsCompare.filter {
                (it.numberOfProductsCompare ?: 0) != 0 || (it.numberOfWasteCompare ?: 0) != 0
            }
                .sortedBy { it.qualityPercentConpare }.toMutableList()
//        for (i in 0..10) listChart.add(t)
        var totalPc = 0.0
        var totalCc = 0
        for (i in 0 until listChart.size) {
            val s = listChart[i]
            totalPc += ((s.numberOfProductsCompare ?: 0) + (s.numberOfWasteCompare
                ?: 0)) * (s.qualityPercentConpare ?: 0.0)
            totalCc += (s.numberOfProductsCompare ?: 0) + (s.numberOfWasteCompare ?: 0)
        }
        val percentAverageCompare =
            if (totalCc != 0) ((totalPc / totalCc) * 100.toLong()) / 100 else 0.0

        val listItem = mutableListOf<CompareModel>()
        listItem.add(
            CompareModel(
                (percentAverage ?: 0.0),
                percentAverageCompare ?: 0.0,
                "percent",
                "Chất lượng trung bình sản phẩm",
                R.drawable.ic_money
            )
        )
        listItem.add(
            CompareModel(
                (listGoodsCompare.sumOf {
                    (it.numberOfProducts ?: 0) * (it.goods?.pricePerBaseUnit ?: 0.0)
                }),
                (listGoodsCompare.sumOf {
                    (it.numberOfProductsCompare ?: 0) * (it.goods?.pricePerBaseUnit ?: 0.0)
                }),
                "money", "Giá trị dự kiến tổng sản phẩm", R.drawable.ic_money
            )
        )
        listItem.add(
            CompareModel(
                roundPercent(
                    dataPlanNow.numberPlanCompleted ?: 0,
                    dataPlanNow.numberPlanNeedCompleted ?: 0
                ), roundPercent(
                    dataPlanPre.numberPlanCompleted ?: 0,
                    dataPlanPre.numberPlanNeedCompleted ?: 0
                ), "percent", "Tỉ lệ hoàn thành kế hoạch sản xuất đúng hạn", R.drawable.ic_money
            )
        )

        binding?.rcvSale?.adapter = adapter
        adapter.items = listItem
        binding?.tvNote?.text = getString(R.string.note_dashboard,"${homeViewModel.fromDate}" ,"${homeViewModel.toDate}" ,"${homeViewModel.fromDateCompare}",
            homeViewModel.toDateCompare
        )

        val barEntriesExpense = mutableListOf<BarEntry>()
        barEntriesExpense.add(BarEntry(1f, percentAverageCompare.toFloat()))
        barEntriesExpense.add(BarEntry(2f, percentAverage.toFloat()))

//
        val listStringXAxis = when (homeViewModel.typeTimeReport) {
            Constants.DatePicker.QUICK_THIS_MONTH -> listOf<String>("Kỳ trước", "Kỳ này")
            else -> listOf<String>("Kỳ trước", "Kỳ này")
        }

//        val barEntriesRevenue = mutableListOf<BarEntry>()
//        barEntriesRevenue.add(BarEntry(1f,10000000.toFloat()))
//        barEntriesRevenue.add(BarEntry(2f,2000000.toFloat()))
//
//        val barEntriesExpense = mutableListOf<BarEntry>()
//        barEntriesExpense.add(BarEntry(1f,5000000.toFloat()))
//        barEntriesExpense.add(BarEntry(2f,1200000.toFloat()))

        val barDataSetRevenue = BarDataSet(barEntriesRevenue, "% kế hoạch hoàn thành đúng hạn")
        val barDataSetExpense = BarDataSet(barEntriesExpense, "% chất lượng sản phẩm")
        barDataSetExpense.setColors(Color.GREEN)
        barDataSetRevenue.setColors(Color.CYAN)
        val data = BarData(barDataSetRevenue, barDataSetExpense)
        chart.axisLeft.labelCount = 4
        data.barWidth = 0.25f
        data.setDrawValues(false)
        mkRate = object: Marker(context){
            override fun refreshContent(
                e: Entry,
                highlight: Highlight,
            ) {
                val i = e.x.toDouble()
                val name = ""

                if (currentFilter == "money") {
                    val value = e.y.toDouble()

                    val money = name + "\n" + formatMoney(
                        value,
                        isAcceptMinus = true,
                        isAcceptZero = true
                    )
                    textView.text = money
                } else if(currentFilter == "percent") {
                    val orderCount = String.format("%.2f", e.y)+"%"
                    textView.text = orderCount
                } else {
                    val orderCount = name + "\n" + e.y.toString()
                    textView.text = orderCount
                }

                super.refreshContent(e, highlight)
            }
        }
        chart.marker = mkRate
        chart.data = data
        chart.xAxis.valueFormatter = IndexAxisValueFormatter(listStringXAxis)
        chart.groupBars(0f, 0.2f, 0.15f)
        chart.highlightValue(null)
        chart.animateXY(500, 500)
        chart.invalidate()

    }
}