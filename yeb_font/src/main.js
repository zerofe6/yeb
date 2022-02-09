import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import store from './store'
import 'font-awesome/css/font-awesome.css'
// eslint-disable-next-line no-unused-vars
import { getRequest, postRequest, putRequest, deleteRequest } from './utils/api'
import { initMenu } from './utils/menus'
Vue.config.productionTip = false
Vue.use(ElementUI)

Vue.prototype.postRequest = postRequest
Vue.prototype.getRequest = getRequest
Vue.prototype.putRequest = putRequest
Vue.prototype.deleteRequest = deleteRequest

// 路由导航守卫
router.beforeEach((to, from, next) => {
  // 存在token 该用户已经登录初始化路由
  console.log('路由导航守卫from：' + from.path)
  console.log('路由导航守卫to：' + to.path)
  if (window.sessionStorage.getItem('tokenStr')) {
    initMenu(router, store)
    if (!window.sessionStorage.getItem('user')) {
      // 不存在用户信息 获取用户信息 并存入session中
      return getRequest('/admin/info').then(resp => {
        if (resp) {
          window.sessionStorage.setItem('user', JSON.stringify(resp))
          // next()
          next(to.path)
        }
      })
    }
    next()
  } else {
    if (to.path === '/') {
      next()
    } else {
      next('/?redirect=' + to.path)
    }
  }
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
