
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
}

export interface Captain {
    id: string;
    name: string;
    ship: Ship;
}
