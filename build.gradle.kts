plugins {
    `java-library`
    `maven-publish`
}

group = "dev.lookforfps.oja"
description = "OpenAI Java API"
version = Version("1", "2", "1")

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jackson.databind)
    implementation(libs.okhttp)
    implementation(libs.slf4j)

    compileOnly(libs.lombok)

    annotationProcessor(libs.lombok)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            artifactId = project.name
            groupId = project.group as String
            version = project.version.toString()

            pom {
                name.set(project.name)
                description.set(project.description)
                url.set("https://github.com/LookforFPS/OJA")
                inceptionYear.set("2023")

                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("http://opensource.org/licenses/MIT")
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set("lookforfps")
                        name.set("LookforFPS")
                        email.set("me@lookforfps.dev")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/LookforFPS/OJA")
                    developerConnection.set("scm:git:ssh:git@github.com:LookforFPS/OJA")
                    url.set("https://github.com/LookforFPS/OJA")
                }
            }
        }
    }

    repositories {
        maven {
            name = "nexus"
            url = uri("https://repo.lookforfps.dev/repository/maven-releases/")
            credentials {
                username = project.findProperty("user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("token") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}


data class Version(
    val major: String,
    val minor: String,
    val revision: String,
    val classifier: String? = null
) {
    override fun toString(): String {
        return "$major.$minor.$revision${classifier?.let { "-$it" } ?: ""}"
    }
}