package com.example.test.dxworkspace.presentation.ui.home.report.financial

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.report.ReportRequestModel
import com.example.test.dxworkspace.databinding.FragmentReportFinancialBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.model.menu.CompareModel
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardControlManufacturingViewModel
import com.example.test.dxworkspace.presentation.ui.home.report.ReportViewModel
import com.example.test.dxworkspace.presentation.ui.home.report.adapter.DetailFinancialAdapter
import com.example.test.dxworkspace.presentation.ui.timepicker.RangeTimeSelectFragment
import com.example.test.dxworkspace.presentation.utils.Marker
import com.example.test.dxworkspace.presentation.utils.VariantChartFormat
import com.example.test.dxworkspace.presentation.utils.common.*
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import javax.inject.Inject


class ReportFinancialFragment : BaseFragment<FragmentReportFinancialBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModel : ReportViewModel

    @Inject
    lateinit var configRepository: ConfigRepository

    val adapter by lazy { DetailFinancialAdapter()}
    private var mkRate: Marker? = null
    var currentFilter = "money"

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

    override fun getLayoutId(): Int {
        return R.layout.fragment_report_financial
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory){
            observe(failure){
                showToast(EventToast(R.string.error_notification))
                binding?.pullToRefresh?.isRefreshing = false
            }
            observe(isSuccess){
                binding?.pullToRefresh?.isRefreshing = false
                setDataChart()
                setDataRcv()
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
            rcvSale.adapter = adapter

        }
        setupChart()
        getReportData()
//        setupChart()
//        setData()
    }

    fun setupChart(){
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
        leftAxisVariant.valueFormatter = vcFormat
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

    fun setDataChart(){
        val chart = binding!!.chart
        val dataNow = viewModel.financialData.value!!
        val dataPre = viewModel.financialDataCompare.value!!
        if((dataNow.revenue ?: 0.0) < 0 ||
            (dataNow.expense ?: 0.0) < 0 ||
            (dataNow.profit ?: 0.0) < 0 ||
            (dataPre.profit ?: 0.0) < 0 ||
            (dataPre.expense ?: 0.0) < 0 ||
            (dataPre.revenue ?: 0.0) < 0 )
        {
            binding?.chart?.axisLeft?.resetAxisMinimum()

        }
        val barEntriesRevenue = mutableListOf<BarEntry>()
        barEntriesRevenue.add(BarEntry(1f,(dataPre.revenue ?: 0L).toFloat()))
        barEntriesRevenue.add(BarEntry(2f,(dataNow.revenue ?: 0L).toFloat()))

        val barEntriesExpense = mutableListOf<BarEntry>()
        barEntriesExpense.add(BarEntry(1f,(dataPre.expense ?: 0L).toFloat()))
        barEntriesExpense.add(BarEntry(2f,(dataNow.expense ?: 0L).toFloat()))

        val barEntriesProfit = mutableListOf<BarEntry>()
        barEntriesProfit.add(BarEntry(1f,(dataPre.profit ?: 0L).toFloat()))
        barEntriesProfit.add(BarEntry(2f,(dataNow.profit ?: 0L).toFloat()))
//
//
        val listStringXAxis = when(homeViewModel.typeTimeReport){
            Constants.DatePicker.QUICK_THIS_MONTH -> listOf<String>("Kỳ trước","Kỳ này")
            else -> listOf<String>("Kỳ trước","Kỳ này")
        }

//        val barEntriesRevenue = mutableListOf<BarEntry>()
//        barEntriesRevenue.add(BarEntry(1f,10000000.toFloat()))
//        barEntriesRevenue.add(BarEntry(2f,2000000.toFloat()))
//
//        val barEntriesExpense = mutableListOf<BarEntry>()
//        barEntriesExpense.add(BarEntry(1f,5000000.toFloat()))
//        barEntriesExpense.add(BarEntry(2f,1200000.toFloat()))
//
//        val barEntriesProfit = mutableListOf<BarEntry>()
//        barEntriesProfit.add(BarEntry(1f,3400000.toFloat()))
//        barEntriesProfit.add(BarEntry(2f, (-1200000).toFloat()))

        val barDataSetRevenue = BarDataSet(barEntriesRevenue,"Doanh thu")
        val barDataSetExpense = BarDataSet(barEntriesExpense,"Chi phí")
        val barDataSetProfit = BarDataSet(barEntriesProfit,"Lợi nhuận")
        barDataSetExpense.setColors(Color.RED)
        barDataSetRevenue.setColors(Color.YELLOW)
        barDataSetProfit.setColors(Color.GREEN)
        val data = BarData(barDataSetRevenue,barDataSetExpense,barDataSetProfit)
        chart.axisLeft.labelCount = 5
        data.barWidth = 0.25f
        data.setDrawValues(false)
        mkRate = object: Marker(context){
            override fun refreshContent(
                e: Entry,
                highlight: Highlight,
            ) {
                val i = e.x.toDouble()
                println("XAXISSSSSSSSSSSS : ${chart.xAxis.valueFormatter.getFormattedValue(e.x,chart.xAxis)}")
                val name = if((0.2<=i && i < 0.45) || (1.15<=i && i<1.45)) "Doanh thu"
                else if((0.45<=i && i < 0.75) || (1.45<=i && i<1.75)) "Chi phí"
                else if((0.75<=i && i < 1.0) || (1.75<=i && i<2.0)) "Lợi nhuận"
                else ""

                if (currentFilter == "money") {
                    val value = e.y.toDouble()

                    val money = name + "\n" + formatMoney(
                        value,
                        isAcceptMinus = true,
                        isAcceptZero = true
                    )
                    textView.text = money
                } else if(currentFilter == "percent") {
                    val orderCount = e.y.toString()+"%"
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
        chart.xAxis.valueFormatter =  IndexAxisValueFormatter(listStringXAxis)
        chart.groupBars(0f,0.1f,0.05f)
        chart.highlightValue(null)
        chart.animateXY(1000, 1000)
        chart.invalidate()

    }

    fun getReportData(){
        binding?.pullToRefresh?.isRefreshing = true
        viewModel.getFinancialReport(ReportRequestModel(homeViewModel.fromDate,homeViewModel.toDate,homeViewModel.fromDateCompare,homeViewModel.toDateCompare))
    }

    fun setDataRcv(){
        val dataNow = viewModel.financialData.value!!
        val dataPre = viewModel.financialDataCompare.value!!
        val listItem = mutableListOf<CompareModel>()
        listItem.add(CompareModel((dataNow.revenue ?: 0.0),dataPre.revenue ?: 0.0,"money","Doanh thu",R.drawable.ic_money))
        listItem.add(CompareModel((dataNow.expense ?: 0.0),dataPre.expense ?: 0.0,"money","Chi phí",R.drawable.ic_money))
        listItem.add(CompareModel((dataNow.numberOrder ?: 0L).toDouble(),(dataPre.numberOrder ?: 0L).toDouble(),"value","Tổng đơn hàng",R.drawable.ic_money))
        listItem.add(CompareModel((dataNow.repair ?: 0.0),dataPre.repair ?: 0.0,"money","Tiền bảo trì",R.drawable.ic_money))
        listItem.add(CompareModel((dataNow.cancelOrderMoney ?: 0.0),dataPre.cancelOrderMoney ?: 0.0,"money","Tiền hủy đơn",R.drawable.ic_money))
        listItem.add(CompareModel((dataNow.profit ?: 0.0),dataPre.profit ?: 0.0,"money","Lợi nhuận",R.drawable.ic_money))
        adapter.items = listItem
        binding?.tvNote?.text = getString(R.string.note_dashboard,"${homeViewModel.fromDate}" ,"${homeViewModel.toDate}" ,"${homeViewModel.fromDateCompare}",
            homeViewModel.toDateCompare
        )
    }

// set up linechart
//    fun setupChart(){
//        val chart = binding!!.chart
//        chart.setDrawGridBackground(false)
//        chart.description.isEnabled = false
//        chart.setDrawBorders(false)
//
//        chart.axisLeft.isEnabled = false
//        chart.axisRight.setDrawAxisLine(false)
//        chart.axisRight.setDrawGridLines(false)
//        chart.xAxis.setDrawAxisLine(false)
//        chart.xAxis.setDrawGridLines(false)
//
//        // enable touch gestures
//
//        // enable touch gestures
//        chart.setTouchEnabled(true)
//
//        // enable scaling and dragging
//
//        // enable scaling and dragging
//        chart.isDragEnabled = true
//        chart.setScaleEnabled(true)
//
//        // if disabled, scaling can be done on x- and y-axis separately
//
//        // if disabled, scaling can be done on x- and y-axis separately
//        chart.setPinchZoom(false)
//
//
//        val l = chart.legend
//        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
//        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
//        l.orientation = Legend.LegendOrientation.VERTICAL
//        l.setDrawInside(false)
//    }
//
//    fun setData(){
//        val dataSets: ArrayList<ILineDataSet> = ArrayList()
//
//        for (z in 0..2) {
//            val values = mutableListOf<Entry>()
//            for (i in 0 until 10) {
//                val `val`: Double = Math.random() * 50 -40
//                values.add(Entry(i.toFloat(), `val`.toFloat()))
//            }
//            val d = LineDataSet(values, "DataSet " + (z + 1))
//            d.lineWidth = 2.5f
//            d.circleRadius = 4f
//            val color = colors[z % colors.size]
//            d.color = color
//            d.setCircleColor(color)
//            dataSets.add(d)
//        }
//
//        // make the first DataSet dashed
//
//        // make the first DataSet dashed
//        (dataSets[0] as LineDataSet).enableDashedLine(10f, 10f, 0f)
//        (dataSets[0] as LineDataSet).setColors(*ColorTemplate.VORDIPLOM_COLORS)
//        (dataSets[0] as LineDataSet).setCircleColors(*ColorTemplate.VORDIPLOM_COLORS)
//
//        val data = LineData(dataSets)
//        binding!!.chart.setData(data)
//        binding!!.chart.invalidate()
//    }


}