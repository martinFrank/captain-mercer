import './ViewToggle.css';

interface ViewToggleOption {
    key: string;
    label: string;
}

interface ViewToggleProps {
    options: ViewToggleOption[];
    active: string;
    onChange: (key: string) => void;
}

export function ViewToggle({ options, active, onChange }: ViewToggleProps) {
    return (
        <div className="view-toggle-controls">
            {options.map(option => (
                <button
                    key={option.key}
                    className={`btn view-toggle-btn ${active === option.key ? 'active' : ''}`}
                    onClick={() => onChange(option.key)}
                >
                    {option.label}
                </button>
            ))}
        </div>
    );
}
