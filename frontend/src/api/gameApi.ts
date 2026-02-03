import { api } from '../api';
import type { Captain, Ship, Sector } from '../types/game';

interface GameResponse {
    id: string;
    user: {
        id: number;
        username: string;
    };
    ship: Ship;
}

export const fetchGameState = async (): Promise<Captain> => {
    const response = await api.get<GameResponse>('/api/game');
    const game = response.data;
    return {
        id: String(game.user.id),
        name: game.user.username,
        ship: game.ship
    };
};

export const fetchCurrentSector = async (): Promise<Sector | null> => {
    // Sector is now fetched as part of the game state (captain.ship.sector)
    // This function can be deprecated or used to force refresh if needed
    return null;
};

export const saveGameState = async (captain: Captain): Promise<Captain> => {
    // Backend expects GameDTO { ship: ShipDTO }
    const payload = {
        ship: captain.ship
    };

    const response = await api.post<GameResponse>('/api/game', payload);
    const game = response.data;
    return {
        id: String(game.user.id),
        name: game.user.username,
        ship: game.ship
    };
};
