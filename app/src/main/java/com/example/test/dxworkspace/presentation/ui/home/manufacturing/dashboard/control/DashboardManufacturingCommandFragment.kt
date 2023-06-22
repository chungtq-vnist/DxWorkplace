package com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.DashboardManufacturingCommandByProgress
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.DashboardManufacturingCommandByStatus
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.DashboardManufacturingPlanByProgress
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.DashboardManufacturingPlanByStatus
import com.example.test.dxworkspace.databinding.FragmentDashboardManufacturingPlanBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.adapter.ManufacturingCommandDashboardAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.adapter.ManufacturingPlanDashboardAdapter
import com.example.test.dxworkspace.presentation.utils.event.EventBus
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

class DashboardManufacturingCommandFragment : BaseFragment<FragmentDashboardManufacturingPlanBinding>() {
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

    val adapterDetail by lazy { ManufacturingCommandDashboardAdapter() }

    // status and progress
    var isTypeStatus = true

    var numberCommandStatus = DashboardManufacturingCommandByStatus()
    var numberCommandProgress = DashboardManufacturingCommandByProgress()

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
            observe(numberOfCommandByStatus) {
                numberCommandStatus = it ?: DashboardManufacturingCommandByStatus()
                if (isTypeStatus) setDataCommandStatus()
                setDataForRcvDetail()
                binding?.pullToRefresh?.isRefreshing = false
            }
            observe(numberOfCommandByProgress) {
                numberCommandProgress = it ?: DashboardManufacturingCommandByProgress()
                if (!isTypeStatus) setDataProgress()
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
            tvStatus.setTextColor(resources.getColor(R.color.sp_color_blue))
            viewStatus.setBackgroundColor(resources.getColor(R.color.sp_color_blue))
            tvProgress.setTextColor(resources.getColor(R.color.sp_color_black_opacity_80))
            viewProgress.setBackgroundColor(resources.getColor(R.color.sp_color_black_opacity_80))
            tvTitleChart.text = getText(R.string.tv_title_chart_command_status)
            rlStatus.setOnClickListener {
                if (!isTypeStatus) {
                    isTypeStatus = true
                    tvStatus.setTextColor(resources.getColor(R.color.sp_color_blue))
                    viewStatus.setBackgroundColor(resources.getColor(R.color.sp_color_blue))
                    tvProgress.setTextColor(resources.getColor(R.color.sp_color_black_opacity_80))
                    viewProgress.setBackgroundColor(resources.getColor(R.color.sp_color_black_opacity_80))
                    tvTitleChart.text = getText(R.string.tv_title_chart_command_status)
                    setDataCommandStatus()
                    setDataForRcvDetail()
                }
            }
            rlProgress.setOnClickListener {
                if (isTypeStatus) {
                    isTypeStatus = false
                    tvStatus.setTextColor(resources.getColor(R.color.sp_color_black_opacity_80))
                    viewStatus.setBackgroundColor(resources.getColor(R.color.sp_color_black_opacity_80))
                    tvProgress.setTextColor(resources.getColor(R.color.sp_color_blue))
                    viewProgress.setBackgroundColor(resources.getColor(R.color.sp_color_blue))
                    tvTitleChart.text = getText(R.string.tv_title_chart_command_progress)
                    setDataProgress()
                    setDataForRcvDetail()
                }
            }
            rcvDetail.adapter = adapterDetail
            pullToRefresh.setOnRefreshListener {
                getNumberOfPlan()
            }
            tvTitleNoData.text = getString(R.string.title_nodata_command)
        }
    }

    fun getNumberOfPlan() {
        viewModel.getNumberOfCommandByStatus(
            configRepository.getCurrentRole().id,
            homeViewModel.listWorksSelected.filter { it.isSelected }.map { it.id },
            homeViewModel.fromDate,
            homeViewModel.toDate
        )
        viewModel.getNumberOfCommandByProgress(
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
    private fun setDataCommandStatus() {
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
//   1. Chờ phê duyệt || 2. Lệnh đã duyệt || 3. Lệnh đang được thực thi || 4. Đã hoàn thành || 5. Đã hủy || 6. Mới tạo
        entries.add(
            PieEntry(
                (numberCommandStatus.command6 ?: 0).toFloat(),
                "Mới tạo"
            )
        )
        entries.add(
            PieEntry(
                (numberCommandStatus.command1 ?: 0).toFloat(),
                "Chờ phê duyệt"
            )
        )
        entries.add(
            PieEntry(
                (numberCommandStatus.command2 ?: 0).toFloat(),
                "Đã duyệt"
            )
        )
        entries.add(
            PieEntry(
                (numberCommandStatus.command3 ?: 0).toFloat(),
                "Đang thực thi"
            )
        )
        entries.add(
            PieEntry(
                (numberCommandStatus.command4 ?: 0).toFloat(),
                "Đã hoàn thành"
            )
        )
        entries.add(
            PieEntry(
                (numberCommandStatus.command5 ?: 0).toFloat(),
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
                    "Σ = ${
                        (numberCommandStatus.command1 ?: 0) + (numberCommandStatus.command2 ?: 0) + (numberCommandStatus.command3 ?: 0) + (numberCommandStatus.command4 ?: 0) +
                                (numberCommandStatus.command5 ?: 0) + (numberCommandStatus.command6 ?: 0)
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

    private fun setDataProgress() {
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
//        1. Đang chờ duyệt || 2. Đã phê duyệt || 3. Đang thực hiện || 4. Đã hoàn thành || 5. Đã hủy
        entries.add(
            PieEntry(
                (numberCommandProgress.trueCommands ?: 0).toFloat(),
                "Đúng tiến độ"
            )
        )
        entries.add(
            PieEntry(
                (numberCommandProgress.slowCommands ?: 0).toFloat(),
                "Chậm tiến độ"
            )
        )
        entries.add(
            PieEntry(
                (numberCommandProgress.expiredCommands ?: 0).toFloat(),
                "Quá hạn"
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
        dataSet.setColors(Color.GREEN, Color.YELLOW, Color.rgb(255, 76, 31))

        binding?.chartPlan?.apply {
            setNoDataText("Không có dữ liệu!")
        }
        if (entries.find { it.value != 0f } == null) {
            binding?.chartPlan?.setDrawCenterText(false)
            binding?.tvTitleNoData?.isVisible = true
            binding?.chartPlan?.highlightValues(null)
            binding?.chartPlan?.invalidate()
        } else {
            binding?.chartPlan?.setDrawCenterText(true)
            binding?.tvTitleNoData?.isVisible = false
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
                    "Σ = ${(numberCommandProgress.trueCommands ?: 0) + (numberCommandProgress.slowCommands ?: 0) + (numberCommandProgress.expiredCommands ?: 0)}"
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
                Pair(numberCommandStatus.listCommand6, "Mới tạo"),
                Pair(numberCommandStatus.listCommand1, "Đang chờ duyệt"),
                Pair(numberCommandStatus.listCommand2, "Đã phê duyệt"),
                Pair(numberCommandStatus.listCommand3, "Đang thực hiện"),
                Pair(numberCommandStatus.listCommand4, "Đã hoàn thành"),
                Pair(numberCommandStatus.listCommand5, "Đã hủy")
            ).filterNot { it.first.isNullOrEmpty() }
            binding?.tvTitle?.text = getString(R.string.title_status_command)
            adapterDetail.items = data

        } else {
            var data = listOf(
                Pair(numberCommandProgress.arrayTrueCommands, "Đúng tiến độ"),
                Pair(numberCommandProgress.arraySlowCommands, "Chậm tiến độ"),
                Pair(numberCommandProgress.arrayExpiredCommands, "Quá hạn"),
            ).filterNot { it.first.isNullOrEmpty() }
            adapterDetail.items = data
            binding?.tvTitle?.text = getString(R.string.title_progress_command)
        }
    }
}