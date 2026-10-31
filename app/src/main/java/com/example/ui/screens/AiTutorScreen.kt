package com.example.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.TextPrimary
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

data class ChatMessage(
  val text: String,
  val isUser: Boolean
)

@Composable
fun AiTutorScreen(
  onBackClick: () -> Unit,
  onShowToast: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val scope = rememberCoroutineScope()
  var promptText by remember { mutableStateOf("") }
  var isLoading by remember { mutableStateOf(false) }
  var chatMessages by remember {
    mutableStateOf(
      listOf(
        ChatMessage("Hello! I am your Gemini AI Study Tutor. Ask me any question about SSC, Banking, UPSC, Math, Science, or General Knowledge!", false)
      )
    )
  }

  // Initialize Gemini model lazily using recommended gemini-3.8-flash
  val generativeModel = remember {
    Firebase.ai.generativeModel("gemini-3.8-flash")
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF7F8FC))
  ) {
    // Top Bar
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(RoyalBlue)
        .statusBarsPadding()
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
      ) {
        IconButton(onClick = onBackClick) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.White
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = Color(0xFFFFD700),
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "Gemini AI Study Tutor",
              color = Color.White,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Powered by Google Gemini",
              color = Color.White.copy(alpha = 0.8f),
              fontSize = 11.sp
            )
          }
        }
      }
    }

    // Chat Message List
    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(chatMessages) { message ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
        ) {
          Box(
            modifier = Modifier
              .widthIn(max = 300.dp)
              .clip(
                RoundedCornerShape(
                  topStart = 12.dp,
                  topEnd = 12.dp,
                  bottomStart = if (message.isUser) 12.dp else 2.dp,
                  bottomEnd = if (message.isUser) 2.dp else 12.dp
                )
              )
              .background(if (message.isUser) RoyalBlue else Color.White)
              .border(
                1.dp,
                if (message.isUser) RoyalBlue else Color(0xFFE2E8F0),
                RoundedCornerShape(
                  topStart = 12.dp,
                  topEnd = 12.dp,
                  bottomStart = if (message.isUser) 12.dp else 2.dp,
                  bottomEnd = if (message.isUser) 2.dp else 12.dp
                )
              )
              .padding(12.dp)
          ) {
            Text(
              text = message.text,
              color = if (message.isUser) Color.White else TextPrimary,
              fontSize = 13.5.sp,
              lineHeight = 19.sp
            )
          }
        }
      }

      if (isLoading) {
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(12.dp)
            ) {
              Text(
                text = "Gemini is thinking...",
                color = Color(0xFF64748B),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }
    }

    // Input Bar
    Surface(
      modifier = Modifier.fillMaxWidth(),
      color = Color.White,
      shadowElevation = 8.dp
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = promptText,
          onValueChange = { promptText = it },
          placeholder = { Text("Ask your doubt or topic (e.g. Mughal History)...", fontSize = 13.sp) },
          modifier = Modifier
            .weight(1f)
            .testTag("ai_prompt_input"),
          maxLines = 3,
          textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.5.sp)
        )

        Button(
          onClick = {
            if (promptText.isNotBlank() && !isLoading) {
              val question = promptText.trim()
              promptText = ""
              chatMessages = chatMessages + ChatMessage(question, true)
              isLoading = true

              scope.launch {
                try {
                  val response = withTimeout(60_000L) {
                    generativeModel.generateContent(question)
                  }
                  val answer = response.text ?: "I could not generate an answer. Please try again."
                  chatMessages = chatMessages + ChatMessage(answer, false)
                } catch (e: TimeoutCancellationException) {
                  Log.e("AiTutor", "Generation timed out", e)
                  chatMessages = chatMessages + ChatMessage("Generation timed out after 60 seconds. Please try again.", false)
                } catch (e: CancellationException) {
                  throw e
                } catch (e: Exception) {
                  Log.e("AiTutor", "Gemini AI generation failed", e)
                  chatMessages = chatMessages + ChatMessage("Error: ${e.localizedMessage ?: "AI service error. Check App Check / internet connection."}", false)
                } finally {
                  isLoading = false
                }
              }
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("ai_send_button")
        ) {
          Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "Send",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}
