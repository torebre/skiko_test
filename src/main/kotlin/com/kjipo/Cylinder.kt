package com.kjipo


/**
 * @param c0Vector is the center point of the cylinder
 * @param w0Vector is the unit length axis direction
 * @param r0 is the radius of the cylinder
 * @param h0 is the height of the cylinder
 *
 */
data class Cylinder(val c0Vector: Vector3,
                    val w0Vector: Vector3,
                    val r0: Double,
                    val h0: Double)