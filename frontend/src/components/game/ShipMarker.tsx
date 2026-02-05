import './ShipMarker.css';

interface ShipMarkerProps {
    x: number;
    y: number;
    sectorWidth: number;
    sectorHeight: number;
}

export function ShipMarker({ x, y, sectorWidth, sectorHeight }: ShipMarkerProps) {
    return (
        <div
            className="ship-marker"
            style={{
                left: `${(x / sectorWidth) * 100}%`,
                top: `${(y / sectorHeight) * 100}%`
            }}
        >
            <div className="ship-dot"></div>
            <div className="ship-ring"></div>
        </div>
    );
}
