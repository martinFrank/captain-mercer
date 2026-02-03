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

export const fetchCurrentSector = async (): Promise<Sector> => {
    // Mock implementation
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve({
                id: 'sector-1',
                name: 'Alpha Centauri',
                width: 1000,
                height: 1000,
                stars: [
                    { id: 's1', name: 'Alpha A', position: { x: 300, y: 300 }, type: 'yellow', size: 'large' },
                    { id: 's2', name: 'Alpha B', position: { x: 700, y: 600 }, type: 'blue', size: 'medium' },
                    { id: 's3', name: 'Proxima', position: { x: 500, y: 500 }, type: 'red', size: 'small' },
                    { id: 's4', name: 'Rigel', position: { x: 150, y: 150 }, type: 'white', size: 'large' },
                    { id: 's5', name: 'Betelgeuse', position: { x: 850, y: 200 }, type: 'red', size: 'large' },
                    { id: 's6', name: 'Sirius', position: { x: 200, y: 800 }, type: 'white', size: 'medium' },
                ]
            });
        }, 300);
    });
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
