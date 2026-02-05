package com.example.qp_scenarios

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

/** Composable Function: QP_Scenario2_Page()
 *
 * Description: Quantum Particle moving between
 * 2 Parallel Plates Connected to a Potential gradient "V".
 * This is a Particle in an Electric Field as it happens
 * within a Capacitor "C"
 *
 * This Scenario graphs the Wave Function Y(x) of the Particle
 * moving in one dimension "X" between the parallel Plates Connected
 * to a Potential gradient of "V" volts.
 *
 * - The Particle moves inside a region of an Electric Field E = V/d
 *   where "d" is the distance between the Plates.
 * - The Particle experiences a constant Coulomb Strength F = q.V/d
 *   where "q" is the electric charge of the Particle.
 * - The Electric Potential inside the Plates varies according to V(x) = V.(1 - x/d)
 *   where "x" is the distance measured from the origin (x = 0) to the current position
 *   of the Particle and "d" is the distance between the Plates.
 * - The Potential Energy U(x) = U(x) = F.x = q.V.x/d = (q.V/d).x = C.x where "C"
 *   is a constant C = q.V/d
 * - The Schrodinger equation for the Particle moving between the Plates is Y''(x) - y.Y(x) = 0
 *   which is a special case of Bessel´s equation called AIRY equation. The solution to this
 *   differential equation: homogeneous, 2nd order complies with Bessel Functions:
 *   Y(x) = C.(exp)(-2/3X(3/2))/(2.sqr(Pi).X(1/4))
 *
 */
@Composable
fun QP_Scenario2_Page(
    consoleViewModel: ConsoleViewModel,
    onBack: () -> Unit,
    viewModel: QuantumViewModel = viewModel()
) {

    // Clear "Yn_Wave_Data[]" Wave Function ONLY once when page is entered
    LaunchedEffect(Unit) {
        viewModel.clearWave() // Clear Yn_Wave_data[]
    }

    // Init Console Message
    LaunchedEffect(Unit) {
        consoleViewModel.clear()
        consoleViewModel.addMessage("<<< Quantum Particle Scenario 2 >>>")
        consoleViewModel.addMessage("\nParticle moving between 2 parallel Plates\nof length X: <0-L> & Potential gradient\n\"V\" between the Plates")
    }

    // Boolean to graph/not graph the Airy Solution Wave Function
    var isAiry by remember { mutableStateOf(false) }

    // Boolean to graph/not graph a "Bessel Like" Wave Function
    var isBesselLike by remember { mutableStateOf(false) }

    /* Note: this LaunchedEffect() is used to
     * give Animation to the "Bessel Like" Wave Form
     * when it is used as a Simulation of Solution to the
     * Differential Equation for Particle moving
     * between Parallel Plates.
     *
     * LaunchedEffect() is Updated when the State of "isBesselLike"
     * changes
     *
     */
    LaunchedEffect(isBesselLike) {
        if (isBesselLike) {
            while (true) {
                viewModel.advancePhase(0.05)
                viewModel.calculateBesselLikeWave()
                delay(16L) // ~60 FPS
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Page Background Image
        Image(
            painter = painterResource(id = R.drawable.quantumparticle_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // QP_Scenario2_Page Content
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Space under the Page Header
            Spacer(modifier = Modifier.height(30.dp))

            // Row of Buttons: "Low energy State", "Higher energy State"
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // One Left, one Right
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Button: "Low Energy": Show a Particular Solution for the Differential Equation
                App_Button(
                    text = "Low Energy", // Low energy State: Particle close to the Origin
                    color = C_LightBlue,
                    modifier = Modifier.width(160.dp),
                    onClick = {
                        isAiry = false // Update State
                        isBesselLike = false // Update State
                        viewModel.calculateLinearPotentialWave(energyShift = 0.0)
                        consoleViewModel.addMessage("\n-------------------------")
                        consoleViewModel.addMessage("-> Low Energy State")
                        consoleViewModel.addMessage("-> ψ(x): Localized near lower Plate")
                    }
                )

                // Button: "Higher Energy": Show a Particular Solution for the Differential Equation
                App_Button(
                    text = "Higher Energy", // Higher energy State: Particle moving to x -> d
                    color = C_LightBlue,
                    modifier = Modifier.width(160.dp),
                    onClick = {
                        isAiry = false // Update State
                        isBesselLike = false // Update State
                        viewModel.calculateLinearPotentialWave(energyShift = 1.5)
                        consoleViewModel.addMessage("\n-------------------------")
                        consoleViewModel.addMessage("-> Higher Energy State")
                        consoleViewModel.addMessage("-> ψ(x): More extended in the Field")
                    }
                )
            } // End Row of Buttons

            Spacer(modifier = Modifier.height(20.dp))

            // Row of Buttons: "Airy Solution", "Bessel Like Simulation"
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // One Left, one Right
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Button: "Airy Solution": Show the Solution of the Differential Equation (Airy Wave)
                App_Button(
                    text = "Airy Solution",
                    color = C_LightBlue,
                    modifier = Modifier.width(160.dp),
                    onClick = {
                        isAiry = true // Update State
                        isBesselLike = false // Update State
                        viewModel.calculateAiryWave() // This is the Real Solution to the Differential Equation: Airy Wave
                        consoleViewModel.addMessage("\n-------------------------")
                        consoleViewModel.addMessage("-> ψ(x): Airy Wave Physical Solution")
                    }
                )

                // Button: "Bessel Like Simulation": Show a "Bessel Like" Solution of the Differential Equation
                App_Button(
                    text = "Bessel Simu",
                    color = C_LightBlue,
                    modifier = Modifier.width(160.dp),
                    onClick = {
                        isAiry = false // Update State
                        isBesselLike = true // Update State
                        viewModel.calculateBesselLikeWave() // This is just a SIMULATION of a "Bessel Like" Wave
                        consoleViewModel.addMessage("\n-------------------------")
                        consoleViewModel.addMessage("-> ψ(x): \"Bessel Like\" Simulation Wave")
                    }
                )
            } // End Row of Buttons

            Spacer(modifier = Modifier.height(40.dp))

            /* Particle between parallel Plates: Linear U(x) = Cx
             * There are 4 cases that can be shown in this App, every case is
             * Triggered by the corresponding Button.
             *
             * 1) Particular Solution: Particle close to the Origin: Low Energy
             * 2) Particular Solution: Particle in the Field: Higher Energy
             * 3) General Solution: Airy Wave (particular case of Bessel´s function)
             * 4) Simulation of a Bessel Wave called "Bessel Like" Wave which
             *    actually is a Sin(x) multiplied by an Exponential decay
             *
             */
            if(!isAiry && !isBesselLike) { // Particular Solutions: Low energy & Higher Energy
                WaveFunctionGraph(
                    data = viewModel.Yn_Wave_Data,
                    title = "ψ(x) – Linear Potential",
                    ifBarrier = false // There is Not a Potential Barrier for this Scenario
                )

            } else { // Check whether "Airy" or "Bessel Like" solution applies
                if(isAiry) { // For Airy Wave Solution
                    WaveFunctionGraph(
                        data = viewModel.Yn_Wave_Data, // Real Solution of the Differential Equation: Airy Wave
                        title = "ψ(x) = Ai(x): Airy Solution",
                        ifBarrier = false // There is Not a Potential Barrier for this Scenario
                    )
                } else { // For Bessel Like Wave Solution
                    WaveFunctionGraph(
                        data = viewModel.Yn_Wave_Data, // Simulation of a "Bessel Like" Wave
                        title = "ψ(x) = Bessel Like Simulation",
                        ifBarrier = false // There is Not a Potential Barrier for this Scenario
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // App Output Console UI: Includes Text Scrolling
            val listState = rememberLazyListState() // Console Scroll State

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(C_LightYellow)
                    .padding(start = 8.dp, end = 8.dp, top = 1.dp, bottom = 3.dp)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(consoleViewModel.consoleMessages) { msg ->
                        Text(
                            text = msg,
                            color = Color.Black,
                            fontSize = 9.sp,
                            lineHeight = 12.sp  // IMPORTANT: Controls the Height of Line to AVOID Empty Line between Messages
                        )
                    }
                }

                // Automatic Scrolls from the Last Message Added
                LaunchedEffect(consoleViewModel.consoleMessages.size) {
                    if (consoleViewModel.consoleMessages.isNotEmpty()) {
                        listState.animateScrollToItem(consoleViewModel.consoleMessages.size - 1)
                    }
                }

            } // End of Box for Console

            Spacer(modifier = Modifier.height(30.dp))

            // Button: MainPage (Button to go back to MainPage)
            App_Button(
                text = "MainPage",
                color = C_LightOrange,
                modifier = Modifier.width(160.dp),
                onClick = onBack // On Click goes back to MainPage
            )

        } // End Column

    } // End Box

    // *** Note: Header MUST go Here, at the End of the Page ***
    // QP_Scenario2_Page Header
    PageHeader(
        "Quantum Particle\n\"Scenario 2\"",
        C_LightYellow
    )

} // End Function