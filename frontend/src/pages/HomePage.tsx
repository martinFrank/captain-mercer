import GameView from "../components/GameView";

export default function HomePage() {
  return (
    <div className="page-container">
      <div className="page-content">
        <div className="center-content text-center" style={{ marginBottom: "var(--spacing-lg)" }}>
          <h1 className="title">Captain Mercer</h1>
          <p className="subtitle">Bereit zum Abflug?</p>
        </div>

        <div className="card" style={{ padding: "var(--spacing-sm)" }}>
          <GameView />
        </div>
      </div>
    </div>
  );
}
