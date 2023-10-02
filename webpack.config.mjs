// Node.js의 내장 모듈과 Webpack 패키지를 불러옵니다.
import { resolve, dirname } from "path";
import { fileURLToPath } from "url";
import pkg from "webpack";
const { DefinePlugin } = pkg;

/**
 * __dirname과 __filename을 계산하기 위한 코드
 * ES 모듈에서는 __dirname과 __filename이 지원되지 않으므로, 직접 계산해야 합니다.
 */
const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

// Webpack 설정 객체를 내보냅니다.
export default {
  /**
   * context: 웹팩이 프로젝트를 빌드할 때 기준이 되는 디렉토리입니다.
   * resolve() 함수를 사용해 절대경로를 지정합니다.
   */
  context: resolve(__dirname, "src/main/jsx"),

  resolve: {
    extensions: [".js", ".jsx"],
    alias: {
      components: resolve(__dirname, "src/main/jsx/components/"),
      api: resolve(__dirname, "src/main/jsx/api/")
    },
  },

  /**
   * entry: 번들링을 시작할 초기 진입점입니다.
   */
  entry: {
    home: "./App.jsx",
  },

  /**
   * devtool: 소스 맵을 생성할 방식을 지정합니다. 'source-map'을 사용하면 별도의 소스 맵 파일을 생성합니다.
   */
  devtool: "source-map",

  /**
   * cache: 캐시 사용을 활성화하여 빌드 속도를 향상시킵니다.
   */
  cache: true,

  /**
   * output: 번들링 결과물의 출력 경로와 파일 이름을 지정합니다.
   */
  output: {
    path: resolve(__dirname, "src/main/webapp/js/react"),
    filename: "[name].bundle.js",
  },

  /**
   * mode: 빌드 모드를 설정합니다. 'none'을 지정하여 최적화하지 않고 빌드합니다.
   */
  mode: "none",

  /**
   * module: 프로젝트 내의 다양한 유형의 파일을 처리하는 방법을 정의합니다.
   */
  module: {
    rules: [
      /**
       * .jsx 파일과 .js 파일을 Babel을 사용하여 변환합니다.
       * node_modules 폴더는 제외합니다.
       */
      {
        test: /\.jsx?$/,
        exclude: /(node_modules)/,
        use: {
          loader: "babel-loader",
          options: {
            presets: ["@babel/preset-env", "@babel/preset-react"],
          },
        },
      },

      /**
       * .css 파일을 처리하기 위한 로더를 설정합니다.
       * 'style-loader'와 'css-loader'를 차례로 적용합니다.
       */
      {
        test: /\.css$/,
        use: ["style-loader", "css-loader"],
      },
    ],
  },

  /**
   * plugins: 웹팩 빌드 과정에 추가적인 작업을 수행할 플러그인들을 배열로 설정합니다.
   */
  plugins: [
    new DefinePlugin({
      "process.env": JSON.stringify(process.env),
    }),
  ],
};
