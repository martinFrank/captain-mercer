import type { Sector, Ship } from '../../types/game';
import { SectorView } from './SectorView';
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

export function GalacticChartView({ sectors, currentSectorId, ship }: GalacticChartViewProps) {
    const grid = buildGrid(sectors);

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
                                />
                            )}
                        </div>
                    ))
                )}
            </div>
        </div>
    );
}
