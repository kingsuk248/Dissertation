var path = require('path');
var webpack = require('webpack');
module.exports = {
    entry: ['./src/main.js'],
    resolve: {
        extensions: ['.webpack.js', '.web.js', '.ts', '.js', '.scss']
    },
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'bundle.js'
    },
    module: {
        rules: [
            { test: /.ts$/, loader: 'babel-loader' },
            { test: /\.(s*)css$/, use: ['style-loader', 'css-loader', 'sass-loader'] }
        ]
    },
    devServer: {
        port: 3600
    }
};