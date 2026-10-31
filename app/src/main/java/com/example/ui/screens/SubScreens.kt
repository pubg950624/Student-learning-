package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CourseBatch
import com.example.model.FeatureType
import com.example.model.NotificationItem
import com.example.model.StudyMaterialItem
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.TextPrimary

// 1. CoursesScreen (delegates to CourseListingScreen)
@Composable
fun CoursesScreen(
  courses: List<CourseBatch>,
  onCourseClick: (CourseBatch) -> Unit,
  onBackClick: () -> Unit
) {
  CourseListingScreen(
    title = "All Courses & Batches",
    courses = courses,
    onCourseClick = onCourseClick,
    onBackClick = onBackClick
  )
}

// 2. ProfileScreen
@Composable
fun ProfileScreen(
  studentName: String,
  studentMobile: String,
  studentEmail: String,
  onNavigateDownloads: () -> Unit,
  onNavigateSubmissions: () -> Unit,
  onNavigateOrders: () -> Unit,
  onNavigateFaqs: () -> Unit,
  onNavigateTerms: () -> Unit,
  onNavigateHelp: () -> Unit,
  onNavigateEditProfile: () -> Unit,
  onNavigateAdmin: () -> Unit = {},
  onShowToast: (String) -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF7F8FC)),
    contentAlignment = Alignment.TopCenter
  ) {
    Column(
      modifier = Modifier
        .widthIn(max = 600.dp)
        .fillMaxSize()
    ) {
      // Header
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(RoyalBlue)
          .statusBarsPadding()
          .padding(20.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Box(
            modifier = Modifier
              .size(72.dp)
              .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Person,
              contentDescription = null,
              tint = RoyalBlue,
              modifier = Modifier.size(40.dp)
            )
          }
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = studentName,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "$studentMobile • $studentEmail",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 12.sp
          )
          Spacer(modifier = Modifier.height(12.dp))
          Button(
            onClick = onNavigateEditProfile,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = RoyalBlue),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.testTag("edit_profile_button")
          ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Edit Profile", fontSize = 13.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      // Menu List
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        val options = listOf(
          Triple("Downloaded Notes", Icons.Default.Download, onNavigateDownloads),
          Triple("My Submissions", Icons.Default.CheckCircle, onNavigateSubmissions),
          Triple("My Orders & Payments", Icons.Default.Receipt, onNavigateOrders),
          Triple("Admin Control Panel (Netlify)", Icons.Default.Settings, onNavigateAdmin),
          Triple("FAQs", Icons.Default.Help, onNavigateFaqs),
          Triple("Terms & Conditions", Icons.Default.Star, onNavigateTerms),
          Triple("Help & Support", Icons.Default.Help, onNavigateHelp)
        )

        items(options) { (title, icon, action) ->
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { action() }
              .testTag("profile_option_${title.lowercase().replace(" ", "_")}"),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(imageVector = icon, contentDescription = null, tint = DarkNavy, modifier = Modifier.size(22.dp))
              Spacer(modifier = Modifier.width(16.dp))
              Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary, modifier = Modifier.weight(1f))
              Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp))
            }
          }
        }
      }
    }
  }
}

// 3. TestsScreen
@Composable
fun TestsScreen(
  onStartQuiz: () -> Unit,
  onShowToast: (String) -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF7F8FC)),
    contentAlignment = Alignment.TopCenter
  ) {
    Column(
      modifier = Modifier
        .widthIn(max = 600.dp)
        .fillMaxSize()
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(RoyalBlue)
          .statusBarsPadding()
          .padding(16.dp),
        contentAlignment = Alignment.Center
      ) {
        Text(text = "Tests & Daily Quizzes", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
      }

      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .testTag("daily_quiz_card"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Column(modifier = Modifier.padding(20.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(44.dp)
                    .background(RoyalBlue.copy(alpha = 0.1f), CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(imageVector = Icons.Default.Quiz, contentDescription = null, tint = RoyalBlue)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                  Text(text = "Daily Current Affairs Quiz", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                  Text(text = "10 Questions • 10 Marks • Instant Score", fontSize = 12.sp, color = Color(0xFF64748B))
                }
              }
              Spacer(modifier = Modifier.height(16.dp))
              Button(
                onClick = onStartQuiz,
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("start_quiz_btn")
              ) {
                Text(text = "Start Quiz Now", fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  }
}

// 4. DownloadsScreen & MyDownloadsScreen
@Composable
fun DownloadsScreen(
  studyMaterials: List<StudyMaterialItem>,
  onToggleDownload: (StudyMaterialItem) -> Unit,
  onBackClick: () -> Unit,
  onShowToast: (String) -> Unit
) {
  MyDownloadsScreen(studyMaterials, onToggleDownload, onBackClick, onShowToast)
}

@Composable
fun MyDownloadsScreen(
  studyMaterials: List<StudyMaterialItem>,
  onToggleDownload: (StudyMaterialItem) -> Unit,
  onBackClick: () -> Unit,
  onShowToast: (String) -> Unit
) {
  val context = LocalContext.current
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF7F8FC)),
    contentAlignment = Alignment.TopCenter
  ) {
    Column(
      modifier = Modifier
        .widthIn(max = 600.dp)
        .fillMaxSize()
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(RoyalBlue)
          .statusBarsPadding()
          .padding(8.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
          Text(text = "Downloaded Study Materials", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
      }

      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(studyMaterials) { material ->
          val hasDriveUrl = material.driveUrl.isNotBlank()
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                if (hasDriveUrl) {
                  try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(material.driveUrl))
                    context.startActivity(intent)
                  } catch (e: Exception) {
                    onShowToast("Opening PDF: ${material.title}")
                  }
                } else {
                  onToggleDownload(material)
                }
              },
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                if (hasDriveUrl) {
                  Box(
                    modifier = Modifier
                      .background(Color(0xFFE0F2FE), RoundedCornerShape(4.dp))
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = "Google Drive PDF",
                      color = Color(0xFF0369A1),
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                  Spacer(modifier = Modifier.height(4.dp))
                }
                Text(text = material.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = "${material.subject} • ${material.fileSize}", fontSize = 11.5.sp, color = Color(0xFF64748B))
              }
              Button(
                onClick = {
                  if (hasDriveUrl) {
                    try {
                      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(material.driveUrl))
                      context.startActivity(intent)
                    } catch (e: Exception) {
                      onShowToast("Opening: ${material.title}")
                    }
                  } else {
                    onToggleDownload(material)
                  }
                },
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (hasDriveUrl) Color(0xFF10B981) else if (material.isDownloaded) Color(0xFF10B981) else RoyalBlue
                )
              ) {
                Text(
                  text = if (hasDriveUrl) "📄 Open PDF" else if (material.isDownloaded) "Downloaded" else "Download",
                  fontSize = 12.sp
                )
              }
            }
          }
        }
      }
    }
  }
}

// 5. EditProfileScreen
@Composable
fun EditProfileScreen(
  currentName: String,
  currentMobile: String,
  currentEmail: String,
  onSave: (String, String, String) -> Unit,
  onBackClick: () -> Unit
) {
  var name by remember { mutableStateOf(currentName) }
  var mobile by remember { mutableStateOf(currentMobile) }
  var email by remember { mutableStateOf(currentEmail) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF7F8FC)),
    contentAlignment = Alignment.TopCenter
  ) {
    Column(
      modifier = Modifier
        .widthIn(max = 600.dp)
        .fillMaxSize()
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(RoyalBlue)
          .statusBarsPadding()
          .padding(8.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
          Text(text = "Edit Student Profile", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
      }

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Full Name") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("edit_name_input")
        )
        OutlinedTextField(
          value = mobile,
          onValueChange = { mobile = it },
          label = { Text("Mobile Number") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("edit_mobile_input")
        )
        OutlinedTextField(
          value = email,
          onValueChange = { email = it },
          label = { Text("Email Address") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("edit_email_input")
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
          onClick = { onSave(name, mobile, email) },
          colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("save_profile_btn")
        ) {
          Text(text = "Save Changes", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

// 6. FaqScreen
@Composable
fun FaqScreen(onBackClick: () -> Unit) {
  SimpleInfoScreen(title = "FAQs & Help", content = "Here you can find answers to frequently asked questions about video playback, batch access, PDF downloads, and live classes.", onBackClick = onBackClick)
}

// 7. TermsScreen
@Composable
fun TermsScreen(onBackClick: () -> Unit) {
  SimpleInfoScreen(title = "Terms & Conditions", content = "All courses and study materials are protected by copyright. Unauthorized distribution or copying is strictly prohibited.", onBackClick = onBackClick)
}

// 8. HelpSupportScreen
@Composable
fun HelpSupportScreen(onBackClick: () -> Unit, onShowToast: (String) -> Unit) {
  SimpleInfoScreen(title = "Help & Support", content = "Contact our 24/7 student support helpline at support@learning.com or call +91-9876543210 for immediate assistance.", onBackClick = onBackClick)
}

// 9. SubmissionsScreen
@Composable
fun SubmissionsScreen(onBackClick: () -> Unit) {
  SimpleInfoScreen(title = "My Submissions", content = "You have successfully submitted 12 quizzes and 3 assignment assessments.", onBackClick = onBackClick)
}

// 10. OrdersScreen
@Composable
fun OrdersScreen(onBackClick: () -> Unit) {
  SimpleInfoScreen(title = "My Orders & Payments", content = "Transaction history and active course purchases are displayed here securely.", onBackClick = onBackClick)
}

// 11. NotificationsScreen
@Composable
fun NotificationsScreen(
  notifications: List<NotificationItem>,
  onMarkAllRead: () -> Unit,
  onBackClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF7F8FC)),
    contentAlignment = Alignment.TopCenter
  ) {
    Column(
      modifier = Modifier
        .widthIn(max = 600.dp)
        .fillMaxSize()
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(RoyalBlue)
          .statusBarsPadding()
          .padding(8.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
          Text(text = "Notifications", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        }
      }

      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(notifications) { n ->
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(text = n.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
              Spacer(modifier = Modifier.height(4.dp))
              Text(text = n.message, fontSize = 13.sp, color = Color(0xFF475569))
              Spacer(modifier = Modifier.height(6.dp))
              Text(text = n.timeAgo, fontSize = 11.sp, color = Color(0xFF94A3B8))
            }
          }
        }
      }
    }
  }
}

// 12. SearchScreen
@Composable
fun SearchScreen(
  query: String,
  results: List<CourseBatch>,
  onQueryChange: (String) -> Unit,
  onSelectCourse: (CourseBatch) -> Unit,
  onBackClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF7F8FC)),
    contentAlignment = Alignment.TopCenter
  ) {
    Column(
      modifier = Modifier
        .widthIn(max = 600.dp)
        .fillMaxSize()
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(RoyalBlue)
          .statusBarsPadding()
          .padding(8.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
          OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search courses, tests...", color = Color.White.copy(alpha = 0.7f)) },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("search_input")
          )
        }
      }

      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(results) { course ->
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onSelectCourse(course) },
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(text = course.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
              Spacer(modifier = Modifier.height(4.dp))
              Text(text = "${course.examCategory} • ${course.price}", fontSize = 12.sp, color = RoyalBlue)
            }
          }
        }
      }
    }
  }
}

// 13. FeatureDetailScreen
@Composable
fun FeatureDetailScreen(
  feature: FeatureType,
  studyMaterials: List<StudyMaterialItem>,
  courses: List<CourseBatch>,
  onToggleDownload: (StudyMaterialItem) -> Unit,
  onLaunchCustomQuiz: () -> Unit,
  onShowToast: (String) -> Unit,
  onBackClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF7F8FC)),
    contentAlignment = Alignment.TopCenter
  ) {
    Column(
      modifier = Modifier
        .widthIn(max = 600.dp)
        .fillMaxSize()
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(RoyalBlue)
          .statusBarsPadding()
          .padding(8.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
          Text(text = feature.label, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
      }

      val context = LocalContext.current
      if (feature == FeatureType.PDF_CLASS_NOTES || feature == FeatureType.BOOKS) {
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          item {
            Text(
              text = "Available Study Notes & PDFs (${studyMaterials.size})",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          }

          items(studyMaterials) { material ->
            val hasDriveUrl = material.driveUrl.isNotBlank()
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  if (hasDriveUrl) {
                    try {
                      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(material.driveUrl))
                      context.startActivity(intent)
                    } catch (e: Exception) {
                      onShowToast("Opening PDF: ${material.title}")
                    }
                  } else {
                    onToggleDownload(material)
                  }
                },
              shape = RoundedCornerShape(10.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Box(
                    modifier = Modifier
                      .background(Color(0xFFE0F2FE), RoundedCornerShape(4.dp))
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = "${material.subject} • Google Drive",
                      color = Color(0xFF0369A1),
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(text = material.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(text = "${material.subtitle} • ${material.fileSize}", fontSize = 11.5.sp, color = Color(0xFF64748B))
                }

                Button(
                  onClick = {
                    if (hasDriveUrl) {
                      try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(material.driveUrl))
                        context.startActivity(intent)
                      } catch (e: Exception) {
                        onShowToast("Opening: ${material.title}")
                      }
                    } else {
                      onToggleDownload(material)
                    }
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                  Text(text = "📄 Open PDF", fontSize = 12.sp)
                }
              }
            }
          }
        }
      } else {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(text = feature.label, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
          Spacer(modifier = Modifier.height(10.dp))
          Text(text = feature.categoryDescription, fontSize = 14.sp, color = Color(0xFF64748B), textAlign = TextAlign.Center)
          Spacer(modifier = Modifier.height(20.dp))
          Button(
            onClick = onLaunchCustomQuiz,
            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(text = "Access Feature Content", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

// Helper Simple Info Screen
@Composable
fun SimpleInfoScreen(
  title: String,
  content: String,
  onBackClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF7F8FC)),
    contentAlignment = Alignment.TopCenter
  ) {
    Column(
      modifier = Modifier
        .widthIn(max = 600.dp)
        .fillMaxSize()
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(RoyalBlue)
          .statusBarsPadding()
          .padding(8.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
          Text(text = title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
      }

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
          Spacer(modifier = Modifier.height(12.dp))
          Text(text = content, fontSize = 14.sp, color = Color(0xFF475569), lineHeight = 20.sp)
        }
      }
    }
  }
}
