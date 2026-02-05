import type { Captain } from '../../types/game';
import './CaptainHUD.css';

interface CaptainHUDProps {
    captain: Captain;
}

export function CaptainHUD({ captain }: CaptainHUDProps) {
    return (
        <div className="captain-hud">
            <div className="hud-row">
                <strong>CAPTAIN: </strong> {captain.name}
            </div>
            <div>POS: [{captain.ship.x ?? 0}, {captain.ship.y ?? 0}]</div>
            <div>CREW: {captain.ship.crewSize} | MASS: {captain.ship.weight}kg</div>
        </div>
    );
}
