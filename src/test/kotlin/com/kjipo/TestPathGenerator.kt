package com.kjipo

import org.junit.jupiter.api.Test

class TestPathGenerator {


    @Test
    fun testGenerateCylinder() {
        val pathGenerate = PathGenerator()

        val cylinder = pathGenerate.generateCylinder()

        println("Cylinder: $cylinder")

        val cylinderEndpoints = PathGenerator.getXYEndpointsOfCylinder(cylinder)

        println("Cylinder endpoints: $cylinderEndpoints")
    }


    @Test
    fun testPathPoints() {
        val pathGenerate = PathGenerator()

        for (i in 0..<10) {
            println(pathGenerate.getNextFrame())
        }


    }


}