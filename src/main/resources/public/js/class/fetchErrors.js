class ApiError extends Error {
    constructor(message, status) {
        super(message);
        this.status = status;
        this.name = 'ApiError';
    }
}

class JsonError extends Error {
    constructor(message) {
        super(message);
        this.name = 'JsonError';
    }
}

export { ApiError, JsonError };