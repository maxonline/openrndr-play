import org.openrndr.*
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.Vector2
import org.openrndr.shape.Rectangle

enum class ArrowKey { DOWN, UP, LEFT, RIGHT }

var arrow: ArrowKey? = null

val windowRect = Rectangle(0.0, 0.0, 1000.0, 800.0)

var playerPosition = windowRect.center

val points = createGrid(100, 80, 10.0)

val dx = Vector2(2.0, 0.0)
val dy = Vector2(0.0, 2.0)

fun main() = application {
    configure {
        width = windowRect.width.toInt()
        height = windowRect.height.toInt()
    }

    oliveProgram {
        var lastFrameCount = 0
        var lastTime = System.currentTimeMillis()
        var diplayFps = 0

        extend {

            when (arrow) {
                ArrowKey.DOWN -> playerPosition += dy
                ArrowKey.UP -> playerPosition -= dy
                ArrowKey.LEFT -> playerPosition -= dx
                ArrowKey.RIGHT -> playerPosition += dx
                null -> ""
            }
            drawer.fill = ColorRGBa.GREEN
            drawer.stroke = null
            drawer.circle(playerPosition, 10.0)

            drawer.text(arrow?.name ?: "null", 10.0, 20.0)
            drawer.text(playerPosition.toString(), 10.0, 40.0)
            if (System.currentTimeMillis() - lastTime > 1000) {
                diplayFps = frameCount - lastFrameCount
                lastFrameCount = frameCount;
                lastTime = System.currentTimeMillis()
            }
            drawer.text(diplayFps.toString().take(5), 10.0, 60.0)

            drawer.fill = ColorRGBa.WHITE
            drawer.points(points)
        }
        keyboard.keyDown.listen {
            when (it.key) {
                KEY_ARROW_UP -> arrow = ArrowKey.UP
                KEY_ARROW_DOWN -> arrow = ArrowKey.DOWN
                KEY_ARROW_LEFT -> arrow = ArrowKey.LEFT
                KEY_ARROW_RIGHT -> arrow = ArrowKey.RIGHT
            }
        }
        keyboard.keyUp.listen {
            when (it.key) {
                KEY_ARROW_UP -> if (arrow == ArrowKey.UP) arrow = null
                KEY_ARROW_DOWN -> if (arrow == ArrowKey.DOWN) arrow = null
                KEY_ARROW_LEFT -> if (arrow == ArrowKey.LEFT) arrow = null
                KEY_ARROW_RIGHT -> if (arrow == ArrowKey.RIGHT) arrow = null
            }
        }
    }
}

fun createGrid(xSize: Int, ySize: Int, spacing: Double) =
    List(xSize) { x -> List(ySize) { y -> Vector2(x * spacing, y * spacing) } }.flatten()