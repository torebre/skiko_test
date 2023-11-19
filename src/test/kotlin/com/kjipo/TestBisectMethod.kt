package com.kjipo

import kotlin.math.pow
import kotlin.test.Test

class TestBisectMethod {


    @Test
    fun testBisectMethodConverges() {

        val functionToMinimize = { value: Double ->
            value.pow(4) - 3 * value.pow(2) + value + 1
        }

        val value = CylinderIntersectionUtilities.bisect(functionToMinimize,
            -2.0,
            0.0,
            2.0
            )

        println("Value: $value")


    }


}