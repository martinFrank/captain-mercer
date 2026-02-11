
export interface TechnicalEquipment {
    id: string;
    name: string;
    status: 'active' | 'damaged' | 'offline';
    description?: string;
}

export interface Ship {
    id: string;
    name: string;
    weight: number; // in metric tons
    crewSize: number;
    equipment: TechnicalEquipment[];
    currentStarId: string;
    currentStarName: string;
    sector: Sector;
}


export interface Quest {
    id: string;
    title: string;
    description: string;
    status: 'active' | 'completed';
    sortOrder: number;
}

export interface SectorSummary {
    id: string;
    name: string;
    gridX: number;
    gridY: number;
}

export interface Captain {
    id: string;
    name: string;
    ship: Ship;
    quests: Quest[];
    sectors: Sector[];
}

export interface StarService {
    id: string;
    type: string;
}

export interface Star {
    id: string;
    name: string;
    x: number;
    y: number;
    type: 'yellow' | 'blue' | 'red' | 'white'; // simplistic types for now
    size: 'small' | 'medium' | 'large';
    services: StarService[];
}

export interface StarConnection {
    id: string;
    starFromId: string;
    starToId: string;
    distance: number;
}

export interface Sector {
    id: string;
    name: string;
    width: number;
    height: number;
    gridX: number;
    gridY: number;
    stars: Star[];
    connections: StarConnection[];
}
