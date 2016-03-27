package ch.yvu.teststore.integration

import ch.yvu.teststore.integration.test.ListBackedTestRepository
import ch.yvu.teststore.integration.testsuite.ListBackedTestSuiteRepository
import ch.yvu.teststore.test.TestRepository
import ch.yvu.teststore.testsuite.TestSuiteRepository
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType.ANNOTATION
import org.springframework.stereotype.Repository

@Configuration
@EnableAutoConfiguration(exclude = arrayOf(
        DataSourceAutoConfiguration::class,
        CassandraDataAutoConfiguration::class))
@ComponentScan(basePackages = arrayOf("ch.yvu.teststore"), excludeFilters = arrayOf(
        Filter(type = ANNOTATION, value = Repository::class),
        Filter(type = ANNOTATION, value = Configuration::class)))
open class RepositoryMockingConfiguration {

    @Bean
    open fun testSuiteRepository(): TestSuiteRepository {
        return ListBackedTestSuiteRepository(ListBackedRepository())
    }


    @Bean
    open fun testRepository(): TestRepository {
        return ListBackedTestRepository(ListBackedRepository())
    }
}