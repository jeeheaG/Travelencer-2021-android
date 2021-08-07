package com.example.travelencer_android_2021.model.modelTourApiKeyword

data class ModelTourApiKeyword(
        val response: Response
)

data class Response(
        val body: Body,
        val header: Header
)

data class Header(
        val resultCode: String,
        val resultMsg: String
)

data class Body(
        val items: Items,
        val numOfRows: String,
        val pageNo: String,
        val totalCount: String
)

data class Items(
        // TODO : 검색결과가 없어서 아래처럼 items에 json array []인 item 대신 빈 문자열 ""가 와도 오류가 안나도록 처리하기..how?
        //{"response":{"header":{"resultCode":"0000","resultMsg":"OK"},"body":{"items":"","numOfRows":10,"pageNo":1,"totalCount":0}}}
        val item: List<Item>
)

data class Item(
        val addr1: String,
        val addr2: String,
        val areacode: String,
        val booktour: String,
        val cat1: String,
        val cat2: String,
        val cat3: String,
        val contentid: String,
        val contenttypeid: String,
        val createdtime: String,
        val firstimage: String,
        val firstimage2: String,
        val mapx: String,
        val mapy: String,
        val mlevel: String,
        val modifiedtime: String,
        val readcount: String,
        val sigungucode: String,
        val tel: String?,
        val title: String
)
