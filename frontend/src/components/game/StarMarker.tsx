import type { Star } from '../../types/game';
import './StarMarker.css';

interface StarMarkerProps {
    star: Star;
    sectorWidth: number;
    sectorHeight: number;
    isSelected?: boolean;
    onSelect?: (starId: string) => void;
}

export function StarMarker({ star, sectorWidth, sectorHeight, isSelected, onSelect }: StarMarkerProps) {
    const handleClick = () => {
        if (onSelect) {
            onSelect(star.id);
        }
    };

    return (
        <div
            className={`star-marker star-${star.type} star-${star.size}${isSelected ? ' star-selected' : ''}`}
            style={{
                left: `${(star.x / sectorWidth) * 100}%`,
                top: `${(star.y / sectorHeight) * 100}%`
            }}
            title={star.name}
            onClick={handleClick}
        >
            <div className="star-icon"></div>
            <span className="star-label">{star.name}</span>
        </div>
    );
}
