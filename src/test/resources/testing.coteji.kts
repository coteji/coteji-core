@file:DependsOn("org.apache.commons:commons-compress:1.21")
import org.apache.commons.compress.archivers.ArchiveStreamFactory
val factory = ArchiveStreamFactory()
println("it works!")