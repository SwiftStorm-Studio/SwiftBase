import cl.franciscosolis.sonatypecentralupload.SonatypeCentralUploadTask
import java.io.FileInputStream
import java.util.*

dependencies {
    implementation("net.kyori:adventure-api:4.17.0")
}

val localProperties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

publishing {
    publications {
        // Core用のパッケージ
        create<MavenPublication>("core") {
            groupId = rootProject.group.toString()
            artifactId = "${rootProject.name}-core"
            version = rootProject.version.toString()

            from(components["java"])

            pom {
                name.set("SwiftBase Core")
                description.set("The core module of SwiftBase, used as a common library for multiple platforms")
                url.set("https://github.com/SwiftStorm-Studio/SwiftBase")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/license/mit")
                    }
                }
                developers {
                    developer {
                        id.set("lars")
                        name.set("Lars")
                        email.set("main@rk4z.net")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/SwiftStorm-Studio/SwiftBase.git")
                    developerConnection.set("scm:git:ssh://github.com/SwiftStorm-Studio/SwiftBase.git")
                    url.set("https://github.com/SwiftStorm-Studio/SwiftBase")
                }
            }
        }
    }
}

tasks.named<SonatypeCentralUploadTask>("sonatypeCentralUpload") {
    dependsOn("clean", "jar", "sourcesJar", "javadocJar", "generatePomFileForCorePublication")

    username = localProperties.getProperty("cu")
    password = localProperties.getProperty("cp")

    archives = files(
        tasks.named("jar"),
        tasks.named("sourcesJar"),
        tasks.named("javadocJar"),
    )

    pom = file(
        tasks.named("generatePomFileForCorePublication").get().outputs.files.single()
    )

    signingKey = localProperties.getProperty("signing.key")
    signingKeyPassphrase = localProperties.getProperty("signing.passphrase")
}