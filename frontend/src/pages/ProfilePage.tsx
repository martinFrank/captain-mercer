import { useCurrentUser } from "../hooks/useCurrentUser";
import { LoadingSpinner } from "../components/common/LoadingSpinner";
import { InfoItem } from "../components/common/InfoItem";
import "./ProfilePage.css";

export default function ProfilePage() {
  const user = useCurrentUser();

  if (!user) {
    return (
      <div className="page-container">
        <div className="page-content">
          <LoadingSpinner message="Lade Benutzerdaten..." />
        </div>
      </div>
    );
  }

  return (
    <div className="page-container">
      <div className="page-content">
        <div className="profile-header">
          <div className="adventure-icon profile-icon">👤</div>
          <h1 className="title">Mein Profil</h1>
          <p className="subtitle">Verwalte deine Kontoinformationen und Einstellungen</p>
        </div>

        <div className="grid grid-cols-2 gap-lg">
          <div className="card">
            <h2 className="card-header">👤 Persönliche Informationen</h2>
            <div className="info-list">
              <InfoItem label="Benutzername" value={user.username} bold />
              <InfoItem label="Name" value={`${user.firstName} ${user.lastName}`} />
              <InfoItem label="E-Mail" value={user.email} />
            </div>
          </div>

          <div className="card">
            <h2 className="card-header">🛡️ Konto & Berechtigungen</h2>
            <div className="info-list">
              <InfoItem
                label="Rollen"
                value={
                  <div className="roles-container">
                    {user.roles?.map((role, index) => (
                      <span key={index} className="status active">
                        {role}
                      </span>
                    )) || (
                      <span className="roles-empty">Keine Rollen zugewiesen</span>
                    )}
                  </div>
                }
              />

              <div className="status-badge">
                <div className="status-icon">✨</div>
                <h3 className="status-title">Abenteurer Status</h3>
                <p className="status-text">Aktiver Spieler</p>
              </div>
            </div>
          </div>
        </div>

        <div className="card quick-actions-card">
          <h2 className="card-header">🎮 Schnellaktionen</h2>
          <div className="quick-actions-container">
            <button className="btn btn-primary">🎯 Adventure starten</button>
            <button className="btn btn-secondary">📊 Statistiken</button>
            <button className="btn btn-secondary">⚙️ Einstellungen</button>
          </div>
        </div>
      </div>
    </div>
  );
}
