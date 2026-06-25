package com.andika.temudecision.domain.methods

class AHPMethod {
    /**
     * Calculates the priority vector from a pairwise comparison matrix.
     * @param matrix Square matrix of pairwise comparisons.
     * @return Normalized priority vector (weights).
     */
    fun calculateWeights(matrix: Array<DoubleArray>): DoubleArray {
        val n = matrix.size
        val colSums = DoubleArray(n)
        for (j in 0 until n) {
            for (i in 0 until n) {
                colSums[j] += matrix[i][j]
            }
        }

        val normalizedMatrix = Array(n) { DoubleArray(n) }
        for (i in 0 until n) {
            for (j in 0 until n) {
                normalizedMatrix[i][j] = matrix[i][j] / colSums[j]
            }
        }

        val weights = DoubleArray(n)
        for (i in 0 until n) {
            var rowSum = 0.0
            for (j in 0 until n) {
                rowSum += normalizedMatrix[i][j]
            }
            weights[i] = rowSum / n
        }
        return weights
    }

    fun calculateConsistencyRatio(matrix: Array<DoubleArray>, weights: DoubleArray): Double {
        val n = matrix.size
        if (n <= 2) return 0.0
        
        val lambdaMax = (0 until n).sumOf { i ->
            var rowSum = 0.0
            for (j in 0 until n) {
                rowSum += matrix[i][j] * weights[j]
            }
            rowSum / weights[i]
        } / n

        val ci = (lambdaMax - n) / (n - 1)
        val ri = listOf(0.0, 0.0, 0.58, 0.90, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49)
        val riValue = if (n <= ri.size) ri[n - 1] else 1.5
        
        return ci / riValue
    }
}
