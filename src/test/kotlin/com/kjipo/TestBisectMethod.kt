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

        // This is a local maximum
        assertTrue { abs(x - 0.169938) < 0.0001 }
        assertTrue { abs(functionToMinimize(x) - 1.08414) < 0.0001 }
    }

    @Test
    fun testGetBracketdMinimumMethodConverges() {
        val functionToMinimize = { value: Double ->
            value.pow(4) - 3 * value.pow(2) + value + 1
        }

        val aPair = Pair(-2.0, functionToMinimize(-2.0))
        val bPair = Pair(0.0, functionToMinimize(0.0))
        val cPair = Pair(2.0, functionToMinimize(2.0))
        val currentMin = listOf(aPair, bPair, cPair).maxBy { it.second }

        val (x, fx) = CylinderIntersectionUtilities.getBracketedMinimum(
            functionToMinimize,
            aPair,
            bPair,
            cPair,
            currentMin
        )

        assertTrue { abs(x + 1.3008) < 0.0001 }
        assertTrue { abs(fx + 2.5139) < 0.0001 }
    }

    @Test
    fun testBisect2MethodConverges() {
        val functionToMinimize = { value: Double ->
            value.pow(4) - 3 * value.pow(2) + value + 1
        }

        val (x, fx) = CylinderIntersectionUtilities.bisect2(
            functionToMinimize,
            -2.0,
            0.0,
            2.0
        )

        assertTrue { abs(x + 1.3008) < 0.0001 }
        assertTrue { abs(fx + 2.5139) < 0.0001 }
    }

}