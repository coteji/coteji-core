import io.github.coteji.tests.sources.*
import io.github.coteji.tests.targets.FakeTarget

val source = FakeSource(
        component = "users"
)

setSource(source)
setTarget(FakeTarget())
