<template>
  <div>
    <el-container>
      <el-header class="homeHeader">
        <div class="title">云E办系统</div>
        <el-dropdown class="userInfo" @command="commandHandler">
          <span class="el-dropdown-link">
           {{user.name}}<i>
           <img :src = "user.userFace"/>
           <!-- <span>{{user.userFace}}</span> -->
           <!-- <img src = "http://tva1.sinaimg.cn/large/005ujAMXgy1gvcsodg4jmj60500500sn02.jpg"/> -->
           </i>
          </span>
          <el-dropdown-menu slot="dropdown" >
            <el-dropdown-item command="userinfo">个人中心</el-dropdown-item>
            <el-dropdown-item command="setting">系统设置</el-dropdown-item>
            <el-dropdown-item command="logout">注销登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu router unique-opened>
            <el-submenu
              :index="index + ''"
              v-for="(item, index) in routers"
              :key="index"
            >
            <template v-if="item.hidden!=true">
            <!-- <div  v-if="!item.hidden" -->
                <template slot="title">
                  <i :class=item.iconCls style="color:#1accff; magrin-right:5px"></i>
                  <span>  {{item.name}}</span>
                </template>
                <el-menu-item
                  :index="children.path"
                  v-for="(children, indexj) in item.children"
                  :key="indexj"
                  >{{children.name}}</el-menu-item
                >
            </template>
            </el-submenu>
          </el-menu>
        </el-aside>
        <el-main>
          <el-breadcrumb separator-class="el-icon-arrow-right"
           v-if="this.$router.currentRoute.path!='/home'">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{this.$router.currentRoute.name}}</el-breadcrumb-item>
          </el-breadcrumb>
          <div class="homeWelcome" v-if="this.$router.currentRoute.path=='/home'">
            欢迎来到云E办公系统！
          </div>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>

export default {
  name: 'Home',
  computed: {
    routers () {
      // v-for和v-if 不能一起使用（官方不建议一起使用） 再次过滤
      return this.$store.state.routes
      // .filter(function (filed) {
      //   // console.log('获取到的filed为：' + filed)
      //   return !filed.hidden
      // })
    }
  },
  data () {
    return {
      user: JSON.parse(window.sessionStorage.getItem('user'))
    }
  },
  methods: {
    menuClick (index) {
      this.$router.push(index)
    },
    commandHandler (command) {
      if (command === 'logout') {
        this.$confirm('此操作将退出该系统, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // this.$message({
          //   type: 'success'
          //   // message: '注销登录成功!'
          // })
          this.postRequest('/logout ')
          window.sessionStorage.removeItem('tokenStr')
          window.sessionStorage.removeItem('user')
          // 清除菜单信息
          this.$store.commit('initRouters', [])
          this.$router.replace('/')
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消注销登录'
          })
        })
      }
    }
  }
}
</script>

<style lang="less" scoped>
.homeHeader{
  background: #409eff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 15px;
  box-sizing: border-box;
  .userInfo{

  }
}
.homeHeader .title{
  font-size: 30px;
  font-family: 华文楷体;
  color: white;
}
.el-dropdown-link img{
  width: 48px;
  height: 48px;
  border-radius: 24px;
  margin-left: 8px;
}
.homeWelcome{
  text-align: center;
  font-size: 30px;
  font-family: 华文楷体;
  color: #409eff;
  padding-top: 20px;
}
</style>
