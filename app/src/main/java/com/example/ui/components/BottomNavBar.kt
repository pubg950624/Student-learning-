package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenDestination
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.TextPrimary

private data class NavTabItem(
  val destination: ScreenDestination,
  val label: String,
  val filledIcon: ImageVector,
  val outlinedIcon: ImageVector
)

@Composable
fun BottomNavBar(
  currentDestination: ScreenDestination,
  onNavigate: (ScreenDestination) -> Unit,
  modifier: Modifier = Modifier
) {
  val tabs = listOf(
    NavTabItem(ScreenDestination.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home),
    NavTabItem(ScreenDestination.COURSES, "Courses", Icons.Filled.PlayCircle, Icons.Outlined.PlayCircle),
    NavTabItem(ScreenDestination.TESTS, "Tests", Icons.Filled.Assignment, Icons.Outlined.Assignment),
    NavTabItem(ScreenDestination.PROFILE, "Profile", Icons.Filled.Person, Icons.Outlined.Person)
  )

  Column(
    modifier = modifier
      .fillMaxWidth()
      .shadow(8.dp, spotColor = Color(0x1F000000))
      .background(Color.White)
      .navigationBarsPadding()
  ) {
    HorizontalDivider(color = Color(0xFFE5E9F0), thickness = 0.8.dp)

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(60.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      tabs.forEach { tab ->
        val isSelected = currentDestination == tab.destination
        val activeColor = RoyalBlue
        val inactiveColor = Color(0xFF6C7289)

        Box(
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigate(tab.destination) }
            .testTag("nav_tab_${tab.label.lowercase()}"),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            // Active indicator pill
            Box(
              modifier = Modifier
                .size(width = 44.dp, height = 28.dp)
                .background(
                  if (isSelected) RoyalBlue.copy(alpha = 0.12f) else Color.Transparent,
                  RoundedCornerShape(14.dp)
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (isSelected) tab.filledIcon else tab.outlinedIcon,
                contentDescription = tab.label,
                tint = if (isSelected) activeColor else inactiveColor,
                modifier = Modifier.size(22.dp)
              )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
              text = tab.label,
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) activeColor else inactiveColor
            )
          }
        }
      }
    }
  }
}
