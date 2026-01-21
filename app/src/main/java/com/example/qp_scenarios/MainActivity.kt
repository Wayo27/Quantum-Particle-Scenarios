/** Project: QP_Scenarios: Quantum Particle Scenarios
 *
 * Author: Eduardo León-Prado
 * Start Date: 16/01/2026
 * Last Update: 21/01/2026
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

// ================================
// ===== GLOBAL DECLARATIONS ======
// ================================
val C_LightGreen = Color(0xFF90EE90) // Light Green
val C_LightBlue  = Color(0xFFADD8E6) // Light Blue
val C_DeepPink   = Color(0xFFFF1493) // Deep Pink
val C_LightYellow = Color(0xFFFFF9C4) // Light Yellow
val C_LightOrange = Color(0xFFFFA500) // Light Orange
val C_Orange = Color(0xFFFF9800) // Orange
val C_LightPink = Color(0xFFFFC0CB) // Light Pink
val C_Well_Length = 1260; // Defines the Well Length
val C_Nbr_Waves = 1261; // Maximum Number of Waves (3,5 Waves along the axis)
val C_Wave_Multiplier = 20; // Short Function Height
val consoleViewModel = ConsoleViewModel() // view Model for Output Console
const val platesDistance = 400 // Plates Distance or X axis Length
enum class BarrierScenario {
    ENERGY_GREATER_THAN_BARRIER,
    ENERGY_LESS_THAN_BARRIER
}


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

    NavHost(
        navController = navController,
        startDestination = Routes.MAIN
    ) {
        // Route to MainPage
        composable(Routes.MAIN) { backStackEntry ->

            val sharedViewModel: QuantumViewModel =
                viewModel(backStackEntry)

            MainPage(
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
                onBack = { navController.popBackStack() }, // Define the way Back to MainPage
                viewModel = sharedViewModel
            )
        }

        // Route to QP_Scenario2_Page: Particle moving between parallel plates
        composable(Routes.PLATES) {

            val sharedViewModel: QuantumViewModel =
                viewModel(navController.getBackStackEntry(Routes.MAIN))

            QP_Scenario2_Page(
                onBack = { navController.popBackStack() }, // Define the way Back to MainPage
                viewModel = sharedViewModel
            )
        }

        // Route to QP_Scenario3_Page: Particle moving along the X axis with a Potential Barrier
        composable(Routes.BARRIER) {

            val sharedViewModel: QuantumViewModel =
                viewModel(navController.getBackStackEntry(Routes.MAIN))

            QP_Scenario3_Page(
                onBack = { navController.popBackStack() }, // Define the way Back to MainPage
                viewModel = sharedViewModel
            )
        }

        // Route to Analysis_Page
        composable(Routes.ANALYSIS) {

            val sharedViewModel: QuantumViewModel =
                viewModel(navController.getBackStackEntry(Routes.MAIN))

            Analysis_Page(
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

            Spacer(modifier = Modifier.height(30.dp))

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

            Spacer(modifier = Modifier.height(30.dp))

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

            Spacer(modifier = Modifier.height(30.dp))

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


/** Composable Function: QP_Scenario1_Page()
 *
 * Description: Quantum Particle confined inside a WELL.
 * - Inside the Well, the Potential Energy is U(x) = 0
 * - Outside the Well, the Potential Energy is U(x) = "infinite"
 * Therefore the Particle can never scape out of the Well.
 *
 */
@Composable
fun QP_Scenario1_Page(onBack: () -> Unit,
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
            Spacer(modifier = Modifier.height(60.dp))

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
                        consoleViewModel.addMessage("\n-------------------------")
                        consoleViewModel.addMessage("\n-> ψ(x) Wave Function Orbital n = $orbitalNumber")
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
                        consoleViewModel.addMessage("\n-------------------------")
                        consoleViewModel.addMessage("\n-> ψ(x) Wave Function Orbital n = $orbitalNumber")
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
                        consoleViewModel.addMessage("\n-------------------------")
                        consoleViewModel.addMessage("\n-> ψ(x) Wave Function Orbital n = $orbitalNumber")
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
                        consoleViewModel.addMessage("\n-------------------------")
                        consoleViewModel.addMessage("\n-> ψ(x) Wave Function Orbital n = $orbitalNumber")
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
                        consoleViewModel.addMessage("\n-------------------------")
                        consoleViewModel.addMessage("\n-> ψ(x) Wave Function Orbital n = $orbitalNumber")
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
                        consoleViewModel.addMessage("\n-------------------------")
                        consoleViewModel.addMessage("\n-> ψ(x) Wave Function Orbital n = $orbitalNumber")
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
                        consoleViewModel.addMessage("\n-------------------------")
                        consoleViewModel.addMessage("\n-> ψ(x) Wave Function Orbital n = $orbitalNumber")
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
                        consoleViewModel.addMessage("\n-------------------------")
                        consoleViewModel.addMessage("\n-> ψ(x) Wave Function Orbital n = $orbitalNumber")
                    }
                )
            } // End Row of Buttons

            Spacer(modifier = Modifier.height(20.dp))

            if(orbitalNumber != 0) { // Only Draws Wave Function if Orbital n != 0
                WaveFunctionGraph(
                    data = viewModel.Yn_Wave_Data,
                    title = "ψ(x): Wave Function for n = $orbitalNumber",
                    ifBarrier = false // There is Not Potential Barrier for this Scenario
                )
            }

            /* THIS IS NOT LONGER USED
            if (viewModel.stackedWaves.isNotEmpty()) {
                StackedWaveGraphs(
                    waves = viewModel.stackedWaves,
                    startN = viewModel.currentStartN
                )
            }
            */

            Spacer(modifier = Modifier.height(20.dp))

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

            Spacer(modifier = Modifier.height(20.dp))

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
fun QP_Scenario2_Page(onBack: () -> Unit,
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
fun QP_Scenario3_Page(onBack: () -> Unit,
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


/** Composable Function: Analysis_Page()
 *
 * Description: Analysis of previous Scenarios
 *
 */
@Composable
fun Analysis_Page(
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


/** Composable Function PageHeader()
 *
 * This Function defines the Header UI
 * for any Page within this App:
 * - MainPage
 * - QP_Scenario1_Page
 * - QP_Scenario2_Page
 * - QP_Scenario3_Page
 *
 * Input parameters:
 * - p_text: Text to show in the Header
 * - p_color: Background Color of the Header
 */
@Composable
fun PageHeader(p_text: String, p_color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(
                color = p_color,
                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFE65100),
                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = p_text,
            color = Color.Blue, // Color of the Text
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Center // Center each Line of the Text
        )
    }

} // End Function PageHeader()


/** Composable Function App_Button()
 *
 * This is the Standard Button to be used
 * throughout the App
 *
 */
@Composable
fun App_Button(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = modifier
            .height(40.dp)
            .shadow(2.dp, shape = RoundedCornerShape(6.dp), clip = false)
            .border(
                width = 3.dp,
                color = Color.Blue.copy(alpha = 0.7f),
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }

} // End Function App_Button


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


/** Composable WaveGraph()
 *
 * This is the UI to Draw the Schrodinger
 * Wave Forms Yn_Wave(x)
 *
 */
@Composable
fun WaveGraph(
    data: DoubleArray,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
) {
    Canvas(
        modifier = modifier
            .background(Color.White)
            .border(1.dp, Color.Black)
    ) {
        if (data.isEmpty()) return@Canvas

        val midY = size.height / 2f
        val scaleX = size.width / (data.size - 1)
        val scaleY = 1f   // Already Scaled with C_Wave_Multiplier

        for (i in 0 until data.size - 1) {
            drawLine(
                color = Color.Blue,
                start = Offset(
                    x = i * scaleX,
                    y = midY - (data[i] * scaleY).toFloat()
                ),
                end = Offset(
                    x = (i + 1) * scaleX,
                    y = midY - (data[i + 1] * scaleY).toFloat()
                ),
                strokeWidth = 2f
            )
        }

        // X axis
        drawLine(
            color = Color.Gray,
            start = Offset(0f, midY),
            end = Offset(size.width, midY),
            strokeWidth = 1f
        )
    }

} // End Function


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


/** Composable Function WaveFunctionGraph()
 *
 * This Function Graphics the Solution of
 * Schrodinger´s Equation as a Wave Function
 * on a Canvas UI
 *
 */

@Composable
fun WaveFunctionGraph(
    data: DoubleArray,
    title: String,
    ifBarrier: Boolean,
    x0: Double? = null,   // Optional: Barrier Initial Position
    L: Double? = null,    // Optional: Barrier Length
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(120.dp)
) {
    Column(modifier = modifier.padding(horizontal = 8.dp)) {

        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(Color.White)
                .border(1.dp, Color.Gray)
        ) {
            if (data.isEmpty()) return@Canvas

            val midY = size.height / 2f
            val scaleX = size.width / (data.size - 1)

            val maxY = data.maxOf { kotlin.math.abs(it) }.toFloat()
            val verticalScale =
                if (maxY == 0f) 1f
                else (size.height * 0.45f) / maxY

            /* ===============================
               X axis
               =============================== */
            drawLine(
                color = Color.LightGray,
                start = Offset(0f, midY),
                end = Offset(size.width, midY),
                strokeWidth = 1f
            )

            /* =================================================
               Barrier (only if there is a Barrier: Scenario 3)
               ================================================= */
            if(ifBarrier) {
                if (x0 != null && L != null && L > 0.0) {

                    val x0Pixel = (x0 / (data.size - 1)) * size.width
                    val xLPixel = ((x0 + L) / (data.size - 1)) * size.width

                    val dashEffect = PathEffect.dashPathEffect(
                        floatArrayOf(10f, 8f),
                        0f
                    )

                    drawLine(
                        color = Color.Gray,
                        start = Offset(x0Pixel.toFloat(), 0f),
                        end = Offset(x0Pixel.toFloat(), size.height),
                        strokeWidth = 2f,
                        pathEffect = dashEffect
                    )

                    drawLine(
                        color = Color.Gray,
                        start = Offset(xLPixel.toFloat(), 0f),
                        end = Offset(xLPixel.toFloat(), size.height),
                        strokeWidth = 2f,
                        pathEffect = dashEffect
                    )
                }
            } // End of Potential Barrier

            /* ===============================
               Wave Function ψ(x)
               =============================== */
            for (i in 0 until data.size - 1) {
                drawLine(
                    color = Color.Blue,
                    start = Offset(
                        i * scaleX,
                        midY - data[i].toFloat() * verticalScale
                    ),
                    end = Offset(
                        (i + 1) * scaleX,
                        midY - data[i + 1].toFloat() * verticalScale
                    ),
                    strokeWidth = 2f
                )
            }
        }
    }

} // End Function


/** Composable Function StackedWaveGraph()
 *
 * This Function draws several Wave Forms
 * corresponding to different Orbital Number (n)
 * each in a different Level
 *
 */
@Composable
fun StackedWaveGraphs(
    waves: List<DoubleArray>,
    startN: Int
) {
    Column {
        waves.forEachIndexed { index, data ->
            WaveFunctionGraph(
                data = data,
                title = "ψ${startN + index}(x)",
                ifBarrier = false // There is Not a Potential Barrier for this Scenario
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

} // End Function


/** Function drawVerticalDashedLine()
 *
 * This function is used to draw a vertical Dashed Line
 * to show the Barrier Limits
 */
fun DrawScope.drawVerticalDashedLine(
    x: Float,
    color: Color,
    dashLength: Float = 12f,
    gapLength: Float = 10f,
    strokeWidth: Float = 2f
) {
    drawLine(
        color = color,
        start = Offset(x, 0f),
        end = Offset(x, size.height),
        strokeWidth = strokeWidth,
        pathEffect = PathEffect.dashPathEffect(
            floatArrayOf(dashLength, gapLength),
            0f
        )
    )

} // End Function


/** Helper Function rk4Step()
 *
 * This is a Helper Function to find the solution
 * to the differential equation (scenario 2)
 * using Runge-Kutta method
 *
 * Note: Kotlin/Compose Do Not Implement Airy Equation
 * solution, therefore it is solved using Runge-Kutta
 *
 */
private fun rk4Step(
    i: Int,
    x: Double,
    h: Double,
    y: DoubleArray,
    dy: DoubleArray
) {
    val k1y = dy[i]
    val k1dy = x * y[i]

    val k2y = dy[i] + 0.5 * h * k1dy
    val k2dy = (x + 0.5 * h) * (y[i] + 0.5 * h * k1y)

    val k3y = dy[i] + 0.5 * h * k2dy
    val k3dy = (x + 0.5 * h) * (y[i] + 0.5 * h * k2y)

    val k4y = dy[i] + h * k3dy
    val k4dy = (x + h) * (y[i] + h * k3y)

    y[i + 1] = y[i] + h * (k1y + 2*k2y + 2*k3y + k4y) / 6.0
    dy[i + 1] = dy[i] + h * (k1dy + 2*k2dy + 2*k3dy + k4dy) / 6.0

} // End Function


/** Helper Function rk4StepBackward()
 *
 * This is a Helper Function to find the solution
 * to the differential equation (scenario 2)
 * using Runge-Kutta method
 *
 * Note: Kotlin/Compose Do Not Implement Airy Equation
 * solution, therefore it is solved using Runge-Kutta
 *
 */
private fun rk4StepBackward(
    i: Int,
    x: Double,
    h: Double,
    y: DoubleArray,
    dy: DoubleArray
) {
    val k1y = -dy[i]
    val k1dy = -x * y[i]

    val k2y = -(dy[i] + 0.5 * h * k1dy)
    val k2dy = -(x - 0.5 * h) * (y[i] + 0.5 * h * k1y)

    val k3y = -(dy[i] + 0.5 * h * k2dy)
    val k3dy = -(x - 0.5 * h) * (y[i] + 0.5 * h * k2y)

    val k4y = -(dy[i] + h * k3dy)
    val k4dy = -(x - h) * (y[i] + h * k3y)

    y[i - 1] = y[i] + h * (k1y + 2*k2y + 2*k3y + k4y) / 6.0
    dy[i - 1] = dy[i] + h * (k1dy + 2*k2dy + 2*k3dy + k4dy) / 6.0

} // End Function

// END OF APPLICATION