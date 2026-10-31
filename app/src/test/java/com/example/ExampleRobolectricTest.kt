package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.FeatureType
import com.example.model.ScreenDestination
import com.example.viewmodel.AppViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("My Student Learning", appName)
  }

  @Test
  fun `test navigation and daily quiz workflow`() {
    val viewModel = AppViewModel()

    // Initially at Home
    assertEquals(ScreenDestination.HOME, viewModel.uiState.value.currentDestination)

    // Open Daily Quiz
    viewModel.openFeature(FeatureType.DAILY_QUIZ)
    assertEquals(ScreenDestination.DAILY_QUIZ, viewModel.uiState.value.currentDestination)
    assertFalse(viewModel.uiState.value.quizState.isSubmitted)

    // Answer questions
    val q1 = viewModel.uiState.value.quizState.questions[0]
    viewModel.selectQuizOption(q1.id, q1.correctOptionIndex)
    assertEquals(q1.correctOptionIndex, viewModel.uiState.value.quizState.userAnswers[q1.id])

    // Submit quiz
    viewModel.submitQuiz()
    assertTrue(viewModel.uiState.value.quizState.isSubmitted)
    assertEquals(1, viewModel.uiState.value.quizState.score)

    // Navigate back to Home
    val popped = viewModel.navigateBack()
    assertTrue(popped)
    assertEquals(ScreenDestination.HOME, viewModel.uiState.value.currentDestination)
  }
}
