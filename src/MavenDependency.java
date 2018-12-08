package src;

public class MavenDependency {
    static final String MAVEN_JAR =
        "maven_jar(\n" +
        "    name = \"%s\",\n" +
        "    artifact = \"%s:%s:%s\"\n" +
        ")";

    private final String groupId;
    private final String artifactId;
    private final String version;

    public MavenDependency(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public String getName() {
        return groupId.replaceAll("\\.", "_") + "_" + artifactId;
    }

    public String asMavenJarStatement() {
        return String.format(MAVEN_JAR, getName(), groupId, artifactId, version);
    }
}
