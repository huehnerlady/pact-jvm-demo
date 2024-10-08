package com.example.pact.demo

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.PactBuilder
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.PactSpecVersion.V4
import au.com.dius.pact.core.model.V4Pact
import au.com.dius.pact.core.model.annotations.Pact
import kotlin.test.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@ExtendWith(value = [SpringExtension::class, PactConsumerTestExt::class])
@Tag("pact")
class ConsumerPactTest {

  @Pact(consumer = "Consumer", provider = "Provider")
  fun single(builder: PactBuilder): V4Pact {
    return builder
        .expectsToReceiveHttpInteraction("single") { httpBuilder ->
          httpBuilder
              .withRequest { httpRequestBuilder ->
                httpRequestBuilder
                    .path("/single")
                    .method("GET")
              }
              .willRespondWith { httpResponseBuilder ->
                httpResponseBuilder
                    .status(200)
                    .body(PactDslJsonBody()
                        .`object`("myData", PactDslJsonBody()
                            .stringType("foo", "foo")
                            .`object`("bar", PactDslJsonBody()
                                .stringType("bar", "bar")
                            )
                        )
                    )
              }
        }
        .toPact()
  }

  @Test
  @PactTestFor(pactMethod = "single", pactVersion = V4)
  fun `test single`(mockServer: MockServer) {
    val response = RestTemplate().getForObject<SingleData>("${mockServer.getUrl()}/single")

    assertEquals(SingleData(myData = MyData(foo = "foo", bar = Bar("bar"))), response)
  }

  @Pact(consumer = "Consumer", provider = "Provider")
  fun multi(builder: PactBuilder): V4Pact {
    return builder
        .expectsToReceiveHttpInteraction("multi") { httpBuilder ->
          httpBuilder
              .withRequest { httpRequestBuilder ->
                httpRequestBuilder
                    .path("/multi")
                    .method("GET")
              }
              .willRespondWith { httpResponseBuilder ->
                httpResponseBuilder
                    .status(200)
                    .body(PactDslJsonBody()
                        .`object`("myData", PactDslJsonBody()
                            .eachLike("myData", 2)
                            .stringType("foo", "foo")
                            .`object`("bar", PactDslJsonBody()
                                .stringType("bar", "bar")
                            )
                        )
                    )
              }
        }
        .toPact()
  }

  @Test
  @PactTestFor(pactMethod = "multi", pactVersion = V4)
  fun `test multi`(mockServer: MockServer) {
    val response = RestTemplate().getForObject<MultipleData>("${mockServer.getUrl()}/multi")

    assertEquals(MultipleData(myData = listOf(MyData(foo = "foo", bar = Bar("bar")), MyData(foo = "foo", bar = Bar("bar")))), response)
  }
}
