/** @type {import('next').NextConfig} */
const nextConfig = {
    webpack: (config) => {
        config.resolve.fallback = {
            net: false,  // Prevents 'net' module from being resolved
            tls: false,  // Prevents 'tls' module from being resolved
            // Add other Node.js core modules here if needed
        };
        return config;
    },
};

export default nextConfig;
