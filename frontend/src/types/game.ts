
export interface Position {
    x: number;
    y: number;
}

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
    position: Position;
    sector: Sector;
}


export interface Captain {
    id: string;
    name: string;
    ship: Ship;
}

export interface Star {
    id: string;
    name: string;
    x: number;
    y: number;
    type: 'yellow' | 'blue' | 'red' | 'white'; // simplistic types for now
    size: 'small' | 'medium' | 'large';
}

export interface Sector {
    id: string;
    name: string;
    width: number;
    height: number;
    stars: Star[];
}
