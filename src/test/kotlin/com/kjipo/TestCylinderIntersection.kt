package com.kjipo

import kotlin.test.Test

class TestCylinderIntersection {


    @Test
    fun testCylindersNotIntersecting() {
        val cylinder1 = Cylinder(
            Vector3(5.0, 0.0, 0.0),
            Vector3(0.0, 1.0, 0.0), 1.0, 10.0
        )
        val cylinder2 = Cylinder(
            Vector3(0.0, 1.0, 0.0),
            Vector3(0.0, 0.0, 1.0), 1.0, 10.0
        )

        val separatingAxis = CylinderIntersectionUtilities.searchForSeparatingAxis(cylinder1, cylinder2)

        println("Separating axis: $separatingAxis")
    }


}