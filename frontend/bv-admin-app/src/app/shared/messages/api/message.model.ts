export interface Message {
    message: string;
    level: 'ERROR' | 'WARN' | 'INFO' | 'NOOP';
}

export const noopMessage: Message = {
    message: '',
    level: 'NOOP'
}
