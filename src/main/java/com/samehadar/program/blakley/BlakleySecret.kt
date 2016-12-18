package com.samehadar.program.blakley

import org.apache.commons.math3.linear.Array2DRowRealMatrix
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.LUDecomposition
import java.math.BigInteger
import java.util.*


data class SecretPoint(val secret: BigInteger, val y: BigInteger, val z: BigInteger)
data class Secret(val a1: BigInteger, val a2: BigInteger, val a3: BigInteger, val b: BigInteger) {
    fun toArray(): DoubleArray {
        return doubleArrayOf(a1.toDouble(), a2.toDouble(), a3.toDouble())
    }
    fun value(): Double {
        return b.toDouble()
    }
}

//TODO: get rid of BigInteger?
object BlakleySecret {
    val random = Random(300)

    @JvmStatic
    fun main(args: Array<String>) {
        //Example of usage
        val p = BigInteger.probablePrime(8, random)
        val secret = BigInteger.valueOf(322L)

        val secretPoint = getSecretPoint(secret, p)
        val secret1 = makeSecret(secretPoint, p = p)
        val secret2 = makeSecret(secretPoint, p = p)
        val secret3 = makeSecret(secretPoint, p = p)

        val restoredSecret = solve(secret1, secret2, secret3)
        println("Restored secret is: " + restoredSecret)
    }

    private fun solve(secret1: Secret, secret2: Secret, secret3: Secret): Long {
        val coefficients = Array2DRowRealMatrix(
                arrayOf(secret1.toArray(), secret2.toArray(), secret3.toArray()),
                false
        )

        val solver = LUDecomposition(coefficients).solver

        val constants = ArrayRealVector(doubleArrayOf(secret1.value(), secret2.value(), secret3.value()), false)
        //TODO: we can get serious problem with integers modulo group because apache-math solver
        //TODO: does not work in ring, so it is necessary to think about consequences
        val solution = solver.solve(constants)

        return Math.round(solution.getEntry(0))
    }

    private fun getSecretPoint(secret: BigInteger, p: BigInteger): SecretPoint {
        val y = genRndBigInt(p)
        val z = genRndBigInt(p)

        return SecretPoint(secret, y,z)
    }

    private fun makeSecret(secretPoint: SecretPoint, p: BigInteger): Secret {
        val (secret, y, z) = secretPoint

        //TODO: we can generalize algorithm to `n` secret parts
        val a1 = genRndBigInt(p)
        val a2 = genRndBigInt(p)
        val a3 = genRndBigInt(p)

        val b = a1 * secret + a2 * y + a3 * z

        return Secret(a1, a2, a3, b)
    }

    private fun genRndBigInt(p: BigInteger): BigInteger {
        var result: BigInteger

        do {
            result = BigInteger(p.bitLength(), random)
        } while (result.compareTo(p) > -1)

        return result
    }

}