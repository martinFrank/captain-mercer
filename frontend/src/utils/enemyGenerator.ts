import type { Ship, TechnicalEquipment } from '../types/game';

const SHIP_NAMES = [
    'Void Stalker', 'Iron Fang', 'Black Nebula', 'Crimson Raider',
    'Shadow Viper', 'Star Reaver', 'Dusk Runner', 'Plasma Wraith',
    'Comet Striker', 'Obsidian Claw', 'Warp Jackal', 'Nebula Scorpion',
    'Nova Predator', 'Flux Marauder', 'Dark Horizon'
];

const EQUIPMENT_TEMPLATES: Omit<TechnicalEquipment, 'id'>[] = [
    { name: 'Pulse Laser Mk-I', status: 'active', description: 'Standard pulse laser' },
    { name: 'Shield Generator', status: 'active', description: 'Basic energy shield' },
    { name: 'Hull Plating', status: 'active', description: 'Reinforced hull armor' },
    { name: 'Plasma Cannon', status: 'active', description: 'High-energy plasma weapon' },
    { name: 'ECM Jammer', status: 'active', description: 'Electronic countermeasures' },
    { name: 'Missile Pod', status: 'active', description: 'Short-range missile launcher' },
    { name: 'Ion Disruptor', status: 'active', description: 'Disables ship systems' },
    { name: 'Cargo Scanner', status: 'active', description: 'Scans nearby cargo' },
    { name: 'Afterburner', status: 'active', description: 'Emergency speed boost' },
    { name: 'Repair Drone', status: 'active', description: 'Automated hull repair' }
];

function randomInt(min: number, max: number): number {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function pickRandom<T>(array: T[]): T {
    return array[Math.floor(Math.random() * array.length)];
}

function pickRandomEquipment(count: number): TechnicalEquipment[] {
    const shuffled = [...EQUIPMENT_TEMPLATES].sort(() => Math.random() - 0.5);
    return shuffled.slice(0, count).map((template, index) => ({
        ...template,
        id: `enemy-eq-${index}`
    }));
}

export function generateEnemyShip(): Ship {
    const equipmentCount = randomInt(1, 4);
    return {
        id: `enemy-${Date.now()}`,
        name: pickRandom(SHIP_NAMES),
        weight: randomInt(50, 500),
        crewSize: randomInt(2, 20),
        equipment: pickRandomEquipment(equipmentCount),
        currentStarId: '',
        currentStarName: '',
        sector: { id: '', name: '', width: 0, height: 0, gridX: 0, gridY: 0, stars: [], connections: [] }
    };
}
