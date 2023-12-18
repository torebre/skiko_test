import com.kjipo.PathGenerator
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Color
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect
import org.jetbrains.skiko.GenericSkikoView
import org.jetbrains.skiko.SkiaLayer
import org.jetbrains.skiko.SkikoView
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

fun main() {
    val skiaLayer = SkiaLayer()
    val pathGenerate = PathGenerator()

    val cylinder = pathGenerate.generateCylinder()
    val cylinderEndpoints = PathGenerator.getXYEndpointsOfCylinder(cylinder)

    skiaLayer.skikoView = GenericSkikoView(skiaLayer, object : SkikoView {
        val paint = Paint().apply {
            color = Color.RED
        }
        override fun onRender(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
            canvas.clear(Color.CYAN)
            val ts = nanoTime / 5_000_000
            canvas.drawCircle( (ts % width).toFloat(), (ts % height).toFloat(), 20f, paint )

            val paint = Paint()
            paint.strokeWidth = 2 * cylinderEndpoints.radius.toFloat()

            canvas.drawLine(cylinderEndpoints.xStart.toFloat(), cylinderEndpoints.yStart.toFloat(),
                cylinderEndpoints.xStop.toFloat(), cylinderEndpoints.yStop.toFloat(), paint)

        }
    })
    SwingUtilities.invokeLater {
        val window = JFrame("Skiko example").apply {
            defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            preferredSize = Dimension(800, 600)
        }
        skiaLayer.attachTo(window.contentPane)
        skiaLayer.needRedraw()
        window.pack()
        window.isVisible = true
    }
}