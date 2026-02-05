/** App Global Declarations
 *
 */


package com.example.qp_scenarios

import androidx.compose.ui.graphics.Color

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

val platesDistance = 400 // Plates Distance or X axis Length
enum class BarrierScenario {
    ENERGY_GREATER_THAN_BARRIER,
    ENERGY_LESS_THAN_BARRIER
}