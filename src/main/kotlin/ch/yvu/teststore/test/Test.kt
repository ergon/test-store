package ch.yvu.teststore.test

import ch.yvu.teststore.common.Model
import org.springframework.data.cassandra.mapping.PrimaryKey
import org.springframework.data.cassandra.mapping.Table

@Table("test")
data class Test(@PrimaryKey val id: String, val name: String) : Model {
    override fun id(): String {
        return id
    }
}