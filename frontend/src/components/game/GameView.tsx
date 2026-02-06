import { useState, useEffect } from 'react';
import type { Captain, Sector } from '../../types/game';
import { fetchGameState, saveGameState } from '../../api/gameApi';
import { LoadingSpinner } from '../common/LoadingSpinner';
import { ErrorMessage } from '../common/ErrorMessage';
import { ViewToggle } from '../common/ViewToggle';
import { ShipStatusView } from './ShipStatusView';
import { SectorView } from './SectorView';
import { QuestView } from './QuestView';
import './GameView.css';

const VIEW_OPTIONS = [
    { key: 'status', label: 'SHIP STATUS' },
    { key: 'sector', label: 'SECTOR MAP' },
    { key: 'quest', label: 'QUEST' }
];

export default function GameView() {
    const [captain, setCaptain] = useState<Captain | null>(null);
    const [sector, setSector] = useState<Sector | null>(null);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [viewMode, setViewMode] = useState('status');

    useEffect(() => {
        loadGame();
    }, []);

    const loadGame = async () => {
        try {
            const captainData = await fetchGameState();
            setCaptain(captainData);
            if (captainData.ship.sector) {
                setSector(captainData.ship.sector);
            }
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
            console.info("Game saved successfully!", updated);
            setCaptain(updated);
            alert("Game saved successfully!");
        } catch (error) {
            console.error("Failed to save game:", error);
            alert("Failed to save game.");
        } finally {
            setSaving(false);
        }
    };

    if (loading) {
        return (
            <div className="game-view-container">
                <LoadingSpinner message="Loading sector data..." />
            </div>
        );
    }

    if (!captain) {
        return (
            <div className="game-view-container">
                <ErrorMessage message="Error initializing game. Please try again." />
            </div>
        );
    }

    return (
        <div className="game-view-container">
            <ViewToggle
                options={VIEW_OPTIONS}
                active={viewMode}
                onChange={setViewMode}
            />

            <div className="game-content-area">
                {viewMode === 'status' && (
                    <ShipStatusView
                        captain={captain}
                        onSave={handleSave}
                        saving={saving}
                    />
                )}
                {viewMode === 'sector' && (
                    <div className="sector-view-wrapper">
                        {sector && <SectorView sector={sector} ship={captain.ship} />}
                    </div>
                )}
                {viewMode === 'quest' && (
                    <QuestView quests={captain.quests ?? []} />
                )}
            </div>
        </div>
    );
}
