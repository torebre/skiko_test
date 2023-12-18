package com.kjipo

import kotlin.math.*


class CylinderIntersectionUtilities {

    var pVector = Vector3(0.0, 0.0, 0.0)
    var lVector = Vector3(0.0, 0.0, 0.0)
    var r0 = 0.0
    var r1 = 0.0
    var w0Vector = Vector3(0.0, 0.0, 0.0)
    var w1Vector = Vector3(0.0, 0.0, 0.0)
    var h0 = 0.0
    var h1 = 0.0
    var w0CrossW1 = Vector3(0.0, 0.0, 0.0)

    var q0Vector = Vector3(0.0, 0.0, 0.0)
    var q1Vector = Vector3(0.0, 0.0, 0.0)

    val numLines = 20


    /**
     * Compute one-sided limits at t = 0 for any line
     */
    fun limitsDgdtZero(
//        r0: Double, r1: Double,
//        pVector: Vector3, w0Vector: Vector3,
//        lVector: Vector3, w1Vector: Vector3
    ): Pair<Double, Double> {
        val r0Term = r0 * pVector.crossProduct(w0Vector)
            .dot(lVector.crossProduct(w0Vector))
            .div(pVector.crossProduct(w0Vector).length())
        val r1Term = r1 * pVector.crossProduct(w1Vector)
            .dot(lVector.crossProduct(w1Vector))
            .div(pVector.crossProduct(w1Vector).length())

        val h0Term = (h0 / 2) * lVector.dot(w0Vector).absoluteValue
        val h1Term = (h1 / 2) * lVector.dot(w1Vector).absoluteValue

        val dgdt0n = r0Term + r1Term - (h0Term + h1Term)
        val dgdt0p = r0Term + r1Term - (h0Term + h1Term)

        return Pair(dgdt0n, dgdt0p)
    }


    /**
     * Compute one-sided limits at t = 1 for line P + t(Q_0 - P) where Q_0 cross W_0 = 0 with Q_0 last component 1
     */
    fun limitsDgdtOneQ0(
        r0: Double, r1: Double, pVector: Vector3, w0Vector: Vector3,
        lVector: Vector3, w1Vector: Vector3, q0Vector: Vector3, q1Vector: Vector3
    ): Pair<Double, Double> {
        val r0Term = r0 * lVector.crossProduct(w0Vector).length()
        val r1Term = r1 * q0Vector.crossProduct(w1Vector).dot(lVector.crossProduct(w1Vector))
            .div(q1Vector.crossProduct(w0Vector).length())
        val h0Term = (h0 / 2) * lVector.dot(w0Vector).absoluteValue
        val h1Term = (h1 / 2) * lVector.dot(w1Vector).absoluteValue

        val dgdt1n = -r0Term + r1Term + h0Term + h1Term
        val dgdt1p = r0Term + r1Term + h1Term + h1Term

        return Pair(dgdt1n, dgdt1p)
    }


    /**
     * Compute one-sided limits at t = 1 for line P + t(Q_1 - P)
     * where Q_1 cross W1 = 0 with Q_1 last component 1
     */
    fun limitsDgdtOneQ1(q1Vector: Vector3): Pair<Double, Double> {
        val r0Term =
            r0 * q1Vector.crossProduct(w0Vector).dot(lVector.crossProduct(w0Vector)) / q1Vector.crossProduct(w0Vector)
                .length()
        val r1Term = r1 * lVector.crossProduct(w1Vector).length()
        val h0Term = (h0 / 2) * lVector.dot(w0Vector).absoluteValue
        val h1Term = (h1 / 2) * lVector.dot(w1Vector).absoluteValue

        val dgdt1n = r0Term - r1Term + h0Term + h1Term
        val dgdt1p = r0Term + r1Term + h0Term + h1Term

        return Pair(dgdt1n, dgdt1p)
    }


    /**
     * Compute one-sided limit g'(+infinity) with g'(-infinity) = -g'(+infinity)
     */
    fun limitsDgdtInfinity(dgdtInfinity: Double, w0Vector: Vector3, w1Vector: Vector3): Double {
        val r0Term = r0 * lVector.crossProduct(w0Vector).length()
        val r1Term = r1 * lVector.crossProduct(w1Vector).length()
        val h0Term = (h0 / 2) * lVector.dot(w0Vector).absoluteValue
        val h1Term = (h1 / 2) * lVector.dot(w1Vector).absoluteValue

        val dgdtInfinity = r0Term + r1Term + h0Term + h1Term

        return dgdtInfinity
    }


    fun computeMinimumSingularZero(dgdt0n: Double, dgdt0p: Double): Double {
        var t0 = -1.0

        val tMin = if (dgdt0n > 0) {
            var dgdtT0 = 0.0

            for (i in 0 until imax) {
                dgdtT0 = dgdt(t0)
                if (dgdtT0 < 0) {
                    break
                }
                t0 *= 2
            }

            bisect({ value -> dgdt(value) }, t0, t0 / 2, dgdtT0)
        } else if (dgdt0p < 0) {
            var dgdtT1 = 0.0
            var t1 = 1.0

            for (i in 0 until imax) {
                dgdtT1 = dgdt(t1)
                if (dgdtT1 > 0) {
                    break
                }
                t1 *= 2
            }

            bisect({ value -> dgdt(value) }, dgdtT1, t1 / 2, t1)
        } else {
            0.0
        }

        return tMin
    }

    fun computeMinimumSingularZeroOne(
        dgdt0n: Double, dgdt0p: Double,
        dgdt1n: Double, dgdt1p: Double
    ): Double {
        val tMin = if (dgdt0n > 0) {
            // The root of g'(t) occurs on (-infinity, 0). Locate t_0 for which g'(t_0) < 0
            var t0 = -1.0
            var t1 = 0.0
            var dgdtT0 = 0.0
            var dgdtTmin = 0.0

            for (i in 0 until imax) {
                val dgdtT0 = dgdt(t0)
                if (dgdtT0 < 0) {
                    break
                }
                t0 *= 2
            }
            bisect({ value -> dgdt(value) }, t0, t0 / 2, 0.0)


        } else if (dgdt1p < 0) {
            val t0 = 1.0
            var t1 = 2.0

            for (i in 0 until imax) {
                var dgdtT1 = dgdt(t1)

                if (dgdtT1 > 0) {
                    break
                }
                t1 *= 2
            }

            // TODO Check that the limits are correct
            bisect({ value -> dgdt(value) }, 0.0, t1 / 2, t1)
        } else {
            // At this time g'(0-) <= 0 <= g'(1+)
            if (dgdt0p < 0) {
                if (dgdt1n > 0) {
                    // The root of g'(t) occurs on (0, 1)
//                    var t0 = 0.0
//                    var t1 = 1.0
                    bisect({ value -> dgdt(value) }, 0.0, 0.5, 1.0)
                } else {
                    // The minimum of g(t) occurs on (0, 1)
                    1.0
                }
            } else {
                0.0
            }
        }

        return tMin

    }


    /**
     * Evaluation of g(t) for all t
     */
    fun gFunction(
        t: Double
//        , pVector: Vector3, lVector: Vector3,
//        r0: Double, r1: Double,
//        w0: Vector3, w1: Vector3,
//        h0: Double, h1: Double
    ): Double {
        val dVector = pVector + lVector * t
        val r0Term = r0 * dVector.crossProduct(w0Vector).length()
        val r1Term = r1 * dVector.crossProduct(w1Vector).length()

        val h0Term = (h0 / 2) * dVector.dot(w0Vector).absoluteValue
        val h1Term = (h1 / 2) * dVector.dot(w1Vector).absoluteValue

        return r0Term + r1Term + h0Term + h1Term
    }


    /**
     * Evaluation of g'(t) except at singularity t = 0 and
     * potential singularity t = 1
     */
    fun dgdt(
        t: Double
//        , pVector: Vector3, lVector: Vector3,
//        r0: Double, r1: Double, w0: Vector3, w1: Vector3,
//        h0: Double, h1: Double
    ): Double {
        val dVector = pVector + lVector * t
        val r0Term =
            r0 * dVector.crossProduct(w0Vector).dot(lVector.crossProduct(w0Vector)) / dVector.crossProduct(w0Vector)
                .length()
        val r1Term =
            r1 * dVector.crossProduct(w1Vector).dot(lVector.crossProduct(w1Vector)) / dVector.crossProduct(w1Vector)
                .length()
        val sgn = if (t > 0) 1 else -1
        val h0Term = (h0 / 2) * abs(lVector.dot(w0Vector) * sgn)
        val h1Term = (h1 / 2) * abs(lVector.dot(w1Vector) * sgn)

        return r0Term + r1Term + h0Term + h1Term
    }


    fun separatedCylinders(cylinder: Cylinder, cylinder2: Cylinder): Vector3? {
        if (cylinder == cylinder2) {
            return null
        }

        val delta = cylinder2.c0Vector - cylinder.c0Vector
        val w0CrosswW1 = cylinder.w0Vector.crossProduct(cylinder2.w0Vector)
        val lengthW0CrossW1 = w0CrosswW1.length()

        if (lengthW0CrossW1 > 0) {
            // Test for separation by W_0
            if (cylinder2.r0 * lengthW0CrossW1 + cylinder.h0 / 2 + (cylinder2.h0 / 2) * cylinder.w0Vector.dot(cylinder2.w0Vector).absoluteValue - cylinder.w0Vector.dot(
                    delta
                ).absoluteValue < 0
            ) {
                return cylinder.w0Vector
            }

            // Test for separation by W_1
            if (cylinder.r0 * lengthW0CrossW1 + (cylinder.h0 / 2) * cylinder.w0Vector.dot(cylinder2.w0Vector).absoluteValue + (cylinder2.h0 / 2) * delta.dot(
                    cylinder2.w0Vector
                ).absoluteValue < 0
            ) {
                return cylinder2.w0Vector
            }

            // Test for separation by W_0 cross W_1
            if ((cylinder.r0 + cylinder2.r0) * lengthW0CrossW1 - w0CrosswW1.dot(delta).absoluteValue < 0) {
                return w0CrosswW1.normalize()
            }

            // Test for separation by delta
            if (cylinder.r0 * delta.crossProduct(cylinder.w0Vector).length()
                + cylinder2.r0 * delta.crossProduct(cylinder2.w0Vector).length()
                + (h0 / 2) * delta.dot(cylinder.w0Vector).absoluteValue
                + (h1 / 2) * delta.dot(cylinder.w0Vector).absoluteValue
                - delta.dot(delta) < 0
            ) {
                return delta.normalize()
            }

            return separatedByOtherDirections(
                delta,
                cylinder.w0Vector,
                cylinder.r0,
                cylinder.h0,
                cylinder2.w0Vector,
                cylinder2.r0,
                cylinder2.h0
            )

        } else {
            // Test for separation by height
            if ((cylinder.h0 + cylinder2.h0) / 2 - delta.dot(w0Vector).absoluteValue < 0) {
                return w0Vector
            }

            // Test for separation radially
            if ((cylinder.r0 + cylinder2.r0) - delta.crossProduct(w0Vector).length() < 0) {
                return (delta - w0Vector * delta.dot(w0Vector)).normalize()
            }

        }

        return null
    }


    fun separatedByOtherDirections(
        delta: Vector3,
        cylinder1AxisVector: Vector3,
        cylinder1Radius: Double,
        cylinder1Height: Double,
        cylinder2AxisVector: Vector3,
        cylinder2Radius: Double,
        cylinder2Height: Double,
    ): Vector3? {
        this.r0 = cylinder1Radius
        this.h0 = cylinder1Height
        this.r1 = cylinder2Radius
        this.h1 = cylinder2Height

        val lengthDelta = delta.length()
//        , vVector, nVector
        val uVector = delta / lengthDelta
        val vVector = uVector
        val nVector = vVector

        // Convert to the coordinates system where N = Delta / |Delta| is the north
        // pole of the hemisphere to be searched
        this.w0Vector = Vector3(
            uVector.dot(cylinder1AxisVector),
            vVector.dot(cylinder1AxisVector),
            nVector.dot(cylinder1AxisVector)
        )
        this.w1Vector = Vector3(
            uVector.dot(cylinder2AxisVector),
            vVector.dot(cylinder2AxisVector),
            nVector.dot(cylinder2AxisVector)
        )
        this.w0CrossW1 = cylinder1AxisVector.crossProduct(cylinder2AxisVector)

        if (cylinder1AxisVector.z > 0) {
            this.w0Vector *= -1.0
        }

        if (cylinder2AxisVector.z < 0) {
            this.w1Vector *= -1.0
        }

//        if(w0CrossW1.z < 0) {
//            w0CrossW1
//        }


        // Compute the common origin for the line discontinuities
        pVector = this.w0CrossW1 / this.w0CrossW1.z

        // Compute the point discontinuities
        q0Vector = this.w0Vector / this.w0Vector.z
        q1Vector = this.w1Vector / this.w1Vector.z


        val lineMinimums = mutableListOf<Double>()
        for (i in 0..<numLines) {
            // Compute a line direction
            val angle = PI * i / numLines

            // Compute the minimum of g(t) on the line P + tL(theta)
            lVector = Vector3(cos(angle), sin(angle), 0.0)
            val tMin = computeLineMinimum(lVector, q0Vector, q1Vector, pVector)
            val gMin = gFunction(tMin)
            lineMinimums.add(gMin)

            if (gMin < 0) {
                val polePoint = pVector + lVector * tMin
                return (uVector * polePoint.x + vVector * polePoint.y + nVector).normalize()
            }
        }

        lineMinimums.add(lineMinimums[0])

        // Locate a triple of points that bracket the minimum
        var i0 = 0
        var i1 = 1
        var i2 = 2

        // TODO Check that the loops and arrays have the correct limits
        val bracket = arrayOf(numLines - 1, 0, 1)
        while (i2 < lineMinimums.size) {
            if (lineMinimums[i1] < lineMinimums[bracket[1]]) {
                bracket[0] = i0
                bracket[1] = i1
                bracket[2] = i2
            }

            i0 = i1
            i1 = i2++
        }

        val angle0 = PI * bracket[0] / numLines
        val angle1 = PI * bracket[1] / numLines
        val angle2 = PI * bracket[2] / numLines

        val result = findMinimum(angle0, angle1, angle2)

        if (result == null) {
            return null
        }

        if (result.gValue < 0) {
            // Transform to original coordinate system
            val polePoint = pVector + lVector.times(result.angle)
            val separatingDirection = uVector.times(polePoint.x) + vVector.times(polePoint.y) + nVector
            return separatingDirection.normalize()
        }

        return null
    }


    fun findMinimum(bracketAngle0: Double, bracketAngle1: Double, bracketAngle2: Double): AngleGValue? {
        val lineMinimumForAngle: (Double) -> Double = { angle: Double ->
            // Compute the minimum of g(t) on the line P + tL(theta)
            val lineVector = Vector3(cos(angle), sin(angle), 0.0)
            computeLineMinimum(lineVector, q0Vector, q1Vector, pVector)
        }

        val inputForMinimum = bisect2(lineMinimumForAngle, bracketAngle0, bracketAngle1, bracketAngle2) ?: return null

        return AngleGValue(inputForMinimum.first, inputForMinimum.second)
    }


    fun computeLineMinimum(
        lVector: Vector3,
        q0Vector: Vector3, q1Vector: Vector3,
        pVector: Vector3
    ): Double {
        val lPerpVector = Vector3(lVector.x, lVector.y, 0.0)

        return if (lPerpVector.dot(q0Vector - pVector) != 0.0) {
            // Q0 is not in the line P + t * L(angle)
            if (lPerpVector.dot(q1Vector - pVector) != 0.0) {
                // Q1 is not on the line P + t * L(angle)
                val (dgdt0n, dgdt0p) = limitsDgdtZero()
//                dgdt0n = result.first
//                dgdt0p = result.second
                computeMinimumSingularZero(dgdt0n, dgdt0p)
            } else {
                // Q1 is on the line P + tL(angle)
                val (dgdt0n, dgdt0p) = limitsDgdtZero()
                val (dgdt1n, dgdt1p) = limitsDgdtOneQ1(q1Vector)
                computeMinimumSingularZeroOne(
                    dgdt0n, dgdt0p,
                    dgdt1n, dgdt1p
                )
            }
        } else {
            // Q_0 is on the lint P + tL(angle)
            val (dgdt0n, dgdt0p) = limitsDgdtZero()
            val (dgdt1n, dgdt1p) = limitsDgdtOneQ1(q1Vector)

            computeMinimumSingularZeroOne(dgdt0n, dgdt0p, dgdt1n, dgdt1p)
        }

    }


    companion object {

        val imax = 10
        val maxTries = 1000
        val maxBisections = 20
        val epsilon = 1e-08
        val tolerance = 1e-04


        fun searchForSeparatingAxis(cylinder1: Cylinder, cylinder2: Cylinder): Vector3? {
            val cylinderIntersectionComputation = CylinderIntersectionUtilities()

            return cylinderIntersectionComputation.separatedCylinders(cylinder1, cylinder2)

        }

        /**
         * TODO As it is now this method does not distinguish between minimum or maximum points, so it cannot be guaranteed to find a minimum
         */
        fun bisect(
            valueFunction: (Double) -> Double,
//        t0: Double,
            bracketStart: Double,
            pointInBracket: Double,
            bracketEnd: Double
//               t1: Double,
//    dgdt0n: Double, tMin: Double, dgdtTmin: Double
        ): Double {
//        var a = t0
//        var b = t0 / 2
//        var c = 0.0

            var a = bracketStart
            var b = pointInBracket
            var c = bracketEnd

//        var cDev = dgdt0n

            var tryCounter = 0
            while (tryCounter < maxTries) {
                val alpha = (valueFunction(b) - valueFunction(a)) / (b - a)
                val beta = (valueFunction(c) - valueFunction(a) - alpha * (c - a)) / ((c - a) * (c - b))
                val x = (a + b) / 2 - alpha / (2 * beta)
                a = b
                b = c
                c = x

                if (maxOf(a, b, c) - minOf(a, b, c) < 1e-6) {
                    break
                }

                ++tryCounter
            }

//            if (tryCounter == maxTries) {
            // TODO Write warning or throw an exception?
//            }

            return c
        }

        fun bisect2(
            valueFunction: (Double) -> Double,
            bracketStart: Double,
            pointInBracket: Double,
            bracketEnd: Double
        ): Pair<Double, Double> {
            val a = Pair(bracketStart, valueFunction(bracketStart))
            val b = Pair(pointInBracket, valueFunction(pointInBracket))
            val c = Pair(bracketEnd, valueFunction(bracketEnd))
            val currentMin = listOf(a, b, c).maxBy { it.second }

            return if ((b.second < a.second && c.second >= b.second) ||
                (c.second > b.second && a.second >= b.second)
            ) {
                getBracketedMinimum(valueFunction, a, b, c, currentMin)

            } else {
                listOf(
                    subdivide(valueFunction, a, b, currentMin, maxBisections),
                    subdivide(valueFunction, b, c, currentMin, maxBisections)
                ).minBy { it.second }
            }
        }

        private fun subdivide(
            valueFunction: (Double) -> Double,
            a: Pair<Double, Double>,
            b: Pair<Double, Double>,
            currentMin: Pair<Double, Double>,
            maxBisections: Int
        ): Pair<Double, Double> {
            if (maxBisections - 1 == 0) {
                return currentMin
            }

            val midPoint = 0.5 * (a.first + b.first)
            val midPointValue = 0.5 * (a.second + b.second)


            val updatedCurrentMin = if (midPointValue < currentMin.second) {
                Pair(midPoint, midPointValue)
            } else {
                currentMin
            }

            return if ((midPointValue < a.second && b.second >= midPointValue) ||
                (b.second > midPointValue && a.second >= midPointValue)
            ) {
                getBracketedMinimum(valueFunction, a, Pair(midPoint, midPointValue), b, updatedCurrentMin)
            } else {
                val remainingBisections = maxBisections - 1
                return listOf(
                    subdivide(valueFunction, a, Pair(midPoint, midPointValue), updatedCurrentMin, remainingBisections),
                    subdivide(valueFunction, a, Pair(midPoint, midPointValue), updatedCurrentMin, remainingBisections)
                ).minBy { it.second }
            }
        }

        fun getBracketedMinimum(
            valueFunction: (Double) -> Double,
            a: Pair<Double, Double>,
            b: Pair<Double, Double>,
            c: Pair<Double, Double>,
            currentMin: Pair<Double, Double>
        ): Pair<Double, Double> {
            var currentMinInput = currentMin.first
            var currentMinValue = currentMin.second
            var aPair = a
            var bPair = b
            var cPair = c

            for (i in 0..maxBisections) {
                if (cPair.second < currentMinValue) {
                    currentMinInput = cPair.first
                    currentMinValue = cPair.second
                }

                val diff = cPair.first - aPair.first
                val bound = 2 * tolerance * abs(bPair.first) + epsilon
                if (diff <= bound) {
                    return Pair(currentMinInput, currentMinValue)
                }

                val diffInputAB = aPair.first - bPair.first
                val diffInputCB = cPair.first - bPair.first
                val diffValueAB = aPair.second - bPair.second
                val diffValueCB = cPair.second - bPair.second
                val temp = diffInputAB * diffValueCB
                val temp2 = diffInputCB * diffValueAB
                val denom = temp2 - temp

                if (abs(denom) <= epsilon) {
                    return Pair(currentMinInput, currentMinValue)
                }

                val tempNewInput = bPair.first + 0.5 * (diffInputCB * temp2 - diffInputAB * temp) / denom
                val newInput = max(aPair.first, min(tempNewInput, cPair.first))
                val newValue = valueFunction(newInput)

                if (newValue < currentMinValue) {
                    currentMinInput = newInput
                    currentMinValue = newValue
                }

                if (newInput < bPair.first) {
                    if (newValue < bPair.second) {
                        cPair = bPair
                        bPair = Pair(newInput, newValue)
                    } else {
                        aPair = Pair(newInput, newValue)
                    }


                } else if (newInput > b.first) {
                    if (newValue < b.second) {
                        aPair = bPair
                        bPair = Pair(newInput, newValue)
                    } else {
                        cPair = Pair(newInput, newValue)
                    }
                } else {
                    val midAB = 0.5 * (a.first + b.first)
                    val midValueAB = valueFunction(midAB)
                    val midBC = 0.5 * (b.first + c.first)
                    val midValueBC = valueFunction(midBC)

                    if (midValueAB < b.second) {
                        if (midValueBC < b.second) {
                            if (midValueAB < midValueBC) {
                                cPair = bPair
                                bPair = Pair(midAB, midValueAB)
                            } else {
                                aPair = bPair
                                bPair = Pair(midBC, midValueBC)
                            }
                        } else {
                            cPair = bPair
                            bPair = Pair(midAB, midValueAB)
                        }

                    } else if (midValueAB > b.second) {
                        if (midValueBC < b.second) {
                            aPair = bPair
                            bPair = Pair(midBC, midValueBC)
                        } else {
                            aPair = Pair(midAB, midValueAB)
                            cPair = Pair(midBC, midValueBC)
                        }

                    } else {
                        if (midValueBC > b.second) {
                            aPair = bPair
                            bPair = Pair(midBC, midValueBC)
                        } else {
                            aPair = Pair(midAB, midValueAB)
                            cPair = Pair(midBC, midValueBC)
                        }
                    }
                }
            }

            return Pair(currentMinInput, currentMinValue)
        }
    }

}
