package com.example.qp_scenarios

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.exp
import kotlin.math.sin

class App_Classes {
}

/** Class ConsoleViewModel
 *
 * This is a ViewModel to be used as the
 * App Output Console
 *
 */
class ConsoleViewModel : ViewModel() {
    val consoleMessages = mutableStateListOf<String>("")

    fun addMessage(msg: String) {
        consoleMessages.add(msg)
    }

    fun clear() {
        consoleMessages.clear()
    }

} // End Class


/** Class QuantumViewModel
 *
 * This Class is used to Calculate the Schrodinger
 * Wave Forms for all the different Scenarios.
 *
 * Note: Yn_Wave_Data(x) is the general global variable
 * for the Wave Function
 *
 * There are several methods in this Class:
 * - SingleWave: method calculateWave()
 * - Stacked Waves: method calculateStackedWaves()
 * - Linear Potential Wave: method calculateLinearPotentialWave()
 * - General Solution for a Particle moving between 2 parallel Plates.
 *   This method is a particular solution of Bessel´s equation called
 *   Airy Wave: calculateAiryWave()
 * - Bessel Like Wave: method calculateBesselLikeWave() to calculate a
 *   "Bessel Like Wave" which is just a mathematical illustration that
 *   can give an idea of a Bessel Wave, but strictly speaking this is NOT a Bessel
 *   Wave. This Wave includes an Animation using the variable "phase"
 * - E > U Scenario: method
 * - E < U Scenario: method
 *
 */
class QuantumViewModel : ViewModel() {

    // Wave Function Array for all Scenarios: Yn_Wave_Data[]
    var Yn_Wave_Data by mutableStateOf(DoubleArray(C_Nbr_Waves))
        private set // Variable can be Modified only within this ViewModel

    var currentStartN by mutableStateOf(1)
        private set // Variable can be Modified only within this ViewModel

    // Variable for Animation of the Wave Form Generated
    var phase by mutableStateOf(0.0)
        private set // Variable can be Modified only within this ViewModel

    // Method to Update the "phase" to produce Animation
    fun advancePhase(delta: Double = 0.05) {
        phase += delta
    }

    // ===== Single Wave (Single Orbital) =====
    fun calculateSingleWave(n: Int) {
        Yn_Wave_Data = DoubleArray(C_Nbr_Waves) { i ->
            sin((n * Math.PI * i) / C_Well_Length) * C_Wave_Multiplier
        }

    } // End of method

    // ===== Stacked Waves (multiple orbitals, Scenario 1) =====
    // Method not used any more !!. Do not Remove !!
    var stackedWaves by mutableStateOf<List<DoubleArray>>(emptyList())
        private set // Variable can be Modified only within this ViewModel

    fun calculateStackedWaves(ns: List<Int>) {
        if (ns.isEmpty()) return // If no Orbital Number was Passed, return

        currentStartN = ns.first()

        stackedWaves = ns.map { n ->
            DoubleArray(C_Nbr_Waves) { i ->
                sin((n * Math.PI * i) / C_Well_Length) * C_Wave_Multiplier
            }
        }

    } // End of method

    // ===== Linear Potential Wave (Scenario 2) =====

    fun calculateLinearPotentialWave(energyShift: Double) {
        Yn_Wave_Data = DoubleArray(C_Nbr_Waves) { i ->
            val x = i.toDouble() / C_Nbr_Waves
            val spatialPhase = 6.0 * x
            val decay = exp(-2.5 * x)

            sin(spatialPhase + energyShift) * decay * C_Wave_Multiplier
        }

        //Yn_Wave_Data = linearWave // Update Yn_Wave_Data for further use in Probability Density

    } // End of method

    // ===== Airy Equation Solver (Scenario 2) =====

    fun calculateAiryWave(
        xMin: Double = -5.0,
        xMax: Double = 5.0
    ) {
        val n = C_Nbr_Waves
        val h = (xMax - xMin) / (n - 1)

        val y = DoubleArray(n)
        val dy = DoubleArray(n)

        // Initial conditions for Ai(0)
        val i0 = ((0.0 - xMin) / h).toInt().coerceIn(0, n - 1)
        y[i0] = 0.3550280539
        dy[i0] = -0.2588194038

        // Integrate forward
        for (i in i0 until n - 1) {
            val x = xMin + i * h
            rk4Step(i, x, h, y, dy)
        }

        // Integrate backward
        for (i in i0 downTo 1) {
            val x = xMin + i * h
            rk4StepBackward(i, x, h, y, dy)
        }

        Yn_Wave_Data = y.map { it * C_Wave_Multiplier }.toDoubleArray()

    } // End of method

    // ===== Bessel Like (not actual a Bessel equation) Equation Solver (Scenario 2) =====

    fun calculateBesselLikeWave() {

        val waveNbr = 9.0          // Number of oscillations
        val decayFactor = 6.0      // Confinement strength
        val amplitude = 200.0

        Yn_Wave_Data = DoubleArray(C_Nbr_Waves) { i ->

            val x = i.toDouble() / C_Nbr_Waves   // x ∈ [0,1]

            val oscillation = sin(waveNbr * PI * x + phase)
            val decay = exp(-decayFactor * x)

            oscillation * decay * amplitude
        }

    } // End of method

    // ===== Potential Barrier (Scenario 3) =====
    val x0 = platesDistance * 0.35
    val L  = platesDistance * 0.30

    fun generateBarrierWaveFunction(scenario: BarrierScenario) {
        when (scenario) {
            BarrierScenario.ENERGY_GREATER_THAN_BARRIER -> {
                generateWave_E_Greater_U()
            }
            BarrierScenario.ENERGY_LESS_THAN_BARRIER -> {
                generateWave_E_Less_U()
            }
        }
    }

    /* E > U Scenario: Oscillating Wave in the 3 Regions
     *
     * -> x < 0 : U(x) = 0
     * -> 0 <= x <= L : U(x) = U
     * -> x > L : U(x) = 0
     *
     */
    private fun generateWave_E_Greater_U() {

        val visibleWaves = 6.0

        val kFree    = 2 * Math.PI * visibleWaves / platesDistance
        val kBarrier = kFree * 0.6

        val AFree = 1.0
        val ABarrier = 1.0   // Same scale for visual stability

        val newData = DoubleArray(platesDistance)

        val x0i = x0.toInt()
        val xLi = (x0 + L).toInt()

        // -------- Region 1 --------
        for (i in 0 until x0i) {
            val x = i.toDouble()
            newData[i] = AFree * sin(kFree * x)
        }

        // Value and phase at entry
        val psiEntry = AFree * sin(kFree * x0)
        val phiBarrier = asin(psiEntry / ABarrier)

        // -------- Region 2 (Barrier) --------
        for (i in x0i until xLi) {
            val x = i.toDouble() - x0
            newData[i] = ABarrier * sin(kBarrier * x + phiBarrier)
        }

        // Value at exit
        val psiExit = ABarrier * sin(kBarrier * L + phiBarrier)

        // Phase for region 3
        val phiOut = asin(psiExit / AFree)

        // -------- Region 3 --------
        for (i in xLi until platesDistance) {
            val x = i.toDouble() - (x0 + L)
            newData[i] = AFree * sin(kFree * x + phiOut)
        }

        // Copy Data Array into Wave Function Yn_Wave_Data[]
        Yn_Wave_Data = newData

    } // End method

    /**
     * E < U Scenario : Quantum Tunneling
     *
     * - Continuous ψ(x)
     * - Visible evanescent decay
     * - Transmitted wave not flattened
     * - Pedagogical / aesthetic balance
     */
    private fun generateWave_E_Less_U() {

        val visibleWaves = 6.0
        val k = 2 * Math.PI * visibleWaves / platesDistance
        val alpha = 1.0 / L

        val newData = DoubleArray(platesDistance)

        // Incident + reflected wave at x0
        val psi_x0 = sin(k * x0) + 0.5 * sin(-k * x0)

        // Evanescent value at x0 + L
        val psi_xL = psi_x0 * exp(-alpha * L)

        // Phase chosen for perfect continuity
        val phase = Math.PI / 2

        for (i in 0 until platesDistance) {

            val x = i.toDouble()

            newData[i] = when {
                // Region I
                x < x0 -> {
                    sin(k * x) + 0.5 * sin(-k * x)
                }

                // Region II
                x < x0 + L -> {
                    psi_x0 * exp(-alpha * (x - x0))
                }

                // Region III (phase matched)
                else -> {
                    psi_xL * sin(k * (x - x0 - L) + phase)
                }
            }
        }

        // Copy Data Array into Wave Function Yn_Wave_Data[]
        Yn_Wave_Data = newData

    } // End method

    /** Method to Show the Probability Density
     *
     *
     */
    var probabilityDensity by mutableStateOf(DoubleArray(C_Nbr_Waves))
        private set // Variable can be Modified only within this ViewModel

    fun calculateProbabilityDensity(source: DoubleArray) {
        probabilityDensity = source.map { it * it }.toDoubleArray()
    }

    /**
     * Clear / Reset Wave Function Yn_Wave_Data[]
     */
    fun clearWave() {
        Yn_Wave_Data = DoubleArray(C_Nbr_Waves)
    }

} // End Class

