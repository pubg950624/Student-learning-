package com.example.model

enum class ScreenDestination {
  HOME,
  COURSES,
  TESTS,
  PROFILE,
  BATCH_DETAILS,
  DAILY_QUIZ,
  FEATURE_DETAIL,
  COURSE_LISTING,
  COURSE_DETAIL,
  MY_DOWNLOADS,
  MY_SUBMISSIONS,
  MY_ORDERS,
  FAQS,
  TERMS_CONDITIONS,
  HELP_SUPPORT,
  EDIT_PROFILE,
  NOTIFICATIONS,
  DOWNLOADS,
  SEARCH,
  AI_TUTOR,
  ADMIN_PORTAL
}

enum class FeatureType(
  val id: String,
  val label: String,
  val categoryDescription: String
) {
  PAID_CLASSES("paid_classes", "Paid Classes", "Structured live & recorded foundation batches with expert faculties"),
  FREE_COURSES("free_courses", "Free Courses", "Free conceptual masterclasses and subject-wise revision series"),
  FREE_WEEKLY_TEST("free_weekly_test", "Free Weekly Test", "All-India live ranking mock tests conducted every Sunday"),
  BOOKS("books", "Books", "Curated printed & digital textbooks, formula guides, and solved papers"),
  PDF_CLASS_NOTES("pdf_class_notes", "PDF Class Notes", "Handwritten class notes, teacher slides, and summary cheat-sheets"),
  PAID_TEST_SERIES("paid_test_series", "Paid Test Series", "Exhaustive exam-oriented test packs with percentile analytics"),
  PREVIOUS_SYLLABUS("previous_syllabus", "Previous Syllabus", "Previous 10 years official exam syllabus and topic-wise weightage"),
  CREATE_TEST("create_test", "Create Test", "Custom practice tests tailored by subject, difficulty, and timer"),
  DAILY_QUIZ("daily_quiz", "Daily Quiz", "10-question daily boost for General Awareness, Math, and Reasoning"),
  AI_TUTOR("ai_tutor", "AI Study Tutor", "Instant AI-powered doubt solving and exam concept explanations using Gemini")
}

data class BannerItem(
  val id: String,
  val title: String,
  val hindiHeading: String,
  val subTitle: String,
  val price: String,
  val originalPrice: String,
  val discountPercent: String,
  val features: List<String>,
  val targetExam: String,
  val batchStartDate: String
)

data class QuizQuestion(
  val id: Int,
  val subject: String,
  val question: String,
  val options: List<String>,
  val correctOptionIndex: Int,
  val explanation: String
)

data class StudyMaterialItem(
  val id: String,
  val title: String,
  val subtitle: String,
  val subject: String,
  val fileSize: String,
  val pageCount: Int,
  val isDownloaded: Boolean = false,
  val driveUrl: String = "",
  val isPublished: Boolean = true
)

data class NotificationItem(
  val id: String,
  val title: String,
  val message: String,
  val timeAgo: String,
  val isRead: Boolean = false,
  val tag: String
)

data class RecordedLesson(
  val id: String,
  val title: String,
  val duration: String,
  val instructor: String,
  val date: String,
  val videoUrl: String = ""
)

data class CourseBatch(
  val id: String,
  val title: String,
  val examCategory: String,
  val educatorName: String,
  val rating: Float,
  val learnersCount: String,
  val price: String,
  val originalPrice: String,
  val isLive: Boolean = true,
  val validityDate: String = "VALID UNTIL 27 Oct 2027",
  val recordedLessons: List<RecordedLesson> = listOf(
    RecordedLesson("yt1", "New YouTube Lecture", "50 mins", "SSC & Railway", "Published", "https://youtube.com/playlist?list=RD8BfbY0vb7Ro&playnext=1&si=vRgSpcXd1GmTCoo6"),
    RecordedLesson("yt2", "ADMIN_SECURE_2026", "50 mins", "SSC & Railway", "Published", "https://youtube.com/playlist?list=RD8BfbY0vb7Ro&playnext=1&si=vRgSpcXd1GmTCoo6"),
    RecordedLesson("l1", "Introduction & Syllabus Strategy", "45 mins", "Expert Faculty", "15 Oct 2026", "https://www.youtube.com/watch?v=dQw4w9WgXcQ"),
    RecordedLesson("l2", "Chapter 1: Fundamental Concepts & Shortcuts", "1 hr 12 mins", "Senior Educator", "18 Oct 2026", "https://www.youtube.com/watch?v=dQw4w9WgXcQ"),
    RecordedLesson("l3", "Previous Year Question Paper Discussion", "58 mins", "Special Guest Faculty", "22 Oct 2026", "https://www.youtube.com/watch?v=dQw4w9WgXcQ")
  )
)

data class SubmissionItem(
  val id: String,
  val testTitle: String,
  val date: String,
  val score: String,
  val status: String
)

data class OrderItem(
  val id: String,
  val courseName: String,
  val orderDate: String,
  val price: String,
  val status: String
)

data class FaqItem(
  val id: String,
  val question: String,
  val answer: String
)

data class DownloadableVideo(
  val id: String,
  val title: String,
  val courseName: String,
  val duration: String,
  val fileSize: String
)

object MockDataProvider {
  val categories = listOf(
    "All",
    "SSC & Railway",
    "Banking & Insurance",
    "Teaching & TET",
    "State PSC",
    "Defence Exams"
  )

  val banners = listOf(
    BannerItem(
      id = "batch_2026",
      title = "UPCOMING EXAM BATCH 2026",
      hindiHeading = "सफलता की तैयारी",
      subTitle = "Target SSC CGL & RRB NTPC 2026 Comprehensive Master Course",
      price = "₹599",
      originalPrice = "₹2,499",
      discountPercent = "76% OFF",
      features = listOf("Live Classes", "PDF Notes", "Mock Tests"),
      targetExam = "SSC CGL / CHSL / RRB NTPC",
      batchStartDate = "Starts 15th Nov 2026"
    ),
    BannerItem(
      id = "batch_banking",
      title = "BANKING MAHAPACK 2026",
      hindiHeading = "सपनों की उड़ान",
      subTitle = "IBPS PO, Clerk & SBI Master Foundation Series with Speed Quizzes",
      price = "₹799",
      originalPrice = "₹3,999",
      discountPercent = "80% OFF",
      features = listOf("Daily Live Sessions", "Sectional Tests", "Interview Prep"),
      targetExam = "IBPS / SBI / RBI",
      batchStartDate = "Starts 20th Nov 2026"
    )
  )

  val sampleQuizzes = listOf(
    QuizQuestion(
      id = 1,
      subject = "General Awareness",
      question = "Which Indian space mission successfully landed near the south pole region of the Moon in 2023?",
      options = listOf("Mangalyaan 2", "Chandrayaan-3", "Aditya-L1", "Gaganyaan"),
      correctOptionIndex = 1,
      explanation = "Chandrayaan-3 achieved a historic soft landing near the lunar south pole on August 23, 2023, celebrated as National Space Day."
    ),
    QuizQuestion(
      id = 2,
      subject = "General Science",
      question = "Which enzyme present in human saliva initiates the digestion of starch into simpler sugars?",
      options = listOf("Pepsin", "Salivary Amylase (Ptyalin)", "Trypsin", "Lipase"),
      correctOptionIndex = 1,
      explanation = "Salivary amylase (also known as ptyalin) breaks down complex carbohydrates like starch into maltose and dextrin."
    ),
    QuizQuestion(
      id = 3,
      subject = "Indian Polity",
      question = "Under which Article of the Indian Constitution is the 'Right to Constitutional Remedies' guaranteed?",
      options = listOf("Article 19", "Article 21", "Article 32", "Article 44"),
      correctOptionIndex = 2,
      explanation = "Article 32 allows citizens to approach the Supreme Court directly for enforcement of Fundamental Rights, called the 'Heart and Soul' of the Constitution by Dr. B.R. Ambedkar."
    ),
    QuizQuestion(
      id = 4,
      subject = "Quantitative Aptitude",
      question = "If an article is bought for ₹400 and sold for ₹480, what is the profit percentage?",
      options = listOf("15%", "20%", "25%", "18%"),
      correctOptionIndex = 1,
      explanation = "Profit = ₹480 - ₹400 = ₹80. Profit percentage = (80 / 400) * 100 = 20%."
    ),
    QuizQuestion(
      id = 5,
      subject = "Reasoning Ability",
      question = "Find the missing number in the sequence: 4, 9, 16, 25, 36, ?",
      options = listOf("42", "48", "49", "64"),
      correctOptionIndex = 2,
      explanation = "The sequence represents squares of consecutive integers: 2²=4, 3²=9, 4²=16, 5²=25, 6²=36, so next is 7² = 49."
    )
  )

  val pdfNotes = listOf(
    StudyMaterialItem("pdf_1", "Modern Indian History Short Notes", "Handwritten summary covering 1857 to 1947", "History", "4.2 MB", 48, true),
    StudyMaterialItem("pdf_2", "Quantitative Aptitude Shortcut Formula Sheet", "Arithmetic & Advanced Math quick tricks", "Maths", "2.8 MB", 32, true),
    StudyMaterialItem("pdf_3", "General Science 1000 One-Liners", "Physics, Chemistry and Biology exam points", "Science", "5.1 MB", 64, false),
    StudyMaterialItem("pdf_4", "English Grammar Rules & Vocabulary Deck", "Essential rules with error spotting examples", "English", "3.6 MB", 50, false),
    StudyMaterialItem("pdf_5", "Current Affairs 2026 Monthly Capsule", "National & International highlights", "Current Affairs", "6.4 MB", 80, true)
  )

  val notifications = listOf(
    NotificationItem("n1", "Live Class Alert!", "Railway NTPC Reasoning Live Practice is starting in 15 minutes.", "10m ago", false, "LIVE"),
    NotificationItem("n2", "Free Weekly Test Live", "All-India Sunday Mock Test is now active. Attempt now to check rank.", "1h ago", false, "TEST"),
    NotificationItem("n3", "New PDF Uploaded", "Science Chapter 4 Handwritten Notes have been added to your dashboard.", "3h ago", true, "NOTES"),
    NotificationItem("n4", "Batch Discount Ending Soon", "Flat 76% discount on Exam Batch 2026 expires tonight at 11:59 PM.", "5h ago", true, "OFFER")
  )

  val sampleCourses = listOf(
    CourseBatch(
      id = "c_upsssc",
      title = "UPSSSC PET 2026 Complete Foundation Batch",
      examCategory = "State Exams",
      educatorName = "R.K. Tiwari & Expert Team",
      rating = 4.8f,
      learnersCount = "32.4k learners",
      price = "₹599",
      originalPrice = "₹2,499",
      isLive = true,
      validityDate = "VALID UNTIL 27 Oct 2027"
    ),
    CourseBatch(
      id = "c_navodaya",
      title = "Navodaya Vidyalaya 2027 Entrance Master Batch",
      examCategory = "School Entrance",
      educatorName = "Pooja Gupta Ma'am",
      rating = 4.9f,
      learnersCount = "14.1k learners",
      price = "₹799",
      originalPrice = "₹3,499",
      isLive = true,
      validityDate = "VALID UNTIL 15 May 2027"
    ),
    CourseBatch(
      id = "c_ssc_gd",
      title = "SSC GD Constable 2026 Target Batch",
      examCategory = "SSC & Defence",
      educatorName = "Amitabh Verma Sir",
      rating = 4.7f,
      learnersCount = "45.0k learners",
      price = "₹499",
      originalPrice = "₹1,999",
      isLive = true,
      validityDate = "VALID UNTIL 10 Dec 2027"
    ),
    CourseBatch(
      id = "c_railway",
      title = "Railway Exam Preparation (NTPC & Group D)",
      examCategory = "SSC & Railway",
      educatorName = "Dr. S. Mukherjee",
      rating = 4.8f,
      learnersCount = "28.9k learners",
      price = "₹699",
      originalPrice = "₹2,999",
      isLive = true,
      validityDate = "VALID UNTIL 30 Nov 2027"
    ),
    CourseBatch(
      id = "c5",
      title = "Banking Super Achievers Batch 2026",
      examCategory = "Banking & Insurance",
      educatorName = "Amitabh Verma Sir",
      rating = 4.9f,
      learnersCount = "12.1k learners",
      price = "₹799",
      originalPrice = "₹3,499",
      isLive = false,
      validityDate = "VALID UNTIL 27 Oct 2027"
    )
  )

  val sampleSubmissions = listOf(
    SubmissionItem("sub_1", "SSC CGL Tier-1 Full Mock Test 04", "24 Oct 2026", "142 / 200", "Evaluated (Rank 312)"),
    SubmissionItem("sub_2", "Daily Booster Quiz #24", "26 Oct 2026", "10 / 10", "Passed"),
    SubmissionItem("sub_3", "Quantitative Aptitude Sectional Test", "21 Oct 2026", "42 / 50", "Evaluated")
  )

  val sampleOrders = listOf(
    OrderItem("ord_1", "UPSSSC PET 2026 Complete Foundation Batch", "15 Oct 2026", "₹599", "Successful (Demo Order)"),
    OrderItem("ord_2", "Railway Exam Preparation (NTPC & Group D)", "10 Sep 2026", "₹699", "Successful (Demo Order)")
  )

  val sampleFaqs = listOf(
    FaqItem("faq_1", "How do I access my downloaded PDF notes offline?", "Go to Profile -> My Downloads -> PDFs tab to view and read all downloaded study notes without internet connection."),
    FaqItem("faq_2", "Are live classes recorded if I miss them?", "Yes! Every live class is recorded automatically and uploaded to the Course -> Recorded tab within 2 hours of completion."),
    FaqItem("faq_3", "How do I participate in the Daily Quiz?", "Tap on the 'Daily Quiz' card on the Home screen or Tests tab to start the 10-question speed booster quiz with timer and instant explanations."),
    FaqItem("faq_4", "Can I update my registered mobile number or email?", "Yes, you can edit your profile details anytime by clicking the Edit button in the Profile screen.")
  )

  val sampleDownloadVideos = listOf(
    DownloadableVideo("v1", "Introduction & Syllabus Strategy", "UPSSSC PET 2026", "45 mins", "180 MB"),
    DownloadableVideo("v2", "Chapter 1: Number Systems & Simplification", "Railway Exam Preparation", "1 hr 12 mins", "310 MB")
  )
}
