import { useState, useEffect } from 'react';
import type { Captain } from '../types/game';
import { fetchGameState, saveGameState } from '../api/gameApi';

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
        <div className="game-view-container" style={{
            position: 'relative',
            width: '100%',
            height: '600px', /* Fixed height for the game canvas */
            backgroundColor: '#000',
            borderRadius: 'var(--radius-lg)',
            overflow: 'hidden',
            border: '2px solid var(--border-color)',
            boxShadow: 'var(--shadow-lg)'
        }}>
            {/* Space Background Layer */}
            <div style={{
                position: 'absolute',
                top: 0,
                left: 0,
                width: '100%',
                height: '100%',
                background: 'radial-gradient(ellipse at bottom, #1B2735 0%, #090A0F 100%)',
                zIndex: 1,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center'
            }}>
                {/* Ship Representation (Center) */}
                <div style={{
                    position: 'relative',
                    textAlign: 'center',
                    zIndex: 2
                }}>
                    <div style={{
                        fontSize: '4rem',
                        marginBottom: '1rem',
                        filter: 'drop-shadow(0 0 10px rgba(0, 200, 255, 0.5))'
                    }}>🚀</div>
                    <div style={{
                        color: '#fff',
                        background: 'rgba(0,0,0,0.7)',
                        padding: '4px 8px',
                        borderRadius: '4px',
                        fontSize: '0.9rem'
                    }}>
                        {captain.ship.name}
                    </div>
                </div>
            </div>

            {/* HUD Overlay - Top Left */}
            <div style={{
                position: 'absolute',
                top: '20px',
                left: '20px',
                zIndex: 10,
                padding: 'var(--spacing-sm) var(--spacing-md)',
                background: 'rgba(0, 0, 0, 0.6)',
                border: '1px solid var(--primary-color)',
                borderRadius: 'var(--radius-sm)',
                color: 'var(--primary-color)',
                fontFamily: 'monospace',
                fontSize: 'var(--font-size-sm)',
                backdropFilter: 'blur(4px)'
            }}>
                <div style={{ marginBottom: '8px', borderBottom: '1px solid rgba(255,255,255,0.1)', paddingBottom: '4px' }}>
                    <strong>CAPTAIN: </strong> {captain.name}
                </div>
                <div>POS: [{captain.ship.position?.x ?? 0}, {captain.ship.position?.y ?? 0}]</div>
                <div>CREW: {captain.ship.crewSize} | MASS: {captain.ship.weight}kg</div>
            </div>

            {/* HUD Overlay - Bottom Panel (Equipment) */}
            <div style={{
                position: 'absolute',
                bottom: '20px',
                left: '20px',
                right: '20px',
                zIndex: 10,
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'flex-end'
            }}>
                <div style={{
                    background: 'rgba(0, 20, 40, 0.8)',
                    padding: '10px',
                    borderRadius: '8px',
                    border: '1px solid #334',
                    color: '#aec'
                }}>
                    <div style={{ fontSize: '0.8rem', opacity: 0.7, marginBottom: '5px' }}>SYSTEMS</div>
                    <div style={{ display: 'flex', gap: '10px' }}>
                        {captain.ship.equipment?.map(eq => (
                            <span key={eq.id} style={{
                                padding: '2px 6px',
                                background: eq.status === 'active' ? 'rgba(0, 255, 100, 0.1)' : 'rgba(255, 0, 0, 0.1)',
                                border: `1px solid ${eq.status === 'active' ? '#0f0' : '#f00'}`,
                                borderRadius: '4px',
                                fontSize: '0.8rem'
                            }}>
                                {eq.name}
                            </span>
                        ))}
                    </div>
                </div>

                <div style={{ display: 'flex', gap: '10px' }}>
                    <button className="btn btn-primary" style={{ fontSize: '0.8rem', padding: '6px 16px' }}>
                        SCAN SECTOR
                    </button>
                    <button
                        className="btn btn-primary"
                        style={{ fontSize: '0.8rem', padding: '6px 16px', background: saving ? '#444' : '' }}
                        onClick={handleSave}
                        disabled={saving}
                    >
                        {saving ? 'SAVING...' : 'SAVE STATE'}
                    </button>
                    <button className="btn btn-primary" style={{ fontSize: '0.8rem', padding: '6px 16px' }}>
                        ENGAGE ENGINES
                    </button>
                </div>
            </div>
        </div>
    );
}
