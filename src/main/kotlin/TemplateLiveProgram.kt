import org.openrndr.*
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.ffmpeg.ScreenRecorder
import org.openrndr.math.Vector2
import org.openrndr.shape.Rectangle
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

enum class ArrowKey { DOWN, UP, LEFT, RIGHT }

var arrow: ArrowKey? = null

val windowRect = Rectangle(0.0, 0.0, 1000.0, 800.0)

var playerPosition = Vector2(0.0, 0.0)

val points = createGrid(70, 60, 10.0)

val dx = Vector2(3.0, 0.0)
val dy = Vector2(0.0, 3.0)

fun main() = application {
    configure {
        width = windowRect.width.toInt()
        height = windowRect.height.toInt()
    }

    oliveProgram {
        var lastFrameCount = 0
        var lastTime = System.currentTimeMillis()
        var diplayFps = 0

        extend(ScreenRecorder())
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
            drawer.circle(windowRect.center, 10.0)

            drawer.text(arrow?.name ?: "null", 10.0, 20.0)
            drawer.text(playerPosition.toString(), 10.0, 40.0)
            if (System.currentTimeMillis() - lastTime > 1000) {
                diplayFps = frameCount - lastFrameCount
                lastFrameCount = frameCount;
                lastTime = System.currentTimeMillis()
            }
            drawer.text(diplayFps.toString().take(5), 10.0, 60.0)

            drawer.points{
                points.forEach{
                    val screenPosition =  Vector2(it.x - playerPosition.x, it.y - playerPosition.y)
                    val windowsDistanceToPlayer = screenPosition.distanceTo(windowRect.center)
                    val factor = windowsDistanceToPlayer / 500
                    val minusFactor = factor * -1.0 + 1.0
                    fill = ColorRGBa.WHITE
                    val angle = getAngleBetweenPoints(screenPosition, windowRect.center)
                    val newPoint = getPointAtDistanceAndAngle(windowRect.center, angle, windowsDistanceToPlayer*windowsDistanceToPlayer/100)
                    point(newPoint)
                }
            }
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

fun getAngleBetweenPoints(point1: Vector2, point2: Vector2): Double {
    val deltaX = point2.x - point1.x
    val deltaY = point2.y - point1.y
    return atan2(deltaY, deltaX)
}

fun getPointAtDistanceAndAngle(point: Vector2, angle: Double, distance: Double): Vector2 {
    val newX = point.x + distance * cos(angle)
    val newY = point.y + distance * sin(angle)
    return Vector2(newX, newY)
}

fun createGrid(xSize: Int, ySize: Int, spacing: Double) =
    List(xSize) { x -> List(ySize) { y -> Vector2(x * spacing, y * spacing) } }.flatten()