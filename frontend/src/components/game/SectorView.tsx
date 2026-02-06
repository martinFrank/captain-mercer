import type { Sector, Ship } from '../../types/game';
import { ConnectionLines } from './ConnectionLines';
import { StarMarker } from './StarMarker';
import { ShipMarker } from './ShipMarker';
import './SectorView.css';

interface SectorViewProps {
    sector: Sector;
    ship: Ship;
}

export function SectorView({ sector, ship }: SectorViewProps) {
    const currentStar = sector.stars.find(s => s.id === ship.currentStarId);
    const starX = currentStar?.x ?? 0;
    const starY = currentStar?.y ?? 0;

    return (
        <div className="sector-view-container">
            <h3 className="sector-title">SECTOR: {sector.name.toUpperCase()}</h3>
            <div className="sector-map">
                <ConnectionLines
                    connections={sector.connections ?? []}
                    stars={sector.stars}
                    sectorWidth={sector.width}
                    sectorHeight={sector.height}
                />
                {sector.stars.map(star => (
                    <StarMarker
                        key={star.id}
                        star={star}
                        sectorWidth={sector.width}
                        sectorHeight={sector.height}
                    />
                ))}

                <ShipMarker
                    x={starX}
                    y={starY}
                    sectorWidth={sector.width}
                    sectorHeight={sector.height}
                />
            </div>
            <div className="sector-coordinates">
                STAR: {ship.currentStarName ?? 'Unknown'}
            </div>
        </div>
    );
}
