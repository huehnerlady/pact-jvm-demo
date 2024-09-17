package com.example.pact.demo

import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.loader.PactFolder
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@Tag("pact")
@PactFolder("pacts")
@SpringBootTest
@Provider("Provider")
class ProviderPactTest {

  @Autowired
  lateinit var webApplicationContext: WebApplicationContext

  @BeforeEach
  fun setup(pactContext: PactVerificationContext) {
    val mockMvc: MockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .build()
    pactContext.target = MockMvcTestTarget(mockMvc)
  }

  @TestTemplate
  @ExtendWith(PactVerificationInvocationContextProvider::class)
  fun pactVerificationTestTemplate(pactContext: PactVerificationContext) {
    pactContext.verifyInteraction()
  }
}
