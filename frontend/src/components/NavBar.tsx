import { Link, useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import { useCurrentUser } from "../hooks/useCurrentUser";
import "./NavBar.css";

export default function NavBar() {
  const { logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const user = useCurrentUser();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const isActiveLink = (path: string) => {
    return location.pathname === path;
  };

  return (
    <nav className="navbar">
      <div className="navbar-content">
        <div className="navbar-brand">
          <div className="navbar-icon">🗡️</div>
          Captain Mercer
        </div>
        
        <ul className="navbar-nav">
          <li>
            <Link
              to="/"
              className={`navbar-link ${isActiveLink('/') ? 'active' : ''}`}
            >
              Home
            </Link>
          </li>
          <li>
            <Link
              to="/profile"
              className={`navbar-link ${isActiveLink('/profile') ? 'active' : ''}`}
            >
              Profil
            </Link>
          </li>
        </ul>

        <div className="navbar-user">
          {user && (
            <div className="navbar-user-info">
              <span className="navbar-username">{user.firstName} {user.lastName}</span>
              <span className="navbar-status">@{user.username}</span>
            </div>
          )}
          <button onClick={handleLogout} className="navbar-logout">
            Logout
          </button>
        </div>
      </div>
    </nav>
  );
}
