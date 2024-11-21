// pages/profile/password.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
    showOldPassword: false,
    showNewPassword: false,
    showConfirmPassword: false,
    loading: false,
    errors: {}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },

  // 输入处理
  onOldPasswordInput(e) {
    this.setData({
      oldPassword: e.detail.value,
      'errors.oldPassword': ''
    });
  },

  onNewPasswordInput(e) {
    this.setData({
      newPassword: e.detail.value,
      'errors.newPassword': ''
    });
  },

  onConfirmPasswordInput(e) {
    this.setData({
      confirmPassword: e.detail.value,
      'errors.confirmPassword': ''
    });
  },

  // 切换密码可见性
  toggleOldPasswordVisibility() {
    this.setData({
      showOldPassword: !this.data.showOldPassword
    });
  },

  toggleNewPasswordVisibility() {
    this.setData({
      showNewPassword: !this.data.showNewPassword
    });
  },

  toggleConfirmPasswordVisibility() {
    this.setData({
      showConfirmPassword: !this.data.showConfirmPassword
    });
  },

  // 表单验证
  validateForm() {
    const { oldPassword, newPassword, confirmPassword } = this.data;
    const errors = {};
    let isValid = true;

    if (!oldPassword) {
      errors.oldPassword = '请输入当前密码';
      isValid = false;
    }

    if (!newPassword) {
      errors.newPassword = '请输入新密码';
      isValid = false;
    } else if (newPassword.length < 8 || newPassword.length > 20) {
      errors.newPassword = '密码长度需在8-20位之间';
      isValid = false;
    } else if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,20}$/.test(newPassword)) {
      errors.newPassword = '密码必须包含字母和数字';
      isValid = false;
    }

    if (!confirmPassword) {
      errors.confirmPassword = '请确认新密码';
      isValid = false;
    } else if (confirmPassword !== newPassword) {
      errors.confirmPassword = '两次输入的密码不一致';
      isValid = false;
    }

    this.setData({ errors });
    return isValid;
  },

  // 修改密码
  async changePassword() {
    if (!this.validateForm()) {
      return;
    }

    this.setData({ loading: true });

    try {
      // TODO: 调用修改密码API
      await new Promise(resolve => setTimeout(resolve, 1000)); // 模拟API调用

      wx.showToast({
        title: '修改成功',
        icon: 'success',
        duration: 2000
      });

      setTimeout(() => {
        wx.navigateBack();
      }, 2000);

    } catch (error) {
      wx.showToast({
        title: error.message || '修改失败',
        icon: 'none'
      });
    } finally {
      this.setData({ loading: false });
    }
  }
})