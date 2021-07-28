package com.example.travelencer_android_2021.model

data class ModelKakaoLocalApi(
    val documents: List<Document>,
    val meta: Meta
)

data class Meta(
        val is_end: Boolean,
        val pageable_count: Int,
        val total_count: Int
)

data class Document(
        val address: Address,
        val address_name: String,
        val address_type: String,
        val road_address: Any,
        val x: String,
        val y: String
)

data class Address(
        val address_name: String,
        val b_code: String,
        val h_code: String,
        val main_address_no: String,
        val mountain_yn: String,
        val region_1depth_name: String,
        val region_2depth_name: String,
        val region_3depth_h_name: String,
        val region_3depth_name: String,
        val sub_address_no: String,
        val x: String,
        val y: String
)