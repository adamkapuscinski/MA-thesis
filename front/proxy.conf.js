const PROXY_CONFIG = {
  "/api": {
    target: 'http://localhost:8080',
    changeOrigin: false,
    secure: false,
    headers: { host: 'localhost:4200' }
  }
};

module.exports = PROXY_CONFIG;
