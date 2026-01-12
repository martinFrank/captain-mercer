import { useState } from "react"; 
import { useAuth } from "./AuthContext";
import { useNavigate } from "react-router-dom";
import "./LoginPage.css";
import { api } from "../api";

export default function LoginPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setError("");
        
        try {
            const res = await api.post("/api/auth/login", {
                username,
                password
            });
            login(res.data.token);
            navigate("/");
        } catch (err: any) {
            console.error(err);
            setError(
                err.response?.data?.message ||
                "Login fehlgeschlagen. Bitte prüfen Sie Ihre Eingaben."
            );
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="login-container">
            <div className="login-card">
                <div className="login-header">
                    <div className="adventure-icon">
                        🗡️
                    </div>
                    <h1 className="login-title">Adventure Game</h1>
                    <p className="login-subtitle">Willkommen zurück, Abenteurer!</p>
                </div>
                
                <form className="login-form" onSubmit={handleSubmit}>
                    <div className="input-group">
                        <input
                            type="text"
                            value={username}
                            onChange={e => setUsername(e.target.value)}
                            placeholder="Benutzername"
                            required
                            disabled={isLoading}
                        />
                    </div>
                    
                    <div className="input-group">
                        <input
                            type="password"
                            value={password}
                            onChange={e => setPassword(e.target.value)}
                            placeholder="Passwort"
                            required
                            disabled={isLoading}
                        />
                    </div>
                    
                    <button
                        type="submit"
                        className="login-button"
                        disabled={isLoading || !username || !password}
                    >
                        {isLoading ? "Wird eingeloggt..." : "Einloggen"}
                    </button>
                    
                    {error && (
                        <div className="error-message">
                            {error}
                        </div>
                    )}
                </form>
            </div>
        </div>
    );
}
