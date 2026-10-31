export interface Course {
  id?: string;
  title: string;
  examCategory: string;
  educatorName: string;
  price: string;
  originalPrice: string;
  rating: number;
  learnersCount: string;
  isLive: boolean;
  isPublished: boolean;
  createdAt?: any;
}

export interface VideoItem {
  id?: string;
  courseId: string;
  title: string;
  description: string;
  videoType: 'youtube';
  videoUrl: string;
  pdfUrl?: string;
  thumbnailUrl: string;
  isPublished: boolean;
  createdAt?: any;
}

export interface BannerItem {
  id?: string;
  title: string;
  hindiHeading: string;
  price: string;
  imageUrl: string;
  isPublished: boolean;
}

export interface QuizQuestion {
  id?: string;
  subject: string;
  question: string;
  options: string[];
  correctOptionIndex: number;
  explanation: string;
}
