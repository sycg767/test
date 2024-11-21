Page({
  data: {
    phone: '',
    verifyCode: '',
    countdown: 0,
    canSendCode: true,
    loading: false
  },

  // 手机号输入
  onPhoneInput(e) {
    const phone = e.detail.value;
    this.setData({
      phone,
      canSendCode: this.validatePhone(phone)
    });
  },

  // 验证码输入
  onVerifyCodeInput(e) {
    this.setData({
      verifyCode: e.detail.value
    });
  },

  // 验证手机号
  validatePhone(phone) {
    return /^1[3-9]\d{9}$/.test(phone);
  },

  // 发送验证码
  async sendVerifyCode() {
    if (!this.data.canSendCode || this.data.countdown > 0) {
      return;
    }

    const { phone } = this.data;
    if (!this.validatePhone(phone)) {
      wx.showToast({
        title: '请输入正确的手机号',
        icon: 'none'
      });
      return;
    }

    try {
      // TODO: 调用发送验证码API
      await new Promise(resolve => setTimeout(resolve, 1000)); // 模拟API调用

      // 开始倒计时
      this.setData({
        countdown: 60,
        canSendCode: false
      });

      this.startCountdown();

      wx.showToast({
        title: '验证码已发送',
        icon: 'success'
      });
    } catch (error) {
      wx.showToast({
        title: error.message || '发送失败',
        icon: 'none'
      });
    }
  },

  // 倒计时
  startCountdown() {
    const timer = setInterval(() => {
      if (this.data.countdown <= 1) {
        clearInterval(timer);
        this.setData({
          countdown: 0,
          canSendCode: this.validatePhone(this.data.phone)
        });
      } else {
        this.setData({
          countdown: this.data.countdown - 1
        });
      }
    }, 1000);
  },

  // 绑定手机号
  async bindPhone() {
    const { phone, verifyCode } = this.data;

    if (!this.validatePhone(phone)) {
      wx.showToast({
        title: '请输入正确的手机号',
        icon: 'none'
      });
      return;
    }

    if (!verifyCode || verifyCode.length !== 6) {
      wx.showToast({
        title: '请输入6位验证码',
        icon: 'none'
      });
      return;
    }

    this.setData({ loading: true });

    try {
      // TODO: 调用绑定手机号API
      await new Promise(resolve => setTimeout(resolve, 1000)); // 模拟API调用

      wx.showToast({
        title: '绑定成功',
        icon: 'success',
        duration: 2000
      });

      // 更新全局用户信息
      const app = getApp();
      app.globalData.userInfo = {
        ...app.globalData.userInfo,
        phone
      };

      setTimeout(() => {
        wx.navigateBack();
      }, 2000);

    } catch (error) {
      wx.showToast({
        title: error.message || '绑定失败',
        icon: 'none'
      });
    } finally {
      this.setData({ loading: false });
    }
  }
}); 