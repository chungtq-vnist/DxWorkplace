package com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.quality

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.QualityGoodsCompare
import com.example.test.dxworkspace.databinding.FragmentDashboardQualityManufacturingBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.model.menu.ManufacturingWorkSelect
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardControlManufacturingViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.dialog.DialogSelectManufacturingWork
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.quality.adapter.ManufacturingGoodsQualityAdapter
import com.example.test.dxworkspace.presentation.ui.timepicker.RangeTimeSelectFragment
import com.example.test.dxworkspace.presentation.utils.common.getTextValueChart
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.common.registerGreen
import com.example.test.dxworkspace.presentation.utils.common.unregisterGreen
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.github.mikephil.charting.components.AxisBase
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis


import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis

import com.github.mikephil.charting.data.BarData

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet

import com.github.mikephil.charting.data.BarDataSet


import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener


class DashboardQualityManufacturingFragment : BaseFragment<FragmentDashboardQualityManufacturingBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_dashboard_quality_manufacturing
    }

    override fun onStart() {
        super.onStart()
        registerGreen()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        unregisterGreen()
        EventBus.getDefault().unregister(this)
    }

    var dialog: DialogSelectManufacturingWork? = null
    var isQualityCompare = true // quality compare and price compare

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: DashboardControlManufacturingViewModel

    @Inject
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var configRepository: ConfigRepository

    var percentAverage = 0.0
    var listChart = mutableListOf<QualityGoodsCompare>()
    val qualityAdapter by lazy { ManufacturingGoodsQualityAdapter() }
    var isUpdate = false

    @Subscribe
    fun onEventUpdate(event: EventUpdate) {

    }

    fun onBusEvent(event: EventUpdate) {
        when (event.type) {
            EventUpdate.UPDATE_DASHBOARD_MANUFACTURING -> {
//                binding?.tvRangeTime?.text = homeViewModel.fromDate + " - " + homeViewModel.toDate
                getGoodsReport()
            }

            EventUpdate.UPDATE_LISTWORK_DASHBOARD_MANUFACTURING -> {
                getGoodsReport()
            }
            EventUpdate.SYNC_DASHBOARD_MANUFACTURING -> {
                isUpdate = true
                getGoodsReport()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory) {
            observe(failure) {
                showToast(EventToast(R.string.error_notification))
            }
            observe(listGoods) {
//                setData(5,100f)
                if(isUpdate){
                    isUpdate = false
                    binding!!.pullToRefresh.isRefreshing = false
                    val listTemp = listGoods.value ?: emptyList()
                    if(!isEqual(listChart,listTemp.toMutableList())) {
                        setDataChartReport(listGoods.value ?: emptyList())
                    }
                } else {
                    binding!!.pullToRefresh.isRefreshing = false
                    setDataChartReport(listGoods.value ?: emptyList())
                }
            }
            observe(optionSelect){
                if(it == "-1"){
                    binding?.tvStatus?.text = getText(R.string.title_percent_average)
                    binding?.tvProgress?.text = percentAverage.toString() + "%"
                } else {
                    binding?.tvStatus?.text = it?:""
                    binding?.tvProgress?.text = viewModel.valueSelect.value ?: ""
                }
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
//            tvRangeTime.text = homeViewModel.fromDate + " - " + homeViewModel.toDate
//            rlRangeTime.setOnClickListener {
//                postNormal(EventNextHome(RangeTimeSelectFragment::class.java))
//            }
//            ivBack.setOnClickListener { onBackPress() }
//            btnMenuMore.setOnClickListener {
//                showDialogSelectWorks(if (homeViewModel.listWorksSelected.isEmpty()) viewModel.listAllWorks else homeViewModel.listWorksSelected)
//            }
            pullToRefresh.setOnRefreshListener {
                getGoodsReport()
            }
            qualityAdapter.hasCompare = homeViewModel.isCompare
            rcvDetail.adapter = qualityAdapter
        }
        viewModel.getAllManufacturingWorkCanManage()
        getGoodsReport()
        setupChart2()

    }

    fun getGoodsReport() {
        viewModel.getReportGoodsQuality(
            configRepository.getCurrentRole().id,
            homeViewModel.listWorksSelected.filter { it.isSelected }.map { it.id },
            homeViewModel.fromDate,
            homeViewModel.toDate,
            if (homeViewModel.isCompare) homeViewModel.fromDateCompare else null,
            if (homeViewModel.isCompare) homeViewModel.toDateCompare else null,
        )
    }


    private fun showDialogSelectWorks(items: MutableList<ManufacturingWorkSelect>) {
        if (dialog != null || dialog?.showsDialog == true) {
            dialog?.dismiss() // Hủy dialog cũ
            dialog = null // Gán giá trị null cho biến dialog
        }
        dialog = DialogSelectManufacturingWork()
        dialog?.onApplyWorks = { list ->
            viewModel.listWorksSelected = list
            homeViewModel.listWorksSelected = list
            EventBus.getDefault()
                .post(EventUpdate(EventUpdate.UPDATE_LISTWORK_DASHBOARD_MANUFACTURING))
        }
        dialog?.data = items.map { e ->
            val t = ManufacturingWorkSelect(e.id, e.name)
            t.isSelected = e.isSelected
            t

        }.toMutableList()
        dialog?.show(childFragmentManager, "dialog2")

    }

    fun setupChart() {
//        val chartReport = binding!!.chart
        val chart = binding!!.chart

        chart.setDrawBarShadow(false)

        chart.setDrawValueAboveBar(true)

        chart.description.isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(true)
        chart.isDragEnabled = true
        chart.isAutoScaleMinMaxEnabled = true
        chart.setScaleEnabled(true)


        // draw shadows for each bar that show the maximum value
        // chart.setDrawBarShadow(true);


        // draw shadows for each bar that show the maximum value
        // chart.setDrawBarShadow(true);
        chart.setDrawGridBackground(false)

        val xl = chart.xAxis
        xl.position = XAxisPosition.BOTTOM
        xl.setDrawAxisLine(true)
        xl.setDrawGridLines(false)
        xl.granularity = 10f

        val yl = chart.axisLeft
        yl.setDrawAxisLine(true)
        yl.setDrawGridLines(true)
        yl.axisMinimum = 0f // this replaces setStartAtZero(true)

//        yl.setInverted(true);

        //        yl.setInverted(true);
        val yr = chart.axisRight
        yr.setDrawAxisLine(true)
        yr.setDrawGridLines(false)
        yr.axisMinimum = 0f // this replaces setStartAtZero(true)

//        yr.setInverted(true);

        //        yr.setInverted(true);
        chart.setFitBars(true)
        chart.animateY(2500)


        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.formSize = 8f
        l.xEntrySpace = 4f

    }

    fun setupChart2() {
        val chartReport = binding!!.chart
        chartReport.setDrawValueAboveBar(true)
        chartReport.isEnabled = true
        //chartReport.setTouchEnabled(true)
        chartReport.isDragEnabled = false
        chartReport.description = null
        //        chartReport.setMaxVisibleValueCount(6);
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
        val rightAxisVariant: YAxis = chartReport.axisRight
        rightAxisVariant.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        rightAxisVariant.spaceTop = 15f
        rightAxisVariant.axisLineColor = Color.argb(150, 95, 95, 95)
        rightAxisVariant.setDrawGridLines(true)
        rightAxisVariant.gridColor = Color.argb(88, 218, 218, 218)
        rightAxisVariant.setDrawLabels(true)
        rightAxisVariant.textSize = 12f
        rightAxisVariant.setDrawGridLines(false)
//        val vcFormat = VariantChartFormat()
        rightAxisVariant.valueFormatter = object : IAxisValueFormatter {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                return value.toString() + "%"
            }

        }
        val leftAxisVariant: YAxis = chartReport.axisLeft
        rightAxisVariant.textSize = 12f
        leftAxisVariant.isEnabled = false

        chartReport.setNoDataTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.ebony_clay_dark
            )
        )
        val legend: Legend = chartReport.legend
        legend.isEnabled = false
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.form = Legend.LegendForm.SQUARE
        legend.formSize = 9f
        legend.textSize = 11f
        legend.isWordWrapEnabled = true
        legend.xEntrySpace = 4f
    }


    private fun setDataChartReport(result: List<QualityGoodsCompare>) {
        val chartReport = binding!!.chart
        chartReport.removeAllViews()
        val xVals = ArrayList<String>()
        val yVals1 = ArrayList<BarEntry>()
        var shouldUseZeroMinValue = true
        var number = 0

//        val t = result[0]

        listChart =
            result.filter { (it.numberOfProducts ?: 0) != 0 || (it.numberOfWaste ?: 0) != 0 }
                .sortedBy { it.qualityPercent }.toMutableList()
//        for (i in 0..10) listChart.add(t)
        var totalP = 0.0
        var totalC = 0
        for(i in 0 until listChart.size){
            val s = listChart[i]
            totalP += ((s.numberOfProducts ?: 0 ) + (s.numberOfWaste ?: 0))*(s.qualityPercent ?: 0.0)
            totalC += (s.numberOfProducts ?: 0 ) + (s.numberOfWaste ?: 0)
        }
        if(listChart.isEmpty()){
            binding?.apply {
                chart.visibility = View.INVISIBLE
                tvTitleNoData.isVisible = true
                llOption.isVisible = false
            }
        } else {
            binding?.apply {
                chart.visibility = View.VISIBLE
                tvTitleNoData.isVisible = false
                percentAverage = (((totalP/totalC)*100).toLong())/100.0
                viewModel.setDataSelect("-1",percentAverage.toString()+"%")
                llOption.isVisible = true
            }
        }

        qualityAdapter.items = listChart
        for (i in listChart.size - 1 downTo 0) {
            xVals.add(
                getTextValueChart(listChart[i].goods?.name ?: "-")
            )

            val values: Float = listChart[i].qualityPercent!!.toFloat()

            if (values < 0f) {
                shouldUseZeroMinValue = false
            }
            yVals1.add(BarEntry(number.toFloat(), values))
            number += 1
        }
        val count = if (xVals.size > 5) 5 else xVals.size
        chartReport.axisRight.labelCount = count
        chartReport.axisRight.isGranularityEnabled = true

        chartReport.xAxis.labelCount = yVals1.size
        chartReport.xAxis.granularity = 1.0f
        if (shouldUseZeroMinValue) {
            chartReport.axisRight.axisMinimum = 0f
            chartReport.axisLeft.axisMinimum = 0f
        } else {
            chartReport.axisRight.resetAxisMinimum()
            chartReport.axisLeft.resetAxisMinimum()
        }

        val params: ViewGroup.LayoutParams = chartReport.layoutParams
        params.height =
            yVals1.size * requireActivity().resources.getDimension(R.dimen._20sdp).toInt()
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        chartReport.layoutParams = params
        chartReport.requestLayout()

        if (yVals1.size == 1 && !shouldUseZeroMinValue) chartReport.axisRight.axisMaximum = 0f
        val set1 = BarDataSet(yVals1, "DataSet")
        set1.setColors(Color.rgb(102, 138, 255))
        set1.setDrawValues(false)
        val data = BarData(set1)
//        mkRate = object : MarkerHorizontal(context) {
//            override fun refreshContent(
//                e: Entry,
//                highlight: Highlight,
//            ) {
//                val i = e.x.toInt()
//                val name = xVals[i]
//
//                if (currentFilter == ReportByStaffFragment.BY_MONEY) {
//                    val value = payMethodAdapter.getStaffAmount(i, e.y.toDouble())
//
//                    val money = name + "\n" + FormatUtil.formatMoney(
//                        value,
//                        isAcceptMinus = true,
//                        isAcceptZero = true
//                    )
//                    textView.text = money
//                } else {
//                    val orderCount = name + "\n" + FormatUtil.formatDouble(e.y.toDouble())
//                    textView.text = orderCount
//                }
//
//                super.refreshContent(e, highlight)
//            }
//        }
//        chartReport.marker = mkRate
//
        chartReport.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
                viewModel.optionSelect.value = "-1"
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val i = listChart[e?.x?.toInt() ?: 0]

                val d = e?.y ?: 0f
                viewModel.setDataSelect(
                    i.goods?.name ?: "--",
                    d.toString() + "%"
                )
            }
        })

        data.setValueTextSize(8f)
        data.setDrawValues(false)

        chartReport.data = data
        chartReport.xAxis.valueFormatter = IAxisValueFormatter { value: Float, _: AxisBase? ->
            val index = value.toInt()
            if (index >= 0 && index < xVals.size) {
                return@IAxisValueFormatter xVals[index]
            }
            ""
        }

        chartReport.xAxis.resetAxisMaximum()
        chartReport.xAxis.resetAxisMinimum()
        chartReport.axisRight
        chartReport.axisLeft
        chartReport.highlightValue(null)
        chartReport.animateXY(1000, 1000)
        chartReport.invalidate()
        chartReport.notifyDataSetChanged()
    }

    private fun setData(count: Int, range: Float) {
        val chart = binding!!.chart
        val barWidth = 9f
        val spaceForBar = 10f
        val values: ArrayList<BarEntry> = ArrayList()
        for (i in 0 until count) {
            val y = (Math.random() * range).toFloat()
            values.add(
                BarEntry(
                    i * spaceForBar, y
                )
            )
        }
        val values2: ArrayList<BarEntry> = ArrayList()
        for (i in 0 until count) {
            val y = (Math.random() * range).toFloat()
            values2.add(
                BarEntry(
                    i * spaceForBar, y
                )
            )
        }
        val set1: BarDataSet
        if (chart.getData() != null &&
            chart.getData().getDataSetCount() > 0
        ) {
            set1 = chart.getData().getDataSetByIndex(0) as BarDataSet
            set1.values = values
            chart.getData().notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(values, "DataSet 1")
            val set2 = BarDataSet(values2, "DataSet 2")
            set1.setDrawIcons(false)
            val dataSets: ArrayList<IBarDataSet> = ArrayList()
            dataSets.add(set1)
            dataSets.add(set2)
            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.barWidth = barWidth
            chart.setData(data)
        }
    }

    fun isEqual(a : MutableList<QualityGoodsCompare> , b : MutableList<QualityGoodsCompare>) : Boolean{
        if(a.size != b.size) return false
        a.forEach { k ->
            val t = b.find { k.goods?._id == it.goods?._id }
            if(t == null) return false
            if(t.numberOfProducts != k.numberOfProducts || t.numberOfWaste != k.numberOfWaste) return false
        }
        return true
    }
}