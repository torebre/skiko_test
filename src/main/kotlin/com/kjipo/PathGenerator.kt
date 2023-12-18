package com.kjipo

import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class PathGenerator {
    val xMin = 0.0
    val xMax = 100.0
    val yMin = 0.0
    val yMax = 100.0

    val cylinderHeight = 1000.0


    fun generateCylinder(): Cylinder {
        val xCoord = Random.nextDouble(xMin, xMax)
        val yCoord = Random.nextDouble(yMin, yMax)
        val timeCoord = Random.nextDouble()

        val polar = Random.nextDouble(0.0, Math.PI)
        val alpha = Random.nextDouble(0.0, Math.PI)

        return Cylinder(
            Vector3(xCoord, yCoord, timeCoord),
            vectorFromPolarCoordinates(polar, alpha),
            1.0, cylinderHeight
        )
    }

    private fun vectorFromPolarCoordinates(polar: Double, alpha: Double): Vector3 {
        return Vector3(
            cylinderHeight * sin(polar) * cos(alpha),
            cylinderHeight * sin(polar) * sin(alpha),
            cylinderHeight * cos(polar)
        ).normalize()
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

    }

}


data class CylinderEndpoints(
    val xStart: Double, val xStop: Double,
    val yStart: Double, val yStop: Double,
    val radius: Double
)