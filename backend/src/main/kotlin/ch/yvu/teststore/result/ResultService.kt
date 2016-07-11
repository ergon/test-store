package ch.yvu.teststore.result

import ch.yvu.teststore.run.RunRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
open class ResultService @Autowired constructor(
        open val resultRepository: ResultRepository,
        open val runRepository: RunRepository
) {
    fun getGroupedResults(runId: UUID): Map<TestWithResults.TestResult, List<TestWithResults>> {
        return getTestsWithResults(runId).groupBy { it.getTestResult() }
    }

    fun getTestsWithResults(runId: UUID): List<TestWithResults> {
        return resultRepository.findAllByRunId(runId)
                .groupBy { it.testName }
                .map {
                    val (name, results) = it
                    TestWithResults(name!!, results)
                }
    }

    fun getTestWithResults(runId: UUID, testName: String): TestWithResults? {
        return resultRepository.findAllByRunIdAndTestName(runId, testName)
                .groupBy { it.testName }
                .map {
                    val (name, results) = it
                    TestWithResults(name!!, results)
                }.firstOrNull()
    }

    fun getResultsByTestSuiteAndTestName(testSuiteId: UUID, testName: String): List<Result> {
        return runRepository.findAllByTestSuiteId(testSuiteId).flatMap {
            resultRepository.findAllByRunIdAndTestName(it.id!!, testName)
        }
    }
}