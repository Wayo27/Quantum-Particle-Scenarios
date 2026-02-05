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

/** Composable Function: Analysis_Page()
 *
 * Description: Analysis of previous Scenarios
 *
 */
@Composable
fun Analysis_Page(
    consoleViewModel: ConsoleViewModel,
    onBack: () -> Unit,
    viewModel: QuantumViewModel
) {
    // Init Console Message
    LaunchedEffect(Unit) {
        consoleViewModel.clear()
        consoleViewModel.addMessage("<<< Quantum Particle Analysis >>>")
        consoleViewModel.addMessage("\n-> Calculates the Probability Density")
        consoleViewModel.addMessage("    for the Last Wave Function Selected")
        consoleViewModel.addMessage("    in Scenario 1, 2 or 3")
    }

    // Boolean indicating whether Yn_Wave_data[] contains any Data filled
    val waveFunctionHasData = viewModel.Yn_Wave_Data.any { it != 0.0 }

    var isButtonClicked by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        // Page Background Image
        Image(
            painter = painterResource(id = R.drawable.quantumparticle_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Analysis_Page Content
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Space under the Page Header
            Spacer(modifier = Modifier.height(60.dp))

            // Row of Buttons: "|ψ(x)|²", "TBD"
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // One Left, one Right
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Button: |ψ(x)|²
                App_Button(
                    text = "|ψ(x)|²",
                    color = C_LightBlue,
                    modifier = Modifier.width(160.dp),
                    onClick = {
                        isButtonClicked = true // Update State
                        if(waveFunctionHasData) { // if Yn_Wave_Data has Data
                            viewModel.calculateProbabilityDensity(viewModel.Yn_Wave_Data)
                            consoleViewModel.addMessage("\n-------------------------")
                            consoleViewModel.addMessage("-> ρ(x) = |ψ(x)|²")
                            consoleViewModel.addMessage("-> Measurable Probability Density")
                        } else { // Yn_Wave_Data is Empty: No Probability Density can be Calculated
                            consoleViewModel.addMessage("\n-------------------------")
                            consoleViewModel.addMessage("-> First Select a Scenario to calculate")
                            consoleViewModel.addMessage("    its Wave Function ψ(x)")
                        }
                    }
                )

                // Button: TBD
                App_Button(
                    text = "TBD",
                    color = C_LightBlue,
                    modifier = Modifier.width(160.dp),
                    onClick = {
                        consoleViewModel.addMessage("\n-------------------------")
                        consoleViewModel.addMessage("-> ψ(x): TBD")
                    }
                )
            } // End Row of Buttons

            Spacer(modifier = Modifier.height(20.dp))

            /* Draw the Wave Function for which the Probability Density
             * is going to be Calculated.
             *
             * Note: the Wave Function has been previously selected
             * in any of the items available in Scenarios 1, 2, 3
             *
             */
            WaveFunctionGraph(
                data = viewModel.Yn_Wave_Data,
                title = "ψ(x) : Wave Function",
                ifBarrier = false, // There is Not a Potential Barrier for this Scenario
                x0 = viewModel.x0,
                L  = viewModel.L
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Draw the Probability Density
            if(isButtonClicked) { // Only Draw if Button is Clicked
                WaveFunctionGraph(
                    data = viewModel.probabilityDensity,
                    title = "|ψ(x)|² : Probability Density",
                    ifBarrier = false, // There is Not a Potential Barrier for this Scenario
                    x0 = viewModel.x0,
                    L  = viewModel.L
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // App Output Console UI: Includes Text Scrolling
            val listState = rememberLazyListState() // Console Scroll State

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
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
    // Analysis_Page Header
    PageHeader(
        "Quantum Particle\n\"Analysis\"",
        C_LightBlue
    )

} // End Function