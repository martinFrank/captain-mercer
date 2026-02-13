import { useState, useCallback } from 'react';
import type { Sector, Ship, Star } from '../../types/game';
import { SectorView } from './SectorView';
import { StarInfoPopup } from './StarInfoPopup';
import './GalacticChartView.css';

interface GalacticChartViewProps {
    sectors: Sector[];
    currentSectorId: string;
    ship: Ship;
}

function buildGrid(sectors: Sector[]): (Sector | null)[][] {
    const grid: (Sector | null)[][] = [
        [null, null, null],
        [null, null, null],
        [null, null, null],
    ];
    for (const sector of sectors) {
        if (sector.gridX >= 0 && sector.gridX < 3 && sector.gridY >= 0 && sector.gridY < 3) {
            grid[sector.gridY][sector.gridX] = sector;
        }
    }
    return grid;
}

function findStarById(sectors: Sector[], starId: string): Star | undefined {
    for (const sector of sectors) {
        const star = sector.stars.find(s => s.id === starId);
        if (star) return star;
    }
    return undefined;
}

export function GalacticChartView({ sectors, currentSectorId, ship }: GalacticChartViewProps) {
    const grid = buildGrid(sectors);
    const [selectedStar, setSelectedStar] = useState<Star | null>(null);

    const handleStarSelect = useCallback((starId: string) => {
        const star = findStarById(sectors, starId);
        if (star) setSelectedStar(star);
    }, [sectors]);

    const handleDismiss = useCallback(() => {
        setSelectedStar(null);
    }, []);

    return (
        <div className="game-panel galactic-chart">
            <div className="galactic-grid">
                {grid.flatMap((row, y) =>
                    row.map((cell, x) => (
                        <div
                            key={`${x}-${y}`}
                            className={`galactic-cell${cell?.id === currentSectorId ? ' current' : ''}`}
                        >
                            {cell && (
                                <SectorView
                                    sector={cell}
                                    ship={cell.id === currentSectorId ? ship : undefined}
                                    onStarSelect={handleStarSelect}
                                />
                            )}
                        </div>
                    ))
                )}
            </div>
            {selectedStar && <StarInfoPopup star={selectedStar} onDismiss={handleDismiss} />}
        </div>
    );
}
