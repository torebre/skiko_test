package com.kjipo

import kotlin.math.abs
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertTrue

class TestBisectMethod {


    @Test
    fun testBisectMethodConverges() {
        val functionToMinimize = { value: Double ->
            value.pow(4) - 3 * value.pow(2) + value + 1
        }

        val x = CylinderIntersectionUtilities.bisect(
            functionToMinimize,
            -2.0,
            0.0,
            2.0
        )

//        println("Input: $x. Value: ${functionToMinimize(x)}")

        assertTrue { abs(x - 0.169938) < 0.0001 }
        assertTrue { abs(functionToMinimize(x) - 1.08414) < 0.0001 }
    }

}