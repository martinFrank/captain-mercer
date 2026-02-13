import type { Captain } from '../../types/game';
import { CaptainHUD } from './CaptainHUD';
import { SystemsPanel } from './SystemsPanel';
import './ShipStatusView.css';

interface ShipStatusViewProps {
    captain: Captain;
    onSave: () => void;
    saving: boolean;
}

export function ShipStatusView({ captain, onSave, saving }: ShipStatusViewProps) {
    return (
        <div className="ship-status-view">
            <div className="game-panel space-background">
                <div className="ship-container">
                    <div className="ship-icon">🚀</div>
                    <div className="ship-label">{captain.ship.name}</div>
                </div>
            </div>

            <CaptainHUD captain={captain} />

            <SystemsPanel
                equipment={captain.ship.equipment}
                onSave={onSave}
                saving={saving}
            />
        </div>
    );
}
