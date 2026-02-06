import type { Star, StarConnection } from '../../types/game';
import './ConnectionLines.css';

interface ConnectionLinesProps {
    connections: StarConnection[];
    stars: Star[];
    sectorWidth: number;
    sectorHeight: number;
}

export function ConnectionLines({ connections, stars, sectorWidth, sectorHeight }: ConnectionLinesProps) {
    const starMap = new Map(stars.map(star => [star.id, star]));

    const toPercent = (value: number, max: number) => (value / max) * 100;

    return (
        <svg className="connection-lines" viewBox="0 0 100 100" preserveAspectRatio="none">
            {connections.map(connection => {
                const fromStar = starMap.get(connection.starFromId);
                const toStar = starMap.get(connection.starToId);

                if (!fromStar || !toStar) return null;

                const x1 = toPercent(fromStar.x, sectorWidth);
                const y1 = toPercent(fromStar.y, sectorHeight);
                const x2 = toPercent(toStar.x, sectorWidth);
                const y2 = toPercent(toStar.y, sectorHeight);

                return (
                    <line
                        key={connection.id}
                        className="connection-line"
                        x1={x1}
                        y1={y1}
                        x2={x2}
                        y2={y2}
                    />
                );
            })}
        </svg>
    );
}
