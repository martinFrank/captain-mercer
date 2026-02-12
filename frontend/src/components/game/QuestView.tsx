import type { Quest } from '../../types/game';
import './QuestView.css';

interface QuestViewProps {
    quests: Quest[];
}

export function QuestView({ quests }: QuestViewProps) {
    const sorted = [...quests].sort((a, b) => a.sortOrder - b.sortOrder);

    return (
        <div className="game-panel">
            <div className="game-list">
                {sorted.map(quest => (
                    <div key={quest.id} className={`game-card quest-item ${quest.status}`}>
                        <div className="quest-header">
                            <span className={`game-badge ${quest.status === 'active' ? 'game-badge-active' : 'game-badge-completed'}`}>
                                {quest.status === 'active' ? '[AKTIV]' : '[ERLEDIGT]'}
                            </span>
                            <span className="quest-title">{quest.title}</span>
                        </div>
                        <div className="quest-description">{quest.description}</div>
                    </div>
                ))}
                {sorted.length === 0 && (
                    <div className="game-empty">Keine Quests verfügbar.</div>
                )}
            </div>
        </div>
    );
}
