import { useCurrentUser } from "../hooks/useCurrentUser";
import { usePlayer } from "../hooks/usePlayer";
import './AdventurePage.css';

export default function AdventurePage() {
  const user = useCurrentUser();
  //const { adventure, loading: adventureLoading, error: adventureError } = useAdventure();
  const { player, loading: playerLoading, error: playerError } = usePlayer();


  if (!user) {
    return (
      <div className="adventure-page">
        <div className="adventure-content">
          <div className="loading">
            <div className="spinner"></div>
            Lade Benutzerdaten...
          </div>
        </div>
      </div>
    );
  }

  if (playerLoading) {
    return (
      <div className="adventure-page">
        <div className="adventure-content">
          <div className="adventure-loading">
            <div className="spinner"></div>
            Lade Adventure und Spielerdaten...
          </div>
        </div>
      </div>
    );
  }

  if (playerError) {
    return (
      <div className="adventure-page">
        <div className="adventure-content">
          <div className="adventure-error">
            <h2>⚠️ Fehler beim Laden der Daten</h2>
            {playerError && <p>Spieler: {playerError}</p>}
          </div>
        </div>
      </div>
    );
  }


  return (
    <div className="adventure-page">
      <div className="adventure-content">
        <header className="adventure-header">
          <div>
            <h1>🗡️ Adventure Game</h1>
            <div className="player-info">
              <div className="user-info">
                <span>Spieler: {user.firstName} {user.lastName}</span>
              </div>
              {player && (
                <div className="character-info">
                  <span>Charakter: {player.name}</span>
                  <span className="character-details">
                    {player.playerRace} {player.playerClass}
                  </span>
                </div>
              )}
            </div>
          </div>
        </header> 

        </div>
      </div> 
  );
}