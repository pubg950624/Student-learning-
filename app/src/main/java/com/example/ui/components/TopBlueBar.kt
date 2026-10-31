package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MockDataProvider
import com.example.ui.theme.RoyalBlue

@Composable
fun TopBlueBar(
  selectedCategory: String,
  isDropdownExpanded: Boolean,
  unreadNotificationsCount: Int,
  onMenuClick: () -> Unit,
  onCategoryClick: () -> Unit,
  onCategorySelect: (String) -> Unit,
  onDismissDropdown: () -> Unit,
  onNotificationClick: () -> Unit,
  onDownloadClick: () -> Unit,
  onSearchClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(RoyalBlue)
      .statusBarsPadding()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Left: Hamburger menu
      IconButton(
        onClick = onMenuClick,
        modifier = Modifier
          .size(44.dp)
          .testTag("nav_drawer_button")
      ) {
        Icon(
          imageVector = Icons.Default.Menu,
          contentDescription = "Navigation Menu",
          tint = Color.White,
          modifier = Modifier.size(26.dp)
        )
      }

      Spacer(modifier = Modifier.width(4.dp))

      // Category Selector ("All" with downward chevron)
      Box {
        Row(
          modifier = Modifier
            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(18.dp))
            .clickable { onCategoryClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("category_selector"),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = selectedCategory,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
          )
          Spacer(modifier = Modifier.width(2.dp))
          Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Select Category",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
        }

        DropdownMenu(
          expanded = isDropdownExpanded,
          onDismissRequest = onDismissDropdown,
          modifier = Modifier.background(Color.White)
        ) {
          MockDataProvider.categories.forEach { category ->
            DropdownMenuItem(
              text = {
                Text(
                  text = category,
                  fontWeight = if (category == selectedCategory) FontWeight.Bold else FontWeight.Normal,
                  color = if (category == selectedCategory) RoyalBlue else Color(0xFF20243A)
                )
              },
              onClick = { onCategorySelect(category) }
            )
          }
        }
      }

      Spacer(modifier = Modifier.weight(1f))

      // Notification icon with red badge
      Box {
        IconButton(
          onClick = onNotificationClick,
          modifier = Modifier
            .size(40.dp)
            .testTag("notification_button")
        ) {
          Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifications",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
          )
        }
        if (unreadNotificationsCount > 0) {
          Box(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .offset(x = (-6).dp, y = 6.dp)
              .size(10.dp)
              .background(Color(0xFFE53935), CircleShape)
          )
        }
      }

      // Downloads icon
      IconButton(
        onClick = onDownloadClick,
        modifier = Modifier
          .size(40.dp)
          .testTag("downloads_button")
      ) {
        Icon(
          imageVector = Icons.Default.FileDownload,
          contentDescription = "Downloads",
          tint = Color.White,
          modifier = Modifier.size(24.dp)
        )
      }

      // Search icon
      IconButton(
        onClick = onSearchClick,
        modifier = Modifier
          .size(40.dp)
          .testTag("search_button")
      ) {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = "Search",
          tint = Color.White,
          modifier = Modifier.size(24.dp)
        )
      }
    }
  }
}
