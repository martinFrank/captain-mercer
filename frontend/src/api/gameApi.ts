import { api } from '../api';
import type { Captain, Sector } from '../types/game';

export const fetchGameState = async (): Promise<Captain> => {
    const response = await api.get<Captain>('/api/game');
    return response.data;
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

    const response = await api.post<Captain>('/api/game', payload);
    return response.data;
};
