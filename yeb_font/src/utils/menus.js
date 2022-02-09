import { getRequest } from './api'

export const initMenu = (router, store) => {
  if (store.state.routes.length > 0) {
    // eslint-disable-next-line no-useless-return
    return
  }
  getRequest('/system/cfg/menu').then(data => {
    if (data) {
    // 格式化routers
      console.log(data)
      // eslint-disable-next-line prefer-const
      let fmtRouters = formatRouters(data)
      // 添加到路由中
      //   fmtRouters = Array.from(fmtRouters)
      console.log('接收到的参数类型为：' + Array.isArray(fmtRouters))
      router.addRoutes(fmtRouters)
      //   将数据存入Vuex
      console.log(fmtRouters)
      store.commit('initRouters', fmtRouters)
    }
  })
}

export const formatRouters = (routes) => {
  const fmRouters = []
  // console.log('接收到的参数类型为：' + Array.isArray(routes))
  routes.forEach(router => {
    let{
      path,
      component,
      name,
      iconCls,
      children
    } = router
    if (children && children instanceof Array) {
      // 递归
      children = formatRouters(children)
    }
    const formatRouter = {
      path: path,
      name: name,
      iconCls: iconCls,
      children: children,
      component (resolve) {
        if (component.startsWith('Home')) {
          require(['../views/' + component + '.vue'], resolve)
        } else if (component.startsWith('Emp')) {
          require(['../views/emp/' + component + '.vue'], resolve)
        } else if (component.startsWith('Per')) {
          require(['../views/per/' + component + '.vue'], resolve)
        } else if (component.startsWith('Sal')) {
          require(['../views/sal/' + component + '.vue'], resolve)
        } else if (component.startsWith('Sta')) {
          require(['../views/sta/' + component + '.vue'], resolve)
        } else if (component.startsWith('Sys')) {
          require(['../views/sys/' + component + '.vue'], resolve)
        }
      }
    }
    fmRouters.push(formatRouter)
  })
  return fmRouters
}
