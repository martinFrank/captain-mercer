import { useCurrentUser } from "../hooks/useCurrentUser";

export default function ProfilePage() {
  const user = useCurrentUser();

  if (!user) {
    return (
      <div className="page-container">
        <div className="page-content">
          <div className="loading">
            <div className="spinner"></div>
            Lade Benutzerdaten...
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="page-container">
      <div className="page-content">
        <div className="text-center" style={{ marginBottom: "var(--spacing-2xl)" }}>
          <div className="adventure-icon" style={{ margin: "0 auto var(--spacing-lg)" }}>
            👤
          </div>
          <h1 className="title">Mein Profil</h1>
          <p className="subtitle">Verwalte deine Kontoinformationen und Einstellungen</p>
        </div>

        <div className="grid grid-cols-2 gap-lg">
          <div className="card">
            <h2 style={{
              color: "var(--primary-color)",
              fontSize: "var(--font-size-xl)",
              marginBottom: "var(--spacing-lg)"
            }}>
              👤 Persönliche Informationen
            </h2>
            
            <div style={{ display: "flex", flexDirection: "column", gap: "var(--spacing-md)" }}>
              <div style={{
                padding: "var(--spacing-md)",
                background: "rgba(255, 255, 255, 0.05)",
                borderRadius: "var(--radius-md)",
                border: "1px solid var(--border-color)"
              }}>
                <label style={{
                  color: "var(--text-secondary)",
                  fontSize: "var(--font-size-sm)",
                  display: "block",
                  marginBottom: "var(--spacing-xs)"
                }}>
                  Benutzername
                </label>
                <span style={{
                  color: "var(--text-primary)",
                  fontSize: "var(--font-size-lg)",
                  fontWeight: "600"
                }}>
                  {user.username}
                </span>
              </div>

              <div style={{
                padding: "var(--spacing-md)",
                background: "rgba(255, 255, 255, 0.05)",
                borderRadius: "var(--radius-md)",
                border: "1px solid var(--border-color)"
              }}>
                <label style={{
                  color: "var(--text-secondary)",
                  fontSize: "var(--font-size-sm)",
                  display: "block",
                  marginBottom: "var(--spacing-xs)"
                }}>
                  Name
                </label>
                <span style={{
                  color: "var(--text-primary)",
                  fontSize: "var(--font-size-lg)"
                }}>
                  {user.firstName} {user.lastName}
                </span>
              </div>

              <div style={{
                padding: "var(--spacing-md)",
                background: "rgba(255, 255, 255, 0.05)",
                borderRadius: "var(--radius-md)",
                border: "1px solid var(--border-color)"
              }}>
                <label style={{
                  color: "var(--text-secondary)",
                  fontSize: "var(--font-size-sm)",
                  display: "block",
                  marginBottom: "var(--spacing-xs)"
                }}>
                  E-Mail
                </label>
                <span style={{
                  color: "var(--text-primary)",
                  fontSize: "var(--font-size-lg)"
                }}>
                  {user.email}
                </span>
              </div>
            </div>
          </div>

          <div className="card">
            <h2 style={{
              color: "var(--primary-color)",
              fontSize: "var(--font-size-xl)",
              marginBottom: "var(--spacing-lg)"
            }}>
              🛡️ Konto & Berechtigungen
            </h2>
            
            <div style={{ display: "flex", flexDirection: "column", gap: "var(--spacing-md)" }}>
              <div style={{
                padding: "var(--spacing-md)",
                background: "rgba(255, 255, 255, 0.05)",
                borderRadius: "var(--radius-md)",
                border: "1px solid var(--border-color)"
              }}>
                <label style={{
                  color: "var(--text-secondary)",
                  fontSize: "var(--font-size-sm)",
                  display: "block",
                  marginBottom: "var(--spacing-xs)"
                }}>
                  Rollen
                </label>
                <div style={{ display: "flex", gap: "var(--spacing-sm)", flexWrap: "wrap" }}>
                  {user.roles?.map((role, index) => (
                    <span
                      key={index}
                      className="status active"
                    >
                      {role}
                    </span>
                  )) || (
                    <span style={{ color: "var(--text-muted)" }}>Keine Rollen zugewiesen</span>
                  )}
                </div>
              </div>

              <div style={{
                padding: "var(--spacing-lg)",
                background: "rgba(100, 108, 255, 0.1)",
                borderRadius: "var(--radius-md)",
                border: "1px solid rgba(100, 108, 255, 0.2)",
                textAlign: "center" as const
              }}>
                <div style={{
                  color: "var(--primary-color)",
                  fontSize: "var(--font-size-2xl)",
                  marginBottom: "var(--spacing-sm)"
                }}>
                  ✨
                </div>
                <h3 style={{
                  color: "var(--primary-color)",
                  marginBottom: "var(--spacing-sm)"
                }}>
                  Abenteurer Status
                </h3>
                <p style={{
                  color: "var(--text-secondary)",
                  fontSize: "var(--font-size-sm)"
                }}>
                  Aktiver Spieler
                </p>
              </div>
            </div>
          </div>
        </div>

        <div className="card" style={{ marginTop: "var(--spacing-xl)" }}>
          <h2 style={{
            color: "var(--primary-color)",
            fontSize: "var(--font-size-xl)",
            marginBottom: "var(--spacing-lg)"
          }}>
            🎮 Schnellaktionen
          </h2>
          <div style={{
            display: "flex",
            gap: "var(--spacing-md)",
            justifyContent: "center",
            flexWrap: "wrap" as const
          }}>
            <button className="btn btn-primary">
              🎯 Adventure starten
            </button>
            <button className="btn btn-secondary">
              📊 Statistiken
            </button>
            <button className="btn btn-secondary">
              ⚙️ Einstellungen
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
