import { initializeApp } from 'firebase/app';
import { getAuth, GoogleAuthProvider } from 'firebase/auth';
import { getFirestore } from 'firebase/firestore';

const firebaseConfig = {
  apiKey: "AIzaSyBgBo4cbKEHT3go2QW0OxuCqqXNeHkHKUM",
  authDomain: "gen-lang-client-0244732333.firebaseapp.com",
  projectId: "gen-lang-client-0244732333",
  storageBucket: "gen-lang-client-0244732333.firebasestorage.app",
  messagingSenderId: "439015824400",
  appId: "1:439015824400:web:1b30962e945ba78e43fe95"
};

const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
export const db = getFirestore(app);
export const googleProvider = new GoogleAuthProvider();
