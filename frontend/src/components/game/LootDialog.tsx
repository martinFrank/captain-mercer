import type { TechnicalEquipment } from '../../types/game';
import './LootDialog.css';

interface LootDialogProps {
    enemyShipName: string;
    loot: TechnicalEquipment[];
    onDismiss: () => void;
}

export function LootDialog({ enemyShipName, loot, onDismiss }: LootDialogProps) {
    return (
        <div className="loot-overlay">
            <div className="loot-dialog">
                <h2 className="loot-title">SALVAGE FROM {enemyShipName.toUpperCase()}</h2>
                <div className="loot-items">
                    {loot.map(item => (
                        <div key={item.id} className="loot-card">
                            <span className="loot-item-name">{item.name}</span>
                            {item.description && (
                                <span className="loot-item-desc">{item.description}</span>
                            )}
                        </div>
                    ))}
                </div>
                <button className="btn loot-continue-btn" onClick={onDismiss}>CONTINUE</button>
            </div>
        </div>
    );
}
