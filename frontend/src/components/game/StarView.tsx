import type { Star } from '../../types/game';
import './StarView.css';

interface StarViewProps {
    star: Star;
}

const SERVICE_LABELS: Record<string, { label: string; description: string }> = {
    TRADING_PLATFORM: { label: 'Handelsplattform', description: 'Handel mit Guetern und Rohstoffen' },
    REPAIR_SHOP: { label: 'Reparaturwerkstatt', description: 'Reparaturen und neue Ausruestung' },
    BULLETIN_BOARD: { label: 'Schwarzes Brett', description: 'Informationen und Nachrichten' },
    BANK: { label: 'Bank', description: 'Finanztransaktionen und Kredite' },
    GOVERNMENT: { label: 'Regierung', description: 'Paesse, Erlaubnisse und Polizei' },
};

export function StarView({ star }: StarViewProps) {
    return (
        <div className="game-panel">
            <div className="game-card star-info">
                <div className="star-info-header">{star.name}</div>
                <div className="star-info-details">
                    <div className="star-info-detail">
                        <span className="star-info-label">Typ:</span> {star.type}
                    </div>
                    <div className="star-info-detail">
                        <span className="star-info-label">Groesse:</span> {star.size}
                    </div>
                </div>
            </div>

            <div className="game-list">
                {star.services.map(service => {
                    const info = SERVICE_LABELS[service.type] ?? { label: service.type, description: '' };
                    return (
                        <div key={service.id} className="game-card service-item">
                            <div className="service-header">
                                <span className="game-badge game-badge-info">[DIENST]</span>
                                <span className="service-title">{info.label}</span>
                            </div>
                            <div className="service-description">{info.description}</div>
                        </div>
                    );
                })}
                {star.services.length === 0 && (
                    <div className="game-empty">Keine Dienstleistungen verfuegbar.</div>
                )}
            </div>
        </div>
    );
}
