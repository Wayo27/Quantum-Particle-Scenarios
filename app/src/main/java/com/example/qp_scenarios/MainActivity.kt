/** Project: QP_Scenarios: Quantum Particle Scenarios
 *
 * Author: Eduardo León-Prado
 * Start Date: 16/01/2026
 * Last Update: 25/01/2026
 *
 * Description:
 * This App analyzes several Scenarios of a Quantum Particle
 * moving in one Dimension "X" and independent of Time "t"
 *
 * This App has the following Pages:
 * - "Scenario 1": Particle confined within a Potential Well.
 *   The Potential Energy inside the Well is U(x) = 0 for X: <0-L>
 *   and outside the Well U(x) = infinite, so the particle can not scape
 *   from the Well. Wave Function for Orbital Numbers n <1-8> are selectable.
 * - "Scenario 2": Particle moving within 2 parallel infinite metal plates
 *   connected to a potential gradient. Here there are 4 Scenarios:
 *      - Low Energy: Particle close to the left Plate
 *      - Higher Energy: Particle in the Field between the Plates
 *      - Airy Solution to the Schrodinger Differential Equation
 *      - Simulation of a Bessel Kind (Like) Function with Animation
 * - "Scenario 3": Particle of Energy E moving along the X axis with
 *   the following features:
 *      1) Free region U(x) = 0, for X < 0
 *      2) Potential Barrier of Energy U(x) = U for 0 <= X <= L
 *      3) Free region U(x) = 0, for X > L
 *   Options  E > U & E < U are selectable for this Scenario.
 * - "Analysis" Page to Draw the Density of Probability of the Quantum Particle
 *   for the last Selected Scenario, which has a "Real" physical meaning unlike
 *   just the Wave Functions graphs.
 *
 */


package com.example.qp_scenarios

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qp_scenarios.ui.theme.QP_ScenariosTheme
import kotlin.math.sin
import kotlin.math.asin
import kotlin.math.PI
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.exp


// App Entry Point
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QP_ScenariosTheme {

                // App Navigation Function
                AppNavigation()

            }
        }
    }
}


/** Composable Function: AppNavigation()
 *
 * This Function allows the App to Navigate to different Pages:
 *  - MainPage
 *  - QP_Scenario1_Page: Particle confined to move within Infinite Walls Well
 *  - QP_Scenario2_Page: Particle moving between 2 Parallel Plates (Electric Field)
 *  - QP_Scenario3_Page: Particle Moving along the X axis with a Potential Barrier
 *  - Analysis_Page: Analyzes the above Scenarios
 *
 * A NavHost is used to define the navigation graph, specifying the composables that correspond
 * to each destination.
 * A NavController is used to manage the navigation state, including the back stack and navigating
 * between screens.
 *
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Creates ConsoleViewModel for App Output Console
    val consoleViewModel: ConsoleViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.MAIN
    ) {
        // Route to MainPage
        composable(Routes.MAIN) { backStackEntry ->

            val sharedViewModel: QuantumViewModel =
                viewModel(backStackEntry)

            MainPage(
                consoleViewModel = consoleViewModel,
                onScenario1 = { navController.navigate(Routes.WELL) },
                onScenario2 = { navController.navigate(Routes.PLATES) },
                onScenario3 = { navController.navigate(Routes.BARRIER) },
                onAnalysis  = { navController.navigate(Routes.ANALYSIS) }
            )
        }

        // Route to QP_Scenario1_Page: Particle confined within a well
        composable(Routes.WELL) {

            val sharedViewModel: QuantumViewModel =
                viewModel(navController.getBackStackEntry(Routes.MAIN))

            QP_Scenario1_Page(
                consoleViewModel = consoleViewModel,
                onBack = { navController.popBackStack() }, // Define the way Back to MainPage
                viewModel = sharedViewModel
            )
        }

        // Route to QP_Scenario2_Page: Particle moving between parallel plates
        composable(Routes.PLATES) {

            val sharedViewModel: QuantumViewModel =
                viewModel(navController.getBackStackEntry(Routes.MAIN))

            QP_Scenario2_Page(
                consoleViewModel = consoleViewModel,
                onBack = { navController.popBackStack() }, // Define the way Back to MainPage
                viewModel = sharedViewModel
            )
        }

        // Route to QP_Scenario3_Page: Particle moving along the X axis with a Potential Barrier
        composable(Routes.BARRIER) {

            val sharedViewModel: QuantumViewModel =
                viewModel(navController.getBackStackEntry(Routes.MAIN))

            QP_Scenario3_Page(
                consoleViewModel = consoleViewModel,
                onBack = { navController.popBackStack() }, // Define the way Back to MainPage
                viewModel = sharedViewModel
            )
        }

        // Route to Analysis_Page
        composable(Routes.ANALYSIS) {

            val sharedViewModel: QuantumViewModel =
                viewModel(navController.getBackStackEntry(Routes.MAIN))

            Analysis_Page(
                consoleViewModel = consoleViewModel,
                onBack = { navController.popBackStack() }, // Define the way Back to MainPage
                viewModel = sharedViewModel
            )
        }
    }

} // End Function AppNavigation()


/** Composable Function: MainPage()
 *
 * MainPage allows User to Navigate between the App Pages:
 *
 * - MainPage (this)
 * - QP_Scenario1_Page
 * - QP_Scenario2_Page
 * - QP_Scenario3_Page
 * - Analysis_Page
 *
 */
@Composable
fun MainPage(
    consoleViewModel: ConsoleViewModel,
    onScenario1: () -> Unit,
    onScenario2: () -> Unit,
    onScenario3: () -> Unit,
    onAnalysis: () -> Unit
) {
    val context = LocalContext.current // Variable to Quit App
    val coroutineScope = rememberCoroutineScope() // Variable for Asynchronous Delay

    // Init Console Message
    LaunchedEffect(Unit) {
        consoleViewModel.clear()
        consoleViewModel.addMessage("<<< Quantum Particle Scenarios >>>")
        consoleViewModel.addMessage("\n1) Particle Confined inside a Well")
        consoleViewModel.addMessage("2) Particle moving between Parallel\n    Plates Connected to a Potential\n    difference \"V\"")
        consoleViewModel.addMessage("3) Particle moving along the X axis\n    finding a Potential Barrier in X:<0-L>")
    }

    Box(modifier = Modifier.fillMaxSize()
    ) {
        // Page Background Image
        Image(
            painter = painterResource(id = R.drawable.quantumparticle_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // MainPage Content
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Space under the Page Header
            Spacer(modifier = Modifier.height(20.dp))

            // Row of Buttons: "Scenario 1", "Scenario 2"
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // One Left, one Right
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Button: "Scenario 1"
                App_Button(
                    text = "Scenario 1",
                    color = C_LightGreen,
                    modifier = Modifier.width(160.dp),
                    onClick = onScenario1 // Goes to QP_Scenario1_Page
                )

                // Button: "Scenario 2"
                App_Button(
                    text = "Scenario 2",
                    color = C_LightYellow,
                    modifier = Modifier.width(160.dp),
                    onClick = onScenario2 // Goes to QP_Scenario2_Page
                )
            } // End Row of Buttons

            Spacer(modifier = Modifier.height(40.dp))

            // Row of Buttons: "Scenario 3", "Analysis"
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // One Left, one Right
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Button: "Scenario 3"
                App_Button(
                    text = "Scenario 3",
                    color = C_LightPink,
                    modifier = Modifier.width(160.dp),
                    onClick = onScenario3 // Goes to QP_Scenario3_Page
                )

                // Button: "Analysis"
                App_Button(
                    text = "QP Analysis",
                    color = C_LightBlue,
                    modifier = Modifier.width(160.dp),
                    onClick = onAnalysis // Goes to Analysis_Page
                )
            } // End Row of Buttons

            Spacer(modifier = Modifier.height(50.dp))

            // App Output Console UI: Includes Text Scrolling
            val listState = rememberLazyListState() // Console Scroll State

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
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

            Spacer(modifier = Modifier.height(50.dp))

            // Button: Quit Application
            App_Button(
                text = "Quit App",
                color = C_DeepPink,
                modifier = Modifier.width(160.dp),
                onClick = {
                    // Delay & Close App
                    consoleViewModel.clear()
                    consoleViewModel.addMessage("<<< Quit Application >>>")
                    consoleViewModel.addMessage("\n-> Closing Appp in 2 secs")

                    coroutineScope.launch {
                        delay(2000)
                        (context as Activity).finish() // Quit App
                    }
                } // End onClick
            )

        } // End Column

    } // End Box

    // *** Note: Header MUST go Here, at the End of the Page ***
    // MainPage Header
    PageHeader(
        "Quantum Particle Scenarios\nDesigned by: ELPD-2026",
        C_LightOrange
    )

} // End Function MainPage


// END OF APPLICATION