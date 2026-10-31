package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BannerItem
import com.example.model.FeatureType
import com.example.ui.components.NineFeatureGrid
import com.example.ui.components.PromotionalCarouselBanner
import com.example.ui.components.StudentGreeting
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.TextPrimary

@Composable
fun HomeScreen(
  banners: List<BannerItem>,
  currentBannerIndex: Int,
  onBannerIndexChanged: (Int) -> Unit,
  onExploreBatch: (BannerItem) -> Unit,
  onViewBatchDetails: (BannerItem) -> Unit,
  onFeatureClick: (FeatureType) -> Unit,
  onShowAllCourses: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF7F8FC)),
    contentAlignment = Alignment.TopCenter
  ) {
    Column(
      modifier = Modifier
        .widthIn(max = 600.dp) // Responsive centering for tablets and foldables
        .fillMaxSize()
        .verticalScroll(scrollState)
    ) {
      // 1. GREETING
      StudentGreeting(
        studentName = "Student"
      )

      // 2. LIVE ANNOUNCEMENT TICKER
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp)
          .background(Color(0xFFFFF7ED), RoundedCornerShape(8.dp))
          .border(1.dp, Color(0xFFFFEDD5), RoundedCornerShape(8.dp))
          .padding(horizontal = 12.dp, vertical = 7.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Campaign,
            contentDescription = null,
            tint = AccentOrange,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Notice: SSC CGL 2026 Tier-1 Mock Test 4 is now LIVE! Attempt before Sunday.",
            fontSize = 11.5.sp,
            color = Color(0xFF9A3412),
            fontWeight = FontWeight.Medium,
            maxLines = 1
          )
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      // 3. PROMOTIONAL CAROUSEL BANNER
      PromotionalCarouselBanner(
        banners = banners,
        currentIndex = currentBannerIndex,
        onIndexChanged = onBannerIndexChanged,
        onExploreClick = onExploreBatch,
        onDetailsClick = onViewBatchDetails
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Section Title for Nine Features
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Study Zone & Test Series",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = DarkNavy
        )

        Row(
          modifier = Modifier.clickable { onShowAllCourses() },
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "View All",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = RoyalBlue
          )
          Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = RoyalBlue,
            modifier = Modifier.size(16.dp)
          )
        }
      }

      // 4. NINE FEATURE CARDS (3x3 Grid)
      NineFeatureGrid(
        onFeatureClick = onFeatureClick
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Daily Preparation Card / Motivation Banner
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .shadow(1.dp, RoundedCornerShape(12.dp), spotColor = Color(0x15000000))
          .background(Color.White, RoundedCornerShape(12.dp))
          .border(1.dp, Color(0xFFE5E9F0), RoundedCornerShape(12.dp))
          .clickable { onFeatureClick(FeatureType.DAILY_QUIZ) }
          .padding(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .background(Color(0xFFE0F2FE), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.TrendingUp,
              contentDescription = null,
              tint = Color(0xFF0284C7),
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Sharpen Your Speed Today",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
            Text(
              text = "Complete your 10-minute Daily Quiz & boost your All-India rank.",
              fontSize = 11.5.sp,
              color = Color(0xFF6C7289)
            )
          }

          Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color(0xFF94A3B8),
            modifier = Modifier.size(20.dp)
          )
        }
      }

      // Bottom padding so content never hides behind navigation bar
      Spacer(modifier = Modifier.height(80.dp))
    }
  }
}
