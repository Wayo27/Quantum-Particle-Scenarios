package com.example.qp_scenarios

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
public fun rk4Step(
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
public fun rk4StepBackward(
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





