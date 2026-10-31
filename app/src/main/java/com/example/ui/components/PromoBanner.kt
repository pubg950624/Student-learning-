package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BannerItem
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.TextPrimary

@Composable
fun StudentGreeting(
  studentName: String = "Student",
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(Color.White)
      .padding(horizontal = 16.dp, vertical = 14.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Hello, $studentName",
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimary,
          modifier = Modifier.testTag("greeting_text")
        )
        Text(
          text = "Let's crack your dream exam today!",
          fontSize = 13.sp,
          color = Color(0xFF6C7289),
          fontWeight = FontWeight.Normal
        )
      }

      // Small learning badge/streak indicator
      Row(
        modifier = Modifier
          .background(Color(0xFFFFF7ED), RoundedCornerShape(14.dp))
          .border(1.dp, Color(0xFFFFEDD5), RoundedCornerShape(14.dp))
          .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = "🔥", fontSize = 13.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "Day 7 Streak",
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color(0xFFC2410C)
        )
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PromotionalCarouselBanner(
  banners: List<BannerItem>,
  currentIndex: Int,
  onIndexChanged: (Int) -> Unit,
  onExploreClick: (BannerItem) -> Unit,
  onDetailsClick: (BannerItem) -> Unit,
  modifier: Modifier = Modifier
) {
  val currentBanner = banners.getOrElse(currentIndex) { banners.first() }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
  ) {
    // Banner Card with dark navy gradient and educational abstract shapes
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(
          Brush.linearGradient(
            colors = listOf(
              DarkNavy,
              Color(0xFF1B3264),
              Color(0xFF101E3D)
            ),
            start = Offset(0f, 0f),
            end = Offset(1000f, 600f)
          )
        )
        .testTag("promo_banner_card")
    ) {
      // Abstract background art representing education and success
      Canvas(modifier = Modifier.matchParentSize()) {
        val width = size.width
        val height = size.height

        // Subtle decorative glowing circles
        drawCircle(
          color = RoyalBlue.copy(alpha = 0.25f),
          radius = height * 0.7f,
          center = Offset(width * 0.95f, height * 0.2f)
        )
        drawCircle(
          color = AccentOrange.copy(alpha = 0.15f),
          radius = height * 0.45f,
          center = Offset(width * 0.85f, height * 0.85f)
        )
      }

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp)
      ) {
        // Tag + Price row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Title Badge
          Box(
            modifier = Modifier
              .background(AccentOrange, RoundedCornerShape(6.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = currentBanner.title,
              color = Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 0.5.sp
            )
          }

          // Price Tag
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = currentBanner.originalPrice,
              color = Color.White.copy(alpha = 0.6f),
              fontSize = 12.sp,
              textDecoration = TextDecoration.LineThrough
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = currentBanner.price,
              color = Color(0xFFFFD54F),
              fontSize = 18.sp,
              fontWeight = FontWeight.Black
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Main Hindi Heading + Avatar/Illustration Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = currentBanner.hindiHeading,
              fontSize = 22.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = currentBanner.subTitle,
              fontSize = 12.sp,
              color = Color(0xFFCAD5E8),
              maxLines = 2
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          // Generic Illustrated Education Emblem
          Box(
            modifier = Modifier
              .size(54.dp)
              .background(Color.White.copy(alpha = 0.12f), CircleShape)
              .border(1.5.dp, Color.White.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.School,
              contentDescription = "Education Batch",
              tint = AccentOrange,
              modifier = Modifier.size(32.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Compact Feature Labels
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          currentBanner.features.forEach { feature ->
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF4ADE80),
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = feature,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Two CTA buttons: "Explore Batch" & "View Details"
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = { onExploreClick(currentBanner) },
            colors = ButtonDefaults.buttonColors(
              containerColor = AccentOrange,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .weight(1f)
              .height(38.dp)
              .testTag("explore_batch_button")
          ) {
            Text(
              text = "Explore Batch",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }

          OutlinedButton(
            onClick = { onDetailsClick(currentBanner) },
            border = ButtonDefaults.outlinedButtonBorder.copy(
              brush = Brush.horizontalGradient(listOf(Color.White, Color.White.copy(alpha = 0.7f)))
            ),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(
              contentColor = Color.White
            ),
            modifier = Modifier
              .weight(1f)
              .height(38.dp)
              .testTag("view_details_button")
          ) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = null,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "View Details",
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Carousel Pagination Dots
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      banners.indices.forEach { index ->
        val isSelected = index == currentIndex
        Box(
          modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(if (isSelected) 18.dp else 7.dp, 7.dp)
            .clip(CircleShape)
            .background(if (isSelected) RoyalBlue else Color(0xFFD1D5DB))
            .clickable { onIndexChanged(index) }
        )
      }
    }
  }
}
