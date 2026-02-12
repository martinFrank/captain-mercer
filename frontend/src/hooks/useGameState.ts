import { useState, useEffect } from 'react';
import type { Captain, Sector } from '../types/game';
import { fetchGameState, saveGameState } from '../api/gameApi';

export function useGameState() {
    const [captain, setCaptain] = useState<Captain | null>(null);
    const [sector, setSector] = useState<Sector | null>(null);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);

    useEffect(() => {
        loadGame();
    }, []);

    const loadGame = async () => {
        try {
            const captainData = await fetchGameState();
            setCaptain(captainData);
            if (captainData.ship.sector) {
                setSector(captainData.ship.sector);
            }
        } catch (error) {
            console.error("Failed to load game:", error);
        } finally {
            setLoading(false);
        }
    };

    const saveGame = async () => {
        if (!captain) return;
        setSaving(true);
        try {
            const updated = await saveGameState(captain);
            setCaptain(updated);
        } catch (error) {
            console.error("Failed to save game:", error);
            throw error;
        } finally {
            setSaving(false);
        }
    };

    const jumpToStar = async (starId: string) => {
        if (!captain) return;
        setSaving(true);
        try {
            const jumpedCaptain = {
                ...captain,
                ship: { ...captain.ship, currentStarId: starId }
            };
            const updated = await saveGameState(jumpedCaptain);
            setCaptain(updated);
        } catch (error) {
            console.error("FTL jump failed:", error);
            throw error;
        } finally {
            setSaving(false);
        }
    };

    return { captain, sector, loading, saving, saveGame, jumpToStar };
}
