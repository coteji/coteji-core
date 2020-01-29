import org.coteji.sources.*
import org.coteji.targets.JiraTarget

val source = JavaCodeSource(
        testsDir = "path/to/dir",
        isTest = method {
            withAnnotation("Test")
            thatHasAttribute("groups")
            thatContainsText("TEAM")
        }
)

setSource(source)
setTarget(JiraTarget())
