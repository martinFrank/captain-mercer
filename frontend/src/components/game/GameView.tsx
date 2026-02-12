import { useState, useEffect } from 'react';
import type { Captain, Sector, Ship, CombatPhase } from '../../types/game';
import { fetchGameState, saveGameState } from '../../api/gameApi';
import { generateEnemyShip } from '../../utils/enemyGenerator';
import { LoadingSpinner } from '../common/LoadingSpinner';
import { ErrorMessage } from '../common/ErrorMessage';
import { ViewToggle } from '../common/ViewToggle';
import { ShipStatusView } from './ShipStatusView';
import { SectorView } from './SectorView';
import { GalacticChartView } from './GalacticChartView';
import { QuestView } from './QuestView';
import { StarView } from './StarView';
import { CombatView } from './CombatView';
import { LootDialog } from './LootDialog';
import './GameView.css';

const VIEW_OPTIONS = [
    { key: 'status', label: 'SHIP STATUS' },
    { key: 'sector', label: 'SECTOR MAP' },
    { key: 'galactic', label: 'GALACTIC CHART' },
    { key: 'star', label: 'STAR' },
    { key: 'quest', label: 'QUEST' }
];

export default function GameView() {
    const [captain, setCaptain] = useState<Captain | null>(null);
    const [sector, setSector] = useState<Sector | null>(null);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [viewMode, setViewMode] = useState('status');
    const [selectedStarId, setSelectedStarId] = useState<string | null>(null);
    const [combatPhase, setCombatPhase] = useState<CombatPhase | null>(null);
    const [enemyShip, setEnemyShip] = useState<Ship | null>(null);

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

    const handleViewChange = (key: string) => {
        if (combatPhase !== null) return;
        setViewMode(key);
    };

    const handleStarSelect = (starId: string) => {
        if (!captain || starId === captain.ship.currentStarId) return;
        setSelectedStarId(prev => prev === starId ? null : starId);
    };

    const handleFtlJump = async () => {
        if (!captain || !sector || !selectedStarId) return;
        const targetStar = sector.stars.find(s => s.id === selectedStarId);
        if (!targetStar) return;

        const updatedShip = { ...captain.ship, currentStarId: targetStar.id, currentStarName: targetStar.name };
        const updatedCaptain = { ...captain, ship: updatedShip };

        try {
            const saved = await saveGameState(updatedCaptain);
            setCaptain(saved);
            if (saved.ship.sector) {
                setSector(saved.ship.sector);
            }
        } catch (error) {
            console.error("FTL jump failed:", error);
            return;
        }

        setSelectedStarId(null);
        setEnemyShip(generateEnemyShip());
        setCombatPhase('engage');
    };

    const handleTargetEnemy = () => {
        setCombatPhase('targeted');
    };

    const handleFire = () => {
        setCombatPhase('destroyed');
        setTimeout(() => setCombatPhase('loot'), 1500);
    };

    const handleLootDismiss = () => {
        setCombatPhase(null);
        setEnemyShip(null);
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

    const renderSectorContent = () => {
        if (combatPhase === 'loot' && enemyShip) {
            return (
                <LootDialog
                    enemyShipName={enemyShip.name}
                    loot={enemyShip.equipment}
                    onDismiss={handleLootDismiss}
                />
            );
        }

        if (combatPhase && combatPhase !== 'loot' && enemyShip) {
            return (
                <CombatView
                    playerShip={captain.ship}
                    enemyShip={enemyShip}
                    phase={combatPhase}
                    onTargetEnemy={handleTargetEnemy}
                    onFire={handleFire}
                />
            );
        }

        return (
            <>
                {sector && (
                    <SectorView
                        sector={sector}
                        ship={captain.ship}
                        selectedStarId={selectedStarId}
                        onStarSelect={handleStarSelect}
                    />
                )}
                {selectedStarId && sector && (
                    <div className="ftl-jump-panel">
                        <span className="ftl-target-label">
                            TARGET: {sector.stars.find(s => s.id === selectedStarId)?.name?.toUpperCase() ?? 'UNKNOWN'}
                        </span>
                        <button className="btn ftl-jump-btn" onClick={handleFtlJump}>FTL JUMP</button>
                    </div>
                )}
            </>
        );
    };

    return (
        <div className="game-view-container">
            <ViewToggle
                options={VIEW_OPTIONS}
                active={viewMode}
                onChange={handleViewChange}
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
                        {renderSectorContent()}
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
                {viewMode === 'star' && sector && (() => {
                    const currentStar = sector.stars.find(s => s.id === captain.ship.currentStarId);
                    return currentStar ? <StarView star={currentStar} /> : null;
                })()}
            </div>
        </div>
    );
}
