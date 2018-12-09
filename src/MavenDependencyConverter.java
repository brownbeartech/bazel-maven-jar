package src;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;

public class MavenDependencyConverter {
    private static Logger logger = LogManager.getRootLogger();

    private final File file;
    private final String dependency;

    public MavenDependencyConverter(File file) {
        this(file, null);
    }

    public MavenDependencyConverter(String dependency) {
        this(null, dependency);
    }

    private MavenDependencyConverter(File file, String dependency) {
        this.file = file;
        this.dependency = dependency;
    }

    private Document parseDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            if (file != null) {
                return builder.parse(file);
            } else if (dependency != null) {
                return builder.parse(new ByteArrayInputStream(dependency.getBytes()));
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MavenDependency parse() {
            Document doc = parseDocument();
            Element root = doc.getDocumentElement();
            NodeList groupIdNodes = root.getElementsByTagName("groupId");
            if (groupIdNodes.getLength() != 1) {
                return null;
            }
            NodeList artifactIdNodes = root.getElementsByTagName("artifactId");
            if (artifactIdNodes.getLength() != 1) {
                return null;
            }
            NodeList versionNodes = root.getElementsByTagName("version");
            if (versionNodes.getLength() != 1) {
                return null;
            }
            String groupId = groupIdNodes.item(0).getTextContent();
            String artifactId = artifactIdNodes.item(0).getTextContent();
            String version = versionNodes.item(0).getTextContent();
            return new MavenDependency(
                groupId,
                artifactId,
                version);
    }

    public static void main(String[] args) {
        try {
            File file = new File(args[0]);
            MavenDependencyConverter converter = new MavenDependencyConverter(file);
            MavenDependency dependency = converter.parse();
            if (dependency == null) {
                logger.error("Malformed dependency");
                return;
            }
            logger.info(dependency.asMavenJarStatement());
        } catch (Exception e) {
            logger.error("Could not parse file", e);
        }
    }
}
