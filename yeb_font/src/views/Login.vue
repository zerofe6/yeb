<template>
  <div>
   <el-form :rules="loginFromRules"  ref="loginFormRef" :model="loginForm" class="loginContainer"  v-loading="loading"
    element-loading-text="正在登陆..."
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0.8)">
     <h3 class="loginTitle">系统登录</h3>
     <el-form-item prop="userName">
       <el-input type="text" v-model="loginForm.userName" placeholder="请输入用户名"></el-input>
     </el-form-item>
     <el-form-item prop="password">
       <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"></el-input>
     </el-form-item>
     <el-form-item prop="code">
       <el-input style="width: 220px; margin-right: 5px;" type="text" v-model="loginForm.code" placeholder="点击图片更换验证码"></el-input>
       <img :src = "captchaUrl"  alt="图片加载失败" style="vertical-align:middle;"  v-on:click="updateCaptcha" />
     </el-form-item>
     <el-checkbox class="loginRember" v-model="checked">记住我</el-checkbox>
     <el-button type="primary" v-on:click='loginSubmit()'   style="width: 100%; z-index: 100;">登录</el-button>
   </el-form>
  </div>
</template>
<!-- 此处遇到#click点击事件不生效 v-on:click 生效的bug -->

<script>
// eslint-disable-next-line no-unused-vars
export default {
  data () {
    return {
      // 验证码接口,time 保证
      captchaUrl: '/captcha?time=' + new Date(),
      // captchaUrl: require(getRequest('/captcha?time=' + new Date()) + ''),
      // captchaUrl: 'https://img0.baidu.com/it/u=2181760997,1370527330&fm=26&fmt=auto',
      loginForm: {
        userName: 'admin',
        password: '123',
        code: ''
      },
      loading: false,
      checked: true,
      loginFromRules: {
        userName: {
          required: true,
          message: '请输入用户名',
          trigger: 'blur'
        },
        password: {
          required: true,
          message: '请输入密码',
          trigger: 'blur'

        },
        code: {
          required: true,
          message: '请输入验证码',
          trigger: 'blur'
        }
      }
    }
  },
  methods: {
    updateCaptcha () {
      this.captchaUrl = '/captcha?time=' + new Date()
      console.log(this.captchaUrl)
    },
    loginSubmit () {
      this.$refs.loginFormRef.validate((valid) => {
        if (valid) {
          this.loading = true
          this.postRequest('/login', this.loginForm).then(resp => {
            // alert(JSON.stringify(resp))
            if (resp) {
              this.loading = false
              const tokenStr = resp.obj.tokenHead + resp.obj.token
              window.sessionStorage.setItem('tokenStr', tokenStr)
              // 如果输入的地址不是登录地址 则重定向到指定地址
              const path = this.$route.query.redirect
              this.$router.replace((path === '/' || path === undefined) ? '/home' : path)
              // this.$router.replace('/home')
              // this.$message.info('跳转到主页面')
            }
          })
        } else {
          this.$message.error('请输入所有字段')
          return false
        }
      })
    }
  }
}
</script>

<style lang="less">
  .loginContainer{
    border-radius: 15px;
    background-clip: padding-box;
    margin: 188px auto;
    width: 350px;
    padding: 15px 35px 15px 35px;
    background: white;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
  .loginTitle{
    margin: 0px auto 40px auto;
    text-align: center;
  }
  .loginRember{
    text-align: left;
    margin: 0px 0px 15px 0px;
    // background-color: bisque;
  }

</style>
