const CopyWebpackPlugin = require('copy-webpack-plugin');
config.plugins.push(
    new CopyWebpackPlugin({
        patterns: [
            '../../node_modules/bigint_arithmetic/bigint_arithmetic_bg.wasm'
        ]
    })
);
