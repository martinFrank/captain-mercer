import { useState, useEffect } from 'react';
import type { Captain, Sector } from '../types/game';
import { fetchGameState, saveGameState, fetchCurrentSector } from '../api/gameApi';
import { SectorView } from './SectorView';
import './GameView.css';

export default function GameView() {
    const [captain, setCaptain] = useState<Captain | null>(null);
    const [sector, setSector] = useState<Sector | null>(null);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [viewMode, setViewMode] = useState<'status' | 'sector'>('status');

    useEffect(() => {
        loadGame();
    }, []);

    const loadGame = async () => {
        try {
            const [captainData, sectorData] = await Promise.all([
                fetchGameState(),
                fetchCurrentSector()
            ]);
            setCaptain(captainData);
            setSector(sectorData);
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
            {/* View Toggle Buttons */}
            <div className="view-toggle-controls">
                <button
                    className={`btn control-btn ${viewMode === 'status' ? 'active' : ''}`}
                    onClick={() => setViewMode('status')}
                >
                    SHIP STATUS
                </button>
                <button
                    className={`btn control-btn ${viewMode === 'sector' ? 'active' : ''}`}
                    onClick={() => setViewMode('sector')}
                >
                    SECTOR MAP
                </button>
            </div>

            <div className="game-content-area">
                {viewMode === 'status' ? (
                    /* Ship Status View */
                    <div className="ship-status-view">
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
                                <button
                                    className={`btn btn-primary control-btn ${saving ? 'state-saving' : ''}`}
                                    onClick={handleSave}
                                    disabled={saving}
                                >
                                    {saving ? '...' : 'SAVE STATE'}
                                </button>
                            </div>
                        </div>
                    </div>
                ) : (
                    /* Sector View */
                    <div className="sector-view-wrapper">
                        {sector && <SectorView sector={sector} ship={captain.ship} />}
                    </div>
                )}
            </div>
        </div>
    );
}
