package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenDestination
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.TextPrimary

data class DrawerMenuItem(
  val title: String,
  val icon: ImageVector,
  val destination: ScreenDestination? = null,
  val action: (() -> Unit)? = null
)

@Composable
fun DrawerContent(
  onNavigate: (ScreenDestination) -> Unit,
  onCloseDrawer: () -> Unit,
  onShowToast: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val menuItems = listOf(
    DrawerMenuItem("Home", Icons.Default.Home, ScreenDestination.HOME),
    DrawerMenuItem("AI Study Tutor", Icons.Default.AutoAwesome, ScreenDestination.AI_TUTOR),
    DrawerMenuItem("Courses & Batches", Icons.Default.Book, ScreenDestination.COURSES),
    DrawerMenuItem("Daily Quiz & Tests", Icons.Default.Quiz, ScreenDestination.TESTS),
    DrawerMenuItem("My Profile", Icons.Default.Person, ScreenDestination.PROFILE),
    DrawerMenuItem("Downloaded Notes", Icons.Default.Download, ScreenDestination.MY_DOWNLOADS),
    DrawerMenuItem("My Submissions", Icons.Default.Assignment, ScreenDestination.MY_SUBMISSIONS),
    DrawerMenuItem("My Orders", Icons.Default.Receipt, ScreenDestination.MY_ORDERS),
    DrawerMenuItem("FAQs", Icons.Default.Help, ScreenDestination.FAQS),
    DrawerMenuItem("Terms & Conditions", Icons.Default.Info, ScreenDestination.TERMS_CONDITIONS),
    DrawerMenuItem("Help & Support", Icons.Default.Help, ScreenDestination.HELP_SUPPORT),
    DrawerMenuItem("Admin Portal (Netlify)", Icons.Default.Settings, ScreenDestination.ADMIN_PORTAL),
    DrawerMenuItem("Share App", Icons.Default.Share, action = {
      onShowToast("App link copied to clipboard!")
      onCloseDrawer()
    }),
    DrawerMenuItem("Logout", Icons.Default.Logout, action = {
      onShowToast("Logged out successfully")
      onCloseDrawer()
    })
  )

  Column(
    modifier = modifier
      .fillMaxHeight()
      .fillMaxWidth(0.8f)
      .background(androidx.compose.ui.graphics.Color.White)
      .testTag("navigation_drawer")
  ) {
    // Header
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(RoyalBlue)
        .padding(24.dp)
    ) {
      Column {
        Box(
          modifier = Modifier
            .size(56.dp)
            .background(androidx.compose.ui.graphics.Color.White, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = RoyalBlue,
            modifier = Modifier.size(32.dp)
          )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "Student Portal",
          color = androidx.compose.ui.graphics.Color.White,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "student@learning.com",
          color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f),
          fontSize = 12.sp
        )
      }
    }

    Divider()

    // Menu Items
    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .padding(vertical = 8.dp)
    ) {
      items(menuItems) { item ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              if (item.destination != null) {
                onNavigate(item.destination)
                onCloseDrawer()
              } else {
                item.action?.invoke()
              }
            }
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .testTag("drawer_item_${item.title.lowercase().replace(" ", "_")}"),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = item.icon,
            contentDescription = null,
            tint = DarkNavy,
            modifier = Modifier.size(22.dp)
          )
          Spacer(modifier = Modifier.width(16.dp))
          Text(
            text = item.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary
          )
        }
      }
    }
  }
}
