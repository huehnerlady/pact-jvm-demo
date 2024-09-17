package com.example.pact.demo

data class SingleData(
    val myData: MyData?
)

data class MultipleData(
    val myData: List<MyData>?
)

data class MyData(
    val foo: String?,
    val bar: Bar?
)

data class Bar(
    val bar: String?
)
