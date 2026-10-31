package com.example.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.model.CourseBatch
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.TextPrimary

const val ADMIN_NETLIFY_URL = "https://creative-peony-0e7017.netlify.app/admin/"

class AndroidWebAppBridge(private val onSync: (String) -> Unit) {
  @JavascriptInterface
  fun syncData(json: String) {
    Handler(Looper.getMainLooper()).post {
      onSync(json)
    }
  }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AdminPortalScreen(
  coursesList: List<CourseBatch>,
  onAddVideo: (courseTarget: String, title: String, url: String, duration: String) -> Unit,
  onAddPdf: (subject: String, title: String, driveUrl: String, size: String) -> Unit,
  onAddCourse: (title: String, category: String, price: String, educator: String) -> Unit,
  onSyncJson: (json: String) -> Unit,
  onBackClick: () -> Unit,
  onShowToast: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedTab by remember { mutableIntStateOf(0) } // 0 = Direct App Control, 1 = Netlify Portal
  var webViewRef: WebView? = remember { null }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF1F5F9))
  ) {
    // 1. Top Header Bar
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(DarkNavy)
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

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "My Student Learning - Admin",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Full App Management & Control",
            color = Color(0xFF38BDF8),
            fontSize = 11.5.sp
          )
        }

        IconButton(onClick = {
          try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ADMIN_NETLIFY_URL))
            context.startActivity(intent)
          } catch (e: Exception) {
            onShowToast("Opening: ${e.message}")
          }
        }) {
          Icon(
            imageVector = Icons.Default.OpenInBrowser,
            contentDescription = "Open in Browser",
            tint = Color.White
          )
        }
      }
    }

    // 2. Tab Navigation: Direct App Control vs Netlify Portal
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = Color.White,
      contentColor = RoyalBlue,
      indicator = { tabPositions ->
        TabRowDefaults.Indicator(
          Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
          color = RoyalBlue,
          height = 3.dp
        )
      }
    ) {
      Tab(
        selected = selectedTab == 0,
        onClick = { selectedTab = 0 },
        text = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Direct App Control", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }
        }
      )
      Tab(
        selected = selectedTab == 1,
        onClick = { selectedTab = 1 },
        text = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Netlify Portal", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }
        }
      )
    }

    // 3. Tab Content
    if (selectedTab == 0) {
      NativeAdminControlContent(
        coursesList = coursesList,
        onAddVideo = onAddVideo,
        onAddPdf = onAddPdf,
        onAddCourse = onAddCourse,
        onSyncJson = onSyncJson,
        onShowToast = onShowToast
      )
    } else {
      // Netlify Web Portal Tab
      Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
          factory = { ctx ->
            WebView(ctx).apply {
              settings.javaScriptEnabled = true
              settings.domStorageEnabled = true
              settings.loadWithOverviewMode = true
              settings.useWideViewPort = true
              settings.allowContentAccess = true
              settings.allowFileAccess = true
              
              // Attach two-way bridge so Netlify can update the Android app directly
              addJavascriptInterface(AndroidWebAppBridge { json ->
                onSyncJson(json)
                onShowToast("Synced from Netlify Admin! ✅")
              }, "AndroidBridge")

              webViewClient = object : WebViewClient() {
                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                  if (url != null && (url.contains("youtube.com") || url.contains("youtu.be"))) {
                    try {
                      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                      ctx.startActivity(intent)
                      return true
                    } catch (e: Exception) {
                      // ignore
                    }
                  }
                  return false
                }
              }
              loadUrl(ADMIN_NETLIFY_URL)
              webViewRef = this
            }
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}

@Composable
fun NativeAdminControlContent(
  coursesList: List<CourseBatch>,
  onAddVideo: (courseTarget: String, title: String, url: String, duration: String) -> Unit,
  onAddPdf: (subject: String, title: String, driveUrl: String, size: String) -> Unit,
  onAddCourse: (title: String, category: String, price: String, educator: String) -> Unit,
  onSyncJson: (json: String) -> Unit,
  onShowToast: (String) -> Unit
) {
  var activeSection by remember { mutableStateOf("video") } // video, pdf, course, sync

  // Video Form States
  var selectedCourseForVideo by remember { mutableStateOf(coursesList.firstOrNull()?.title ?: "Railway Exam Preparation (NTPC & Group D)") }
  var videoTitle by remember { mutableStateOf("") }
  var videoUrl by remember { mutableStateOf("") }
  var videoDuration by remember { mutableStateOf("50 mins") }

  // PDF Form States
  var pdfSubject by remember { mutableStateOf("History") }
  var pdfTitle by remember { mutableStateOf("") }
  var pdfDriveUrl by remember { mutableStateOf("") }
  var pdfSize by remember { mutableStateOf("3.5 MB") }

  // Course Form States
  var courseTitle by remember { mutableStateOf("") }
  var courseCategory by remember { mutableStateOf("State Exams") }
  var coursePrice by remember { mutableStateOf("₹599") }
  var courseEducator by remember { mutableStateOf("Expert Faculty") }

  // Quick Sync Code State
  var syncJsonText by remember { mutableStateOf("") }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Top Info banner
    item {
      Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2FE))
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text(
            text = "⚡ Real-Time App Controller",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0369A1)
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "आप जो भी वीडियो, PDF या कोर्स यहाँ जोड़ेंगे, वह तुरंत आपके ऐप में सही जगह दिखाई देगा!",
            fontSize = 12.sp,
            color = Color(0xFF0C4A6E)
          )
        }
      }
    }

    // Section Toggle Pills
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        listOf(
          Triple("video", "📹 Video", Icons.Default.VideoLibrary),
          Triple("pdf", "📄 PDF Notes", Icons.Default.Description),
          Triple("course", "📚 Course", Icons.Default.Book),
          Triple("sync", "🔄 Sync Code", Icons.Default.Sync)
        ).forEach { (key, label, _) ->
          val isSelected = activeSection == key
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(8.dp))
              .background(if (isSelected) RoyalBlue else Color.White)
              .clickable { activeSection = key }
              .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = label,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = if (isSelected) Color.White else TextPrimary
            )
          }
        }
      }
    }

    // 1. ADD YOUTUBE VIDEO SECTION
    if (activeSection == "video") {
      item {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(2.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "📹 YouTube Video को कहाँ रखना है चुनें:",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = DarkNavy
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text("Target Course (किस कोर्स में वीडियो दिखेगा):", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
              coursesList.forEach { course ->
                val isChosen = selectedCourseForVideo == course.title
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isChosen) RoyalBlue.copy(alpha = 0.1f) else Color(0xFFF8FAFC))
                    .clickable { selectedCourseForVideo = course.title }
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = if (isChosen) "◉ " else "○ ",
                    color = if (isChosen) RoyalBlue else Color.Gray,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "${course.title} (${course.examCategory})",
                    fontSize = 12.sp,
                    fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                    color = if (isChosen) RoyalBlue else TextPrimary
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
              value = videoTitle,
              onValueChange = { videoTitle = it },
              label = { Text("Video Lecture Title (e.g. Chapter 1: History)") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
              value = videoUrl,
              onValueChange = { videoUrl = it },
              label = { Text("YouTube Video / Playlist Link (https://...)") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
              value = videoDuration,
              onValueChange = { videoDuration = it },
              label = { Text("Duration (e.g. 50 mins)") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = {
                if (videoTitle.isBlank() || videoUrl.isBlank()) {
                  onShowToast("कृपया Video Title और YouTube Link दोनों दर्ज करें!")
                } else {
                  onAddVideo(selectedCourseForVideo, videoTitle, videoUrl, videoDuration)
                  videoTitle = ""
                  videoUrl = ""
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("▶ वीडियो को ऐप में तुरंत जोड़ें (Add Video)", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // 2. ADD GOOGLE DRIVE PDF SECTION
    if (activeSection == "pdf") {
      item {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(2.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "📄 Google Drive PDF को किस विषय में रखना है:",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = DarkNavy
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text("Subject / Category (विषय चुनें):", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              listOf("History", "Maths", "Science", "Reasoning").forEach { sub ->
                val isChosen = pdfSubject == sub
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isChosen) RoyalBlue else Color(0xFFF1F5F9))
                    .clickable { pdfSubject = sub }
                    .padding(vertical = 8.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = sub,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isChosen) Color.White else TextPrimary
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
              value = pdfTitle,
              onValueChange = { pdfTitle = it },
              label = { Text("PDF Notes Title (e.g. Modern History Hand Notes)") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
              value = pdfDriveUrl,
              onValueChange = { pdfDriveUrl = it },
              label = { Text("Google Drive Share Link (https://drive.google.com/...)") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
              value = pdfSize,
              onValueChange = { pdfSize = it },
              label = { Text("File Size (e.g. 4.2 MB)") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = {
                if (pdfTitle.isBlank() || pdfDriveUrl.isBlank()) {
                  onShowToast("कृपया PDF Title और Google Drive Link दोनों दर्ज करें!")
                } else {
                  onAddPdf(pdfSubject, pdfTitle, pdfDriveUrl, pdfSize)
                  pdfTitle = ""
                  pdfDriveUrl = ""
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("📄 PDF को ऐप में तुरंत जोड़ें (Add PDF)", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // 3. ADD COURSE BATCH SECTION
    if (activeSection == "course") {
      item {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(2.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "📚 नया Course / Batch तैयार करें:",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = DarkNavy
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
              value = courseTitle,
              onValueChange = { courseTitle = it },
              label = { Text("Course Title (e.g. UP Police Constable Target 2026)") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
              value = courseCategory,
              onValueChange = { courseCategory = it },
              label = { Text("Exam Category (e.g. State Exams, SSC & Railway)") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              OutlinedTextField(
                value = coursePrice,
                onValueChange = { coursePrice = it },
                label = { Text("Price (₹)") },
                modifier = Modifier.weight(1f),
                singleLine = true
              )
              OutlinedTextField(
                value = courseEducator,
                onValueChange = { courseEducator = it },
                label = { Text("Educator Name") },
                modifier = Modifier.weight(1f),
                singleLine = true
              )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = {
                if (courseTitle.isBlank()) {
                  onShowToast("कृपया Course Title दर्ज करें!")
                } else {
                  onAddCourse(courseTitle, courseCategory, coursePrice, courseEducator)
                  courseTitle = ""
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("📚 नया कोर्स ऐप में प्रकाशित करें (Create Course)", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // 4. SYNC CODE SECTION
    if (activeSection == "sync") {
      item {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(2.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "🔄 Netlify / Web Portal Sync Code:",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = DarkNavy
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "यदि आप कंप्यूटर ब्राउज़र पर Netlify Admin Portal का उपयोग कर रहे हैं, तो वहाँ से 'Export / Copy Sync Code' करें और यहाँ पेस्ट करके 1 क्लिक में सिंक कर लें!",
              fontSize = 12.sp,
              color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
              value = syncJsonText,
              onValueChange = { syncJsonText = it },
              label = { Text("Paste JSON Sync Data Here") },
              modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Button(
              onClick = {
                if (syncJsonText.isBlank()) {
                  onShowToast("कृपया Sync Code पेस्ट करें!")
                } else {
                  onSyncJson(syncJsonText)
                  syncJsonText = ""
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("सिंक करें और ऐप अपडेट करें (Apply Sync)", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}
