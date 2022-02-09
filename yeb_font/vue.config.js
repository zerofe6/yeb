// const proxyObj = {}
// proxyObj['/'] = {
//   // websocket
//   ws: false,
//   target: 'http://localhost:8081',
//   // 发送请求target会被设置为target
//   changeOrigin: true,
//   // 不重写请求地址
//   pathRewrite: {
//     '^/': '/'
//   }
// }
// module.exports = {
//   devServer: {
//     host: 'localhost',
//     prot: 8080,
//     proxy: proxyObj
//   }
// }
module.exports = {
  // pabulicPath:process.env.NODE_ENV === 'production' ? '' : '',
  devServer: {
    host: '127.0.0.1',
    port: '8080',
    // https:false,
    // open: true,
    // 以上的ip和端口是我们本机的;下面为需要跨域的
    proxy: { // 配置跨域
      '/': {
        target: 'http://localhost:8081',
        ws: false,
        changeOrigin: true, // 允许跨域
        Headers: '',
        pathRewrie: {
          '^/': '/' // 请求的时候使用这个/就可以替代http://localhost:8081/
        }
      }
    }
  }
}
