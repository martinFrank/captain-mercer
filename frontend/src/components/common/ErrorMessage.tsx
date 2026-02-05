import './ErrorMessage.css';

interface ErrorMessageProps {
    message: string;
}

export function ErrorMessage({ message }: ErrorMessageProps) {
    return (
        <div className="error-message-container">
            <span className="error-icon">⚠</span>
            <span className="error-text">{message}</span>
        </div>
    );
}
