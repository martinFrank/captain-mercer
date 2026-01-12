import { Link } from "react-router-dom";
import { useCurrentUser } from "../hooks/useCurrentUser";

export default function HomePage() {
  const user = useCurrentUser();

  return (
    <div className="page-container">
      <div className="page-content">
        <div className="text-center" style={{ marginBottom: "var(--spacing-2xl)" }}>
          <div className="adventure-icon" style={{ margin: "0 auto var(--spacing-xl)" }}>
            🗡️
          </div>
          <h1 className="title" style={{ fontSize: "var(--font-size-4xl)" }}>
            Willkommen bei Adventure Game
          </h1>
          <p className="subtitle">
            Begib dich auf epische Abenteuer und entdecke fantastische Welten voller Geheimnisse und Herausforderungen.
          </p>
        </div>

        <div className="grid grid-cols-2 gap-lg" style={{ marginBottom: "var(--spacing-2xl)" }}>
          <div className="card">
            <h3 style={{
              color: "var(--primary-color)",
              fontSize: "var(--font-size-xl)",
              marginBottom: "var(--spacing-md)"
            }}>
              🎮 Spiel starten
            </h3>
            <p style={{
              color: "var(--text-secondary)",
              marginBottom: "var(--spacing-lg)"
            }}>
              Tauche ein in dein persönliches Abenteuer und erlebe spannende Quests und Herausforderungen.
            </p>
            <Link to="/adventure" className="btn btn-primary">
              Adventure beginnen
            </Link>
          </div>

          <div className="card">
            <h3 style={{
              color: "var(--primary-color)",
              fontSize: "var(--font-size-xl)",
              marginBottom: "var(--spacing-md)"
            }}>
              👤 Profil verwalten
            </h3>
            <p style={{
              color: "var(--text-secondary)",
              marginBottom: "var(--spacing-lg)"
            }}>
              Verwalte deine Charakterinformationen und verfolge deinen Fortschritt im Spiel.
            </p>
            <Link to="/profile" className="btn btn-secondary">
              Profil ansehen
            </Link>
          </div>
        </div>

        {user && (
          <div className="card text-center">
            <h2 style={{
              color: "var(--text-primary)",
              marginBottom: "var(--spacing-md)"
            }}>
              Hallo, {user.firstName}! 👋
            </h2>
            <p style={{ color: "var(--text-secondary)" }}>
              Bereit für dein nächstes Abenteuer? Deine Reise wartet auf dich!
            </p>
            <div style={{
              marginTop: "var(--spacing-lg)",
              display: "flex",
              gap: "var(--spacing-md)",
              justifyContent: "center"
            }}>
              <Link to="/adventure" className="btn btn-primary">
                🎯 Adventure fortsetzen
              </Link>
              <Link to="/profile" className="btn btn-secondary">
                📊 Statistiken ansehen
              </Link>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
