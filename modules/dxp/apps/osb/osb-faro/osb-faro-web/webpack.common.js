const BundleQueryStringPlugin = require('./bundle-query-string-webpack-plugin');
const clayCss = require('@clayui/css');
const crypto = require('crypto');
const fs = require('fs');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const path = require('path');
const webpack = require('webpack');
const {CheckerPlugin} = require('awesome-typescript-loader');

const PUBLIC_PATH = '/o/osb-faro-web/dist/';

function checksum(obj) {
	return crypto
		.createHash('MD5')
		.update(JSON.stringify(obj))
		.digest('hex');
}

function getPathsChecksum() {
	const obj = JSON.parse(
		fs.readFileSync(
			'./src/main/resources/META-INF/resources/countries.geo.json',
			'utf8'
		)
	);
	return checksum(obj);
}

function resolveModule(name = '') {
	return path.resolve(__dirname, 'src', 'main', 'js', name);
}

const include = [resolveModule()];

const config = {
	entry: [
		'core-js/fn/array/fill',
		'core-js/fn/string/code-point-at',
		'custom-event-polyfill',
		'unorm',
		'whatwg-fetch',
		resolveModule('main.jsx')
	],
	module: {
		rules: [
			{
				include,
				loader: 'awesome-typescript-loader',
				resolve: {
					alias: {
						assets: resolveModule('assets'),
						'cerebro-shared': resolveModule('cerebro-shared'),
						'clay-charts-react': resolveModule('clay-charts-react'),
						contacts: resolveModule('contacts'),
						'custom-types': resolveModule('custom-types'),
						experiments: resolveModule('experiments'),
						home: resolveModule('home'),
						'route-middleware': resolveModule('route-middleware'),
						settings: resolveModule('settings'),
						shared: resolveModule('shared'),
						sites: resolveModule('sites'),
						test: resolveModule('test'),
						touchpoints: resolveModule('touchpoints'),
						'ui-kit': resolveModule('ui-kit')
					},
					extensions: ['.js', '.jsx', '.ts', '.tsx']
				},
				test: /\.(js|ts)x?$/
			},
			{
				loader: 'graphql-tag/loader',
				test: /\.graphql$/
			},
			{
				include: path.resolve(__dirname, 'src', 'main', 'css'),
				test: /\.scss$/,
				use: [
					MiniCssExtractPlugin.loader,
					{
						loader: 'css-loader',
						options: {
							importLoaders: 2
						}
					},
					{
						loader: 'postcss-loader',
						options: {
							ident: 'postcss',
							plugins: () => [require('autoprefixer')()]
						}
					},
					{
						loader: 'sass-loader',
						options: {
							includePaths: clayCss.includePaths.concat(
								path.join(clayCss.includePaths[0], '../fonts')
							)
						}
					}
				]
			},
			{
				test: /\.svg$/,
				use: [
					'svg-sprite-loader',
					{
						loader: 'svgo-loader',
						options: {
							plugins: [
								{removeDimensions: true},
								{removeUselessStrokeAndFill: false},
								{removeViewBox: false}
							]
						}
					}
				]
			},
			{
				test: /\.(eot|ttf|woff|woff2)(\?v=\d+\.\d+\.\d+)?$/,
				use: 'file-loader'
			}
		]
	},
	output: {
		filename: 'main.js',
		path: path.resolve('src/main/resources/META-INF/resources/dist'),
		publicPath: PUBLIC_PATH
	},
	plugins: [
		new CheckerPlugin(),
		new MiniCssExtractPlugin({
			filename: 'main.css'
		}),
		new BundleQueryStringPlugin(),
		new webpack.DefinePlugin({
			CEREBRO_PATHS_GEOMAP_KEY: JSON.stringify(getPathsChecksum()),
			FARO_ENV: JSON.stringify(process.env.FARO_ENVIRONMENT_NAME || '')
		}),
		new webpack.IgnorePlugin(/^\.\/locale$/, /moment$/)
	],
	target: 'web'
};

module.exports = {
	config,
	include,
	publicPath: PUBLIC_PATH,
	resolve: {
		extensions: ['', '.js', '.jsx', '.ts', '.tsx'],
		root: [resolveModule()]
	}
};
