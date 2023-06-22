package com.example.test.dxworkspace.domain.model.task

data class RequestTaskModel(
   var type: String ="",
   var user: String ="",
   var perPage: Int = 1000,
   var startDate: String ="",
   var endDate: String = "",
   var aPeriodOfTime: Boolean = true
)
