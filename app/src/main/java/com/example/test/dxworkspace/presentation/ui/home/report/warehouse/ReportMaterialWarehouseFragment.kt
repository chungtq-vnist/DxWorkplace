package com.example.test.dxworkspace.presentation.ui.home.report.warehouse

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.report.ReportRequestModel
import com.example.test.dxworkspace.data.entity.report.WarehouseReportModel
import com.example.test.dxworkspace.databinding.FragmentDashboardWarehouseMaterialBinding
import com.example.test.dxworkspace.databinding.FragmentDashboardWarehouseProductBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.report.ReportViewModel
import com.example.test.dxworkspace.presentation.ui.home.report.adapter.WarehouseReportGoodAdapter
import com.example.test.dxworkspace.presentation.utils.MarkerHorizontal
import com.example.test.dxworkspace.presentation.utils.VariantChartFormat
import com.example.test.dxworkspace.presentation.utils.common.Constants
import com.example.test.dxworkspace.presentation.utils.common.formatMoney
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import javax.inject.Inject

class ReportMaterialWarehouseFragment : BaseFragment<FragmentDashboardWarehouseMaterialBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_dashboard_warehouse_material
    }


    val adapter by lazy { WarehouseReportGoodAdapter() }

    @Inject
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ReportViewModel

    var dataChart = listOf<WarehouseReportModel>()
    private var mkRate: MarkerHorizontal? = null
    var currentFilter = "money"
    var isUpdate = false

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
                getDataWarehouseReport()
            }
            EventUpdate.SYNC_DASHBOARD_INVENTORY -> {
                isUpdate = true
                getDataWarehouseReport()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory) {
//            observe(failure) {
//                showToast(EventToast(R.string.error_notification))
//                binding?.pullToRefresh?.isRefreshing = false
//            }
//            observe(isSuccess) {
//                binding?.pullToRefresh?.isRefreshing = false
//                setDataChart()
//                setDataRcv()
//            }


            observe(statusReport) {
                if (it == "DONE") {
                    if(!isUpdate) {
                        dataChart = viewModel.listDataWarehouseReport.value ?: listOf()
                        setDataChart()
                        setDataRcv()
                    } else {
                        isUpdate = false
                        val e = viewModel.listDataWarehouseReport.value ?: listOf()
                        if(!isEquals(e,dataChart)) {
                            setDataChart()
                            setDataRcv()
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            rcvDetail.adapter = adapter
        }
        setupChart()
        setDataChart()
        getDataWarehouseReport()
    }

    fun setupChart() {
        val chartReport = binding!!.chart
        chartReport.isDragEnabled = false
        chartReport.description = null
        chartReport.setPinchZoom(false)
        chartReport.isDoubleTapToZoomEnabled = false
        chartReport.setDrawBarShadow(false)
        chartReport.setDrawGridBackground(false)
        chartReport.setDrawBorders(false)
        chartReport.setScaleEnabled(false)

        val xAxisVariant: XAxis = chartReport.xAxis
        xAxisVariant.position = XAxis.XAxisPosition.BOTTOM
        xAxisVariant.setDrawGridLines(false)
        xAxisVariant.gridLineWidth = 0.5f
        xAxisVariant.axisLineColor = Color.argb(150, 95, 95, 95)
        xAxisVariant.gridColor = Color.argb(88, 218, 218, 218)
        xAxisVariant.setDrawLimitLinesBehindData(false)
        xAxisVariant.textSize = 10f
        xAxisVariant.granularity = 5f
        xAxisVariant.isGranularityEnabled = true
        xAxisVariant.setCenterAxisLabels(true)
        xAxisVariant.setAxisMinimum(0f);
        xAxisVariant.setGranularity(1f);
        xAxisVariant.setAxisMaximum(2f);


        val rightAxisVariant: YAxis = chartReport.axisRight
        rightAxisVariant.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        rightAxisVariant.spaceTop = 15f
        rightAxisVariant.axisLineColor = Color.argb(150, 95, 95, 95)
        rightAxisVariant.setDrawGridLines(true)
        rightAxisVariant.gridColor = Color.argb(88, 218, 218, 218)
        rightAxisVariant.setDrawLabels(true)
        rightAxisVariant.textSize = 12f
        rightAxisVariant.setDrawGridLines(false)
        val vcFormat = VariantChartFormat()
        rightAxisVariant.valueFormatter = vcFormat
//            object : IAxisValueFormatter {
//            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
//                return value.toString() + "%"
//            }
//
//        }
        val leftAxisVariant: YAxis = chartReport.axisLeft
        rightAxisVariant.textSize = 12f
        leftAxisVariant.isEnabled = false
    }


    fun setDataChart() {
        if (dataChart.size != 0) {
            val listStringXAxis = when (homeViewModel.typeTimeReport) {
                Constants.DatePicker.QUICK_THIS_MONTH -> listOf<String>("Kỳ trước", "Kỳ này")
                else -> listOf<String>("Kỳ trước", "Kỳ này")
            }
            val chart = binding!!.chart

            val barEntriesBegin = mutableListOf<BarEntry>()
            dataChart.forEachIndexed { index, warehouseReportModel ->
                barEntriesBegin.add(
                    BarEntry(
                        (index + 1).toFloat(),
                        warehouseReportModel.materialBeginningValue.toFloat()
                    )
                )
            }

            val barEntriesEnd = mutableListOf<BarEntry>()
            dataChart.forEachIndexed { index, warehouseReportModel ->
                barEntriesEnd.add(
                    BarEntry(
                        (index + 1).toFloat(),
                        warehouseReportModel.materialEndingValue.toFloat()
                    )
                )
            }

            val barEntriesImport = mutableListOf<BarEntry>()
            dataChart.forEachIndexed { index, warehouseReportModel ->
                barEntriesImport.add(
                    BarEntry(
                        (index + 1).toFloat(),
                        warehouseReportModel.materialImportValue.toFloat()
                    )
                )
            }

            val barEntriesExport = mutableListOf<BarEntry>()
            dataChart.forEachIndexed { index, warehouseReportModel ->
                barEntriesExport.add(
                    BarEntry(
                        (index + 1).toFloat(),
                        warehouseReportModel.materialExportValue.toFloat()
                    )
                )
            }
//
//            val barEntriesBegin = mutableListOf<BarEntry>()
//            barEntriesBegin.add(BarEntry(1f, dataChart[0].toFloat()))
//            barEntriesBegin.add(BarEntry(2f, 2000000.toFloat()))
//
//            val barEntriesEnd = mutableListOf<BarEntry>()
//            barEntriesEnd.add(BarEntry(1f, 5000000.toFloat()))
//            barEntriesEnd.add(BarEntry(2f, 1200000.toFloat()))
//
//            val barEntriesImport = mutableListOf<BarEntry>()
//            barEntriesImport.add(BarEntry(1f, 100000.toFloat()))
//            barEntriesImport.add(BarEntry(2f, (200000).toFloat()))
//
//            val barEntriesExport = mutableListOf<BarEntry>()
//            barEntriesExport.add(BarEntry(1f, 180000.toFloat()))
//            barEntriesExport.add(BarEntry(2f, (120000).toFloat()))

            val barDataSetBegin = BarDataSet(barEntriesBegin, "GT đầu kỳ")
            val barDataSetEnd = BarDataSet(barEntriesEnd, "GT cuối kỳ")
            val barDataSetImport = BarDataSet(barEntriesImport, "GT nhập")
            val barDataSetExport = BarDataSet(barEntriesExport, "GT xuất")
            barDataSetExport.setColors(Color.RED)
            barDataSetBegin.setColors(Color.GREEN)
            barDataSetEnd.setColors(Color.YELLOW)
            barDataSetImport.setColors(Color.CYAN)
            val data = BarData(barDataSetBegin, barDataSetEnd, barDataSetImport, barDataSetExport)

            val params: ViewGroup.LayoutParams = chart.layoutParams
            params.height =
                12 * requireActivity().resources.getDimension(R.dimen._20sdp).toInt()
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            chart.layoutParams = params
            chart.requestLayout()
            mkRate = object : MarkerHorizontal(context) {
                override fun refreshContent(
                    e: Entry,
                    highlight: Highlight,
                ) {
                    val i = e.x.toDouble()
                    val name = if ((0.0 < i && i < 0.25) || (1.0 < i && i < 1.25)) "GT đầu kỳ"
                    else if ((0.25 < i && i < 0.5) || (1.25 < i && i < 1.5)) "GT cuối kỳ"
                    else if ((0.5 < i && i < 0.75) || (1.5 < i && i < 1.75)) "GT nhập"
                    else if ((0.75 < i && i < 1) || (1.75 < i && i < 2)) "GT xuất"
                    else ""

                    if (currentFilter == "money") {
                        val value = e.y.toDouble()

                        val money = name + "\n" + formatMoney(
                            value,
                            isAcceptMinus = true,
                            isAcceptZero = true
                        )
                        textView.text = money
                    } else {
                        val orderCount = name + "\n" + e.y.toString()
                        textView.text = orderCount
                    }

                    super.refreshContent(e, highlight)
                }
            }
            chart.marker = mkRate
            chart.axisRight.labelCount = 4
            data.barWidth = 0.175f
            data.setDrawValues(false)
            chart.data = data
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(listStringXAxis)
            chart.groupBars(0f, 0.1f, 0.05f)
            chart.highlightValue(null)
            chart.animateXY(1000, 1000)
            chart.invalidate()
            adapter.items =
                dataChart[dataChart.size - 1].listMaterial?.toMutableList() ?: mutableListOf()
        } else {
            binding?.chart?.data = null
            binding?.chart?.invalidate()
            adapter.items = mutableListOf()
        }
    }

    fun setDataRcv() {

    }
    fun getDataWarehouseReport(){
        viewModel.getListDataReportWarehouse(
            ReportRequestModel(
                homeViewModel.fromDate,
                homeViewModel.toDate,
                if (homeViewModel.isCompare) homeViewModel.fromDateCompare else null,
                if (homeViewModel.isCompare) homeViewModel.toDateCompare else null,
            )
        )
    }
    fun isEquals(a : List<WarehouseReportModel>,b:List<WarehouseReportModel>) : Boolean{
        if(a.size != b.size) return false
        for( i in a.indices){
            val t = a[i]
            val r = b[i]
            if( t.materialBeginningValue != r.materialBeginningValue ||
                t.materialEndingValue != r.materialEndingValue ||
                t.materialImportValue != r.materialImportValue ||
                t.materialExportValue != r.materialExportValue  )
                return false
        }
        return true
    }
}