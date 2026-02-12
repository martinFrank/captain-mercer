import { useState } from 'react';
import { useGameState } from '../../hooks/useGameState';
import { LoadingSpinner } from '../common/LoadingSpinner';
import { ErrorMessage } from '../common/ErrorMessage';
import { ViewToggle } from '../common/ViewToggle';
import { ShipStatusView } from './ShipStatusView';
import { SectorView } from './SectorView';
import { GalacticChartView } from './GalacticChartView';
import { QuestView } from './QuestView';
import { StarView } from './StarView';
import './GameView.css';

const VIEW_OPTIONS = [
    { key: 'status', label: 'SHIP STATUS' },
    { key: 'sector', label: 'SECTOR MAP' },
    { key: 'galactic', label: 'GALACTIC CHART' },
    { key: 'star', label: 'STAR' },
    { key: 'quest', label: 'QUEST' }
];

export default function GameView() {
    const { captain, sector, loading, saving, saveGame, jumpToStar } = useGameState();
    const [viewMode, setViewMode] = useState('status');
    const [selectedStarId, setSelectedStarId] = useState<string | null>(null);

    const handleSave = async () => {
        try {
            await saveGame();
            alert("Game saved successfully!");
        } catch {
            alert("Failed to save game.");
        }
    };

    const handleStarSelect = (starId: string) => {
        if (starId === captain?.ship.currentStarId) return;
        setSelectedStarId(prev => prev === starId ? null : starId);
    };

    const handleFtlJump = async () => {
        if (!selectedStarId) return;
        try {
            await jumpToStar(selectedStarId);
            setSelectedStarId(null);
        } catch {
            alert("FTL jump failed.");
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

    const currentStar = sector?.stars.find(s => s.id === captain.ship.currentStarId) ?? null;
    const selectedStarName = selectedStarId
        ? sector?.stars.find(s => s.id === selectedStarId)?.name
        : null;

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
                {viewMode === 'sector' && sector && (
                    <div className="sector-view-wrapper">
                        <SectorView
                            sector={sector}
                            ship={captain.ship}
                            selectedStarId={selectedStarId}
                            onStarSelect={handleStarSelect}
                        />
                        {selectedStarId && (
                            <div className="ftl-jump-panel">
                                <span className="ftl-target-name">
                                    TARGET: {selectedStarName}
                                </span>
                                <button
                                    className={`btn btn-primary ftl-jump-btn ${saving ? 'state-saving' : ''}`}
                                    onClick={handleFtlJump}
                                    disabled={saving}
                                >
                                    {saving ? 'JUMPING...' : 'FTL JUMP'}
                                </button>
                            </div>
                        )}
                    </div>
                )}
                {viewMode === 'galactic' && sector && (
                    <GalacticChartView
                        sectors={captain.sectors ?? []}
                        currentSectorId={sector.id}
                        ship={captain.ship}
                    />
                )}
                {viewMode === 'quest' && (
                    <QuestView quests={captain.quests ?? []} />
                )}
                {viewMode === 'star' && currentStar && (
                    <StarView star={currentStar} />
                )}
            </div>
        </div>
    );
}
