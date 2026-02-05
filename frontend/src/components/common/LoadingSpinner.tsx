import './LoadingSpinner.css';

interface LoadingSpinnerProps {
    message?: string;
}

export function LoadingSpinner({ message = 'Loading...' }: LoadingSpinnerProps) {
    return (
        <div className="loading-spinner-container">
            <div className="spinner"></div>
            <span className="loading-message">{message}</span>
        </div>
    );
}
