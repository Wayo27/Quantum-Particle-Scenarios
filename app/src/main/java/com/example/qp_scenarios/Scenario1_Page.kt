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
import androidx.compose.runtime.mutableIntStateOf
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

/** Composable Function: QP_Scenario1_Page()
 *
 * Description: Quantum Particle confined inside a WELL.
 * - Inside the Well, the Potential Energy is U(x) = 0
 * - Outside the Well, the Potential Energy is U(x) = "infinite"
 * Therefore the Particle can never scape out of the Well.
 *
 */
@Composable
fun QP_Scenario1_Page(
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
        consoleViewModel.addMessage("<<< Quantum Particle Scenario 1 >>>")
        consoleViewModel.addMessage("\nParticle Confined inside a Well of")
        consoleViewModel.addMessage("Length \"L\" and Potential Energy:")
        consoleViewModel.addMessage("-> \"U(x) = 0\" inside the Well: X = <0-L>")
        consoleViewModel.addMessage("-> \"U(x) = infinite\" outside the Well")
    }

    var orbitalNumber by remember { mutableIntStateOf(0) } // Declare & Init Orbital Number "n"

    Box(modifier = Modifier.fillMaxSize()) {

        // Page Background Image
        Image(
            painter = painterResource(id = R.drawable.quantumparticle_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // QP_Scenario1_Page Content
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Space under the Page Header
            Spacer(modifier = Modifier.height(70.dp))

            // Row of Buttons: "Orbital 1", "Orbital 2"
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // One Left, one Right
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Button: Orbital 1
                App_Button(
                    text = "Orbital 1",
                    color = C_LightBlue,
                    modifier = Modifier.width(160.dp),
                    onClick = {
                        orbitalNumber = 1
                        viewModel.calculateSingleWave(orbitalNumber)
                        consoleViewModel.addMessage("-------------------------")
                        consoleViewModel.addMessage("-> ψ(x) Wave Function for Orbital n = $orbitalNumber\n")
                    }
                )

                // Button: Orbital 2
                App_Button(
                    text = "Orbital 2",
                    color = C_LightBlue,
                    modifier = Modifier.width(160.dp),
                    onClick = {
                        orbitalNumber = 2
                        viewModel.calculateSingleWave(orbitalNumber)
                        consoleViewModel.addMessage("-------------------------")
                        consoleViewModel.addMessage("-> ψ(x) Wave Function for Orbital n = $orbitalNumber\n")
                    }
                )
            } // End Row of Buttons

            Spacer(modifier = Modifier.height(20.dp))

            // Row of Buttons: "Orbital 3", "Orbital 4"
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // One Left, one Right
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Button: Orbital 3
                App_Button(
                    text = "Orbital 3",
                    color = C_LightBlue,
                    modifier = Modifier.width(160.dp),
                    onClick = {
                        orbitalNumber = 3
                        viewModel.calculateSingleWave(orbitalNumber)
                        consoleViewModel.addMessage("-------------------------")
                        consoleViewModel.addMessage("-> ψ(x) Wave Function for Orbital n = $orbitalNumber\n")
                    }
                )

                // Button: Orbital 4
                App_Button(
                    text = "Orbital 4",
                    color = C_LightBlue,
                    modifier = Modifier.width(160.dp),
                    onClick = {
                        orbitalNumber = 4
                        viewModel.calculateSingleWave(orbitalNumber)
                        consoleViewModel.addMessage("-------------------------")
                        consoleViewModel.addMessage("-> ψ(x) Wave Function for Orbital n = $orbitalNumber\n")
                    }
                )
            } // End Row of Buttons

            Spacer(modifier = Modifier.height(20.dp))

            // Row of Buttons: "Orbital 5", "Orbital 6"
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // One Left, one Right
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Button: Orbital 5
                App_Button(
                    text = "Orbital 5",
                    color = C_LightBlue,
                    modifier = Modifier.width(160.dp),
                    onClick = {
                        orbitalNumber = 5
                        viewModel.calculateSingleWave(orbitalNumber)
                        consoleViewModel.addMessage("-------------------------")
                        consoleViewModel.addMessage("-> ψ(x) Wave Function for Orbital n = $orbitalNumber\n")
                    }
                )

                // Button: Orbital 6
                App_Button(
                    text = "Orbital 6",
                    color = C_LightBlue,
                    modifier = Modifier.width(160.dp),
                    onClick = {
                        orbitalNumber = 6
                        viewModel.calculateSingleWave(orbitalNumber)
                        consoleViewModel.addMessage("-------------------------")
                        consoleViewModel.addMessage("-> ψ(x) Wave Function for Orbital n = $orbitalNumber\n")
                    }
                )
            } // End Row of Buttons

            Spacer(modifier = Modifier.height(20.dp))

            // Row of Buttons: "Orbital 7", "Orbital 8"
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // One Left, one Right
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Button: Orbital 7
                App_Button(
                    text = "Orbital 7",
                    color = C_LightBlue,
                    modifier = Modifier.width(160.dp),
                    onClick = {
                        orbitalNumber = 7
                        viewModel.calculateSingleWave(orbitalNumber)
                        consoleViewModel.addMessage("-------------------------")
                        consoleViewModel.addMessage("-> ψ(x) Wave Function for Orbital n = $orbitalNumber\n")
                    }
                )

                // Button: Orbital 8
                App_Button(
                    text = "Orbital 8",
                    color = C_LightBlue,
                    modifier = Modifier.width(160.dp),
                    onClick = {
                        orbitalNumber = 8
                        viewModel.calculateSingleWave(orbitalNumber)
                        consoleViewModel.addMessage("-------------------------")
                        consoleViewModel.addMessage("-> ψ(x) Wave Function for Orbital n = $orbitalNumber\n")
                    }
                )
            } // End Row of Buttons

            Spacer(modifier = Modifier.height(10.dp))

            // Wave Function Canvas UI
            WaveFunctionGraph(
                data = viewModel.Yn_Wave_Data,
                title = if(orbitalNumber != 0) "ψ(x): Wave Function for n = $orbitalNumber"
                else "ψ(x): Select an Orbital Number",
                ifBarrier = false // There is Not Potential Barrier for this Scenario
            )

            /* THIS IS NOT LONGER USED
            if (viewModel.stackedWaves.isNotEmpty()) {
                StackedWaveGraphs(
                    waves = viewModel.stackedWaves,
                    startN = viewModel.currentStartN
                )
            }
            */

            Spacer(modifier = Modifier.height(30.dp))

            // App Output Console UI: Includes Text Scrolling
            val listState = rememberLazyListState() // Console Scroll State

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
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
    // QP_Scenario1_Page Header
    PageHeader(
        "Quantum Particle\n\"Scenario 1\"",
        C_LightGreen
    )

} // End Function