import axios from 'axios'
import {
  Message
} from 'element-ui'
import router from '../router/index.js'

// 请求拦截器，对请求加上获得的token
axios.interceptors.request.use(config => {
  // 请求存在token请求携带token
  if (window.sessionStorage.getItem('tokenStr')) {
    config.headers.Authorization = window.sessionStorage.getItem('tokenStr')
  }
  return config
}, error => {
  console.log(error)
})

// 拦截器 对请求失败进行统一的处理
axios.interceptors.response.use(success => {
  // 业务逻辑错误
  if (success.status && success.status === 200) {
    if (success.data.code === 500 || success.data.code === 401 || success.data.code === 403) {
      Message.error({
        message: success.data.message
      })
      return
    }
    if (success.data.message) {
      Message.success({
        // eslint-disable-next-line no-undef
        message: success.data.message
      })
    }
  }
  return success.data
}, error => {
  if (error.response.code === 504 || error.response.code === 404) {
    Message.error({
      message: '服务器宕机？出错了嗷！'
    })
  } else if (error.response.code === 403) {
    Message.error({
      message: '权限不足，请联系管理员'
    })
  } else if (error.response.code === 401) {
    Message.error({
      message: '尚未登录，请登录'
    })
    // 跳转到登录页面
    router.replace('/')
  } else {
    if (error.response.data.message) {
      Message.error({
        message: error.response.data.code
      })
    }
  }
  // eslint-disable-next-line no-useless-return
  return
}
)

// eslint-disable-next-line no-unused-vars
const base = ''
// 传送json格式的get请求
export const getRequest = (url, params) => {
  return axios({
    method: 'get',
    // eslint-disable-next-line no-template-curly-in-string
    url: base + '' + url,
    data: params
  })
}
// 传送json格式的post请求
export const postRequest = (url, params) => {
  return axios({
    method: 'post',
    // eslint-disable-next-line no-template-curly-in-string
    url: base + '' + url,
    data: params
  })
}
// 传送json格式的put请求
export const putRequest = (url, params) => {
  return axios({
    method: 'put',
    // eslint-disable-next-line no-template-curly-in-string
    url: base + '' + url,
    data: params
  })
}// 传送json格式的put请求
export const deleteRequest = (url, params) => {
  return axios({
    method: 'delete',
    // eslint-disable-next-line no-template-curly-in-string
    url: base + '' + url,
    data: params
  })
}
