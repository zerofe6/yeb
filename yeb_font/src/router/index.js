import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'
import Test1 from '../views/Test1.vue'
import Test2 from '../views/Test2.vue'
Vue.use(VueRouter)

const routes = [{
  path: '/',
  name: 'Login',
  component: Login,
  hidden: true
}, {
  path: '/home',
  name: 'Home',
  component: Home,
  children: [
    {
      path: '/test1',
      name: 'Test1',
      component: Test1
    },
    {
      path: '/test2',
      name: 'Test2',
      component: Test2
    }
  ]
}]

const router = new VueRouter({
  routes
})

export default router

const originalPush = VueRouter.prototype.push
const originalReplace = VueRouter.prototype.replace

VueRouter.prototype.push = function push (location, onResolve, onReject) {
  if (onResolve || onReject) {
    return originalPush.call(this, location, onResolve, onReject)
  }
  return originalPush.call(this, location).catch(err => err)
}

VueRouter.prototype.replace = function push (location, onResolve, onReject) {
  if (onResolve || onReject) {
    return originalReplace.call(this, location, onResolve, onReject)
  }
  return originalReplace.call(this, location).catch(err => err)
}
