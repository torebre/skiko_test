import com.kjipo.PathGenerator
import com.kjipo.PathGeneratorInterface
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Color
import org.jetbrains.skia.Paint
import org.jetbrains.skiko.GenericSkikoView
import org.jetbrains.skiko.SkiaLayer
import org.jetbrains.skiko.SkikoView
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants


object MainView {
    private val skiaLayer = SkiaLayer()
    private val pathGenerate: PathGeneratorInterface = PathGenerator()


    fun run() {
        skiaLayer.skikoView = GenericSkikoView(skiaLayer, object : SkikoView {

            override fun onRender(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
                renderFrame(canvas, width, height, nanoTime)
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


    private fun renderFrame(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
        val paint = Paint()
        canvas.clear(Color.CYAN)
        val ts = nanoTime / 5_000_000
        canvas.drawCircle((ts % width).toFloat(), (ts % height).toFloat(), 20f, paint)

        for(circle in pathGenerate.getNextFrame()) {

            canvas.drawCircle(circle.xPoint.toFloat(), circle.yPoint.toFloat(), circle.radius.toFloat(), paint)

//            paint.strokeWidth = 2 * cylinderEndpoints.radius.toFloat()
//            canvas.drawLine(
//                cylinderEndpoints.xStart.toFloat(), cylinderEndpoints.yStart.toFloat(),
//                cylinderEndpoints.xStop.toFloat(), cylinderEndpoints.yStop.toFloat(), paint
//            )

        }

    }

}

fun main() {
    MainView.run()

}