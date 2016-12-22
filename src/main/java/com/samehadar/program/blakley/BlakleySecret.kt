package com.samehadar.program.blakley

import org.apache.commons.math3.linear.Array2DRowRealMatrix
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.LUDecomposition
import java.math.BigInteger
import java.util.*


data class SecretPoint(val secret: Int, val y: Int, val z: Int)
data class Secret(val a1: Int, val a2: Int, val a3: Int, val b: Int) {
    fun toArray(): DoubleArray {
        return doubleArrayOf(a1.toDouble(), a2.toDouble(), a3.toDouble())
    }
    fun value(): Double {
        return b.toDouble()
    }
}
data class ThreeSecrets(val secret1: Secret, val secret2: Secret, val secret3: Secret)

object BlakleySecret {
    val random = Random(300)

    @JvmStatic
    fun main(args: Array<String>) {
        //Example of usage
        println("Type an integer as secret:")
        val secret = readLine()!!.toInt()

        val splitSecret = splitSecret(secret)
        val restoredSecret = restoreSecret(splitSecret)
        println("The restored secret is: " + restoredSecret)
    }

    fun splitSecret(secret: Int): ThreeSecrets {
        val p = BigInteger.probablePrime(8, random).toLong()

        val secretPoint = getSecretPoint(secret, p)

        return ThreeSecrets(
                makeSecret(secretPoint, p = p),
                makeSecret(secretPoint, p = p),
                makeSecret(secretPoint, p = p)
        )
    }

    fun restoreSecret(threeSecrets: ThreeSecrets): Long {
        return solve(threeSecrets.secret1, threeSecrets.secret2, threeSecrets.secret3)
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

    private fun getSecretPoint(secret: Int, p: Long): SecretPoint {
        val y = genRndInt(p)
        val z = genRndInt(p)

        return SecretPoint(secret, y,z)
    }

    private fun makeSecret(secretPoint: SecretPoint, p: Long): Secret {
        val (secret, y, z) = secretPoint

        //TODO: we can generalize algorithm to `n` secret parts
        val a1 = genRndInt(p)
        val a2 = genRndInt(p)
        val a3 = genRndInt(p)

        val b = a1 * secret + a2 * y + a3 * z

        return Secret(a1, a2, a3, b)
    }

    private fun genRndInt(p: Long): Int {
        return (random.nextInt() % p).toInt()
    }
}