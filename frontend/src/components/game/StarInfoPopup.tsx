import type { Star } from '../../types/game';
import { SERVICE_LABELS } from './StarView';
import './StarInfoPopup.css';

interface StarInfoPopupProps {
    star: Star;
    onDismiss: () => void;
}

export function StarInfoPopup({ star, onDismiss }: StarInfoPopupProps) {
    return (
        <div className="star-info-overlay">
            <div className="star-info-dialog">
                <h2 className="star-info-popup-title">{star.name.toUpperCase()}</h2>
                <div className="star-info-popup-details">
                    <div className="star-info-popup-detail">
                        <span className="label">Typ:</span> {star.type}
                    </div>
                    <div className="star-info-popup-detail">
                        <span className="label">Groesse:</span> {star.size}
                    </div>
                </div>
                <div className="star-info-popup-services">
                    {star.services.map(service => {
                        const info = SERVICE_LABELS[service.type] ?? { label: service.type, description: '' };
                        return (
                            <div key={service.id} className="star-info-popup-service">
                                <span className="star-info-popup-service-name">{info.label}</span>
                                <span className="star-info-popup-service-desc">{info.description}</span>
                            </div>
                        );
                    })}
                    {star.services.length === 0 && (
                        <div className="star-info-popup-empty">Keine Dienstleistungen verfuegbar.</div>
                    )}
                </div>
                <button className="btn star-info-dismiss-btn" onClick={onDismiss}>SCHLIESSEN</button>
            </div>
        </div>
    );
}
