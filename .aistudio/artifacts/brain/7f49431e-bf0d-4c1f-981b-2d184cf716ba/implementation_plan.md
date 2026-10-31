# My Student Learning - Android Ed-Tech Home Screen & Learning Portal

A high-fidelity native Android ed-tech learning app inspired by leading Indian competitive exam portals, featuring a royal blue branded top bar, category filters, interactive promotional batch banners, a 3x3 educational feature grid, bottom navigation, and a fully playable Daily Quiz with score tracking.

## User Review & Critical Decisions

> [!IMPORTANT]
> The architectural direction and user experience have been aligned with the user preferences:
> - **Visual Identity**: Royal Blue (`#1769E8`), Dark Navy (`#14254B`), Accent Orange (`#FF9B32`), and crisp background surfaces (`#F7F8FC`) inspired by premier Indian education platforms.
> - **Feature Breadth**: Full implementation of all 9 feature card destinations (Paid Classes, Free Courses, Free Weekly Test, Books, PDF Class Notes, Paid Test Series, Previous Syllabus, Create Test, Daily Quiz) with realistic local content and a live interactive MCQ Daily Quiz.
> - **Navigation & Insets**: Edge-to-edge support with safe drawing insets, top app bar status bar padding, and gesture-friendly bottom navigation.

- **Confirmed Decision 1**: Royal Blue and White education theme with clean high-contrast typography.
- **Confirmed Decision 2**: Complete interactive destinations for all 9 grid features plus an interactive multiple-choice Daily Quiz engine with scoring and explanations.
- **Navigation Model**: Bottom tabs (Home, Courses, Tests, Profile) with sub-screen stacks, BackHandler support, drawer sidebar navigation, and quick search.

---

## 1. Overview & Core Concept

- **What It Does**: Provides students preparing for competitive examinations (such as SSC, Railway, Banking, State PSC, and Teaching exams) with a centralized learning dashboard to browse courses, access free video lectures, read PDF notes, attempt weekly tests and daily quizzes, and purchase comprehensive batches.
- **Target Audience / Persona**: Aspirants seeking structured, mobile-optimized exam preparation with easily accessible study materials, live class updates, and instant practice quizzes.
- **Key Value**: Delivers a frictionless, intuitive mobile dashboard that matches familiar native Indian ed-tech patterns, ensuring swift access to study materials and test series without visual clutter.

---

## 2. User Experience & Visual Design

### Key User Flows
1. **Home Screen Exploration**:
   - Royal blue header with a hamburger menu button (opens a slide-out drawer with student profile & menu links), category selector ("All" dropdown to filter exams), notification bell with an unread badge, offline downloads shortcut, and search action.
   - Warm personalized greeting: "Hello, Student".
   - Carousel banner: "UPCOMING EXAM BATCH 2026", Hindi tagline "सफलता की तैयारी", feature tags (*Live Classes*, *PDF Notes*, *Mock Tests*), pricing badge (₹599), and CTA buttons (*Explore Batch*, *View Details*).
   - 3-column by 3-row grid of high-contrast cards with bespoke illustrated vector icons and labels:
     - Row 1: Paid Classes, Free Courses, Free Weekly Test
     - Row 2: Books, PDF Class Notes, Paid Test Series
     - Row 3: Previous Syllabus, Create Test, Daily Quiz
2. **Interactive Daily Quiz Experience**:
   - Tapping "Daily Quiz" launches a full-featured MCQ test flow with a countdown timer, question progression, options selection, instant submission, detailed answer explanations, and score summary dialog.
3. **Bottom Navigation**:
   - 4-tab bar: Home, Courses, Tests, Profile with smooth tab transitions and badge indicators.
4. **Sub-screens & Modals**:
   - Batch Details bottom sheet/screen with syllabus preview and enrollment CTA.
   - Search bar dialog with instant filtering of courses and study materials.
   - Notification center listing test reminders and live class announcements.
   - Offline Downloads screen showing downloaded PDF notes and offline videos.

### Visual Identity & Theme
- **Primary Royal Blue**: `#1769E8` (app bar, key actions, highlight pills)
- **Dark Navy**: `#14254B` (banner backdrop, primary text, high-emphasis headings)
- **Accent Orange**: `#FF9B32` (badge tags, CTA button highlights, quiz timer, discounts)
- **Background**: `#F7F8FC` (clean off-white for soft contrast and readability)
- **Card Surface**: `#FFFFFF` with 1dp subtle border (`#E4E8F0`) and 8dp corner radius.
- **Typography**: Clean sans-serif hierarchy (Bold 20sp for greeting, SemiBold 13sp for grid titles, Medium 11sp for sub-labels).

---

## 3. Key Product Decisions & Trade-Offs

- **Decision 1: Modular Jetpack Compose Architecture**:
  - *Chosen Approach*: Clean unidirectional state flow (`ViewModel` + `StateFlow`) with distinct screen composables (`HomeScreen`, `DailyQuizScreen`, `BatchDetailSheet`, `NavigationDrawerContent`, `SearchOverlay`).
  - *Why*: Ensures rapid recomposition, predictable backstack behavior, and maintainable code without monolithic files.
- **Decision 2: Rich Local Mock Repository**:
  - *Chosen Approach*: Curated in-memory dataset of competitive exam batches, PDF notes, quiz questions, and notification events.
  - *Why*: The user requirement explicitly states "No backend or API keys required" and "Use realistic local sample data". Local state allows instant responsiveness and zero-latency quiz interactions.
- **Decision 3: Custom Educational Vector Assets**:
  - *Chosen Approach*: Compose vector icons and custom canvas badges for all 9 feature grid items and banner graphics to ensure crisp, scalable rendering across any mobile density without external asset load failures.

---

## 4. Technical Architecture & Data Strategy

### System & Navigation Architecture
```
┌─────────────────────────────────────────────────────────────┐
│                       MainActivity                          │
│          ┌──────────────────────────────────────┐           │
│          │             AppViewModel             │           │
│          │  - uiState (currentTab, activeScreen)│           │
│          │  - quizState (timer, score, answers) │           │
│          │  - selectedCategory ("All", "SSC"...)│           │
│          └──────────────────────────────────────┘           │
│                              │                              │
│              ┌───────────────┴───────────────┐              │
│              ▼                               ▼              │
│      ┌──────────────┐                 ┌─────────────┐       │
│      │  DrawerHost  │                 │ MainLayout  │       │
│      └──────────────┘                 └──────┬──────┘       │
│                                              │              │
│           ┌──────────────────────────────────┴─────┐        │
│           ▼                                        ▼        │
│   ┌───────────────┐                        ┌─────────────┐  │
│   │ TopBlueHeader │                        │ BottomNavBar│  │
│   └───────────────┘                        └─────────────┘  │
│           │                                                 │
│           ▼ (Body content based on activeScreen)            │
│   ┌───────────────────────────────────────────────────────┐ │
│   │ • HomeScreen (Banner Carousel + 3x3 Feature Grid)     │ │
│   │ • DailyQuizScreen (Interactive MCQ practice)          │ │
│   │ • BatchDetailScreen (Exam 2026 syllabus & preview)    │ │
│   │ • FeatureDetailScreen (Courses, Books, Notes, etc.)   │ │
│   │ • SearchOverlay & NotificationSheet                   │ │
│   └───────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### Component & State Mapping
- `AppUiState`: Holds active navigation destination, selected filter category, search query, drawer open/close state, and notification list.
- `QuizUiState`: Holds current question index, selected option per question, remaining seconds, submission state, and explanation visibility.
- `BackHandler`: Integrated into all non-home destinations so pressing Android back smoothly returns to the Home tab.
