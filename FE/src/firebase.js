// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getDatabase } from "firebase/database";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyCy2BDZO1nKsU43fl8LqdAgix92z_G-26E",
  authDomain: "old2new-e8ef2.firebaseapp.com",
  databaseURL: "https://old2new-e8ef2-default-rtdb.asia-southeast1.firebasedatabase.app",
  projectId: "old2new-e8ef2",
  storageBucket: "old2new-e8ef2.firebasestorage.app",
  messagingSenderId: "761431606910",
  appId: "1:761431606910:web:379c565c0e096d16a2fa2f",
  measurementId: "G-VW201BGYC5"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);

export const db = getDatabase(app);