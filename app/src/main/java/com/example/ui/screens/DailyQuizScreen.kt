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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.QuizQuestion
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.TextPrimary
import com.example.viewmodel.QuizUiState

@Composable
fun DailyQuizScreen(
  quizState: QuizUiState,
  onOptionSelected: (questionId: Int, optionIndex: Int) -> Unit,
  onNextQuestion: () -> Unit,
  onPrevQuestion: () -> Unit,
  onJumpToQuestion: (Int) -> Unit,
  onSubmitQuiz: () -> Unit,
  onRestartQuiz: () -> Unit,
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val questions = quizState.questions
  val currentQuestion = questions.getOrElse(quizState.currentQuestionIndex) { questions.first() }
  val currentAnswer = quizState.userAnswers[currentQuestion.id]

  val minutes = quizState.timeRemainingSeconds / 60
  val seconds = quizState.timeRemainingSeconds % 60
  val timeFormatted = String.format("%02d:%02d", minutes, seconds)

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

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Daily Booster Quiz",
              color = Color.White,
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Exam Batch 2026 Practice",
              color = Color.White.copy(alpha = 0.8f),
              fontSize = 11.sp
            )
          }

          // Countdown Timer Pill
          Row(
            modifier = Modifier
              .background(
                if (quizState.timeRemainingSeconds < 60) Color(0xFFDC2626) else AccentOrange,
                RoundedCornerShape(16.dp)
              )
              .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Timer,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (quizState.isSubmitted) "Ended" else timeFormatted,
              color = Color.White,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      // Quiz Progress & Question Nav Pager
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color.White)
          .padding(horizontal = 16.dp, vertical = 10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Question ${quizState.currentQuestionIndex + 1} of ${questions.size}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = DarkNavy
          )

          Box(
            modifier = Modifier
              .background(Color(0xFFE0F2FE), RoundedCornerShape(10.dp))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = currentQuestion.subject,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF0369A1)
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Question selector pills
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          itemsIndexed(questions) { index, q ->
            val isCurrent = index == quizState.currentQuestionIndex
            val isAnswered = quizState.userAnswers.containsKey(q.id)
            val isCorrect = quizState.isSubmitted && quizState.userAnswers[q.id] == q.correctOptionIndex

            val pillBg = when {
              quizState.isSubmitted && isCorrect -> Color(0xFF22C55E)
              quizState.isSubmitted && isAnswered -> Color(0xFFEF4444)
              isCurrent -> RoyalBlue
              isAnswered -> AccentOrange
              else -> Color(0xFFF1F5F9)
            }
            val pillText = if (isCurrent || isAnswered || quizState.isSubmitted) Color.White else Color(0xFF64748B)

            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(pillBg)
                .clickable { onJumpToQuestion(index) },
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "${index + 1}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = pillText
              )
            }
          }
        }
      }

      // Main Quiz Body
      Column(
        modifier = Modifier
          .weight(1f)
          .verticalScroll(rememberScrollState())
          .padding(16.dp)
      ) {
        // Submission Results Banner if submitted
        if (quizState.isSubmitted) {
          QuizResultCard(
            score = quizState.score,
            total = questions.size,
            onRestart = onRestartQuiz
          )
          Spacer(modifier = Modifier.height(14.dp))
        }

        // Question Statement Card
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0)))
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Q${quizState.currentQuestionIndex + 1}.",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = RoyalBlue
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = currentQuestion.question,
              fontSize = 15.5.sp,
              fontWeight = FontWeight.SemiBold,
              color = TextPrimary,
              lineHeight = 22.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Options
        currentQuestion.options.forEachIndexed { optIndex, optionText ->
          val isSelected = currentAnswer == optIndex
          val isCorrectOption = optIndex == currentQuestion.correctOptionIndex

          val optionCardBorder = when {
            quizState.isSubmitted && isCorrectOption -> Color(0xFF22C55E)
            quizState.isSubmitted && isSelected -> Color(0xFFEF4444)
            isSelected -> RoyalBlue
            else -> Color(0xFFE2E8F0)
          }

          val optionCardBg = when {
            quizState.isSubmitted && isCorrectOption -> Color(0xFFF0FDF4)
            quizState.isSubmitted && isSelected -> Color(0xFFFEF2F2)
            isSelected -> Color(0xFFEFF6FF)
            else -> Color.White
          }

          val prefixLetter = ('A' + optIndex).toString()

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 5.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(optionCardBg)
              .border(1.5.dp, optionCardBorder, RoundedCornerShape(10.dp))
              .clickable(enabled = !quizState.isSubmitted) {
                onOptionSelected(currentQuestion.id, optIndex)
              }
              .padding(12.dp)
              .testTag("quiz_option_${currentQuestion.id}_$optIndex")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(28.dp)
                  .clip(CircleShape)
                  .background(
                    if (isSelected || (quizState.isSubmitted && isCorrectOption)) optionCardBorder else Color(0xFFF1F5F9)
                  ),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = prefixLetter,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected || (quizState.isSubmitted && isCorrectOption)) Color.White else Color(0xFF475569)
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              Text(
                text = optionText,
                fontSize = 14.sp,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
              )

              if (quizState.isSubmitted) {
                if (isCorrectOption) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Correct",
                    tint = Color(0xFF16A34A),
                    modifier = Modifier.size(20.dp)
                  )
                } else if (isSelected) {
                  Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Incorrect",
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(20.dp)
                  )
                }
              }
            }
          }
        }

        // Explanation if submitted
        if (quizState.isSubmitted) {
          Spacer(modifier = Modifier.height(14.dp))
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
              .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
              .padding(14.dp)
          ) {
            Column {
              Text(
                text = "💡 Detailed Explanation",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = DarkNavy
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = currentQuestion.explanation,
                fontSize = 12.5.sp,
                color = Color(0xFF334155),
                lineHeight = 18.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
      }

      // Bottom Action Controls
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color.White)
          .navigationBarsPadding()
          .padding(horizontal = 16.dp, vertical = 10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedButton(
            onClick = onPrevQuestion,
            enabled = quizState.currentQuestionIndex > 0,
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Previous")
          }

          if (!quizState.isSubmitted) {
            Button(
              onClick = onSubmitQuiz,
              colors = ButtonDefaults.buttonColors(
                containerColor = AccentOrange,
                contentColor = Color.White
              ),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("submit_quiz_button")
            ) {
              Text("Submit Quiz", fontWeight = FontWeight.Bold)
            }
          } else {
            Button(
              onClick = onRestartQuiz,
              colors = ButtonDefaults.buttonColors(
                containerColor = RoyalBlue,
                contentColor = Color.White
              ),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("Retake Quiz")
            }
          }

          OutlinedButton(
            onClick = onNextQuestion,
            enabled = quizState.currentQuestionIndex < questions.size - 1,
            shape = RoundedCornerShape(8.dp)
          ) {
            Text("Next")
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
fun QuizResultCard(
  score: Int,
  total: Int,
  onRestart: () -> Unit,
  modifier: Modifier = Modifier
) {
  val percentage = (score * 100) / total
  val message = when {
    percentage >= 80 -> "Outstanding! You are exam-ready!"
    percentage >= 60 -> "Good Effort! Keep revising weak areas."
    else -> "Needs Practice. Review solutions below."
  }

  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF86EFAC))
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(48.dp)
          .background(Color(0xFFDCFCE7), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.EmojiEvents,
          contentDescription = null,
          tint = Color(0xFF16A34A),
          modifier = Modifier.size(28.dp)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Quiz Completed!",
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF14532D)
      )

      Text(
        text = "Your Score: $score / $total ($percentage%)",
        fontSize = 15.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFF16A34A)
      )

      Text(
        text = message,
        fontSize = 12.sp,
        color = Color(0xFF166534),
        modifier = Modifier.padding(top = 2.dp)
      )
    }
  }
}
