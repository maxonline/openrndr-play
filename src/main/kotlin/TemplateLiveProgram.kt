import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.LineCap
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.Vector2
import org.openrndr.shape.Circle
import org.openrndr.shape.Shape
import org.openrndr.shape.contains
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


fun main() = application {
    configure {
        width = 800
        height = 800
    }
    val points: MutableList<Vector2> = mutableListOf()
    println("Started application")

    oliveProgram {
        println("Started oliveProgram")

        extend {
            drawer.clear(ColorRGBa.BLACK)
            drawer.stroke = ColorRGBa.GREEN
            drawer.strokeWeight = 5.0
            drawer.lineCap = LineCap.ROUND

            drawer.fill = ColorRGBa.YELLOW
            val angle = seconds * 2 * PI.toFloat() / 8f // seconds for full circle
            val bounds = Circle(
                x = width / 2 + 300 * cos(angle),
                y = height / 2 + 300 * sin(angle),
                100.0
            )
            if (frameCount == 0) {
                points.add(bounds.center.copy())
            }

            drawer.circle(bounds)
            val newPoint = points.last().getNewPointInRandomDirection(30.0, bounds.shape)

            points.add(newPoint)
            if (points.size > 100) {
                points.removeFirst()
            }

            drawer.lineStrip(points)
        }
    }
}

fun Vector2.getNewPointInRandomDirection(distance: Double, shape: Shape): Vector2 {
    var newPoint: Vector2?
    do {
        val randomAngle = Random.nextDouble() * 2.0 * PI
        val direction = Vector2(cos(randomAngle), sin(randomAngle))
        val newDirection = direction.normalized
        val displacement = newDirection * distance
        newPoint = this + displacement
    } while (!shape.contains(newPoint!!))
    return newPoint
}


