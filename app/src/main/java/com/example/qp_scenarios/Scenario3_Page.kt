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

/** Composable Function: QP_Scenario3_Page()
 *
 * Description: Quantum Particle of Energy "E" moving along
 * the X axis. The Particle finds a Potential Barrier
 * of Length "L" located in X: <0 - L>
 *
 * Potential Energy:
 *     - U(x) = 0, x < 0 & x > L : Regions 1 & 3
 *     - U(x) = U, x:<0 - L> Potential Barrier of Energy "U": Region 2
 *
 * The Scenarios shown here are:
 * - E > U: Oscillation in all regions, but Wave Length longer inside the Barrier
 * - E < U: Tunneling: Not Oscillating but Decay inside the Barrier
 *
 */
@Composable
fun QP_Scenario3_Page(
    consoleViewModel: ConsoleViewModel,
    onBack: () -> Unit,
    viewModel: QuantumViewModel = viewModel()
) {

    // Clear "Yn_Wave_Data[]" Wave Function ONLY once when page is entered
    LaunchedEffect(Unit) {
        viewModel.clearWave() // Clear Yn_Wave_data[]
    }

    var selectedScenario by remember {
        mutableStateOf<BarrierScenario?>(null)
    }

    // Init Console Message
    LaunchedEffect(Unit) {
        consoleViewModel.clear()
        consoleViewModel.addMessage("<<< Quantum Particle Scenario 3 >>>")
        consoleViewModel.addMessage("\nParticle with energy E moving along")
        consoleViewModel.addMessage("the X axis where the Potential Energy is:")
        consoleViewModel.addMessage("-> U(x) = 0,  x < 0 & x > L")
        consoleViewModel.addMessage("-> U(x) = U, x:<0-L> : Potential Barrier")
        consoleViewModel.addMessage("\nScenario E > U: Oscillating in all Regions")
        consoleViewModel.addMessage("-> Wave Length Longer inside the Barrier")
        consoleViewModel.addMessage("\nScenario E < U: Quantum Tunneling")
        consoleViewModel.addMessage("-> Barrier: Not Oscillating but Decay")
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Page Background Image
        Image(
            painter = painterResource(id = R.drawable.quantumparticle_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // QP_Scenario3_Page Content
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Space under the Page Header
            Spacer(modifier = Modifier.height(40.dp))

            // Row of Buttons: "E > U", "E < U"
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // One Left, one Right
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Button: E > U (Particle Energy is greater than Barrier Potential Energy)
                App_Button(
                    text = "E > U",
                    color = C_LightBlue,
                    modifier = Modifier.width(160.dp),
                    onClick = {
                        selectedScenario = BarrierScenario.ENERGY_GREATER_THAN_BARRIER
                        viewModel.generateBarrierWaveFunction(selectedScenario!!)
                        consoleViewModel.addMessage("\n-------------------------")
                        consoleViewModel.addMessage("-> ψ(x): E > U : Oscillating in all Regions")
                    }
                )

                // Button: E < U (Particle Energy is lesser than Barrier Potential Energy)
                App_Button(
                    text = "E < U",
                    color = C_LightBlue,
                    modifier = Modifier.width(160.dp),
                    onClick = {
                        selectedScenario = BarrierScenario.ENERGY_LESS_THAN_BARRIER
                        viewModel.generateBarrierWaveFunction(selectedScenario!!)
                        consoleViewModel.addMessage("\n-------------------------")
                        consoleViewModel.addMessage("-> ψ(x): E < U : Quantum Tunneling")
                    }
                )
            } // End Row of Buttons

            Spacer(modifier = Modifier.height(30.dp))

            // Wave Function Graph UI
            WaveFunctionGraph(
                data = viewModel.Yn_Wave_Data,
                title = "ψ(x): Regions 1, 2, 3",
                ifBarrier = true, // There is a Potential Barrier for this Scenario
                x0 = viewModel.x0,
                L  = viewModel.L
            )

            Spacer(modifier = Modifier.height(30.dp))

            // App Output Console UI: Includes Text Scrolling
            val listState = rememberLazyListState() // Console Scroll State

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
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
    // QP_Scenario3_Page Header
    PageHeader(
        "Quantum Particle\n\"Scenario 3\"",
        C_LightPink
    )

} // End Function