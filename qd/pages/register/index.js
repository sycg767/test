// 导入 userApi
import { userApi } from '../../services/api';

Page({
  data: {
    username: '',
    nickname: '',
    password: '',
    confirmPassword: '',
    showPassword: false,
    showConfirmPassword: false,
    errors: {
      username: '',
      nickname: '',
      password: '',
      confirmPassword: ''
    }
  },

  // 输入处理
  onUsernameInput(e) {
    this.setData({ 
      username: e.detail.value,
      'errors.username': ''
    });
  },

  onNicknameInput(e) {
    this.setData({ 
      nickname: e.detail.value,
      'errors.nickname': ''
    });
  },

  onPasswordInput(e) {
    this.setData({ 
      password: e.detail.value,
      'errors.password': ''
    });
  },

  onConfirmPasswordInput(e) {
    this.setData({ 
      confirmPassword: e.detail.value,
      'errors.confirmPassword': ''
    });
  },

  // 表单验证
  validateForm() {
    const { username, nickname, password, confirmPassword } = this.data;
    const errors = {};
    let isValid = true;

    if (!username) {
      errors.username = '请输入用户名';
      isValid = false;
    }

    if (!nickname) {
      errors.nickname = '请输入昵称';
      isValid = false;
    }

    if (!password) {
      errors.password = '请输入密码';
      isValid = false;
    }

    if (!confirmPassword) {
      errors.confirmPassword = '请确认密码';
      isValid = false;
    } else if (password !== confirmPassword) {
      errors.confirmPassword = '两次输入的密码不一致';
      isValid = false;
    }

    this.setData({ errors });
    return isValid;
  },

  // 注册处理
  async handleRegister() {
    if (!this.validateForm()) {
      return;
    }

    const { username, password, nickname } = this.data;
    
    try {
      wx.showLoading({ title: '注册中...' });
      
      // 只进行注册操作
      const registerRes = await userApi.register({
        username,
        password,
        nickname
      });
      
      wx.hideLoading();
      
      if (registerRes.success) {
        // 保存注册信息到全局数据，供登录页面使用
        const app = getApp();
        app.globalData.registerInfo = {
          username,
          password
        };
        
        wx.showToast({
          title: '注册成功',
          icon: 'success',
          duration: 1500,
          success: () => {
            setTimeout(() => {
              // 返回登录页面
              wx.navigateBack({
                delta: 1
              });
            }, 1500);
          }
        });
      } else {
        throw new Error(registerRes.message || '注册失败');
      }
    } catch (error) {
      console.error('注册失败:', error);
      wx.hideLoading();
      wx.showToast({
        title: error.message || '注册失败',
        icon: 'none',
        duration: 2000
      });
    }
  },

  // 返回登录页
  goToLogin() {
    wx.navigateBack();
  },

  // 切换密码可见性
  togglePasswordVisibility() {
    this.setData({
      showPassword: !this.data.showPassword
    });
  },

  // 切换确认密码可见性
  toggleConfirmPasswordVisibility() {
    this.setData({
      showConfirmPassword: !this.data.showConfirmPassword
    });
  }
}); 