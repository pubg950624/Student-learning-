package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.model.BannerItem
import com.example.model.CourseBatch
import com.example.model.FeatureType
import com.example.model.MockDataProvider
import com.example.model.NotificationItem
import com.example.model.QuizQuestion
import com.example.model.RecordedLesson
import com.example.model.ScreenDestination
import com.example.model.StudyMaterialItem
import org.json.JSONObject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuizUiState(
  val questions: List<QuizQuestion> = MockDataProvider.sampleQuizzes,
  val currentQuestionIndex: Int = 0,
  val userAnswers: Map<Int, Int> = emptyMap(),
  val isSubmitted: Boolean = false,
  val score: Int = 0,
  val timeRemainingSeconds: Int = 300,
  val isTimerRunning: Boolean = false
)

data class AppUiState(
  val currentDestination: ScreenDestination = ScreenDestination.HOME,
  val navigationStack: List<ScreenDestination> = listOf(ScreenDestination.HOME),
  val selectedCategory: String = "All",
  val isCategoryDropdownExpanded: Boolean = false,
  val currentBannerIndex: Int = 0,
  val banners: List<BannerItem> = MockDataProvider.banners,
  val selectedFeature: FeatureType = FeatureType.PAID_CLASSES,
  val selectedBatch: BannerItem = MockDataProvider.banners.first(),
  val courseListingTitle: String = "Courses",
  val courseListingItems: List<CourseBatch> = MockDataProvider.sampleCourses,
  val selectedCourse: CourseBatch = MockDataProvider.sampleCourses.first(),
  val courseDetailTab: Int = 0,
  val studentName: String = "Student Aspirant",
  val studentMobile: String = "+91 98765 43210",
  val studentEmail: String = "student@exam.in",
  val searchQuery: String = "",
  val searchResults: List<CourseBatch> = emptyList(),
  val notifications: List<NotificationItem> = MockDataProvider.notifications,
  val studyMaterials: List<StudyMaterialItem> = MockDataProvider.pdfNotes,
  val enrolledBatches: Set<String> = emptySet(),
  val quizState: QuizUiState = QuizUiState()
)

class AppViewModel : ViewModel() {
  private val _uiState = MutableStateFlow(AppUiState())
  val uiState = _uiState.asStateFlow()

  private val _toastEvent = MutableSharedFlow<String>()
  val toastEvent = _toastEvent.asSharedFlow()

  private var timerJob: Job? = null

  fun navigateTo(destination: ScreenDestination) {
    _uiState.update { state ->
      val newStack = if (destination == ScreenDestination.HOME) {
        listOf(ScreenDestination.HOME)
      } else {
        state.navigationStack + destination
      }
      state.copy(
        currentDestination = destination,
        navigationStack = newStack
      )
    }
  }

  fun navigateBack(): Boolean {
    val currentStack = _uiState.value.navigationStack
    return if (currentStack.size > 1) {
      val newStack = currentStack.dropLast(1)
      val prevDestination = newStack.last()
      _uiState.update {
        it.copy(
          currentDestination = prevDestination,
          navigationStack = newStack
        )
      }
      true
    } else if (_uiState.value.currentDestination != ScreenDestination.HOME) {
      _uiState.update {
        it.copy(
          currentDestination = ScreenDestination.HOME,
          navigationStack = listOf(ScreenDestination.HOME)
        )
      }
      true
    } else {
      false
    }
  }

  fun updateProfile(name: String, mobile: String, email: String) {
    _uiState.update {
      it.copy(
        studentName = name,
        studentMobile = mobile,
        studentEmail = email
      )
    }
    showToast("Profile updated successfully!")
  }

  fun selectCategory(category: String) {
    _uiState.update {
      it.copy(
        selectedCategory = category,
        isCategoryDropdownExpanded = false
      )
    }
    showToast("Filter updated: $category")
  }

  fun toggleCategoryDropdown(expanded: Boolean) {
    _uiState.update { it.copy(isCategoryDropdownExpanded = expanded) }
  }

  fun setBannerIndex(index: Int) {
    _uiState.update { it.copy(currentBannerIndex = index) }
  }

  fun openBatchDetails(batch: BannerItem) {
    _uiState.update { it.copy(selectedBatch = batch) }
    navigateTo(ScreenDestination.BATCH_DETAILS)
  }

  fun openFeature(feature: FeatureType) {
    when (feature) {
      FeatureType.DAILY_QUIZ -> startDailyQuiz()
      FeatureType.AI_TUTOR -> navigateTo(ScreenDestination.AI_TUTOR)
      FeatureType.PAID_CLASSES -> {
        _uiState.update {
          it.copy(
            courseListingTitle = "Paid Classes",
            courseListingItems = MockDataProvider.sampleCourses
          )
        }
        navigateTo(ScreenDestination.COURSE_LISTING)
      }
      FeatureType.FREE_COURSES -> {
        _uiState.update {
          it.copy(
            courseListingTitle = "Courses",
            courseListingItems = MockDataProvider.sampleCourses
          )
        }
        navigateTo(ScreenDestination.COURSE_LISTING)
      }
      else -> {
        _uiState.update { it.copy(selectedFeature = feature) }
        navigateTo(ScreenDestination.FEATURE_DETAIL)
      }
    }
  }

  fun openCourseListing(title: String) {
    _uiState.update {
      it.copy(
        courseListingTitle = title,
        courseListingItems = MockDataProvider.sampleCourses
      )
    }
    navigateTo(ScreenDestination.COURSE_LISTING)
  }

  fun openCourseDetail(course: CourseBatch) {
    _uiState.update {
      it.copy(
        selectedCourse = course,
        courseDetailTab = 0
      )
    }
    navigateTo(ScreenDestination.COURSE_DETAIL)
  }

  fun setCourseDetailTab(tabIndex: Int) {
    _uiState.update { it.copy(courseDetailTab = tabIndex) }
  }

  fun enrollInBatch(batchId: String) {
    _uiState.update {
      it.copy(enrolledBatches = it.enrolledBatches + batchId)
    }
    showToast("Successfully enrolled in batch! Check Courses tab.")
  }

  fun startDailyQuiz() {
    timerJob?.cancel()
    _uiState.update {
      it.copy(
        quizState = QuizUiState(
          questions = MockDataProvider.sampleQuizzes,
          currentQuestionIndex = 0,
          userAnswers = emptyMap(),
          isSubmitted = false,
          score = 0,
          timeRemainingSeconds = 180,
          isTimerRunning = true
        )
      )
    }
    navigateTo(ScreenDestination.DAILY_QUIZ)
    startQuizTimer()
  }

  private fun startQuizTimer() {
    timerJob?.cancel()
    timerJob = viewModelScope.launch {
      while (_uiState.value.quizState.timeRemainingSeconds > 0 &&
             !_uiState.value.quizState.isSubmitted &&
             _uiState.value.quizState.isTimerRunning) {
        delay(1000)
        _uiState.update { state ->
          val newTime = state.quizState.timeRemainingSeconds - 1
          if (newTime <= 0) {
            submitQuiz()
            state.copy(quizState = state.quizState.copy(timeRemainingSeconds = 0, isTimerRunning = false))
          } else {
            state.copy(quizState = state.quizState.copy(timeRemainingSeconds = newTime))
          }
        }
      }
    }
  }

  fun selectQuizOption(questionId: Int, optionIndex: Int) {
    if (_uiState.value.quizState.isSubmitted) return
    _uiState.update { state ->
      val updatedAnswers = state.quizState.userAnswers + (questionId to optionIndex)
      state.copy(quizState = state.quizState.copy(userAnswers = updatedAnswers))
    }
  }

  fun nextQuizQuestion() {
    val total = _uiState.value.quizState.questions.size
    val current = _uiState.value.quizState.currentQuestionIndex
    if (current < total - 1) {
      _uiState.update { state ->
        state.copy(quizState = state.quizState.copy(currentQuestionIndex = current + 1))
      }
    }
  }

  fun prevQuizQuestion() {
    val current = _uiState.value.quizState.currentQuestionIndex
    if (current > 0) {
      _uiState.update { state ->
        state.copy(quizState = state.quizState.copy(currentQuestionIndex = current - 1))
      }
    }
  }

  fun jumpToQuizQuestion(index: Int) {
    if (index in _uiState.value.quizState.questions.indices) {
      _uiState.update { state ->
        state.copy(quizState = state.quizState.copy(currentQuestionIndex = index))
      }
    }
  }

  fun submitQuiz() {
    timerJob?.cancel()
    _uiState.update { state ->
      var calculatedScore = 0
      state.quizState.questions.forEach { q ->
        val chosen = state.quizState.userAnswers[q.id]
        if (chosen == q.correctOptionIndex) {
          calculatedScore += 1
        }
      }
      state.copy(
        quizState = state.quizState.copy(
          isSubmitted = true,
          isTimerRunning = false,
          score = calculatedScore
        )
      )
    }
    showToast("Quiz submitted! Your score: ${_uiState.value.quizState.score}/${_uiState.value.quizState.questions.size}")
  }

  fun restartQuiz() {
    startDailyQuiz()
  }

  fun updateSearchQuery(query: String) {
    _uiState.update { state ->
      val results = if (query.isBlank()) {
        emptyList()
      } else {
        MockDataProvider.sampleCourses.filter {
          it.title.contains(query, ignoreCase = true) ||
          it.examCategory.contains(query, ignoreCase = true) ||
          it.educatorName.contains(query, ignoreCase = true)
        }
      }
      state.copy(searchQuery = query, searchResults = results)
    }
  }

  fun markAllNotificationsRead() {
    _uiState.update { state ->
      state.copy(notifications = state.notifications.map { it.copy(isRead = true) })
    }
    showToast("All notifications marked as read")
  }

  fun toggleDownload(item: StudyMaterialItem) {
    _uiState.update { state ->
      val updated = state.studyMaterials.map {
        if (item.id == it.id) it.copy(isDownloaded = !it.isDownloaded) else it
      }
      state.copy(studyMaterials = updated)
    }
    val newStatus = !item.isDownloaded
    showToast(if (newStatus) "Saved to Downloads: ${item.title}" else "Removed from Downloads")
  }

  fun showToast(message: String) {
    viewModelScope.launch {
      _toastEvent.emit(message)
    }
  }

  fun addVideoToCourse(courseTitleOrId: String, title: String, youtubeUrl: String, duration: String, instructor: String) {
    _uiState.update { state ->
      val updatedCourses = state.courseListingItems.map { course ->
        if (course.title.contains(courseTitleOrId, ignoreCase = true) ||
            course.id.equals(courseTitleOrId, ignoreCase = true) ||
            course.examCategory.contains(courseTitleOrId, ignoreCase = true) ||
            courseTitleOrId.equals("All", ignoreCase = true)) {
          val newLesson = RecordedLesson(
            id = "vid_" + System.currentTimeMillis() + "_" + (10..99).random(),
            title = title,
            duration = if (duration.isBlank()) "45 mins" else duration,
            instructor = if (instructor.isBlank()) course.educatorName else instructor,
            date = "Added by Admin",
            videoUrl = youtubeUrl
          )
          course.copy(recordedLessons = listOf(newLesson) + course.recordedLessons)
        } else {
          course
        }
      }

      val updatedSelected = if (state.selectedCourse.title.contains(courseTitleOrId, ignoreCase = true) ||
                                state.selectedCourse.id.equals(courseTitleOrId, ignoreCase = true) ||
                                courseTitleOrId.equals("All", ignoreCase = true)) {
        val newLesson = RecordedLesson(
          id = "vid_" + System.currentTimeMillis(),
          title = title,
          duration = if (duration.isBlank()) "45 mins" else duration,
          instructor = if (instructor.isBlank()) state.selectedCourse.educatorName else instructor,
          date = "Added by Admin",
          videoUrl = youtubeUrl
        )
        state.selectedCourse.copy(recordedLessons = listOf(newLesson) + state.selectedCourse.recordedLessons)
      } else {
        state.selectedCourse
      }

      state.copy(
        courseListingItems = updatedCourses,
        selectedCourse = updatedSelected
      )
    }
    showToast("Video added to $courseTitleOrId! View in Courses -> Recorded tab.")
  }

  fun addPdfMaterial(title: String, subject: String, driveUrl: String, fileSize: String) {
    val newPdf = StudyMaterialItem(
      id = "pdf_" + System.currentTimeMillis(),
      title = title,
      subtitle = "Added from Admin Panel",
      subject = if (subject.isBlank()) "General" else subject,
      fileSize = if (fileSize.isBlank()) "3.5 MB" else fileSize,
      pageCount = 30,
      isDownloaded = true,
      driveUrl = driveUrl,
      isPublished = true
    )
    _uiState.update { state ->
      state.copy(studyMaterials = listOf(newPdf) + state.studyMaterials)
    }
    showToast("PDF added under $subject! View in Profile -> Downloaded Notes.")
  }

  fun addNewCourse(title: String, category: String, price: String, educator: String) {
    val newCourse = CourseBatch(
      id = "c_" + System.currentTimeMillis(),
      title = title,
      examCategory = if (category.isBlank()) "General" else category,
      educatorName = educator.ifBlank { "Expert Educator" },
      rating = 4.9f,
      learnersCount = "New Batch",
      price = price.ifBlank { "₹499" },
      originalPrice = "₹1,999",
      isLive = true,
      validityDate = "VALID UNTIL 31 Dec 2027",
      recordedLessons = emptyList()
    )
    _uiState.update { state ->
      state.copy(courseListingItems = listOf(newCourse) + state.courseListingItems)
    }
    showToast("Course '$title' added successfully!")
  }

  fun syncAdminDataJson(jsonString: String) {
    try {
      val root = JSONObject(jsonString)
      // Parse courses
      if (root.has("courses")) {
        val coursesArray = root.getJSONArray("courses")
        val parsedCourses = mutableListOf<CourseBatch>()
        for (i in 0 until coursesArray.length()) {
          val c = coursesArray.getJSONObject(i)
          val id = c.optString("id", "c_$i")
          val title = c.optString("title", "Course $i")
          val category = c.optString("examCategory", "General")
          val price = c.optString("price", "₹599")
          val educator = c.optString("educatorName", "Expert Educator")
          val isPub = c.optBoolean("isPublished", true)
          if (isPub) {
            parsedCourses.add(
              CourseBatch(
                id = id,
                title = title,
                examCategory = category,
                educatorName = educator,
                rating = 4.8f,
                learnersCount = "15.0k learners",
                price = price,
                originalPrice = "₹2,499",
                isLive = true,
                validityDate = "VALID UNTIL 31 Dec 2027",
                recordedLessons = emptyList()
              )
            )
          }
        }
        if (parsedCourses.isNotEmpty()) {
          _uiState.update { it.copy(courseListingItems = parsedCourses) }
        }
      }

      // Parse videos
      if (root.has("videos")) {
        val videosArray = root.getJSONArray("videos")
        for (i in 0 until videosArray.length()) {
          val v = videosArray.getJSONObject(i)
          val courseTitle = v.optString("courseTitle", "")
          val vTitle = v.optString("title", "Lecture $i")
          val url = v.optString("youtubeUrl", "")
          val dur = v.optString("duration", "45 mins")
          val isPub = v.optBoolean("isPublished", true)
          if (isPub && url.isNotBlank()) {
            addVideoToCourse(courseTitle.ifBlank { "All" }, vTitle, url, dur, "Admin")
          }
        }
      }

      // Parse PDFs
      if (root.has("pdfs")) {
        val pdfsArray = root.getJSONArray("pdfs")
        val newPdfs = mutableListOf<StudyMaterialItem>()
        for (i in 0 until pdfsArray.length()) {
          val p = pdfsArray.getJSONObject(i)
          val pTitle = p.optString("title", "PDF Note $i")
          val subject = p.optString("subject", "General")
          val driveUrl = p.optString("driveUrl", "")
          val size = p.optString("fileSize", "3.0 MB")
          val isPub = p.optBoolean("isPublished", true)
          if (isPub && driveUrl.isNotBlank()) {
            newPdfs.add(
              StudyMaterialItem(
                id = "p_sync_$i",
                title = pTitle,
                subtitle = "Google Drive PDF",
                subject = subject,
                fileSize = size,
                pageCount = 25,
                isDownloaded = true,
                driveUrl = driveUrl,
                isPublished = true
              )
            )
          }
        }
        if (newPdfs.isNotEmpty()) {
          _uiState.update { it.copy(studyMaterials = newPdfs + it.studyMaterials) }
        }
      }
      showToast("Data synced from Admin Panel successfully! ✅")
    } catch (e: Exception) {
      showToast("Sync: ${e.message}")
    }
  }

  override fun onCleared() {
    super.onCleared()
    timerJob?.cancel()
  }
}
