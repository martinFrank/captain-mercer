import GameView from "../components/game/GameView";

export default function HomePage() {
  return (
    <div className="page-container">
      <div className="page-content">  
        <div className="card" style={{ padding: "var(--spacing-sm)" }}>
          <GameView />
        </div>
      </div>
    </div>
  );
}
