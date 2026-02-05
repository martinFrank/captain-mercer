import type { ReactNode } from 'react';
import './InfoItem.css';

interface InfoItemProps {
    label: string;
    value: string | ReactNode;
    bold?: boolean;
}

export function InfoItem({ label, value, bold = false }: InfoItemProps) {
    return (
        <div className="info-item">
            <label className="info-label">{label}</label>
            <span className={`info-value ${bold ? 'info-value-bold' : ''}`}>
                {value}
            </span>
        </div>
    );
}
