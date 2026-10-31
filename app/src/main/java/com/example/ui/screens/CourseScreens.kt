package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled. School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VideocamOff
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CourseBatch
import com.example.model.RecordedLesson
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.TextPrimary

// 1. COURSE LISTING SCREEN (For Paid Classes & Courses)
@Composable
fun CourseListingScreen(
  title: String,
  courses: List<CourseBatch>,
  onCourseClick: (CourseBatch) -> Unit,
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
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      // Scrollable list of course cards
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        items(courses) { course ->
          CourseListingCard(
            course = course,
            onViewClick = { onCourseClick(course) }
          )
        }
        item {
          Spacer(modifier = Modifier.height(60.dp))
        }
      }
    }
  }
}

@Composable
fun CourseListingCard(
  course: CourseBatch,
  onViewClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("course_card_${course.id}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Wide promotional course banner thumbnail
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp)
          .background(
            Brush.linearGradient(
              colors = listOf(DarkNavy, RoyalBlue, Color(0xFF1B3264))
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(16.dp)
        ) {
          Icon(
            imageVector = Icons.Default.School,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(36.dp)
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = course.examCategory,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }

        // Price badge top right
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(10.dp)
            .background(Color(0xFFFF9B32), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = course.price,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      // Card Body
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp)
      ) {
        // Course title in Hindi or English
        Text(
          text = course.title,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimary,
          maxLines = 2
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Validity section with calendar icon
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = "Validity",
            tint = Color(0xFF64748B),
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = course.validityDate,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF475569)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Prominent blue "View" button
        Button(
          onClick = onViewClick,
          colors = ButtonDefaults.buttonColors(
            containerColor = RoyalBlue,
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .testTag("view_button_${course.id}")
        ) {
          Text(
            text = "View",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

// 2. COURSE DETAIL SCREEN (With Black Header, Tabs: Live, Recorded, Telegram)
@Composable
fun CourseDetailScreen(
  course: CourseBatch,
  selectedTab: Int, // 0 = Live, 1 = Recorded, 2 = Telegram
  onTabSelected: (Int) -> Unit,
  onBackClick: () -> Unit,
  onShowToast: (String) -> Unit,
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
      // Top Black Navigation Bar
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color.Black)
          .statusBarsPadding()
          .padding(horizontal = 4.dp, vertical = 6.dp)
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
            text = course.title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier.weight(1f)
          )
        }
      }

      // Horizontally scrollable tabs: Live, Recorded, Telegram
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color.White)
          .horizontalScroll(rememberScrollState())
          .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        val tabs = listOf("Live", "Recorded", "Telegram")
        tabs.forEachIndexed { index, tabName ->
          val isSelected = selectedTab == index
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(if (isSelected) RoyalBlue else Color(0xFFF1F5F9))
              .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = if (isSelected) Color.Transparent else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(20.dp)
              )
              .clickable { onTabSelected(index) }
              .padding(horizontal = 22.dp, vertical = 8.dp)
              .testTag("course_tab_${tabName.lowercase()}")
          ) {
            Text(
              text = tabName,
              color = if (isSelected) Color.White else Color(0xFF334155),
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      // Tab Content Area
      val context = LocalContext.current
      Box(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .background(Color.White)
      ) {
        when (selectedTab) {
          0 -> LiveTabContent()
          1 -> RecordedTabContent(
            lessons = course.recordedLessons,
            onPlay = { lesson ->
              if (lesson.videoUrl.isNotBlank()) {
                try {
                  val intent = Intent(Intent.ACTION_VIEW, Uri.parse(lesson.videoUrl))
                  context.startActivity(intent)
                } catch (e: Exception) {
                  onShowToast("Opening video: ${lesson.title}")
                }
              } else {
                onShowToast("Playing: ${lesson.title}")
              }
            }
          )
          2 -> TelegramTabContent(onJoinTelegram = { onShowToast("Opening Telegram channel link...") })
        }
      }
    }
  }
}

// Live Tab Content (Centered empty state as specified)
@Composable
fun LiveTabContent() {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.White),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.padding(24.dp)
    ) {
      Box(
        modifier = Modifier
          .size(80.dp)
          .background(Color(0xFFF1F5F9), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.VideocamOff,
          contentDescription = null,
          tint = Color(0xFF94A3B8),
          modifier = Modifier.size(40.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Whoops!",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "We couldn't find any results",
        fontSize = 14.sp,
        color = Color(0xFF64748B),
        textAlign = TextAlign.Center
      )
    }
  }
}

// Recorded Tab Content
@Composable
fun RecordedTabContent(
  lessons: List<RecordedLesson>,
  onPlay: (RecordedLesson) -> Unit
) {
  if (lessons.isEmpty()) {
    LiveTabContent()
  } else {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF7F8FC))
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      item {
        Row(
          modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "Video Lectures (${lessons.size})",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Text(
            text = "Tap to Watch ▶",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = RoyalBlue
          )
        }
      }

      items(lessons) { lesson ->
        val isYouTube = lesson.videoUrl.contains("youtube") || lesson.videoUrl.contains("youtu.be")
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0)))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onPlay(lesson) }
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .background(
                  if (isYouTube) Color(0xFFFFEAEA) else RoyalBlue.copy(alpha = 0.12f),
                  RoundedCornerShape(8.dp)
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = if (isYouTube) Color(0xFFE50914) else RoyalBlue,
                modifier = Modifier.size(28.dp)
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              if (isYouTube) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFFF0000))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = "YouTube Lecture",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
                Spacer(modifier = Modifier.height(4.dp))
              }
              Text(
                text = lesson.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "${lesson.instructor} • ${lesson.duration} • ${lesson.date}",
                fontSize = 11.5.sp,
                color = Color(0xFF64748B)
              )
            }

            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = "Open",
              tint = if (isYouTube) Color(0xFFE50914) else RoyalBlue,
              modifier = Modifier.size(22.dp)
            )
          }
        }
      }
    }
  }
}

// Telegram Tab Content
@Composable
fun TelegramTabContent(
  onJoinTelegram: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF7F8FC))
      .padding(16.dp),
    contentAlignment = Alignment.Center
  ) {
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(
          modifier = Modifier
            .size(64.dp)
            .background(Color(0xFFE0F2FE), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "Telegram",
            tint = Color(0xFF0284C7),
            modifier = Modifier.size(32.dp)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "Join Official Telegram Channel",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimary,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "Get daily study PDFs, exam updates, quiz links, and doubt discussions directly on Telegram.",
          fontSize = 13.sp,
          color = Color(0xFF64748B),
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = onJoinTelegram,
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0284C7),
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Send,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Join Telegram",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}
