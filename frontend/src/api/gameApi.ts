import { api } from '../api';
import type { Captain } from '../types/game';

export const fetchGameState = async (): Promise<Captain> => {
    const response = await api.get<Captain>('/api/game');
    return response.data;
};

export const saveGameState = async (captain: Captain): Promise<Captain> => {
    // Backend expects GameDTO { ship: ShipDTO }
    const payload = {
        ship: captain.ship
    };

    const response = await api.post<Captain>('/api/game', payload);
    return response.data;
};
