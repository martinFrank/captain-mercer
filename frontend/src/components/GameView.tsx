import { useState, useEffect } from 'react';
import type { Captain } from '../types/game';
import { fetchGameState, saveGameState } from '../api/gameApi';
import './GameView.css';

export default function GameView() {
    const [captain, setCaptain] = useState<Captain | null>(null);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);

    useEffect(() => {
        loadGame();
    }, []);

    const loadGame = async () => {
        try {
            const data = await fetchGameState();
            setCaptain(data);
        } catch (error) {
            console.error("Failed to load game:", error);
        } finally {
            setLoading(false);
        }
    };

    const handleSave = async () => {
        if (!captain) return;
        setSaving(true);
        try {
            const updated = await saveGameState(captain);
            setCaptain(updated);
            alert("Game saved successfully!");
        } catch (error) {
            console.error("Failed to save game:", error);
            alert("Failed to save game.");
        } finally {
            setSaving(false);
        }
    };

    if (loading) return <div style={{ color: '#fff', padding: '20px' }}>Loading sector data...</div>;
    if (!captain) return <div style={{ color: '#f00', padding: '20px' }}>Error initializing game. Please try again.</div>;

    return (
        <div className="game-view-container">
            {/* Space Background Layer */}
            <div className="space-background">
                {/* Ship Representation (Center) */}
                <div className="ship-container">
                    <div className="ship-icon">🚀</div>
                    <div className="ship-label">
                        {captain.ship.name}
                    </div>
                </div>
            </div>

            {/* HUD Overlay - Top Left */}
            <div className="hud-overlay-top-left">
                <div className="hud-row">
                    <strong>CAPTAIN: </strong> {captain.name}
                </div>
                <div>POS: [{captain.ship.position?.x ?? 0}, {captain.ship.position?.y ?? 0}]</div>
                <div>CREW: {captain.ship.crewSize} | MASS: {captain.ship.weight}kg</div>
            </div>

            {/* HUD Overlay - Bottom Panel (Equipment) */}
            <div className="hud-overlay-bottom">
                <div className="systems-panel">
                    <div className="systems-title">SYSTEMS</div>
                    <div className="systems-list">
                        {captain.ship.equipment?.map(eq => (
                            <span
                                key={eq.id}
                                className={`system-status ${eq.status === 'active' ? 'active' : 'inactive'}`}
                            >
                                {eq.name}
                            </span>
                        ))}
                    </div>
                </div>

                <div className="controls-panel">
                    <button className="btn btn-primary control-btn">
                        SCAN SECTOR
                    </button>
                    <button
                        className={`btn btn-primary control-btn ${saving ? 'state-saving' : ''}`}
                        onClick={handleSave}
                        disabled={saving}
                    >
                        {saving ? 'SAVING...' : 'SAVE STATE'}
                    </button>
                    <button className="btn btn-primary control-btn">
                        ENGAGE ENGINES
                    </button>
                </div>
            </div>
        </div>
    );
}
