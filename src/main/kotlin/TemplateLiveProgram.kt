import org.openrndr.*
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.ColorBufferShadow
import org.openrndr.draw.Drawer
import org.openrndr.draw.loadImage
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.ffmpeg.ScreenRecorder
import org.openrndr.math.Vector2
import org.openrndr.shape.*
enum class ArrowKey {
    DOWN,
    UP,
    LEFT,
    RIGHT
}

var arrow: ArrowKey? = null

val windowRect = Rectangle(0.0, 0.0, 1000.0, 800.0)

var playerPosition = windowRect.center.copy()

val points = createGrid(50, 40, 10.0)

fun main() = application {
    configure {
        width = windowRect.width.toInt()
        height = windowRect.height.toInt()
    }

    oliveProgram {
        var fps = 0.0
        var lastTime = System.currentTimeMillis()

        extend {
            val currentTime = System.currentTimeMillis()
            val deltaTime = (currentTime - lastTime) / 1000.0
            fps = 1.0 / deltaTime
            lastTime = currentTime

            when (arrow){
                ArrowKey.DOWN -> playerPosition = playerPosition.copy(y = playerPosition.y+2)
                ArrowKey.UP -> playerPosition = playerPosition.copy(y = playerPosition.y-2)
                ArrowKey.LEFT -> playerPosition = playerPosition.copy(x = playerPosition.x-2)
                ArrowKey.RIGHT -> playerPosition = playerPosition.copy(x = playerPosition.x+2)
                null -> ""
            }
            drawer.fill = ColorRGBa.GREEN
            drawer.stroke = null
            drawer.circle(playerPosition, 10.0)

            drawer.text(arrow?.name ?: "null", 10.0, 10.0)
            drawer.text(playerPosition.toString(), 10.0, 30.0)
            drawer.text(fps.toString(), 10.0, 60.0)

            drawer.fill = ColorRGBa.WHITE
            points.forEach{
                drawer.point(it)
            }


        }
        keyboard.keyDown.listen {
            when {
                it.key == KEY_ARROW_UP -> arrow = ArrowKey.UP
                it.key == KEY_ARROW_DOWN -> arrow = ArrowKey.DOWN
                it.key == KEY_ARROW_LEFT -> arrow = ArrowKey.LEFT
                it.key == KEY_ARROW_RIGHT -> arrow = ArrowKey.RIGHT

            }
        }
        keyboard.keyUp.listen {
            when {
                it.key == KEY_ARROW_UP -> if (arrow == ArrowKey.UP) arrow = null
                it.key == KEY_ARROW_DOWN -> if (arrow == ArrowKey.DOWN) arrow = null
                it.key == KEY_ARROW_LEFT -> if (arrow == ArrowKey.LEFT) arrow = null
                it.key == KEY_ARROW_RIGHT -> if (arrow == ArrowKey.RIGHT) arrow = null

            }
        }
    }
}

fun createGrid(xSize: Int, ySize: Int, spacing: Double): List<Vector2> {
    val points = mutableListOf<Vector2>()

    for (i in 0 until xSize) {
        for (j in 0 until ySize) {
            val x = i * spacing
            val y = j * spacing
            val point = Vector2(x, y)
            points.add(point)
        }
    }

    return points
}