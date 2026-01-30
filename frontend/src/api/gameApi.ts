import { api } from '../api';
import type { Captain, Ship } from '../types/game';

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
