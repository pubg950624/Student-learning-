package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun BatchDetailScreen(
  batch: BannerItem,
  isEnrolled: Boolean,
  onEnrollClick: (String) -> Unit,
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF7F8FC)),
    contentAlignment = Alignment.TopCenter
  ) {
    Column(
      modifier = Modifier
        .widthIn(max = 600.dp)
        .fillMaxSize()
    ) {
      // Top Royal Blue Bar
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(RoyalBlue)
          .statusBarsPadding()
          .padding(horizontal = 8.dp, vertical = 6.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = onBackClick) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Color.White
            )
          }

          Text(
            text = "Batch Overview",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      // Scrollable content
      Column(
        modifier = Modifier
          .weight(1f)
          .verticalScroll(rememberScrollState())
          .padding(16.dp)
      ) {
        // Hero Batch Card
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = DarkNavy)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Box(
              modifier = Modifier
                .background(AccentOrange, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = batch.title,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = batch.hindiHeading,
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )

            Text(
              text = batch.subTitle,
              fontSize = 13.sp,
              color = Color(0xFFCAD5E8),
              modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Event,
                contentDescription = null,
                tint = Color(0xFFFFD54F),
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = batch.batchStartDate,
                fontSize = 12.sp,
                color = Color.White
              )
              Spacer(modifier = Modifier.width(16.dp))
              Icon(
                imageVector = Icons.Default.People,
                contentDescription = null,
                tint = Color(0xFFFFD54F),
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "18,400+ Enrolled",
                fontSize = 12.sp,
                color = Color.White
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // What You Will Get
        Text(
          text = "What You Will Get in this Batch",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = DarkNavy
        )

        Spacer(modifier = Modifier.height(10.dp))

        val perks = listOf(
          "300+ Hours Live Interactive Classes by Top Faculty",
          "150+ Downloadable & Printable PDF Handwritten Notes",
          "25 Full Length Mock Tests + 60 Sectional Practice Quizzes",
          "Instant Doubt Resolution & Teacher Live Interaction",
          "Recorded Class Backups available 24x7 with offline access",
          "1-Year Unlimited Course Validity until Exam 2026"
        )

        perks.forEach { perk ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 5.dp),
            verticalAlignment = Alignment.Top
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = Color(0xFF16A34A),
              modifier = Modifier
                .size(18.dp)
                .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = perk,
              fontSize = 13.sp,
              color = TextPrimary,
              lineHeight = 18.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Syllabus Coverage Breakdown
        Text(
          text = "Subjects & Syllabus Covered",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = DarkNavy
        )

        Spacer(modifier = Modifier.height(10.dp))

        val subjects = listOf(
          "General Awareness & Current Affairs (History, Polity, Geo, Economy)",
          "Quantitative Aptitude (Arithmetic & Advanced Mathematics)",
          "General Intelligence & Logical Reasoning",
          "English Language & Comprehension Skills",
          "General Science (Physics, Chemistry & Biology One-Liners)"
        )

        subjects.forEach { subject ->
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .background(Color.White, RoundedCornerShape(8.dp))
              .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
              .padding(12.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .background(RoyalBlue, CircleShape)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = subject,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))
      }

      // Sticky Bottom Enrollment Bar
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color.White)
          .border(0.8.dp, Color(0xFFE5E9F0))
          .navigationBarsPadding()
          .padding(horizontal = 16.dp, vertical = 10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = batch.price,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DarkNavy
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = batch.originalPrice,
                fontSize = 13.sp,
                color = Color(0xFF94A3B8),
                textDecoration = TextDecoration.LineThrough
              )
            }
            Text(
              text = "${batch.discountPercent} Special Offer",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF16A34A)
            )
          }

          Button(
            onClick = { onEnrollClick(batch.id) },
            enabled = !isEnrolled,
            colors = ButtonDefaults.buttonColors(
              containerColor = AccentOrange,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .height(44.dp)
              .testTag("enroll_now_button")
          ) {
            Text(
              text = if (isEnrolled) "Enrolled ✓" else "Enroll Now",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
