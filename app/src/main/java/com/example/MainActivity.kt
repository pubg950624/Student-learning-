package com.example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.MockDataProvider
import com.example.model.ScreenDestination
import com.example.ui.components.BottomNavBar
import com.example.ui.components.DrawerContent
import com.example.ui.components.TopBlueBar
import com.example.ui.screens.AdminPortalScreen
import com.example.ui.screens.AiTutorScreen

import com.example.ui.screens.BatchDetailScreen
import com.example.ui.screens.CourseDetailScreen
import com.example.ui.screens.CourseListingScreen
import com.example.ui.screens.CoursesScreen
import com.example.ui.screens.DailyQuizScreen
import com.example.ui.screens.DownloadsScreen
import com.example.ui.screens.EditProfileScreen
import com.example.ui.screens.FaqScreen
import com.example.ui.screens.FeatureDetailScreen
import com.example.ui.screens.HelpSupportScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyDownloadsScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SubmissionsScreen
import com.example.ui.screens.TermsScreen
import com.example.ui.screens.TestsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.recaptcha.RecaptchaAppCheckProviderFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val APP_CHECK_TAG = "AppCheckInit"
private const val DEBUG_TOKEN_EXTRA = "FIREBASE_APPCHECK_DEBUG_TOKEN"
private const val DEBUG_STORE_PREFIX = "com.google.firebase.appcheck.debug.store."
private const val DEBUG_SECRET_KEY = "com.google.firebase.appcheck.debug.DEBUG_SECRET"
private const val RECAPTCHA_SITE_KEY_RES = "recaptcha_site_key"

fun configureAppCheck(context: Context, intent: Intent?) {
  try {
    if (FirebaseApp.getApps(context).isEmpty()) {
      FirebaseApp.initializeApp(context)
    }

    if (BuildConfig.DEBUG) {
      val debugToken = intent?.getStringExtra(DEBUG_TOKEN_EXTRA)
      if (!debugToken.isNullOrEmpty()) {
        val persistenceKey = FirebaseApp.getInstance().persistenceKey
        val prefs = context.getSharedPreferences(
          "$DEBUG_STORE_PREFIX$persistenceKey",
          Context.MODE_PRIVATE
        )
        prefs.edit()
          .putString(DEBUG_SECRET_KEY, debugToken)
          .commit()
        Log.d(APP_CHECK_TAG, "App Check debug secret configured successfully.")
      } else {
        Log.w(APP_CHECK_TAG, "No debug token found in Intent extra.")
      }

      FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
        DebugAppCheckProviderFactory.getInstance()
      )
      Log.d(APP_CHECK_TAG, "DebugAppCheckProviderFactory installed.")
    } else {
      val resId = context.resources.getIdentifier(RECAPTCHA_SITE_KEY_RES, "string", context.packageName)
      if (resId != 0) {
        val siteKey = context.getString(resId)
        if (siteKey.isNotBlank()) {
          FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
            RecaptchaAppCheckProviderFactory.getInstance(siteKey)
          )
          Log.d(APP_CHECK_TAG, "RecaptchaAppCheckProviderFactory installed.")
        } else {
          Log.e(APP_CHECK_TAG, "recaptcha_site_key resource string is blank.")
        }
      } else {
        Log.e(APP_CHECK_TAG, "recaptcha_site_key string resource not found in package.")
      }
    }
  } catch (e: Exception) {
    Log.e(APP_CHECK_TAG, "Failed to initialize Firebase App Check", e)
  }
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    configureAppCheck(this, intent)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        MainAppContent()
      }
    }
  }
}

@Composable
fun MainAppContent(
  viewModel: AppViewModel = viewModel()
) {
  val context = LocalContext.current
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  // Listen to Toast events
  LaunchedEffect(Unit) {
    viewModel.toastEvent.collectLatest { message ->
      Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
  }

  // Handle Android back button
  BackHandler(enabled = true) {
    if (drawerState.isOpen) {
      scope.launch { drawerState.close() }
    } else {
      val handled = viewModel.navigateBack()
      if (!handled) {
        (context as? ComponentActivity)?.finish()
      }
    }
  }

  val unreadNotifications = uiState.notifications.count { !it.isRead }

  val showBottomNav = uiState.currentDestination in listOf(
    ScreenDestination.HOME,
    ScreenDestination.COURSES,
    ScreenDestination.TESTS,
    ScreenDestination.PROFILE
  )

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      ModalDrawerSheet {
        DrawerContent(
          onNavigate = { dest ->
            viewModel.navigateTo(dest)
          },
          onCloseDrawer = {
            scope.launch { drawerState.close() }
          },
          onShowToast = { msg ->
            viewModel.showToast(msg)
          }
        )
      }
    }
  ) {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
        if (uiState.currentDestination == ScreenDestination.HOME) {
          TopBlueBar(
            selectedCategory = uiState.selectedCategory,
            isDropdownExpanded = uiState.isCategoryDropdownExpanded,
            unreadNotificationsCount = unreadNotifications,
            onMenuClick = {
              scope.launch { drawerState.open() }
            },
            onCategoryClick = {
              viewModel.toggleCategoryDropdown(!uiState.isCategoryDropdownExpanded)
            },
            onCategorySelect = { cat ->
              viewModel.selectCategory(cat)
            },
            onDismissDropdown = {
              viewModel.toggleCategoryDropdown(false)
            },
            onNotificationClick = {
              viewModel.navigateTo(ScreenDestination.NOTIFICATIONS)
            },
            onDownloadClick = {
              viewModel.navigateTo(ScreenDestination.MY_DOWNLOADS)
            },
            onSearchClick = {
              viewModel.navigateTo(ScreenDestination.SEARCH)
            }
          )
        }
      },
      bottomBar = {
        if (showBottomNav) {
          BottomNavBar(
            currentDestination = uiState.currentDestination,
            onNavigate = { dest ->
              viewModel.navigateTo(dest)
            }
          )
        }
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        when (uiState.currentDestination) {
          ScreenDestination.AI_TUTOR -> {
            AiTutorScreen(
              onBackClick = {
                viewModel.navigateBack()
              },
              onShowToast = { msg ->
                viewModel.showToast(msg)
              }
            )
          }

          ScreenDestination.HOME -> {
            HomeScreen(
              banners = uiState.banners,
              currentBannerIndex = uiState.currentBannerIndex,
              onBannerIndexChanged = { idx ->
                viewModel.setBannerIndex(idx)
              },
              onExploreBatch = { batch ->
                viewModel.openBatchDetails(batch)
              },
              onViewBatchDetails = { batch ->
                viewModel.openBatchDetails(batch)
              },
              onFeatureClick = { feature ->
                viewModel.openFeature(feature)
              },
              onShowAllCourses = {
                viewModel.openCourseListing("Courses")
              }
            )
          }

          ScreenDestination.COURSES -> {
            CourseListingScreen(
              title = "Courses Catalog",
              courses = MockDataProvider.sampleCourses,
              onCourseClick = { course ->
                viewModel.openCourseDetail(course)
              },
              onBackClick = {
                viewModel.navigateBack()
              }
            )
          }

          ScreenDestination.TESTS -> {
            TestsScreen(
              onStartQuiz = {
                viewModel.startDailyQuiz()
              },
              onShowToast = { msg ->
                viewModel.showToast(msg)
              }
            )
          }

          ScreenDestination.PROFILE -> {
            ProfileScreen(
              studentName = uiState.studentName,
              studentMobile = uiState.studentMobile,
              studentEmail = uiState.studentEmail,
              onNavigateDownloads = {
                viewModel.navigateTo(ScreenDestination.MY_DOWNLOADS)
              },
              onNavigateSubmissions = {
                viewModel.navigateTo(ScreenDestination.MY_SUBMISSIONS)
              },
              onNavigateOrders = {
                viewModel.navigateTo(ScreenDestination.MY_ORDERS)
              },
              onNavigateFaqs = {
                viewModel.navigateTo(ScreenDestination.FAQS)
              },
              onNavigateTerms = {
                viewModel.navigateTo(ScreenDestination.TERMS_CONDITIONS)
              },
              onNavigateHelp = {
                viewModel.navigateTo(ScreenDestination.HELP_SUPPORT)
              },
              onNavigateEditProfile = {
                viewModel.navigateTo(ScreenDestination.EDIT_PROFILE)
              },
              onNavigateAdmin = {
                viewModel.navigateTo(ScreenDestination.ADMIN_PORTAL)
              },
              onShowToast = { msg ->
                viewModel.showToast(msg)
              }
            )
          }

          ScreenDestination.EDIT_PROFILE -> {
            EditProfileScreen(
              currentName = uiState.studentName,
              currentMobile = uiState.studentMobile,
              currentEmail = uiState.studentEmail,
              onSave = { name, mobile, email ->
                viewModel.updateProfile(name, mobile, email)
                viewModel.navigateBack()
              },
              onBackClick = {
                viewModel.navigateBack()
              }
            )
          }

          ScreenDestination.MY_DOWNLOADS -> {
            MyDownloadsScreen(
              studyMaterials = uiState.studyMaterials,
              onToggleDownload = { item ->
                viewModel.toggleDownload(item)
              },
              onBackClick = {
                viewModel.navigateBack()
              },
              onShowToast = { msg ->
                viewModel.showToast(msg)
              }
            )
          }

          ScreenDestination.MY_SUBMISSIONS -> {
            SubmissionsScreen(
              onBackClick = { viewModel.navigateBack() }
            )
          }

          ScreenDestination.MY_ORDERS -> {
            OrdersScreen(
              onBackClick = { viewModel.navigateBack() }
            )
          }

          ScreenDestination.FAQS -> {
            FaqScreen(
              onBackClick = { viewModel.navigateBack() }
            )
          }

          ScreenDestination.TERMS_CONDITIONS -> {
            TermsScreen(
              onBackClick = { viewModel.navigateBack() }
            )
          }

          ScreenDestination.HELP_SUPPORT -> {
            HelpSupportScreen(
              onBackClick = { viewModel.navigateBack() },
              onShowToast = { msg -> viewModel.showToast(msg) }
            )
          }

          ScreenDestination.COURSE_LISTING -> {
            CourseListingScreen(
              title = uiState.courseListingTitle,
              courses = uiState.courseListingItems,
              onCourseClick = { course ->
                viewModel.openCourseDetail(course)
              },
              onBackClick = {
                viewModel.navigateBack()
              }
            )
          }

          ScreenDestination.COURSE_DETAIL -> {
            CourseDetailScreen(
              course = uiState.selectedCourse,
              selectedTab = uiState.courseDetailTab,
              onTabSelected = { tabIndex ->
                viewModel.setCourseDetailTab(tabIndex)
              },
              onBackClick = {
                viewModel.navigateBack()
              },
              onShowToast = { msg ->
                viewModel.showToast(msg)
              }
            )
          }

          ScreenDestination.BATCH_DETAILS -> {
            BatchDetailScreen(
              batch = uiState.selectedBatch,
              isEnrolled = uiState.enrolledBatches.contains(uiState.selectedBatch.id),
              onEnrollClick = { id ->
                viewModel.enrollInBatch(id)
              },
              onBackClick = {
                viewModel.navigateBack()
              }
            )
          }

          ScreenDestination.DAILY_QUIZ -> {
            DailyQuizScreen(
              quizState = uiState.quizState,
              onOptionSelected = { qId, optIdx ->
                viewModel.selectQuizOption(qId, optIdx)
              },
              onNextQuestion = {
                viewModel.nextQuizQuestion()
              },
              onPrevQuestion = {
                viewModel.prevQuizQuestion()
              },
              onJumpToQuestion = { idx ->
                viewModel.jumpToQuizQuestion(idx)
              },
              onSubmitQuiz = {
                viewModel.submitQuiz()
              },
              onRestartQuiz = {
                viewModel.restartQuiz()
              },
              onBackClick = {
                viewModel.navigateBack()
              }
            )
          }

          ScreenDestination.FEATURE_DETAIL -> {
            FeatureDetailScreen(
              feature = uiState.selectedFeature,
              studyMaterials = uiState.studyMaterials,
              courses = MockDataProvider.sampleCourses,
              onToggleDownload = { item ->
                viewModel.toggleDownload(item)
              },
              onLaunchCustomQuiz = {
                viewModel.startDailyQuiz()
              },
              onShowToast = { msg ->
                viewModel.showToast(msg)
              },
              onBackClick = {
                viewModel.navigateBack()
              }
            )
          }

          ScreenDestination.NOTIFICATIONS -> {
            NotificationsScreen(
              notifications = uiState.notifications,
              onMarkAllRead = {
                viewModel.markAllNotificationsRead()
              },
              onBackClick = {
                viewModel.navigateBack()
              }
            )
          }

          ScreenDestination.DOWNLOADS -> {
            DownloadsScreen(
              studyMaterials = uiState.studyMaterials,
              onToggleDownload = { item ->
                viewModel.toggleDownload(item)
              },
              onBackClick = {
                viewModel.navigateBack()
              },
              onShowToast = { msg ->
                viewModel.showToast(msg)
              }
            )
          }

          ScreenDestination.SEARCH -> {
            SearchScreen(
              query = uiState.searchQuery,
              results = uiState.searchResults,
              onQueryChange = { q ->
                viewModel.updateSearchQuery(q)
              },
              onSelectCourse = { c ->
                viewModel.openCourseDetail(c)
              },
              onBackClick = {
                viewModel.navigateBack()
              }
            )
          }

          ScreenDestination.ADMIN_PORTAL -> {
            AdminPortalScreen(
              coursesList = uiState.courseListingItems,
              onAddVideo = { targetCourse, title, url, duration ->
                viewModel.addVideoToCourse(targetCourse, title, url, duration, "Faculty")
              },
              onAddPdf = { subject, title, driveUrl, size ->
                viewModel.addPdfMaterial(title, subject, driveUrl, size)
              },
              onAddCourse = { title, category, price, educator ->
                viewModel.addNewCourse(title, category, price, educator)
              },
              onSyncJson = { json ->
                viewModel.syncAdminDataJson(json)
              },
              onBackClick = {
                viewModel.navigateBack()
              },
              onShowToast = { msg ->
                viewModel.showToast(msg)
              }
            )
          }
        }
      }
    }
  }
}
