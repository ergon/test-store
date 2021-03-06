package ch.yvu.teststore.integration.run

import ch.yvu.teststore.common.Page
import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
import java.util.*

open class ListBackedRunRepository(val genericRepository: ListBackedRepository<Run>) : RunRepository {

    override fun findAllByTestSuiteId(testSuiteId: UUID, maxRows: Int): List<Run> {
        return findAllByTestSuiteId(testSuiteId).take(maxRows)
    }

    override fun findAllByTestSuiteId(testSuiteId: UUID, page: String?, fetchSize: Int?): Page<Run> {
        val allElements = findAllByTestSuiteId(testSuiteId)
        val start = page?.toInt() ?: 0
        val end = if (fetchSize == null) allElements.size else start + fetchSize;
        val nextPage = if (end >= allElements.size) null else end.toString()
        return Page(allElements.subList(start, end), nextPage);
    }

    override fun findById(id: UUID): Run? {
        return genericRepository.findAll{ it.id==id }.firstOrNull()
    }

    override fun findLastRunBefore(testSuiteId: UUID, time: Date): Run? {
        return findAllByTestSuiteId(testSuiteId).find { it.time != null && it.time!!.compareTo(time) < 0 }
    }

    override fun deleteAll() {
        genericRepository.deleteAll()
    }

    override fun save(item: Run): Run {
        return genericRepository.save(item)
    }

    override fun findAll(): List<Run> {
        return sorted(genericRepository.findAll())
    }

    override fun count(): Long {
        return genericRepository.count()
    }

    private fun sorted(results: List<Run>): List<Run> {
        return results.sortedByDescending { it.time }
    }

    private fun findAllByTestSuiteId(testSuiteId: UUID): List<Run> {
        return sorted(genericRepository.findAll { run -> testSuiteId == run.testSuite })
    }
}