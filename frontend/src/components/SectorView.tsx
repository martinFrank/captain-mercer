
import React from 'react';
import type { Sector, Ship } from '../types/game';
import './SectorView.css';

interface SectorViewProps {
    sector: Sector;
    ship: Ship;
}

export const SectorView: React.FC<SectorViewProps> = ({ sector, ship }) => {
    // Calculate scaling if necessary, for now assuming 1000x1000 sector maps to 100%

    return (
        <div className="sector-view-container">
            <h3 className="sector-title">SECTOR: {sector.name.toUpperCase()}</h3>
            <div className="sector-map">
                {/* Render Stars */}
                {sector.stars.map(star => (
                    <div
                        key={star.id}
                        className={`star-marker star-${star.type} star-${star.size}`}
                        style={{
                            left: `${(star.x / sector.width) * 100}%`,
                            top: `${(star.y / sector.height) * 100}%`
                        }}
                        title={star.name}
                    >
                        <div className="star-icon"></div>
                        <span className="star-label">{star.name}</span>
                    </div>
                ))}

                {/* Render Ship */}
                <div
                    className="ship-marker"
                    style={{
                        left: `${((ship.x ?? 0) / sector.width) * 100}%`,
                        top: `${((ship.y ?? 0) / sector.height) * 100}%`
                    }}
                >
                    <div className="ship-dot"></div>
                    <div className="ship-ring"></div>
                </div>
            </div>
            <div className="sector-coordinates">
                COORD: {ship.x ?? 0}, {ship.y ?? 0}
            </div>
        </div>
    );
};
