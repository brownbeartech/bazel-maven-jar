package src;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MavenDependencyConverter {
    static final String BAD_DEP_ERROR = "Malformed dependency";
    static final String MAVEN_JAR =
        "maven_jar(\n" +
        "    name = \"%s\",\n" +
        "    artifact = \"%s:%s:%s\"\n" +
        ")";
    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File file = new File(args[0]);
            Document doc = builder.parse(file);
            Element root = doc.getDocumentElement();
            NodeList groupIdNodes = root.getElementsByTagName("groupId");
            if (groupIdNodes.getLength() != 1) {
                System.out.println(BAD_DEP_ERROR);
                return;
            }
            NodeList artifactIdNodes = root.getElementsByTagName("artifactId");
            if (artifactIdNodes.getLength() != 1) {
                System.out.println(BAD_DEP_ERROR);
                return;
            }
            NodeList versionNodes = root.getElementsByTagName("version");
            if (versionNodes.getLength() != 1) {
                System.out.println(BAD_DEP_ERROR);
                return;
            }
            String groupId = groupIdNodes.item(0).getTextContent();
            String artifactId = artifactIdNodes.item(0).getTextContent();
            String version = versionNodes.item(0).getTextContent();
            String name = groupId.replaceAll("\\.", "_") + "_" + artifactId;
            System.out.println(String.format(MAVEN_JAR, name, groupId, artifactId, version));
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.out.println(sw.toString());
        }
    }
}
