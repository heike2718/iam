export type MessageLevel = 'INFO' | 'WARN' | 'ERROR';

export interface Message {
    readonly level: MessageLevel;
    readonly message: string;
};

export interface ResponsePayload {
    readonly message: Message;
    readonly data?: any;
};

export interface ZweiPassworte {
    readonly passwort: string;
    readonly passwortWdh: string;
}

export interface PasswortPayload {
    readonly passwort: string;
    readonly zweiPassworte: ZweiPassworte;
}

export const initialPasswortPayload : PasswortPayload = {
    passwort: '',
    zweiPassworte: {
        passwort: '',
        passwortWdh: ''
    }
}