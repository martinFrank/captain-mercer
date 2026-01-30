import { useState } from "react";
import { useAuth } from "./AuthContext";
import { useNavigate } from "react-router-dom";

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
        <div className="page-container" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>

            <div className="card" style={{ width: '100%', maxWidth: '400px' }}>
                <div className="text-center" style={{ marginBottom: "var(--spacing-xl)" }}>
                    <div className="adventure-icon" style={{ margin: "0 auto var(--spacing-md)" }}>
                        🗡️
                    </div>
                    <h1 className="title">Adventure Game</h1>
                    <p className="subtitle">Willkommen zurück, Abenteurer!</p>
                </div>

                <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 'var(--spacing-md)' }}>
                    <div>
                        <input
                            className="input"
                            type="text"
                            value={username}
                            onChange={e => setUsername(e.target.value)}
                            placeholder="Benutzername"
                            required
                            disabled={isLoading}
                        />
                    </div>

                    <div>
                        <input
                            className="input"
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
                        className="btn btn-primary"
                        style={{ width: '100%', justifyContent: 'center' }}
                        disabled={isLoading || !username || !password}
                    >
                        {isLoading ? "Wird eingeloggt..." : "Einloggen"}
                    </button>

                    {error && (
                        <div className="error">
                            {error}
                        </div>
                    )}
                </form>
            </div>
        </div>
    );
}
