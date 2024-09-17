package com.example.pact.demo

import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RestController

@RestController
class HttpController {

  @RequestMapping(value = ["/single"], method = [GET], produces = [APPLICATION_JSON_VALUE])
  fun singleData(): SingleData {
    return SingleData(MyData("foo", Bar("bar")))
  }

  @RequestMapping(value = ["/multi"], method = [GET], produces = [APPLICATION_JSON_VALUE])
  fun multiData(): MultipleData {
    return MultipleData(listOf(MyData("fooo", Bar("baar"))))
  }
}

