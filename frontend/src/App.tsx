import { useAuth } from "./auth/AuthContext";
import LoginPage from "./auth/LoginPage";
import HomePage from "./pages/HomePage.tsx";
import AdventurePage from "./pages/AdventurePage.tsx";
import ProfilePage from "./pages/ProfilePage.tsx";
import NavBar from "./components/NavBar";
import { Navigate, Routes, Route } from "react-router-dom"; 

export default function App() {
  const { token } = useAuth();
 

  return (
    <>
      {token && <NavBar />}
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/" element={token ? <HomePage /> : <Navigate to="/login" />} />
        <Route path="/adventure" element={token ? <AdventurePage /> : <Navigate to="/login" />} />
        <Route path="/profile" element={token ? <ProfilePage /> : <Navigate to="/login" />} />
      </Routes>
    </>
  );
}


