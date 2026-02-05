import type { Star } from '../../types/game';
import './StarMarker.css';

interface StarMarkerProps {
    star: Star;
    sectorWidth: number;
    sectorHeight: number;
}

export function StarMarker({ star, sectorWidth, sectorHeight }: StarMarkerProps) {
    return (
        <div
            className={`star-marker star-${star.type} star-${star.size}`}
            style={{
                left: `${(star.x / sectorWidth) * 100}%`,
                top: `${(star.y / sectorHeight) * 100}%`
            }}
            title={star.name}
        >
            <div className="star-icon"></div>
            <span className="star-label">{star.name}</span>
        </div>
    );
}
