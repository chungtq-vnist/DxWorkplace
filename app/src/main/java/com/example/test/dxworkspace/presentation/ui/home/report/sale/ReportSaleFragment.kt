package com.example.test.dxworkspace.presentation.ui.home.report.sale

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.report.ReportRequestModel
import com.example.test.dxworkspace.databinding.FragmentReportFinancialBinding
import com.example.test.dxworkspace.databinding.FragmentReportSaleBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.model.menu.CompareModel
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.report.ReportViewModel
import com.example.test.dxworkspace.presentation.ui.home.report.adapter.DetailFinancialAdapter
import com.example.test.dxworkspace.presentation.ui.timepicker.RangeTimeSelectFragment
import com.example.test.dxworkspace.presentation.utils.Marker
import com.example.test.dxworkspace.presentation.utils.VariantChartFormat
import com.example.test.dxworkspace.presentation.utils.common.Constants
import com.example.test.dxworkspace.presentation.utils.common.formatMoney
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.highlight.Highlight


class ReportSaleFragment : BaseFragment<FragmentReportSaleBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModel : ReportViewModel

    @Inject
    lateinit var configRepository: ConfigRepository

    private var mkRate: Marker? = null
    var currentFilter = "money"
    val adapter by lazy { DetailFinancialAdapter() }

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
        return R.layout.fragment_report_sale
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

//        chart.drawOrder = arrayOf(
//            DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.CANDLE, DrawOrder.LINE, DrawOrder.SCATTER
//        )

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
        rightAxisVariant.isEnabled = true
        rightAxisVariant.labelCount = 5
        rightAxisVariant.setAxisMinimum(0f);
        rightAxisVariant.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        rightAxisVariant.spaceTop = 35f
        rightAxisVariant.axisLineColor = Color.argb(150, 218, 218, 218)
        rightAxisVariant.setDrawGridLines(true)
        rightAxisVariant.gridColor = Color.argb(88, 218, 218, 218)
        rightAxisVariant.setDrawLabels(true)
        rightAxisVariant.textSize = 12f
        val vcFormat2 = VariantChartFormat()
        rightAxisVariant.valueFormatter = vcFormat2


        val leftAxisVariant: YAxis = chart.axisLeft
        leftAxisVariant.labelCount = 6
        leftAxisVariant.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxisVariant.spaceTop = 15f
        leftAxisVariant.axisLineColor = Color.argb(150, 218, 218, 218)
        leftAxisVariant.setDrawGridLines(true)
        leftAxisVariant.gridColor = Color.argb(88, 218, 218, 218)
        leftAxisVariant.setDrawLabels(true)
        leftAxisVariant.textSize = 12f
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
        val dataNow = viewModel.saleData.value!!
        val dataPre = viewModel.saleDataCompare.value!!
        val barEntriesRevenue = mutableListOf<BarEntry>()
        barEntriesRevenue.add(BarEntry(1f,(dataPre.revenue ?: 0L).toFloat()))
        barEntriesRevenue.add(BarEntry(2f,(dataNow.revenue ?: 0L).toFloat()))
        var hasNegativeValue = dataNow.revenue <0.0 || dataPre.revenue < 0.0
        var maxOfRightAxis = 20

        val barEntriesNumberOrder = mutableListOf<BarEntry>()
        barEntriesNumberOrder.add(BarEntry(1f,(dataPre.numberOrder ?: 0L).toFloat()))
        barEntriesNumberOrder.add(BarEntry(2f,(dataNow.numberOrder ?: 0L).toFloat()))

        val barEntriesNewQuote = mutableListOf<BarEntry>()
        barEntriesNewQuote.add(BarEntry(1f,(dataPre.numberNewQuote ?: 0L).toFloat()))
        barEntriesNewQuote.add(BarEntry(2f,(dataNow.numberNewQuote ?: 0L).toFloat()))

        val barEntriesNewOrder = mutableListOf<BarEntry>()
        barEntriesNewOrder.add(BarEntry(1f,(dataPre.numberNewOrder ?: 0L).toFloat()))
        barEntriesNewOrder.add(BarEntry(2f,(dataNow.numberNewOrder ?: 0L).toFloat()))
        maxOfRightAxis = maxOf(dataNow.numberOrder,dataNow.numberNewOrder,dataNow.numberNewQuote,dataPre.numberOrder
        ,dataPre.numberNewOrder , dataPre.numberNewQuote).toInt()

//
        val listStringXAxis = when(homeViewModel.typeTimeReport){
            Constants.DatePicker.QUICK_THIS_MONTH -> listOf<String>("Kỳ trước","Kỳ này")
            else -> listOf<String>("Kỳ trước","Kỳ này")
        }

//        val barEntriesRevenue = mutableListOf<BarEntry>()
//        barEntriesRevenue.add(BarEntry(2f,1200000.toFloat()))
//        barEntriesRevenue.add(BarEntry(1f,1000000.toFloat()))
//
//        var maxOfRightAxis = 20
//
//        val barEntriesNumberOrder = mutableListOf<BarEntry>()
//        barEntriesNumberOrder.add(BarEntry(2f,40.toFloat()))
//        barEntriesNumberOrder.add(BarEntry(1f,50.toFloat()))
//
//        val barEntriesNewQuote = mutableListOf<BarEntry>()
//        barEntriesNewQuote.add(BarEntry(2f,120.toFloat()))
//        barEntriesNewQuote.add(BarEntry(1f,90.toFloat()))
//
//        val barEntriesNewOrder = mutableListOf<BarEntry>()
//        barEntriesNewOrder.add(BarEntry(2f,20.toFloat()))
//        barEntriesNewOrder.add(BarEntry(1f,30.toFloat()))
//        maxOfRightAxis = 200

        val barDataSetRevenue = BarDataSet(barEntriesRevenue,"Doanh thu")
        val barDataSetOrder = BarDataSet(barEntriesNumberOrder,"Số đơn")
        val barDataSetNewOrder = BarDataSet(barEntriesNewOrder,"Số đơn mới")
        val barDataSetNewQuote = BarDataSet(barEntriesNewQuote,"Số báo giá mới")
        barDataSetOrder.setColors(Color.RED)
        barDataSetRevenue.setColors(Color.YELLOW)
        barDataSetNewOrder.setColors(Color.GREEN)
        barDataSetNewQuote.setColors(Color.CYAN)
//        val bardata = BarData(barDataSetOrder,barDataSetNewOrder,barDataSetNewQuote)

        barDataSetRevenue.axisDependency = YAxis.AxisDependency.LEFT
        barDataSetOrder.axisDependency = YAxis.AxisDependency.RIGHT
        barDataSetNewOrder.axisDependency = YAxis.AxisDependency.RIGHT
        barDataSetNewQuote.axisDependency = YAxis.AxisDependency.RIGHT

        chart.xAxis.valueFormatter =  IndexAxisValueFormatter(listStringXAxis)
        if(hasNegativeValue) chart.axisLeft.resetAxisMinimum()
            else chart.axisLeft.axisMinimum = 0f
        chart.axisRight.axisMinimum = 0f
        chart.axisRight.labelCount = 4
        val bardata = BarData(barDataSetRevenue,barDataSetOrder,barDataSetNewOrder,barDataSetNewQuote)
        chart.axisLeft.labelCount = 4
        bardata.barWidth = 0.15f

        bardata.groupBars(0f,0.085f,0.05f)


//        val lineEntries = mutableListOf<Entry>()
//        lineEntries.add(Entry(1f,123000f))
//        lineEntries.add(Entry(2f,180090f))
//        val lineDataSet = LineDataSet(lineEntries,"line data")
//        lineDataSet.axisDependency = YAxis.AxisDependency.RIGHT
//        lineDataSet.setColor(Color.rgb(240, 238, 70));
//        lineDataSet.setLineWidth(2.5f);
//        lineDataSet.setCircleColor(Color.rgb(240, 238, 70));
//        lineDataSet.setCircleRadius(5f);
//        val lineData = LineData(lineDataSet)

        bardata.setDrawValues(true)
        mkRate = object: Marker(context){
            override fun refreshContent(
                e: Entry,
                highlight: Highlight,
            ) {
                val i = e.x.toDouble()
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


                super.refreshContent(e, highlight)
            }
        }
        chart.marker = mkRate



//        val combineData = CombinedData()
//        combineData.setData(bardata)
        chart.data = bardata
        chart.highlightValue(null)
        chart.animateXY(1000, 1000)
        chart.invalidate()

    }

    fun getReportData(){
        binding?.pullToRefresh?.isRefreshing = true
        viewModel.getSaleReport(ReportRequestModel(homeViewModel.fromDate,homeViewModel.toDate,homeViewModel.fromDateCompare,homeViewModel.toDateCompare))
    }

    fun setDataRcv(){
        val dataNow = viewModel.saleData.value!!
        val dataPre = viewModel.saleDataCompare.value!!
        val listItem = mutableListOf<CompareModel>()
        listItem.add(
            CompareModel((dataNow.revenue ?: 0.0),dataPre.revenue ?: 0.0,"money","Doanh thu",
                R.drawable.ic_money)
        )
        listItem.add(
            CompareModel((dataNow.expense ?: 0.0),dataPre.expense ?: 0.0,"money","Chi phí",
                R.drawable.ic_money)
        )
        listItem.add(
            CompareModel((dataNow.totalMoneyNewSaleOrder ?: 0.0),dataPre.totalMoneyNewSaleOrder ?: 0.0,"money","Tổng giá trị đơn bán hàng mới",
                R.drawable.ic_money)
        )
        listItem.add(
            CompareModel((dataNow.totalMoneyNewPurchaseOrder ?: 0.0),dataPre.totalMoneyNewPurchaseOrder ?: 0.0,"money","Tổng giá trị đơn mua hàng mới",
                R.drawable.ic_money)
        )
        listItem.add(
            CompareModel((dataNow.cancelMoney ?: 0.0),dataPre.cancelMoney ?: 0.0,"money","Tổng tiền hủy đơn",
                R.drawable.ic_money)
        )
        listItem.add(
            CompareModel((dataNow.numberOrder ?: 0L).toDouble(),(dataPre.numberOrder ?: 0L).toDouble(),"value","Hóa đơn ",
                R.drawable.ic_money)
        )
        listItem.add(
            CompareModel((dataNow.numberNewQuote ?: 0L).toDouble(),(dataPre.numberNewQuote ?: 0L).toDouble(),"value","Báo giá mới",
                R.drawable.ic_money)
        )
        listItem.add(
            CompareModel((dataNow.numberNewQuoteSuccess ?: 0L).toDouble(),(dataPre.numberNewQuoteSuccess ?: 0L).toDouble(),"value","Báo giá mới đã tạo đơn",
                R.drawable.ic_money)
        )
        listItem.add(
            CompareModel((dataNow.numberNewOrder ?: 0L).toDouble(),(dataPre.numberNewOrder ?: 0L).toDouble(),"value","Đơn bán hàng mới",
                R.drawable.ic_money)
        )
        listItem.add(
            CompareModel((dataNow.numberNewOrderSuccess ?: 0L).toDouble(),(dataPre.numberNewOrderSuccess ?: 0L).toDouble(),"value","Đơn bán hàng mới thành công",
                R.drawable.ic_money)
        )
        listItem.add(
            CompareModel((dataNow.numberCancelOrder ?: 0L).toDouble(),(dataPre.numberCancelOrder ?: 0L).toDouble(),"value","Đơn bán hàng đã hủy",
                R.drawable.ic_money)
        )
        listItem.add(
            CompareModel((dataNow.numberPurchaseOrder ?: 0L).toDouble(),(dataPre.numberPurchaseOrder ?: 0L).toDouble(),"value","Đơn mua NVL",
                R.drawable.ic_money)
        )

        binding?.tvNote?.text = getString(R.string.note_dashboard,"${homeViewModel.fromDate}" ,"${homeViewModel.toDate}" ,"${homeViewModel.fromDateCompare}",
            homeViewModel.toDateCompare
        )
        adapter.items = listItem
    }

    private val colors = intArrayOf(
        ColorTemplate.VORDIPLOM_COLORS[0],
        ColorTemplate.VORDIPLOM_COLORS[1],
        ColorTemplate.VORDIPLOM_COLORS[2]
    )


}