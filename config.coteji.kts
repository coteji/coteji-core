import org.coteji.sources.JavaCodeSource
import org.coteji.targets.JiraTarget

val source = JavaCodeSource(
        testsDir = "path/to/dir"
)

setSource(source)
setTarget(JiraTarget())
