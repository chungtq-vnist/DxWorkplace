package com.example.test.dxworkspace.presentation.ui.home.report.warehouse

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.report.ReportRequestModel
import com.example.test.dxworkspace.data.entity.report.WarehouseReportModel
import com.example.test.dxworkspace.databinding.FragmentReportFinancialBinding
import com.example.test.dxworkspace.databinding.FragmentReportInventoryTurnoverBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.model.menu.CompareModel
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
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
import com.example.test.dxworkspace.presentation.utils.getRangeTime
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import java.util.*

import javax.inject.Inject


class ReportInventoryTurnoverFragment : BaseFragment<FragmentReportInventoryTurnoverBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModel: ReportViewModel

    @Inject
    lateinit var configRepository: ConfigRepository
    var dataChart = listOf<WarehouseReportModel>()
    var revenue = 0.0
    var revenuePre = 0.0
    var inventory = 0.0
    var inventoryPre = 0.0

    val adapter by lazy { DetailFinancialAdapter() }
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
                getReportData()
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_report_inventory_turnover
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory) {
            observe(isSuccess) {
                revenue = viewModel.financialData.value?.revenue ?: 0.0
                revenuePre = viewModel.financialDataCompare.value?.revenue ?: 0.0
                if (homeViewModel.statusReport.value == "DONE") {
                    setDataChart()
                }
            }
            observe(statusReport){
                println("statusReportttttttttttttttttttttttt")

//                if (it == "DONE") {
                    val t = viewModel.listDataWarehouseReport.value ?: listOf()
                    inventoryPre =
                        (t[0].productBeginningValue + t[0].materialBeginningValue + t[0].wasteBeginningValue + t[0].equipmentBeginningValue +
                                t[0].productEndingValue + t[0].materialEndingValue + t[0].wasteEndingValue + t[0].equipmentEndingValue) / (2.0)
                    inventory =
                        (t[1].productBeginningValue + t[1].materialBeginningValue + t[1].wasteBeginningValue + t[1].equipmentBeginningValue +
                                t[1].productEndingValue + t[1].materialEndingValue + t[1].wasteEndingValue + t[1].equipmentEndingValue) / (2.0)
                println("inventory ${inventory} -- ${inventoryPre}")
                    if (viewModel.isSuccess.value == true) {
                        setDataChart()
                    }
//                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {

//            rcvSale.adapter = adapter

        }
        setupChart()
        getReportData()
//        setupChart()
//        setData()
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

    fun setDataChart() {
        val chart = binding!!.chart
        chart.setNoDataText("Không có dữ liệu thống kê!")
        val a = homeViewModel.fromDate
        val b = homeViewModel.toDate
        val t = (365.0) / getRangeTime(a, b)
        if (inventory == 0.0 || inventoryPre == 0.0) {
            chart.data = null
            chart.animateXY(1000, 1000)
            chart.invalidate()
            return
        }
        val dataNow = String.format(Locale.ENGLISH,"%.2f", ((revenue * t) / inventory)).toDouble()
        val dataPre = String.format(Locale.ENGLISH,"%.2f", (revenuePre * t) / inventoryPre).toDouble()
//        if((dataNow.revenue ?: 0.0) < 0 ||
//            (dataNow.expense ?: 0.0) < 0 ||
//            (dataNow.profit ?: 0.0) < 0 ||
//            (dataPre.profit ?: 0.0) < 0 ||
//            (dataPre.expense ?: 0.0) < 0 ||
//            (dataPre.revenue ?: 0.0) < 0 )
//        {
//            binding?.chart?.axisLeft?.resetAxisMinimum()
//
//        }

        val barEntriesRevenue = mutableListOf<BarEntry>()
        barEntriesRevenue.add(BarEntry(1f, (dataPre).toFloat()))
        barEntriesRevenue.add(BarEntry(2f, (dataNow).toFloat()))

//        val barEntriesExpense = mutableListOf<BarEntry>()
//        barEntriesExpense.add(BarEntry(1f,(dataPre.expense ?: 0L).toFloat()))
//        barEntriesExpense.add(BarEntry(2f,(dataNow.expense ?: 0L).toFloat()))
//
//        val barEntriesProfit = mutableListOf<BarEntry>()
//        barEntriesProfit.add(BarEntry(1f,(dataPre.profit ?: 0L).toFloat()))
//        barEntriesProfit.add(BarEntry(2f,(dataNow.profit ?: 0L).toFloat()))
//
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
//
//        val barEntriesProfit = mutableListOf<BarEntry>()
//        barEntriesProfit.add(BarEntry(1f,3400000.toFloat()))
//        barEntriesProfit.add(BarEntry(2f, (-1200000).toFloat()))

        val barDataSetRevenue = BarDataSet(barEntriesRevenue, "Số vòng quay hàng tồn kho")
//        val barDataSetExpense = BarDataSet(barEntriesExpense,"Chi phí")
//        val barDataSetProfit = BarDataSet(barEntriesProfit,"Lợi nhuận")
//        barDataSetExpense.setColors(Color.RED)
        barDataSetRevenue.setColors(Color.YELLOW)
//        barDataSetProfit.setColors(Color.GREEN)
        val data = BarData(barDataSetRevenue)
        chart.axisLeft.labelCount = 5
        data.barWidth = 0.25f
        data.setDrawValues(false)
        mkRate = object : Marker(context) {
            override fun refreshContent(
                e: Entry,
                highlight: Highlight,
            ) {
//                val i = e.x.toDouble()
//                println("XAXISSSSSSSSSSSS : ${chart.xAxis.valueFormatter.getFormattedValue(e.x,chart.xAxis)}")
//                val name = if((0.2<=i && i < 0.45) || (1.15<=i && i<1.45)) "Doanh thu"
//                else if((0.45<=i && i < 0.75) || (1.45<=i && i<1.75)) "Chi phí"
//                else if((0.75<=i && i < 1.0) || (1.75<=i && i<2.0)) "Lợi nhuận"
//                else ""
//
//                if (currentFilter == "money") {
//                    val value = e.y.toDouble()
//
//                    val money = name + "\n" + formatMoney(
//                        value,
//                        isAcceptMinus = true,
//                        isAcceptZero = true
//                    )
//                    textView.text = money
//                } else if(currentFilter == "percent") {
//                    val orderCount = e.y.toString()+"%"
//                    textView.text = orderCount
//                } else {
                val orderCount = e.y.toString()
                textView.text = orderCount
//                }

                super.refreshContent(e, highlight)
            }
        }
        chart.marker = mkRate
        chart.data = data
        chart.xAxis.valueFormatter = IndexAxisValueFormatter(listStringXAxis)
        chart.groupBars(0f, 0.1f, 0.05f)
        chart.highlightValue(null)
        chart.animateXY(1000, 1000)
        chart.invalidate()

    }

    fun getReportData() {
        viewModel.getFinancialReport(
            ReportRequestModel(
                homeViewModel.fromDate,
                homeViewModel.toDate,
                homeViewModel.fromDateCompare,
                homeViewModel.toDateCompare
            )
        )
        viewModel.getListDataReportWarehouse(
            ReportRequestModel(
                homeViewModel.fromDate,
                homeViewModel.toDate,
                if (homeViewModel.isCompare) homeViewModel.fromDateCompare else null,
                if (homeViewModel.isCompare) homeViewModel.toDateCompare else null,
            )
        )
    }

    fun setDataRcv() {
        val dataNow = viewModel.financialData.value!!
        val dataPre = viewModel.financialDataCompare.value!!
        val listItem = mutableListOf<CompareModel>()
        listItem.add(
            CompareModel(
                (dataNow.revenue ?: 0.0),
                dataPre.revenue ?: 0.0,
                "money",
                "Doanh thu",
                R.drawable.ic_money
            )
        )
        listItem.add(
            CompareModel(
                (dataNow.expense ?: 0.0),
                dataPre.expense ?: 0.0,
                "money",
                "Chi phí",
                R.drawable.ic_money
            )
        )
        listItem.add(
            CompareModel(
                (dataNow.numberOrder ?: 0L).toDouble(),
                (dataPre.numberOrder ?: 0L).toDouble(),
                "value",
                "Tổng đơn hàng",
                R.drawable.ic_money
            )
        )
        listItem.add(
            CompareModel(
                (dataNow.repair ?: 0.0),
                dataPre.repair ?: 0.0,
                "money",
                "Tiền bảo trì",
                R.drawable.ic_money
            )
        )
        listItem.add(
            CompareModel(
                (dataNow.cancelOrderMoney ?: 0.0),
                dataPre.cancelOrderMoney ?: 0.0,
                "money",
                "Tiền hủy đơn",
                R.drawable.ic_money
            )
        )
        listItem.add(
            CompareModel(
                (dataNow.profit ?: 0.0),
                dataPre.profit ?: 0.0,
                "money",
                "Lợi nhuận",
                R.drawable.ic_money
            )
        )
        adapter.items = listItem
        binding?.tvNote?.text = getString(
            R.string.note_dashboard,
            "${homeViewModel.fromDate}",
            "${homeViewModel.toDate}",
            "${homeViewModel.fromDateCompare}",
            homeViewModel.toDateCompare
        )
    }


}