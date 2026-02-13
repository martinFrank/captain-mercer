import type { Ship } from '../../types/game';
import './CombatView.css';

interface CombatViewProps {
    playerShip: Ship;
    enemyShip: Ship;
    phase: 'engage' | 'targeted' | 'destroyed';
    onTargetEnemy: () => void;
    onFire: () => void;
}

function getPhaseText(phase: CombatViewProps['phase']): string {
    switch (phase) {
        case 'engage': return 'ENEMY DETECTED — SELECT TARGET';
        case 'targeted': return 'TARGET LOCKED — READY TO FIRE';
        case 'destroyed': return 'TARGET DESTROYED';
    }
}

export function CombatView({ playerShip, enemyShip, phase, onTargetEnemy, onFire }: CombatViewProps) {
    return (
        <div className="combat-view">
            <div className="combat-arena">
                <div className="combat-ship combat-player">
                    <div className="combat-ship-icon">&#9650;</div>
                    <div className="combat-ship-name">{playerShip.name}</div>
                    <ul className="combat-equipment-list">
                        {playerShip.equipment.map(eq => (
                            <li key={eq.id} className={`eq-${eq.status}`}>{eq.name}</li>
                        ))}
                    </ul>
                </div>

                <div className="combat-vs">VS</div>

                <div
                    className={`combat-ship combat-enemy ${phase === 'targeted' ? 'enemy-targeted' : ''} ${phase === 'destroyed' ? 'enemy-destroyed' : ''}`}
                    onClick={phase === 'engage' ? onTargetEnemy : undefined}
                >
                    <div className="combat-ship-icon">&#9660;</div>
                    <div className="combat-ship-name">{enemyShip.name}</div>
                    {phase !== 'destroyed' && (
                        <ul className="combat-equipment-list">
                            {enemyShip.equipment.map(eq => (
                                <li key={eq.id}>{eq.name}</li>
                            ))}
                        </ul>
                    )}
                    {phase === 'destroyed' && (
                        <div className="explosion-text">DESTROYED</div>
                    )}
                </div>
            </div>

            <div className="combat-status-bar">
                <span className="combat-phase-text">{getPhaseText(phase)}</span>
                {phase === 'targeted' && (
                    <button className="btn combat-fire-btn" onClick={onFire}>FIRE</button>
                )}
            </div>
        </div>
    );
}
