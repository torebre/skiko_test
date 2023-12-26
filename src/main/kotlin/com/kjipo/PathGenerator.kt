package com.kjipo

import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class PathGenerator(
    private val xMin: Double = 0.0,
    private val xMax: Double = 100.0,
    private val yMin: Double = 0.0,
    private val yMax: Double = 100.0
) : PathGeneratorInterface {

    private val cylinderHeight = 1000.0

    private var currentTime = -1.0


    private val cylindersToRender = mutableListOf<RenderCylinder>()


    init {
        cylindersToRender.add(RenderCylinder(generateCylinder()))
        cylindersToRender.add(RenderCylinder(generateCylinder()))
    }


    fun generateCylinder(): Cylinder {
        val xCoord = Random.nextDouble(xMin, xMax)
        val yCoord = Random.nextDouble(yMin, yMax)
        val timeCoord = Random.nextDouble()

        val polar = Random.nextDouble(0.0, Math.PI)
        val alpha = Random.nextDouble(0.0, Math.PI)

        return Cylinder(
            Vector3(xCoord, yCoord, timeCoord),
            vectorFromPolarCoordinates(polar, alpha, cylinderHeight),
            50.0, cylinderHeight
        )
    }

    override fun getNextFrame(): List<Circle> {
        ++currentTime
        return cylindersToRender.map { cylinderToRender ->
            cylinderToRender.getCircle(currentTime)
        }
    }


    companion object {

        fun getXYEndpointsOfCylinder(cylinder: Cylinder): CylinderEndpoints {
            return CylinderEndpoints(
                cylinder.c0Vector.x - cylinder.h0 / 2.0 * cylinder.w0Vector.x,
                cylinder.c0Vector.x + cylinder.h0 / 2.0 * cylinder.w0Vector.x,
                cylinder.c0Vector.y - cylinder.h0 / 2.0 * cylinder.w0Vector.y,
                cylinder.c0Vector.y + cylinder.h0 / 2.0 * cylinder.w0Vector.y,
                cylinder.r0
            )
        }

        private fun vectorFromPolarCoordinates(polar: Double, alpha: Double, cylinderHeight: Double): Vector3 {
            return Vector3(
                cylinderHeight * sin(polar) * cos(alpha),
                cylinderHeight * sin(polar) * sin(alpha),
                cylinderHeight * cos(polar)
            ).normalize()
        }


    }

}


data class CylinderEndpoints(
    val xStart: Double, val xStop: Double,
    val yStart: Double, val yStop: Double,
    val radius: Double
)

data class Circle(val xPoint: Double, val yPoint: Double, val radius: Double)


private class RenderCylinder(private val cylinder: Cylinder) {

    fun getCircle(timeStep: Double): Circle {
        val zFactor = 1.0 / cylinder.w0Vector.z

        return Circle(
            timeStep * zFactor * cylinder.w0Vector.x + cylinder.c0Vector.x,
            timeStep * zFactor * cylinder.w0Vector.y + cylinder.c0Vector.y, cylinder.r0
        )
    }

}