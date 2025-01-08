export class AuthproviderConfiguration {
    constructor(
        public version: string,
        public envName: string,
        public baseUrl: string,
        public profilUrl: string,
        public datenschutzUrl: string,
        public assetsPath: string,
        public withCredentials: boolean,
        public clientId: string,
        public production: boolean
    ) { }
};
