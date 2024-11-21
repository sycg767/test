// pages/settings/index.js
const app = getApp()
import { settingsAPI } from '../../services/api.js';

Page({
  data: {
    userInfo: null,
    settings: {
      soundEnabled: true,
      vibrationEnabled: true,
      defaultQuestionCount: 10,
      dailyReminder: false,
      reminderTime: '20:00'
    },
    questionCountOptions: [5, 10, 15, 20, 25, 30],
    cacheSize: '0KB',
    version: '1.0.0'
  },

  onLoad() {
    this.loadUserInfo()
    this.loadSettings()
    this.calculateCacheSize()
  },

  // 加载用户信息
  loadUserInfo() {
    const userInfo = app.globalData.userInfo
    this.setData({ userInfo })
  },

  // 加载设置
  async loadSettings() {
    try {
      const settings = await settingsAPI.getSettings();
      this.setData({ settings });
      wx.setStorageSync('settings', settings);
    } catch (error) {
      console.error('获取设置失败:', error);
      const settings = wx.getStorageSync('settings') || this.data.settings;
      this.setData({ settings });
    }
  },

  // 计算缓存大小
  async calculateCacheSize() {
    try {
      const res = await wx.getStorageInfo()
      const size = (res.currentSize / 1024).toFixed(2)
      this.setData({
        cacheSize: size + 'KB'
      })
    } catch (error) {
      console.error('获取缓存信息失败:', error)
    }
  },

  // 切换音效
  toggleSound(e) {
    const { settings } = this.data
    settings.soundEnabled = e.detail.value
    this.updateSettings(settings)
  },

  // 切换震动
  toggleVibration(e) {
    const { settings } = this.data
    settings.vibrationEnabled = e.detail.value
    this.updateSettings(settings)
  },

  // 修改默认题目数量
  onQuestionCountChange(e) {
    const { settings } = this.data
    settings.defaultQuestionCount = this.data.questionCountOptions[e.detail.value]
    this.updateSettings(settings)
  },

  // 切换每日提醒
  toggleDailyReminder(e) {
    const { settings } = this.data
    settings.dailyReminder = e.detail.value
    this.updateSettings(settings)
    
    if (e.detail.value) {
      this.requestNotificationPermission()
    }
  },

  // 修改提醒时间
  onReminderTimeChange(e) {
    const { settings } = this.data
    settings.reminderTime = e.detail.value
    this.updateSettings(settings)
    
    if (settings.dailyReminder) {
      this.updateReminder()
    }
  },

  // 更新设置
  async updateSettings(settings) {
    this.setData({ settings });
    wx.setStorageSync('settings', settings);
    try {
      await settingsAPI.updateSettings(settings);
    } catch (error) {
      console.error('同步设置失败:', error);
    }
  },

  // 请求通知权限
  async requestNotificationPermission() {
    try {
      const res = await wx.requestSubscribeMessage({
        tmplIds: ['Kw2_V_tBgLAEqbFoiYQVQWvbK6x8fqEGLRHAJ5Vj2Zk'] // 每日学习提醒模板ID
      })
      if (res['Kw2_V_tBgLAEqbFoiYQVQWvbK6x8fqEGLRHAJ5Vj2Zk'] === 'accept') {
        this.updateReminder()
      } else {
        this.setData({
          'settings.dailyReminder': false
        })
        wx.showToast({
          title: '需要通知权限才能开启提醒',
          icon: 'none'
        })
      }
    } catch (error) {
      console.error('请求通知权限失败:', error)
    }
  },

  // 更新提醒设置
  async updateReminder() {
    const { settings } = this.data;
    try {
      await settingsAPI.updateReminder({
        enabled: settings.dailyReminder,
        time: settings.reminderTime
      });
    } catch (error) {
      console.error('更新提醒设置失败:', error);
    }
  },

  // 清除缓存
  async clearCache() {
    try {
      await wx.showModal({
        title: '提示',
        content: '确定要清除缓存吗？'
      })
      await wx.clearStorage()
      this.calculateCacheSize()
      wx.showToast({
        title: '清除成功',
        icon: 'success'
      })
    } catch (error) {
      console.error('清除缓存失败:', error)
    }
  },

  // 页面跳转
  goToProfile() {
    wx.navigateTo({
      url: '/pages/profile/edit'
    })
  },

  goToPassword() {
    wx.navigateTo({
      url: '/pages/profile/password'
    })
  },

  goToAbout() {
    wx.navigateTo({
      url: '/pages/about/index'
    })
  },

  goToPhone() {
    wx.navigateTo({
      url: '/pages/profile/phone'
    })
  }
})