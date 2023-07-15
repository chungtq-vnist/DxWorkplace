package com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.DashboardManufacturingRequestByStatus
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.DashboardManufacturingRequestByType
import com.example.test.dxworkspace.databinding.FragmentDashboardManufacturingPlanBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.adapter.ManufacturingCommandDashboardAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.adapter.ManufacturingRequestDashboardAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.request.CreateManufacturingRequestFragment
import com.example.test.dxworkspace.presentation.utils.common.getThemePrimaryColor
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.ViewPortHandler
import javax.inject.Inject

class DashboardManufacturingRequestFragment : BaseFragment<FragmentDashboardManufacturingPlanBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_dashboard_manufacturing_plan
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: DashboardControlManufacturingViewModel

    @Inject
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var configRepository: ConfigRepository

    val adapterDetail by lazy { ManufacturingRequestDashboardAdapter() }

    // status and progress
    var isTypeStatus = true

    var numberRequestStatus = DashboardManufacturingRequestByStatus()
    var numberRequestType = DashboardManufacturingRequestByType()

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
                getNumberOfPlan()
            }
            EventUpdate.UPDATE_LISTWORK_DASHBOARD_MANUFACTURING -> {
                getNumberOfPlan()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory) {
            observe(failure) {
                showToast(EventToast(R.string.error_notification))
            }
            observe(numberOfRequestByStatus) {
                numberRequestStatus = it ?: DashboardManufacturingRequestByStatus()
                if (isTypeStatus) setDataRequestStatus()
                setDataForRcvDetail()
                binding?.pullToRefresh?.isRefreshing = false
            }
            observe(numberOfRequestByType) {
                numberRequestType = it ?: DashboardManufacturingRequestByType()
                if (!isTypeStatus) setDataRequestType()
                setDataForRcvDetail()
                binding?.pullToRefresh?.isRefreshing = false
            }
        }
        homeViewModel = viewModel(viewModelFactory) {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getNumberOfPlan()
        setupPieChart()
        binding?.apply {
//            tvStatus.setTextColor(getThemePrimaryColor(requireContext()))
//            viewStatus.setBackgroundColor(getThemePrimaryColor(requireContext()))
//            tvProgress.setTextColor(resources.getColor(R.color.sp_color_black_opacity_80))
//            viewProgress.setBackgroundColor(getThemePrimaryColor(requireContext()))
            tvTitleChart.text = getText(R.string.tv_title_chart_request_status)
            tvProgress.text = "Loại"
            rlStatus.setOnClickListener {
                if (!isTypeStatus) {
                    isTypeStatus = true
//                    tvStatus.setTextColor(resources.getColor(R.color.sp_color_blue))
//                    viewStatus.setBackgroundColor(resources.getColor(R.color.sp_color_blue))
//                    tvProgress.setTextColor(resources.getColor(R.color.sp_color_black_opacity_80))
//                    viewProgress.setBackgroundColor(resources.getColor(R.color.sp_color_black_opacity_80))
                    viewStatus.isVisible = true
                    viewProgress.isVisible = false
                    tvTitleChart.text = getText(R.string.tv_title_chart_request_status)
                    setDataRequestStatus()
                    setDataForRcvDetail()
                }
            }
            rlProgress.setOnClickListener {
                if (isTypeStatus) {
                    isTypeStatus = false
//                    tvStatus.setTextColor(resources.getColor(R.color.sp_color_black_opacity_80))
//                    viewStatus.setBackgroundColor(resources.getColor(R.color.sp_color_black_opacity_80))
//                    tvProgress.setTextColor(resources.getColor(R.color.sp_color_blue))
//                    viewProgress.setBackgroundColor(resources.getColor(R.color.sp_color_blue))
                    viewStatus.isVisible = false
                    viewProgress.isVisible = true
                    tvTitleChart.text = getText(R.string.tv_title_chart_request_type)
                    setDataRequestType()
                    setDataForRcvDetail()
                }
            }
            rcvDetail.adapter = adapterDetail
            adapterDetail.onClick2 = {
                postNormal(EventNextHome(CreateManufacturingRequestFragment::class.java, bundleOf(Pair("REQUEST_ID",it._id))))
            }
            pullToRefresh.setOnRefreshListener {
                getNumberOfPlan()
            }
            tvTitleNoData.text = getString(R.string.title_nodata_request)
        }
    }

    fun getNumberOfPlan() {
        viewModel.getNumberOfRequestByStatus(
            configRepository.getCurrentRole().id,
            homeViewModel.listWorksSelected.filter { it.isSelected }.map { it.id },
            homeViewModel.fromDate,
            homeViewModel.toDate
        )
        viewModel.getNumberOfRequestByType(
            configRepository.getCurrentRole().id,
            homeViewModel.listWorksSelected.filter { it.isSelected }.map { it.id },
            homeViewModel.fromDate,
            homeViewModel.toDate
        )
    }

    fun setupPieChart() {
        val chart = binding!!.chartPlan
        chart.setUsePercentValues(false)
        chart.description.isEnabled = false
        // set tuy chinh vi tri so voi man hinh
        chart.setExtraOffsets(5f, 10f, 5f, 10f)

        chart.dragDecelerationFrictionCoef = 0.95f

        chart.setCenterTextTypeface(
            ResourcesCompat.getFont(
                requireContext(),
                R.font.roboto_regular
            )
        )


        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)

        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)

        // setup hole
        chart.holeRadius = 45f
        chart.transparentCircleRadius = 45f

        chart.setDrawCenterText(true)

        chart.rotationAngle = 0f
        // enable rotation of the chart by touch
        // enable rotation of the chart by touch
        chart.isRotationEnabled = true
        chart.isHighlightPerTapEnabled = true

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {

            }

            override fun onNothingSelected() {

            }

        })


        chart.animateY(1000, Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutQuad))

//         chart.spin(2000, 0f, 360f)
        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM;
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT;
        l.orientation = Legend.LegendOrientation.HORIZONTAL;
        l.isWordWrapEnabled = true;
        l.setDrawInside(false);
        l.yOffset = 5f;


        // entry label styling
        chart.setEntryLabelColor(Color.WHITE)
        chart.setEntryLabelTypeface(
            ResourcesCompat.getFont(
                requireContext(),
                R.font.roboto_regular
            )
        )
        chart.setEntryLabelTextSize(12f)
        chart.setDrawEntryLabels(false)
    }

    // setData cho chart
    // count : so luong cac phan , range
    private fun setDataRequestStatus() {
        val entries: ArrayList<PieEntry> = ArrayList()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
//        for (i in 0 until count) {
//            entries.add(
//                PieEntry(
//                    numberPlanStatus.plan1.toFloat(),
//                    "thisislabel if it is too long then ...")
//                )
//        }
//      1-Chờ phê duyệt 2-Đã gửi yêu cầu ,3 - Đã phê duyệt  ,4 Đang tiến hành ,5 Đã hoàn thành  ,6 Đã hủy
        entries.add(
            PieEntry(
                (numberRequestStatus.request1 ?: 0).toFloat(),
                "Chờ phê duyệt"
            )
        )
        entries.add(
            PieEntry(
                (numberRequestStatus.request2 ?: 0).toFloat(),
                "Đã gửi yêu cầu"
            )
        )
        entries.add(
            PieEntry(
                (numberRequestStatus.request3 ?: 0).toFloat(),
                "Đã phê duyệt"
            )
        )
        entries.add(
            PieEntry(
                (numberRequestStatus.request4 ?: 0).toFloat(),
                "Đang thực hiện"
            )
        )
        entries.add(
            PieEntry(
                (numberRequestStatus.request5 ?: 0).toFloat(),
                "Đã hoàn thành"
            )
        )
        entries.add(
            PieEntry(
                (numberRequestStatus.request6 ?: 0).toFloat(),
                "Đã hủy"
            )
        )


        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0F, 40F)
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors: ArrayList<Int> = ArrayList()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
//        dataSet.colors = colors
        dataSet.setColors(
            Color.rgb(119, 240, 249),
            Color.YELLOW,
            Color.rgb(65, 108, 249),
            Color.rgb(249, 119, 240),
            Color.GREEN,
            Color.rgb(255, 76, 31)
        )
        //dataSet.setSelectionShift(0f);
//        Color.rgb(228,243,19),
        binding?.chartPlan?.apply {
            setNoDataText("")
        }
        if (entries.find { it.value != 0f } == null) {
            binding?.chartPlan?.setDrawCenterText(false)
            binding?.tvTitleNoData?.isVisible = true
            binding?.chartPlan?.visibility = View.INVISIBLE
            binding?.chartPlan?.highlightValues(null)
            binding?.chartPlan?.invalidate()
        } else {
            binding?.chartPlan?.setDrawCenterText(true)
            binding?.tvTitleNoData?.isVisible = false
            binding?.chartPlan?.visibility = View.VISIBLE
            val data = PieData(dataSet)
            data.setValueFormatter(object : IValueFormatter {
                override fun getFormattedValue(
                    value: Float,
                    entry: Entry?,
                    dataSetIndex: Int,
                    viewPortHandler: ViewPortHandler?
                ): String {
                    if (value == 0f) return ""
                    else return try {
                        value.toInt().toString()
                    } finally {
                        value.toString()
                    }
                }

            })

            data.setValueTextSize(13f)
            data.setValueTextColor(Color.BLACK)
            data.setValueTypeface(ResourcesCompat.getFont(requireContext(), R.font.roboto_bold))
            binding?.chartPlan?.setData(data)

            // undo all highlights
            binding?.chartPlan?.apply {
                centerText =
                    "Σ = ${
                        (numberRequestStatus.request1 ?: 0) + (numberRequestStatus.request2 ?: 0) + (numberRequestStatus.request3 ?: 0) + (numberRequestStatus.request4 ?: 0) +
                                (numberRequestStatus.request5 ?: 0) + (numberRequestStatus.request6 ?: 0)
                    }"
            }
            binding?.chartPlan?.animateY(
                1000,
                Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutQuad)
            )
            binding?.chartPlan?.highlightValues(null)
            binding?.chartPlan?.invalidate()
        }

    }

    private fun setDataRequestType() {
        val entries: ArrayList<PieEntry> = ArrayList()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
//        for (i in 0 until count) {
//            entries.add(
//                PieEntry(
//                    numberPlanStatus.plan1.toFloat(),
//                    "thisislabel if it is too long then ...")
//                )
//        }
//        mua hàng: type = 1, nhập kho: type = 2, xuất kho : type = 3
        entries.add(
            PieEntry(
                (numberRequestType.request1 ?: 0).toFloat(),
                "Yêu cầu mua hàng"
            )
        )
        entries.add(
            PieEntry(
                (numberRequestType.request2 ?: 0).toFloat(),
                "Yêu cầu nhập kho"
            )
        )
        entries.add(
            PieEntry(
                (numberRequestType.request3 ?: 0).toFloat(),
                "Yêu cầu xuất kho"
            )
        )


        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0F, 40F)
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors: ArrayList<Int> = ArrayList()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
//        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        dataSet.setColors(Color.GREEN, Color.YELLOW, Color.CYAN)

        binding?.chartPlan?.apply {
            setNoDataText("Không có dữ liệu!")
        }
        if (entries.find { it.value != 0f } == null) {
            binding?.chartPlan?.setDrawCenterText(false)
            binding?.tvTitleNoData?.isVisible = true
            binding?.chartPlan?.visibility = View.INVISIBLE
            binding?.chartPlan?.highlightValues(null)
            binding?.chartPlan?.invalidate()
        } else {
            binding?.chartPlan?.setDrawCenterText(true)
            binding?.tvTitleNoData?.isVisible = false
            binding?.chartPlan?.visibility = View.VISIBLE
            val data = PieData(dataSet)
            data.setValueFormatter(object : IValueFormatter {
                override fun getFormattedValue(
                    value: Float,
                    entry: Entry?,
                    dataSetIndex: Int,
                    viewPortHandler: ViewPortHandler?
                ): String {
                    if (value == 0f) return ""
                    else return try {
                        value.toInt().toString()
                    } finally {
                        value.toString()
                    }
                }

            })

            data.setValueTextSize(13f)
            data.setValueTextColor(Color.BLACK)
            data.setValueTypeface(ResourcesCompat.getFont(requireContext(), R.font.roboto_bold))
            binding?.chartPlan?.setData(data)

            // undo all highlights
            binding?.chartPlan?.apply {
                centerText =
                    "Σ = ${(numberRequestType.request1 ?: 0) + (numberRequestType.request2 ?: 0) + (numberRequestType.request3 ?: 0)}"
            }
            binding?.chartPlan?.animateY(
                1000,
                Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutQuad)
            )
            binding?.chartPlan?.highlightValues(null)
            binding?.chartPlan?.invalidate()
        }

    }

    fun loadDataFail() {
        binding?.chartPlan?.data = null
        binding?.chartPlan?.highlightValues(null)
        binding?.chartPlan?.invalidate()
    }

    fun setDataForRcvDetail() {
        if (isTypeStatus) {
            var data = listOf(
                Pair(numberRequestStatus.listStatus11, "Chờ phê duyệt"),
                Pair(numberRequestStatus.listStatus21, "Đã gửi yêu cầu"),
                Pair(numberRequestStatus.listStatus31, "Đã phê duyệt"),
                Pair(numberRequestStatus.listStatus41, "Đang thực hiện"),
                Pair(numberRequestStatus.listStatus51, "Đã hoàn thành"),
                Pair(numberRequestStatus.listStatus61, "Đã hủy")
            ).filterNot { it.first.isNullOrEmpty() }
            binding?.tvTitle?.text = getString(R.string.title_status_request)
            adapterDetail.items = data

        } else {
            var data = listOf(
                Pair(numberRequestType.listRequestPurchase, "Yêu cầu mua hàng"),
                Pair(numberRequestType.listRequestImport, "Yêu cầu nhập kho"),
                Pair(numberRequestType.listRequestExport, "Yêu cầu xuất kho"),
            ).filterNot { it.first.isNullOrEmpty() }
            adapterDetail.items = data
            binding?.tvTitle?.text = getString(R.string.title_type_request)
        }
    }
}