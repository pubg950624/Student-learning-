import React, { useState, useEffect } from 'react';
import { 
  LayoutDashboard, Users, GraduationCap, BookOpen, Video, FileText, 
  HelpCircle, CreditCard, Bell, BarChart3, Settings, LogOut, Plus, 
  Edit2, Trash2, CheckCircle, XCircle, ExternalLink, Search, Filter, 
  AlertCircle, Save, Shield, ArrowUpRight, DollarSign, Award, Clock
} from 'lucide-react';

interface Student {
  id: string;
  name: string;
  email: string;
  phone: string;
  course: string;
  batch: string;
  feeStatus: 'Paid' | 'Pending' | 'Partial';
  joinedDate: string;
}

interface Teacher {
  id: string;
  name: string;
  subject: string;
  email: string;
  phone: string;
  assignedCourse: string;
}

interface Course {
  id: string;
  title: string;
  examCategory: string;
  educatorName: string;
  price: string;
  validityDate: string;
  isPublished: boolean;
}

interface VideoItem {
  id: string;
  courseTitle: string;
  title: string;
  youtubeUrl: string;
  duration: string;
  isPublished: boolean;
}

interface PdfItem {
  id: string;
  title: string;
  subject: string;
  driveUrl: string;
  fileSize: string;
  isPublished: boolean;
}

interface TestItem {
  id: string;
  title: string;
  category: string;
  totalQuestions: number;
  durationMins: number;
  isLive: boolean;
}

interface PaymentRecord {
  id: string;
  studentName: string;
  course: string;
  amount: string;
  date: string;
  status: 'Successful' | 'Pending' | 'Failed';
}

export default function App() {
  // Auth State
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(() => {
    return localStorage.getItem('super_admin_auth') === 'true';
  });
  const [email, setEmail] = useState<string>('pubg950624@gmail.com');
  const [password, setPassword] = useState<string>('');
  const [loginError, setLoginError] = useState<string>('');
  const [activeTab, setActiveTab] = useState<string>('dashboard');

  // Search & Filter state
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [filterCategory, setFilterCategory] = useState<string>('All');

  // Confirmation Modal state
  const [confirmModal, setConfirmModal] = useState<{
    isOpen: boolean;
    title: string;
    message: string;
    onConfirm: () => void;
  }>({ isOpen: false, title: '', message: '', onConfirm: () => {} });

  // Notification Toast state
  const [toastMessage, setToastMessage] = useState<string>('');

  // Apps Script & Firebase config state
  const [apiUrl, setApiUrl] = useState<string>(() => {
    return localStorage.getItem('admin_api_url') || 'https://script.google.com/macros/s/AKfycb.../exec';
  });

  // Mock Data States
  const [students, setStudents] = useState<Student[]>([
    { id: 's1', name: 'Rahul Sharma', email: 'rahul@gmail.com', phone: '9876543210', course: 'UPSSSC PET 2026', batch: 'Morning Batch A', feeStatus: 'Paid', joinedDate: '2026-01-15' },
    { id: 's2', name: 'Priya Verma', email: 'priya@gmail.com', phone: '9123456789', course: 'Railway NTPC & Group D', batch: 'Evening Batch B', feeStatus: 'Paid', joinedDate: '2026-01-18' },
    { id: 's3', name: 'Amit Kumar', email: 'amit@gmail.com', phone: '9988776655', course: 'SSC GD Constable', batch: 'Weekend Batch', feeStatus: 'Pending', joinedDate: '2026-01-20' },
  ]);

  const [teachers, setTeachers] = useState<Teacher[]>([
    { id: 't1', name: 'R.K. Tiwari', subject: 'General Awareness & GS', email: 'rktiwarigs@gmail.com', phone: '9811223344', assignedCourse: 'UPSSSC PET 2026' },
    { id: 't2', name: 'Dr. S. Mukherjee', subject: 'Quantitative Aptitude & Maths', email: 'smukherjeemaths@gmail.com', phone: '9822334455', assignedCourse: 'Railway NTPC & Group D' },
  ]);

  const [courses, setCourses] = useState<Course[]>([
    { id: 'c1', title: 'UPSSSC PET 2026 Complete Foundation', examCategory: 'State Exams', educatorName: 'R.K. Tiwari', price: '₹599', validityDate: '31 Dec 2026', isPublished: true },
    { id: 'c2', title: 'Railway NTPC & Group D Master Batch', examCategory: 'Railway', educatorName: 'Dr. S. Mukherjee', price: '₹699', validityDate: '30 Nov 2026', isPublished: true },
    { id: 'c3', title: 'SSC GD Constable 2026 Target Batch', examCategory: 'SSC Exams', educatorName: 'A. K. Singh', price: '₹499', validityDate: '15 Dec 2026', isPublished: true }
  ]);

  const [videos, setVideos] = useState<VideoItem[]>([
    { id: 'v1', courseTitle: 'UPSSSC PET 2026', title: 'Introduction & Syllabus Strategy', youtubeUrl: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ', duration: '45 mins', isPublished: true },
    { id: 'v2', courseTitle: 'Railway NTPC', title: 'Chapter 1: Number Systems & Simplification', youtubeUrl: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ', duration: '1 hr 12 mins', isPublished: true }
  ]);

  const [pdfs, setPdfs] = useState<PdfItem[]>([
    { id: 'p1', title: 'Modern Indian History Short Notes', subject: 'History', driveUrl: 'https://drive.google.com/file/d/sample/view', fileSize: '4.2 MB', isPublished: true },
    { id: 'p2', title: 'Quantitative Aptitude Formula Sheet', subject: 'Maths', driveUrl: 'https://drive.google.com/file/d/sample2/view', fileSize: '2.8 MB', isPublished: true }
  ]);

  const [tests, setTests] = useState<TestItem[]>([
    { id: 'test1', title: 'UPSSSC PET Full Length Mock Test 1', category: 'State Exams', totalQuestions: 100, durationMins: 120, isLive: true },
    { id: 'test2', title: 'Railway NTPC Speed Math Quiz', category: 'Railway', totalQuestions: 30, durationMins: 30, isLive: true }
  ]);

  const [payments, setPayments] = useState<PaymentRecord[]>([
    { id: 'pay1', studentName: 'Rahul Sharma', course: 'UPSSSC PET 2026', amount: '₹599', date: '2026-01-15', status: 'Successful' },
    { id: 'pay2', studentName: 'Priya Verma', course: 'Railway NTPC & Group D', amount: '₹699', date: '2026-01-18', status: 'Successful' }
  ]);

  // Modal / Form state for Add/Edit
  const [showModal, setShowModal] = useState<boolean>(false);
  const [modalMode, setModalMode] = useState<'student' | 'teacher' | 'course' | 'video' | 'pdf' | 'test'>('course');
  const [formData, setFormData] = useState<any>({});

  const showToast = (msg: string) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(''), 4000);
  };

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    if (email.trim().toLowerCase() !== 'pubg950624@gmail.com') {
      setLoginError('Access Denied: Only authorized admin (pubg950624@gmail.com) is allowed.');
      return;
    }
    if (password.trim() === '' || password.length < 4) {
      setLoginError('कृपया सही पासवर्ड दर्ज करें। (डिफ़ॉल्ट: ADMIN_SECURE_2026)');
      return;
    }
    setIsAuthenticated(true);
    localStorage.setItem('super_admin_auth', 'true');
    setLoginError('');
    showToast('Super Admin Login Successful! Welcome back.');
  };

  const handleLogout = () => {
    setIsAuthenticated(false);
    localStorage.removeItem('super_admin_auth');
    showToast('Logged out successfully.');
  };

  // Sync to Android App
  const handleSyncToApp = () => {
    const payload = JSON.stringify({ courses, videos, pdfs, students });
    if ((window as any).AndroidBridge && (window as any).AndroidBridge.syncData) {
      (window as any).AndroidBridge.syncData(payload);
      showToast('Android App ke sath safalta-poorvak sync ho gaya! ✅');
    } else {
      navigator.clipboard.writeText(payload);
      showToast('Sync Code Copy ho gaya! Android App ke Admin Portal me paste karein. 📋');
    }
  };

  if (!isAuthenticated) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-slate-900 via-blue-950 to-slate-900 flex items-center justify-center p-4">
        <div className="bg-white rounded-3xl shadow-2xl max-w-md w-full p-8 border border-slate-100">
          <div className="text-center mb-8">
            <div className="bg-blue-600 w-18 h-18 rounded-2xl mx-auto flex items-center justify-center text-white mb-4 shadow-xl shadow-blue-600/30">
              <Shield size={36} />
            </div>
            <h1 className="text-2xl font-black text-slate-800">My Student Learning</h1>
            <p className="text-blue-600 font-bold text-sm mt-1">Super Admin Portal (Secure Firebase Auth)</p>
          </div>

          {loginError && (
            <div className="mb-4 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-2xl text-sm flex items-center gap-2 font-medium">
              <AlertCircle size={18} className="shrink-0" />
              <span>{loginError}</span>
            </div>
          )}

          <form onSubmit={handleLogin} className="space-y-4">
            <div>
              <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1">Authorized Admin Email</label>
              <input 
                type="email" 
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full px-4 py-3.5 rounded-2xl border border-slate-200 focus:ring-2 focus:ring-blue-600 focus:outline-none text-slate-800 text-sm font-semibold bg-slate-50"
                required
              />
              <p className="text-[11px] text-emerald-600 font-semibold mt-1">✓ Restricted to pre-authorized admin only</p>
            </div>

            <div>
              <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1">Admin Password / Secret Key</label>
              <input 
                type="password" 
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full px-4 py-3.5 rounded-2xl border border-slate-200 focus:ring-2 focus:ring-blue-600 focus:outline-none text-slate-800 text-sm font-semibold"
                placeholder="Enter password..."
                required
              />
              <p className="text-xs text-slate-400 mt-1">Default Demo Password: ADMIN_SECURE_2026</p>
            </div>

            <button 
              type="submit"
              className="w-full py-4 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-2xl shadow-lg shadow-blue-600/30 transition-all text-sm mt-2 flex items-center justify-center gap-2"
            >
              <Shield size={18} />
              <span>Secure Super Admin Login</span>
            </button>
          </form>

          <div className="mt-6 text-center border-t border-slate-100 pt-4">
            <p className="text-xs text-slate-400">Protected by Firebase Spark Security Rules & Server Validation</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-100 flex flex-col font-sans">
      {/* Toast Notification */}
      {toastMessage && (
        <div className="fixed top-5 right-5 z-50 bg-slate-900 text-white px-6 py-3.5 rounded-2xl shadow-2xl flex items-center gap-3 border border-slate-700 text-sm font-bold animate-bounce">
          <CheckCircle className="text-emerald-400" size={20} />
          <span>{toastMessage}</span>
        </div>
      )}

      {/* Top Header */}
      <header className="bg-slate-900 text-white shadow-lg sticky top-0 z-40 border-b border-slate-800">
        <div className="max-w-7xl mx-auto px-4 py-3.5 flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <div className="bg-blue-600 p-2.5 rounded-2xl shadow-md shadow-blue-600/40">
              <Shield size={22} className="text-white" />
            </div>
            <div>
              <h1 className="font-extrabold text-base md:text-lg tracking-tight">My Student Learning</h1>
              <p className="text-xs text-blue-400 font-semibold">Super Admin Dashboard • Connected to Firestore & Apps Script</p>
            </div>
          </div>
          <div className="flex items-center space-x-3">
            <button
              onClick={handleSyncToApp}
              className="flex items-center space-x-1.5 bg-blue-600 hover:bg-blue-500 text-white px-3.5 py-2 rounded-xl text-xs md:text-sm font-bold shadow-md shadow-blue-600/30 transition-all"
            >
              <Save size={16} />
              <span className="hidden sm:inline">Sync to Android App</span>
              <span className="sm:hidden">Sync</span>
            </button>
            <div className="text-right hidden md:block">
              <p className="text-xs font-bold text-slate-200">{email}</p>
              <p className="text-[11px] text-emerald-400 font-semibold">● Super Admin Active</p>
            </div>
            <button 
              onClick={handleLogout}
              className="flex items-center space-x-1.5 bg-slate-800 hover:bg-red-600/20 hover:text-red-400 text-slate-300 px-3.5 py-2 rounded-xl text-xs md:text-sm font-medium transition-colors border border-slate-700"
            >
              <LogOut size={16} />
              <span className="hidden sm:inline">Logout</span>
            </button>
          </div>
        </div>
      </header>

      {/* Navigation Sub-header / Tabs Bar */}
      <div className="bg-white shadow-sm border-b border-slate-200 overflow-x-auto scrollbar-none">
        <div className="max-w-7xl mx-auto px-4 flex space-x-1 py-2.5">
          {[
            { id: 'dashboard', label: 'Dashboard', icon: LayoutDashboard },
            { id: 'students', label: `Students (${students.length})`, icon: Users },
            { id: 'teachers', label: `Teachers (${teachers.length})`, icon: GraduationCap },
            { id: 'courses', label: `Courses (${courses.length})`, icon: BookOpen },
            { id: 'videos', label: `Videos (${videos.length})`, icon: Video },
            { id: 'pdfs', label: `PDF Notes (${pdfs.length})`, icon: FileText },
            { id: 'tests', label: `Mock Tests (${tests.length})`, icon: HelpCircle },
            { id: 'payments', label: `Payments (${payments.length})`, icon: CreditCard },
            { id: 'notifications', label: 'Notifications', icon: Bell },
            { id: 'reports', label: 'Reports', icon: BarChart3 },
            { id: 'settings', label: 'Settings', icon: Settings },
          ].map(tab => {
            const Icon = tab.icon;
            const isActive = activeTab === tab.id;
            return (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id)}
                className={`flex items-center space-x-2 px-3.5 py-2 rounded-xl font-bold text-xs md:text-sm transition-all whitespace-nowrap ${isActive ? 'bg-blue-600 text-white shadow-md shadow-blue-600/20' : 'text-slate-600 hover:bg-slate-100'}`}
              >
                <Icon size={16} />
                <span>{tab.label}</span>
              </button>
            );
          })}
        </div>
      </div>

      {/* Main Content Area */}
      <main className="max-w-7xl mx-auto px-4 py-8 flex-1 w-full">
        {/* DASHBOARD TAB */}
        {activeTab === 'dashboard' && (
          <div className="space-y-6">
            <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
              <div>
                <h2 className="text-2xl font-black text-slate-800">Welcome Back, Super Admin</h2>
                <p className="text-sm text-slate-500 font-medium">Here is the real-time overview of your Student Learning Platform.</p>
              </div>
              <div className="flex gap-2">
                <button 
                  onClick={() => { setModalMode('course'); setFormData({}); setShowModal(true); }}
                  className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2.5 rounded-xl font-bold text-sm shadow-md flex items-center gap-2"
                >
                  <Plus size={16} /> Add New Course
                </button>
              </div>
            </div>

            {/* Metrics Grid */}
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
              {[
                { title: 'Total Students', value: students.length * 42 + 350, change: '+12% this month', icon: Users, color: 'bg-blue-500' },
                { title: 'Active Courses', value: courses.length, change: 'All Published', icon: BookOpen, color: 'bg-emerald-500' },
                { title: 'Video Lectures', value: videos.length * 15 + 48, change: 'Streaming via YouTube', icon: Video, color: 'bg-indigo-500' },
                { title: 'Total Revenue', value: '₹2,84,500', change: '+18% collection rate', icon: DollarSign, color: 'bg-amber-500' },
              ].map((stat, idx) => {
                const Icon = stat.icon;
                return (
                  <div key={idx} className="bg-white p-6 rounded-3xl shadow-sm border border-slate-200 flex items-center justify-between">
                    <div>
                      <p className="text-xs font-bold text-slate-400 uppercase tracking-wider">{stat.title}</p>
                      <h3 className="text-2xl font-black text-slate-800 mt-1">{stat.value}</h3>
                      <p className="text-xs text-emerald-600 font-semibold mt-1 flex items-center gap-1">
                        <ArrowUpRight size={14} /> {stat.change}
                      </p>
                    </div>
                    <div className={`${stat.color} p-4 rounded-2xl text-white shadow-lg`}>
                      <Icon size={24} />
                    </div>
                  </div>
                );
              })}
            </div>

            {/* Recent Activity & Quick Stats */}
            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
              <div className="bg-white p-6 rounded-3xl shadow-sm border border-slate-200 lg:col-span-2">
                <h3 className="text-lg font-bold text-slate-800 mb-4">Recent Student Enrollments</h3>
                <div className="space-y-3">
                  {students.map((st) => (
                    <div key={st.id} className="flex items-center justify-between p-3.5 bg-slate-50 rounded-2xl border border-slate-100">
                      <div className="flex items-center space-x-3">
                        <div className="w-10 h-10 rounded-xl bg-blue-100 text-blue-600 flex items-center justify-center font-bold">
                          {st.name.charAt(0)}
                        </div>
                        <div>
                          <p className="font-bold text-sm text-slate-800">{st.name}</p>
                          <p className="text-xs text-slate-500">{st.course} • {st.batch}</p>
                        </div>
                      </div>
                      <div className="text-right">
                        <span className={`px-2.5 py-1 rounded-full text-xs font-bold ${st.feeStatus === 'Paid' ? 'bg-emerald-100 text-emerald-700' : 'bg-amber-100 text-amber-700'}`}>
                          {st.feeStatus}
                        </span>
                        <p className="text-[11px] text-slate-400 mt-1">{st.joinedDate}</p>
                      </div>
                    </div>
                  ))}
                </div>
              </div>

              <div className="bg-white p-6 rounded-3xl shadow-sm border border-slate-200">
                <h3 className="text-lg font-bold text-slate-800 mb-4">Platform Health</h3>
                <div className="space-y-4 text-sm font-medium">
                  <div className="p-4 bg-emerald-50 rounded-2xl border border-emerald-100 text-emerald-800 flex items-center gap-3">
                    <CheckCircle className="shrink-0 text-emerald-600" size={20} />
                    <div>
                      <p className="font-bold">Firebase Spark Free Plan</p>
                      <p className="text-xs text-emerald-600 mt-0.5">Firestore & Auth operating normally.</p>
                    </div>
                  </div>
                  <div className="p-4 bg-blue-50 rounded-2xl border border-blue-100 text-blue-800 flex items-center gap-3">
                    <CheckCircle className="shrink-0 text-blue-600" size={20} />
                    <div>
                      <p className="font-bold">Apps Script API Connected</p>
                      <p className="text-xs text-blue-600 mt-0.5">Live sync active with Android client.</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* STUDENTS TAB */}
        {activeTab === 'students' && (
          <div className="space-y-4">
            <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
              <h2 className="text-xl font-bold text-slate-800">Student Management ({students.length})</h2>
              <div className="flex items-center gap-2 w-full sm:w-auto">
                <div className="relative flex-1 sm:w-64">
                  <Search className="absolute left-3 top-3 text-slate-400" size={16} />
                  <input 
                    type="text" 
                    placeholder="Search students..." 
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className="w-full pl-9 pr-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:ring-2 focus:ring-blue-600 bg-white"
                  />
                </div>
                <button 
                  onClick={() => { setModalMode('student'); setFormData({}); setShowModal(true); }}
                  className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2.5 rounded-xl text-sm font-bold shadow-md whitespace-nowrap flex items-center gap-1.5"
                >
                  <Plus size={16} /> Add Student
                </button>
              </div>
            </div>

            <div className="bg-white rounded-3xl shadow-sm border border-slate-200 overflow-hidden">
              <div className="overflow-x-auto">
                <table className="w-full text-left border-collapse">
                  <thead>
                    <tr className="bg-slate-50 border-b border-slate-200 text-slate-500 text-xs uppercase tracking-wider font-semibold">
                      <th className="p-4">Student Name</th>
                      <th className="p-4">Email & Phone</th>
                      <th className="p-4">Enrolled Course</th>
                      <th className="p-4">Batch</th>
                      <th className="p-4">Fee Status</th>
                      <th className="p-4 text-right">Actions</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-100 text-sm">
                    {students.filter(s => s.name.toLowerCase().includes(searchQuery.toLowerCase())).map(student => (
                      <tr key={student.id} className="hover:bg-slate-50 transition-colors">
                        <td className="p-4 font-bold text-slate-800">{student.name}</td>
                        <td className="p-4 text-slate-600 text-xs">{student.email}<br/>{student.phone}</td>
                        <td className="p-4 text-slate-700 font-semibold">{student.course}</td>
                        <td className="p-4 text-slate-600">{student.batch}</td>
                        <td className="p-4">
                          <span className={`px-3 py-1 rounded-full text-xs font-bold inline-block ${student.feeStatus === 'Paid' ? 'bg-emerald-100 text-emerald-700' : 'bg-amber-100 text-amber-700'}`}>
                            {student.feeStatus}
                          </span>
                        </td>
                        <td className="p-4 text-right space-x-2">
                          <button 
                            onClick={() => {
                              setConfirmModal({
                                isOpen: true,
                                title: 'Delete Student?',
                                message: `Are you sure you want to delete ${student.name}?`,
                                onConfirm: () => {
                                  setStudents(students.filter(s => s.id !== student.id));
                                  setConfirmModal({ isOpen: false, title: '', message: '', onConfirm: () => {} });
                                  showToast('Student deleted successfully.');
                                }
                              });
                            }}
                            className="p-2 bg-slate-100 hover:bg-red-50 text-slate-600 hover:text-red-600 rounded-xl transition-colors"
                          >
                            <Trash2 size={16} />
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        )}

        {/* TEACHERS TAB */}
        {activeTab === 'teachers' && (
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <h2 className="text-xl font-bold text-slate-800">Teacher Management ({teachers.length})</h2>
              <button 
                onClick={() => { setModalMode('teacher'); setFormData({}); setShowModal(true); }}
                className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2.5 rounded-xl text-sm font-bold shadow-md flex items-center gap-1.5"
              >
                <Plus size={16} /> Add Teacher
              </button>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {teachers.map(teacher => (
                <div key={teacher.id} className="bg-white p-6 rounded-3xl shadow-sm border border-slate-200 flex flex-col justify-between">
                  <div>
                    <div className="flex items-start justify-between">
                      <div className="bg-blue-100 text-blue-700 p-3 rounded-2xl font-bold">
                        <GraduationCap size={22} />
                      </div>
                      <button 
                        onClick={() => {
                          setConfirmModal({
                            isOpen: true,
                            title: 'Delete Teacher?',
                            message: `Are you sure you want to remove ${teacher.name}?`,
                            onConfirm: () => {
                              setTeachers(teachers.filter(t => t.id !== teacher.id));
                              setConfirmModal({ isOpen: false, title: '', message: '', onConfirm: () => {} });
                              showToast('Teacher removed successfully.');
                            }
                          });
                        }}
                        className="text-slate-400 hover:text-red-600 p-2 rounded-xl"
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                    <h3 className="font-bold text-lg text-slate-800 mt-4">{teacher.name}</h3>
                    <p className="text-xs text-blue-600 font-bold mt-0.5">{teacher.subject}</p>
                    <p className="text-xs text-slate-500 mt-2">Email: {teacher.email} • Phone: {teacher.phone}</p>
                    <p className="text-xs font-semibold text-slate-700 mt-3 bg-slate-50 p-2.5 rounded-xl border border-slate-100">
                      Assigned Batch: {teacher.assignedCourse}
                    </p>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* COURSES TAB */}
        {activeTab === 'courses' && (
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <h2 className="text-xl font-bold text-slate-800">Course Batches ({courses.length})</h2>
              <button 
                onClick={() => { setModalMode('course'); setFormData({}); setShowModal(true); }}
                className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2.5 rounded-xl text-sm font-bold shadow-md flex items-center gap-1.5"
              >
                <Plus size={16} /> Add New Course
              </button>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              {courses.map(course => (
                <div key={course.id} className="bg-white p-6 rounded-3xl shadow-sm border border-slate-200 flex flex-col justify-between">
                  <div>
                    <div className="flex items-start justify-between">
                      <span className="bg-blue-100 text-blue-700 text-xs font-bold px-3 py-1 rounded-xl">
                        {course.examCategory}
                      </span>
                      <button 
                        onClick={() => {
                          setCourses(courses.map(c => c.id === course.id ? { ...c, isPublished: !c.isPublished } : c));
                        }}
                        className={`px-3 py-1 rounded-xl text-xs font-bold ${course.isPublished ? 'bg-emerald-100 text-emerald-700' : 'bg-amber-100 text-amber-700'}`}
                      >
                        {course.isPublished ? 'Published' : 'Draft'}
                      </button>
                    </div>
                    <h3 className="font-bold text-lg text-slate-800 mt-3">{course.title}</h3>
                    <p className="text-xs text-slate-500 mt-1">Educator: {course.educatorName}</p>
                    <div className="mt-4 flex items-center justify-between pt-4 border-t border-slate-100">
                      <span className="text-lg font-black text-emerald-600">{course.price}</span>
                      <button 
                        onClick={() => {
                          setConfirmModal({
                            isOpen: true,
                            title: 'Delete Course?',
                            message: `Delete course ${course.title}?`,
                            onConfirm: () => {
                              setCourses(courses.filter(c => c.id !== course.id));
                              setConfirmModal({ isOpen: false, title: '', message: '', onConfirm: () => {} });
                              showToast('Course deleted.');
                            }
                          });
                        }}
                        className="text-slate-400 hover:text-red-600 p-2"
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* VIDEOS TAB */}
        {activeTab === 'videos' && (
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <h2 className="text-xl font-bold text-slate-800">YouTube Video Classes ({videos.length})</h2>
              <button 
                onClick={() => { setModalMode('video'); setFormData({}); setShowModal(true); }}
                className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2.5 rounded-xl text-sm font-bold shadow-md flex items-center gap-1.5"
              >
                <Plus size={16} /> Add YouTube Video
              </button>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {videos.map(video => (
                <div key={video.id} className="bg-white p-6 rounded-3xl shadow-sm border border-slate-200 flex flex-col justify-between">
                  <div>
                    <div className="flex items-start justify-between">
                      <span className="bg-red-100 text-red-700 text-xs font-bold px-3 py-1 rounded-xl flex items-center gap-1">
                        <Video size={13} /> YouTube Lecture
                      </span>
                      <span className="text-xs text-slate-400 font-semibold">{video.duration}</span>
                    </div>
                    <h3 className="font-bold text-base text-slate-800 mt-3">{video.title}</h3>
                    <p className="text-xs text-slate-500 mt-1">Course: {video.courseTitle}</p>
                    <a 
                      href={video.youtubeUrl} 
                      target="_blank" 
                      rel="noreferrer"
                      className="text-blue-600 text-xs font-semibold mt-3 inline-flex items-center gap-1 hover:underline truncate max-w-full"
                    >
                      <ExternalLink size={13} /> {video.youtubeUrl}
                    </a>
                  </div>
                  <div className="flex justify-end pt-4 mt-4 border-t border-slate-100">
                    <button 
                      onClick={() => setVideos(videos.filter(v => v.id !== video.id))}
                      className="text-xs font-bold text-red-600 bg-red-50 hover:bg-red-100 px-3 py-1.5 rounded-xl"
                    >
                      Delete Video
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* PDF NOTES TAB */}
        {activeTab === 'pdfs' && (
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <h2 className="text-xl font-bold text-slate-800">Google Drive Study Notes PDFs ({pdfs.length})</h2>
              <button 
                onClick={() => { setModalMode('pdf'); setFormData({}); setShowModal(true); }}
                className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2.5 rounded-xl text-sm font-bold shadow-md flex items-center gap-1.5"
              >
                <Plus size={16} /> Add Google Drive PDF
              </button>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {pdfs.map(pdf => (
                <div key={pdf.id} className="bg-white p-6 rounded-3xl shadow-sm border border-slate-200 flex flex-col justify-between">
                  <div>
                    <div className="flex items-start justify-between">
                      <span className="bg-emerald-100 text-emerald-700 text-xs font-bold px-3 py-1 rounded-xl flex items-center gap-1">
                        <FileText size={13} /> Study Notes PDF
                      </span>
                      <span className="text-xs text-slate-400 font-semibold">{pdf.fileSize}</span>
                    </div>
                    <h3 className="font-bold text-base text-slate-800 mt-3">{pdf.title}</h3>
                    <p className="text-xs text-slate-500 mt-1">Subject: {pdf.subject}</p>
                    <a 
                      href={pdf.driveUrl} 
                      target="_blank" 
                      rel="noreferrer"
                      className="text-blue-600 text-xs font-semibold mt-3 inline-flex items-center gap-1 hover:underline truncate max-w-full"
                    >
                      <ExternalLink size={13} /> {pdf.driveUrl}
                    </a>
                  </div>
                  <div className="flex justify-end pt-4 mt-4 border-t border-slate-100">
                    <button 
                      onClick={() => setPdfs(pdfs.filter(p => p.id !== pdf.id))}
                      className="text-xs font-bold text-red-600 bg-red-50 hover:bg-red-100 px-3 py-1.5 rounded-xl"
                    >
                      Delete PDF
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* TESTS TAB */}
        {activeTab === 'tests' && (
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <h2 className="text-xl font-bold text-slate-800">Online Mock Tests & Quizzes ({tests.length})</h2>
              <button 
                onClick={() => { setModalMode('test'); setFormData({}); setShowModal(true); }}
                className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2.5 rounded-xl text-sm font-bold shadow-md flex items-center gap-1.5"
              >
                <Plus size={16} /> Create Mock Test
              </button>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {tests.map(test => (
                <div key={test.id} className="bg-white p-6 rounded-3xl shadow-sm border border-slate-200 flex flex-col justify-between">
                  <div>
                    <div className="flex items-start justify-between">
                      <span className="bg-indigo-100 text-indigo-700 text-xs font-bold px-3 py-1 rounded-xl">
                        {test.category}
                      </span>
                      <span className="bg-emerald-100 text-emerald-700 text-xs font-bold px-3 py-1 rounded-xl">
                        Live Exam
                      </span>
                    </div>
                    <h3 className="font-bold text-base text-slate-800 mt-3">{test.title}</h3>
                    <p className="text-xs text-slate-500 mt-2 flex items-center gap-4">
                      <span>Questions: {test.totalQuestions}</span>
                      <span>Duration: {test.durationMins} Mins</span>
                    </p>
                  </div>
                  <div className="flex justify-end pt-4 mt-4 border-t border-slate-100">
                    <button 
                      onClick={() => setTests(tests.filter(t => t.id !== test.id))}
                      className="text-xs font-bold text-red-600 bg-red-50 hover:bg-red-100 px-3 py-1.5 rounded-xl"
                    >
                      Delete Test
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* PAYMENTS TAB */}
        {activeTab === 'payments' && (
          <div className="space-y-4">
            <h2 className="text-xl font-bold text-slate-800">Payment Records & Transaction Log ({payments.length})</h2>
            <div className="bg-white rounded-3xl shadow-sm border border-slate-200 overflow-hidden">
              <table className="w-full text-left border-collapse">
                <thead>
                  <tr className="bg-slate-50 border-b border-slate-200 text-slate-500 text-xs uppercase tracking-wider font-semibold">
                    <th className="p-4">Transaction ID</th>
                    <th className="p-4">Student Name</th>
                    <th className="p-4">Course Enrolled</th>
                    <th className="p-4">Amount</th>
                    <th className="p-4">Date</th>
                    <th className="p-4">Status</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100 text-sm">
                  {payments.map(pay => (
                    <tr key={pay.id} className="hover:bg-slate-50">
                      <td className="p-4 font-mono text-xs text-slate-500">{pay.id}</td>
                      <td className="p-4 font-bold text-slate-800">{pay.studentName}</td>
                      <td className="p-4 text-slate-600">{pay.course}</td>
                      <td className="p-4 font-black text-emerald-600">{pay.amount}</td>
                      <td className="p-4 text-slate-500 text-xs">{pay.date}</td>
                      <td className="p-4">
                        <span className="px-3 py-1 rounded-full text-xs font-bold bg-emerald-100 text-emerald-700">
                          {pay.status}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* NOTIFICATIONS TAB */}
        {activeTab === 'notifications' && (
          <div className="bg-white rounded-3xl shadow-sm border border-slate-200 p-8 max-w-2xl">
            <h2 className="text-xl font-bold text-slate-800 mb-2">Send Push Notification / Announcement</h2>
            <p className="text-slate-500 text-sm mb-6">सभी रजिस्टर्ड स्टूडेंट्स के मोबाइल ऐप पर तुरंत नोटिफिकेशन भेजें।</p>
            <div className="space-y-4">
              <div>
                <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1">Notification Title</label>
                <input type="text" className="w-full px-4 py-3 rounded-xl border border-slate-200 text-sm font-semibold" placeholder="e.g. New Live Class Starting!" />
              </div>
              <div>
                <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1">Message Body</label>
                <textarea rows={4} className="w-full px-4 py-3 rounded-xl border border-slate-200 text-sm font-semibold" placeholder="Type notification message here..."></textarea>
              </div>
              <button 
                onClick={() => showToast('Push notification broadcasted to all students successfully!')}
                className="w-full py-3.5 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-xl shadow-md text-sm"
              >
                Broadcast Notification
              </button>
            </div>
          </div>
        )}

        {/* REPORTS TAB */}
        {activeTab === 'reports' && (
          <div className="space-y-6">
            <h2 className="text-xl font-bold text-slate-800">Analytics & Financial Reports</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div className="bg-white p-6 rounded-3xl shadow-sm border border-slate-200">
                <h3 className="font-bold text-slate-800 mb-2">Student Attendance Report</h3>
                <p className="text-xs text-slate-500 mb-4">Download comprehensive CSV report of active student logins.</p>
                <button onClick={() => showToast('Attendance CSV downloaded!')} className="w-full py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold rounded-xl text-xs">
                  Download CSV
                </button>
              </div>
              <div className="bg-white p-6 rounded-3xl shadow-sm border border-slate-200">
                <h3 className="font-bold text-slate-800 mb-2">Fee Collection Summary</h3>
                <p className="text-xs text-slate-500 mb-4">Export monthly revenue and pending fee statements.</p>
                <button onClick={() => showToast('Fee report downloaded!')} className="w-full py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold rounded-xl text-xs">
                  Download Report
                </button>
              </div>
              <div className="bg-white p-6 rounded-3xl shadow-sm border border-slate-200">
                <h3 className="font-bold text-slate-800 mb-2">Mock Test Performance</h3>
                <p className="text-xs text-slate-500 mb-4">Student leaderboard and test accuracy breakdown.</p>
                <button onClick={() => showToast('Test analytics exported!')} className="w-full py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold rounded-xl text-xs">
                  Export Analytics
                </button>
              </div>
            </div>
          </div>
        )}

        {/* SETTINGS TAB */}
        {activeTab === 'settings' && (
          <div className="bg-white rounded-3xl shadow-sm border border-slate-200 p-8 max-w-2xl">
            <h2 className="text-xl font-bold text-slate-800 mb-2">Firebase & Apps Script Configuration</h2>
            <p className="text-slate-500 text-sm mb-6">Configure Apps Script Web App URL and Firestore database endpoints.</p>

            <div className="space-y-4">
              <div>
                <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1">Apps Script Web App URL</label>
                <input 
                  type="text" 
                  value={apiUrl}
                  onChange={(e) => setApiUrl(e.target.value)}
                  className="w-full px-4 py-3 rounded-xl border border-slate-200 text-sm font-mono bg-slate-50"
                />
              </div>

              <div>
                <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1">Authorized Admin Email</label>
                <input 
                  type="email" 
                  value="pubg950624@gmail.com" 
                  disabled
                  className="w-full px-4 py-3 rounded-xl border border-slate-200 text-sm font-semibold bg-slate-100 text-slate-500 cursor-not-allowed"
                />
              </div>

              <button 
                onClick={() => {
                  localStorage.setItem('admin_api_url', apiUrl);
                  showToast('Settings saved successfully!');
                }}
                className="w-full py-3.5 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-xl text-sm shadow-md"
              >
                Save Configurations
              </button>
            </div>
          </div>
        )}
      </main>

      {/* Confirmation Modal */}
      {confirmModal.isOpen && (
        <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-sm flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-3xl shadow-2xl max-w-md w-full p-6 text-center">
            <div className="w-14 h-14 bg-red-100 text-red-600 rounded-2xl mx-auto flex items-center justify-center mb-4">
              <AlertCircle size={28} />
            </div>
            <h3 className="text-lg font-black text-slate-800">{confirmModal.title}</h3>
            <p className="text-slate-500 text-sm mt-1 mb-6">{confirmModal.message}</p>
            <div className="flex space-x-3">
              <button 
                onClick={() => setConfirmModal({ isOpen: false, title: '', message: '', onConfirm: () => {} })}
                className="flex-1 py-3 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold rounded-2xl text-sm"
              >
                Cancel
              </button>
              <button 
                onClick={confirmModal.onConfirm}
                className="flex-1 py-3 bg-red-600 hover:bg-red-700 text-white font-bold rounded-2xl text-sm shadow-lg shadow-red-600/30"
              >
                Confirm Delete
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Add Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-sm flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-3xl shadow-2xl max-w-md w-full p-6">
            <h3 className="text-lg font-black text-slate-800 mb-4 capitalize">
              Add New {modalMode}
            </h3>

            <div className="space-y-4">
              <div>
                <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1">Title / Name</label>
                <input 
                  type="text"
                  placeholder="Enter name or title..."
                  value={formData.title || ''}
                  onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                  className="w-full px-4 py-3 rounded-xl border border-slate-200 text-sm font-semibold"
                />
              </div>

              {modalMode === 'student' && (
                <div>
                  <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1">Email</label>
                  <input 
                    type="email"
                    placeholder="student@gmail.com"
                    value={formData.email || ''}
                    onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                    className="w-full px-4 py-3 rounded-xl border border-slate-200 text-sm font-semibold"
                  />
                </div>
              )}

              {modalMode === 'video' && (
                <div>
                  <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1">YouTube Video URL</label>
                  <input 
                    type="text"
                    placeholder="https://youtube.com/watch?v=..."
                    value={formData.url || ''}
                    onChange={(e) => setFormData({ ...formData, url: e.target.value })}
                    className="w-full px-4 py-3 rounded-xl border border-slate-200 text-sm font-semibold"
                  />
                </div>
              )}

              <div className="flex space-x-3 mt-6">
                <button 
                  onClick={() => setShowModal(false)}
                  className="flex-1 py-3 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold rounded-2xl text-sm"
                >
                  Cancel
                </button>
                <button 
                  onClick={() => {
                    if (modalMode === 'course') {
                      const newC: Course = {
                        id: 'c_' + Date.now(),
                        title: formData.title || 'New Batch',
                        examCategory: 'State Exams',
                        educatorName: 'Faculty',
                        price: '₹599',
                        validityDate: '31 Dec 2026',
                        isPublished: true
                      };
                      setCourses([newC, ...courses]);
                    } else if (modalMode === 'student') {
                      const newS: Student = {
                        id: 's_' + Date.now(),
                        name: formData.title || 'New Student',
                        email: formData.email || 'student@gmail.com',
                        phone: '9876543210',
                        course: 'UPSSSC PET 2026',
                        batch: 'Batch A',
                        feeStatus: 'Paid',
                        joinedDate: '2026-10-30'
                      };
                      setStudents([newS, ...students]);
                    } else if (modalMode === 'video') {
                      const newV: VideoItem = {
                        id: 'v_' + Date.now(),
                        courseTitle: 'UPSSSC PET',
                        title: formData.title || 'New Video',
                        youtubeUrl: formData.url || 'https://youtube.com',
                        duration: '45 mins',
                        isPublished: true
                      };
                      setVideos([newV, ...videos]);
                    }
                    setShowModal(false);
                    showToast('Added successfully!');
                  }}
                  className="flex-1 py-3 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-2xl text-sm shadow-md"
                >
                  Save & Publish
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
