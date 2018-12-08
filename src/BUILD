package(default_visibility = ["//visibility:public"])

java_library(
    name = "logging",
    exports = [
        "@commons_logging//jar",
        "@org_slf4j_api//jar",
        "@org_slf4j_simple//jar",
        "@org_apache_httpcomponents_core//jar",
        "@org_apache_httpcomponents_client//jar",
        "@org_apache_logging_log4j_api//jar",
        "@org_apache_logging_log4j_core//jar",
    ]
)

filegroup(
    name = "resources",
    srcs = glob(["resources/*.xml"]),
)

java_binary(
    name = "MavenDependencyConverter",
    srcs = [
        "MavenDependencyConverter.java",
        "MavenDependency.java"
    ],
    deps = [":logging"],
    main_class = "src.MavenDependencyConverter",
    classpath_resources = [":resources"],
)

filegroup(
    name = "maven_deps",
    srcs = glob(["maven/*.xml"]),
)

java_test(
    name = "MavenDependencyConverterTest",
    size = "small",
    srcs = [
        "MavenDependencyConverterTest.java",
    ],
    test_class = "src.MavenDependencyConverterTest",
    deps = [
        ":logging",
        ":MavenDependencyConverter",
        "@tech_brownbear_resources//src:resources",
        "@junit//jar",
    ],
    resources = [
        ":maven_deps",
    ],
    classpath_resources = [":resources"],
)