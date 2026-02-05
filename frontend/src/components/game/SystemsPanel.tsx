import type { TechnicalEquipment } from '../../types/game';
import './SystemsPanel.css';

interface SystemsPanelProps {
    equipment: TechnicalEquipment[];
    onSave: () => void;
    saving: boolean;
}

export function SystemsPanel({ equipment, onSave, saving }: SystemsPanelProps) {
    return (
        <div className="systems-panel-container">
            <div className="systems-panel">
                <div className="systems-title">SYSTEMS</div>
                <div className="systems-list">
                    {equipment?.map(eq => (
                        <span
                            key={eq.id}
                            className={`system-status ${eq.status === 'active' ? 'active' : 'inactive'}`}
                        >
                            {eq.name}
                        </span>
                    ))}
                </div>
            </div>

            <div className="controls-panel">
                <button
                    className={`btn btn-primary control-btn ${saving ? 'state-saving' : ''}`}
                    onClick={onSave}
                    disabled={saving}
                >
                    {saving ? '...' : 'SAVE STATE'}
                </button>
            </div>
        </div>
    );
}
