import org.openrndr.KEY_ESCAPE
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.ColorBufferShadow
import org.openrndr.draw.Drawer
import org.openrndr.draw.loadImage
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.ffmpeg.ScreenRecorder
import org.openrndr.math.Vector2
import org.openrndr.shape.*

/**
 *  This is a template for a live program.
 *
 *  It uses oliveProgram {} instead of program {}. All code inside the
 *  oliveProgram {} can be changed while the program is running.
 */

data class Hej(val va: String)

val windowRect = Rectangle(0.0, 0.0, 768.0, 576.0)
val balls = mutableListOf<Ball>()


fun main() = application {
    for (x in 30..(windowRect.width.toInt()) - 30 step 60) {
        for (y in 30..(windowRect.height.toInt()) - 30 step 60) {
            balls.add(
                Ball(
                    Vector2(x.toDouble(), y.toDouble()),
                    Vector2(Math.random(), Math.random()).normalized * 2.0
                )
            )
        }
    }

    configure {
        width = windowRect.width.toInt()
        height = windowRect.height.toInt()
    }

    oliveProgram {
        val testbild = loadImage("data/images/pm5544.png")
        val sh = testbild.shadow.apply { download() }
        val recorder = ScreenRecorder().apply {
            outputToVideo = false
        }
        extend(recorder)
        extend {
            drawer.clear(ColorRGBa.WHITE)
            drawer.image(testbild)
            balls.forEach { it.update(windowRect, drawer, sh) }
        }
        keyboard.keyDown.listen {
            when {
                it.key == KEY_ESCAPE -> program.application.exit()
                it.name == "v" -> {
                    recorder.outputToVideo = !recorder.outputToVideo
                    println(if (recorder.outputToVideo) "Recording" else "Paused")
                }
            }
        }
    }
}

class Ball(var position: Vector2, var velocity: Vector2) {
    private val radius = 17.0
    fun update(windowRect: Rectangle, drawer: Drawer, sh: ColorBufferShadow) {
        if (position.x < radius || position.x > windowRect.width - radius) {
            velocity = velocity.copy(x = -velocity.x)
        }
        if (position.y < radius || position.y > windowRect.height - radius) {
            velocity = velocity.copy(y = -velocity.y)
        }
        position += velocity
        val colorFromTestbild = if (windowRect.contains(this.position)) {
            sh[(this.position.x).toInt(), (this.position.y).toInt()]
        } else {
            ColorRGBa.BLACK
        }
        drawer.fill = colorFromTestbild
        drawer.stroke = null
        drawer.circle(position, radius)
    }
}