package src;

import static org.junit.Assert.*;

import org.junit.Test;
import tech.brownbear.resources.ClasspathResourceFetcher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MavenDependencyConverterTest {
    private static final String GUAVA_FILE_NAME = "guava.xml";
    private static final String JUNIT_FILE_NAME = "junit.xml";

    @Test
    public void testWhenParsed_ThenMavenDependencyHasCorrectValues() {
        ClasspathResourceFetcher resourceFetcher = new ClasspathResourceFetcher(
            MavenDependencyConverterTest.class,
            Collections.singleton("/maven"));

        final Map<String, MavenDependency> mavenDepByName = new HashMap<>();
        resourceFetcher.visit(p -> p.toString().endsWith("xml"), (url, path) -> {
            try {
                byte[] encoded = Files.readAllBytes(path);
                String content = new String(encoded, StandardCharsets.UTF_8);
                MavenDependencyConverter converter = new MavenDependencyConverter(content);
                mavenDepByName.put(path.getFileName().toString(), converter.parse());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        assertTrue(mavenDepByName.containsKey(GUAVA_FILE_NAME));
        assertTrue(mavenDepByName.containsKey(JUNIT_FILE_NAME));

        MavenDependency guava = mavenDepByName.get(GUAVA_FILE_NAME);

        assertTrue("com.google.guava".equals(guava.getGroupId()));
        assertTrue("guava".equals(guava.getArtifactId()));
        assertTrue("27.0.1-jre".equals(guava.getVersion()));

        MavenDependency junit = mavenDepByName.get(JUNIT_FILE_NAME);

        assertTrue("junit".equals(junit.getGroupId()));
        assertTrue("junit".equals(junit.getArtifactId()));
        assertTrue("4.12".equals(junit.getVersion()));
    }
}
