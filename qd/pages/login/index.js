import { userApi } from '../../services/api.js';

Page({
  data: {
    username: '',
    password: '',
    loading: false,
    errors: {}
  },

  // 用户名输入处理
  onUsernameInput(e) {
    this.setData({
      username: e.detail.value,
      errors: {
        ...this.data.errors,
        username: ''
      }
    });
  },

  // 密码输入处理
  onPasswordInput(e) {
    this.setData({
      password: e.detail.value,
      errors: {
        ...this.data.errors,
        password: ''
      }
    });
  },

  // 账号密码登录
  async handleAccountLogin() {
    try {
      // 表单验证
      if (!this.validateForm()) {
        return;
      }

      this.setData({ loading: true });

      const { username, password } = this.data;
      const res = await userApi.login({
        username,
        password
      });

      // 保存用户信息
      getApp().globalData.userInfo = res;
      wx.setStorageSync('userInfo', res);

      wx.showToast({
        title: '登录成功',
        icon: 'success',
        duration: 1500,
        success: () => {
          setTimeout(() => {
            wx.navigateBack();
          }, 1500);
        }
      });

    } catch (error) {
      console.error('登录失败:', error);
      wx.showToast({
        title: error.message || '登录失败',
        icon: 'none'
      });
    } finally {
      this.setData({ loading: false });
    }
  },

  // 表单验证
  validateForm() {
    const { username, password } = this.data;
    const errors = {};

    if (!username) {
      errors.username = '请输入用户名';
    }
    if (!password) {
      errors.password = '请输入密码';
    }

    this.setData({ errors });
    return Object.keys(errors).length === 0;
  },

  // 跳转到注册页面
  goToRegister() {
    wx.navigateTo({
      url: '/pages/register/index'
    });
  }
}); 