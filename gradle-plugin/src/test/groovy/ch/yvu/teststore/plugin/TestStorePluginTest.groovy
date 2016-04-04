package ch.yvu.teststore.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.junit.Before
import org.junit.Test

import static org.gradle.testfixtures.ProjectBuilder.builder
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class TestStorePluginTest {

    private Project project;

    @Before
    public void setUp() {
        project = builder().build();
        project.pluginManager.apply 'ch.yvu.teststore'
    }

    @Test
    public void addsStoreResultsTaskToProject() {
        def storeResultTask = project.tasks.storeResults

        assertTrue(storeResultTask instanceof DefaultTask)
    }

    @Test
    public void addsTeststoreExtensionPointToProject() {
        def testStoreExtension = project.teststore
        assertTrue(testStoreExtension instanceof TestStorePluginExtension)
    }

    @Test
    public void testStoreExtensionDefaultHostIsLocalhost() {
        def host = project.teststore.host

        assertEquals('localhost', host)
    }

    @Test
    public void testStoreExtensionDefaultPortIs8080() {
        def port = project.teststore.port
        assertEquals(8080, port)
    }
}
