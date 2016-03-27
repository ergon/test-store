package ch.yvu.teststore.integration.test

import ch.yvu.teststore.integration.RepositoryMockingConfiguration
import ch.yvu.teststore.test.TestRepository
import com.jayway.restassured.RestAssured
import com.jayway.restassured.RestAssured.given
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner


@WebIntegrationTest("server.port=0")
@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(RepositoryMockingConfiguration::class))
class TestControllerTest {

    @Autowired
    lateinit var tesRepository: TestRepository

    @Value("\${local.server.port}")
    var port: Int = 0

    @Before fun setUp() {
        RestAssured.port = port
    }

    @org.junit.Test fun itCanStoreATest() {
        val testName = "FooTest"

        given().queryParam("name", testName).post("/tests").then().assertThat().statusCode(200)

        assertEquals(1, tesRepository.count())
    }
}