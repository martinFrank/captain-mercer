import type { Sector, Ship } from '../../types/game';
import { StarMarker } from './StarMarker';
import { ShipMarker } from './ShipMarker';
import './SectorView.css';

interface SectorViewProps {
    sector: Sector;
    ship: Ship;
}

export function SectorView({ sector, ship }: SectorViewProps) {
    return (
        <div className="sector-view-container">
            <h3 className="sector-title">SECTOR: {sector.name.toUpperCase()}</h3>
            <div className="sector-map">
                {sector.stars.map(star => (
                    <StarMarker
                        key={star.id}
                        star={star}
                        sectorWidth={sector.width}
                        sectorHeight={sector.height}
                    />
                ))}

                <ShipMarker
                    x={ship.x ?? 0}
                    y={ship.y ?? 0}
                    sectorWidth={sector.width}
                    sectorHeight={sector.height}
                />
            </div>
            <div className="sector-coordinates">
                COORD: {ship.x ?? 0}, {ship.y ?? 0}
            </div>
        </div>
    );
}
