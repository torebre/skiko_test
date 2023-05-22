package com.kjipo

import kotlin.math.pow
import kotlin.math.sqrt


class Vector3(val x: Double, val y: Double, val z: Double) {


    operator fun plus(vector: Vector3): Vector3 {
        return Vector3(x + vector.x, y + vector.y, z + vector.z)
    }

    operator fun minus(vector: Vector3): Vector3 {
        return Vector3(x - vector.x, y - vector.y, z - vector.z)
    }

    operator fun times(scalar: Double): Vector3 {
        return Vector3(scalar * x, scalar * y, scalar * z)
    }

    operator fun div(scalar: Double): Vector3 {
        return Vector3(scalar * x, scalar * y, scalar * z)
    }

    fun dot(vector: Vector3): Double {
        return x * vector.x + y * vector.y + z * vector.z
    }

    fun crossProduct(vector: Vector3): Vector3 {
        return Vector3(y * vector.z - z * vector.y,
            z * vector.x - x * vector.z,
            x * vector.y - y * vector.x)
    }

    fun length(): Double {
        return sqrt(x.pow(2) + y.pow(2) + z.pow(2))
    }

    fun normalize(): Vector3 {
        val length = length()

        return Vector3(x / length, y / length, z / length)
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vector3

        if (x != other.x) return false
        if (y != other.y) return false
        return z == other.z
    }

}