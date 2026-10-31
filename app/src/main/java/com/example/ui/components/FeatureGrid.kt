package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FeatureType
import com.example.ui.theme.TextPrimary

data class FeatureCardConfig(
  val feature: FeatureType,
  val icon: ImageVector,
  val iconBgColor: Color,
  val iconTintColor: Color,
  val badgeText: String? = null
)

@Composable
fun NineFeatureGrid(
  onFeatureClick: (FeatureType) -> Unit,
  modifier: Modifier = Modifier
) {
  // Row 1: Paid Classes, Free Courses, Free Weekly Test
  // Row 2: Books, PDF Class Notes, Paid Test Series
  // Row 3: Previous Syllabus, Create Test, Daily Quiz
  val rows = listOf(
    listOf(
      FeatureCardConfig(FeatureType.AI_TUTOR, Icons.Default.AutoAwesome, Color(0xFFF3E8FF), Color(0xFF7E22CE), "GEMINI"),
      FeatureCardConfig(FeatureType.PAID_CLASSES, Icons.Default.VideoLibrary, Color(0xFFFFF0EB), Color(0xFFE65100), "LIVE"),
      FeatureCardConfig(FeatureType.FREE_COURSES, Icons.Default.PlayCircle, Color(0xFFE8F5E9), Color(0xFF2E7D32), "FREE")
    ),
    listOf(
      FeatureCardConfig(FeatureType.BOOKS, Icons.Default.Book, Color(0xFFEDE7F6), Color(0xFF5E35B1)),
      FeatureCardConfig(FeatureType.PDF_CLASS_NOTES, Icons.Default.PictureAsPdf, Color(0xFFE0F7FA), Color(0xFF00838F), "PDF"),
      FeatureCardConfig(FeatureType.PAID_TEST_SERIES, Icons.Default.Assignment, Color(0xFFFFEBEE), Color(0xFFC62828))
    ),
    listOf(
      FeatureCardConfig(FeatureType.FREE_WEEKLY_TEST, Icons.Default.EmojiEvents, Color(0xFFFFF8E1), Color(0xFFF57F17), "TEST"),
      FeatureCardConfig(FeatureType.CREATE_TEST, Icons.Default.Edit, Color(0xFFF3E5F5), Color(0xFF8E24AA), "NEW"),
      FeatureCardConfig(FeatureType.DAILY_QUIZ, Icons.Default.AutoAwesome, Color(0xFFE1F5FE), Color(0xFF0277BD), "DAILY")
    )
  )

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp, vertical = 6.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    rows.forEach { rowItems ->
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        rowItems.forEach { item ->
          Box(modifier = Modifier.weight(1f)) {
            FeatureGridItem(
              config = item,
              onClick = { onFeatureClick(item.feature) }
            )
          }
        }
      }
    }
  }
}

@Composable
fun FeatureGridItem(
  config: FeatureCardConfig,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .aspectRatio(0.96f) // Maintain square-proportional aspect for clean 3-col layout
      .shadow(1.dp, RoundedCornerShape(10.dp), spotColor = Color(0x1A000000))
      .border(1.dp, Color(0xFFE5E9F0), RoundedCornerShape(10.dp))
      .clickable { onClick() }
      .testTag("feature_card_${config.feature.id}"),
    shape = RoundedCornerShape(10.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White)
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 6.dp, vertical = 8.dp),
      contentAlignment = Alignment.Center
    ) {
      // Optional badge tag (e.g. FREE, PDF, LIVE)
      config.badgeText?.let { badge ->
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .background(config.iconTintColor.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
            .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
          Text(
            text = badge,
            color = config.iconTintColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
      ) {
        // Icon circular container
        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(config.iconBgColor),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = config.icon,
            contentDescription = config.feature.label,
            tint = config.iconTintColor,
            modifier = Modifier.size(26.dp)
          )
        }

        Spacer(modifier = Modifier.height(7.dp))

        // Card Label (centered, readable on 360-430px screens)
        Text(
          text = config.feature.label,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimary,
          textAlign = TextAlign.Center,
          lineHeight = 14.sp,
          maxLines = 2,
          modifier = Modifier.padding(horizontal = 2.dp)
        )
      }
    }
  }
}
