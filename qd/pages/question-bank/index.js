// pages/question-bank/index.js
import { questionBankAPI } from '../../services/api.js';
import { checkLogin } from '../../utils/auth.js';

Page({

  /**
   * 页面的初始数据
   */
  data: {
    categories: [],
    selectedCategory: null,
    keyword: '',
    banks: [],
    loading: false,
    page: 1,
    size: 10,
    hasMore: true,
    searchTimer: null  // 添加搜索防抖定时器
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad() {
    console.log('题库页面加载');
    this.loadCategories();
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
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({
        selected: 1  // 题库页的索引为1
      })
    }
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
    this.setData({
      banks: [],
      page: 1,
      hasMore: true
    }, async () => {
      await this.loadQuestionBanks();
      wx.stopPullDownRefresh();
    });
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {
    if (!this.data.loading && this.data.hasMore) {
      this.loadQuestionBanks();
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },

  // 加载分类
  async loadCategories() {
    try {
      wx.showLoading({ title: '加载中...' });
      // 使用封装的 API 方法
      const categories = await questionBankAPI.getCategories();
      
      console.log('分类数据:', categories);
      
      this.setData({ 
        categories,
        loading: false
      }, () => {
        console.log('分类数据已设置:', this.data.categories);
        // 加载默认分类的题库
        this.loadQuestionBanks();
      });
    } catch (error) {
      console.error('获取分类失败:', error);
      wx.showToast({
        title: '获取分类失败',
        icon: 'none'
      });
    } finally {
      wx.hideLoading();
    }
  },

  // 搜索题库
  onSearch(e) {
    const keyword = e.detail.value.trim();
    
    // 清除之前的定时器
    if (this.data.searchTimer) {
      clearTimeout(this.data.searchTimer);
    }
    
    // 设置新的定时器（防抖）
    const timer = setTimeout(() => {
      this.setData({
        keyword: keyword,
        banks: [],  // 清空当前列表
        page: 1,    // 重置页码
        hasMore: true
      }, () => {
        // 显示加载提示
        wx.showLoading({ title: '搜索中...' });
        
        // 构建搜索参数，搜索时不传categoryId
        const params = {
          page: 0,
          size: this.data.size,
          keyword: keyword
        };
        
        // 直接调用API进行搜索
        questionBankAPI.getQuestionBanks(params).then((result) => {
          wx.hideLoading();
          
          if (result && result.content && result.content.length > 0) {
            // 找到搜索结果后，自动切换到对应分类
            const firstBank = result.content[0];
            if (firstBank.category) {
              this.setData({
                selectedCategory: firstBank.category.id,
                banks: result.content,
                hasMore: !result.last,
                page: result.last ? 1 : 2
              });
            }
            
            // 显示搜索结果数量
            wx.showToast({
              title: `找到 ${result.totalElements} 个题库`,
              icon: 'none',
              duration: 2000
            });
          } else {
            this.setData({
              banks: [],
              hasMore: false
            });
            
            wx.showToast({
              title: '未找到相关题库',
              icon: 'none',
              duration: 2000
            });
          }
        }).catch((error) => {
          wx.hideLoading();
          console.error('搜索失败:', error);
          wx.showToast({
            title: '搜索失败',
            icon: 'none',
            duration: 2000
          });
        });
      });
    }, 500);
    
    this.setData({ searchTimer: timer });
  },

  // 清空搜索
  clearSearch() {
    this.setData({
      keyword: '',
      banks: [],
      page: 1,
      hasMore: true
    }, () => {
      // 使用当前选中的分类重新加载题库
      this.loadQuestionBanks();
    });
  },

  // 选择分类
  onCategorySelect(e) {
    const { id } = e.currentTarget.dataset;
    this.setData({
      selectedCategory: id,
      banks: [],
      page: 1,
      hasMore: true
    }, () => {
      this.loadQuestionBanks();
    });
  },

  // 加载题库列表
  async loadQuestionBanks() {
    if (this.data.loading || !this.data.hasMore) return;
    
    try {
      this.setData({ loading: true });
      
      const { page, size, selectedCategory, keyword } = this.data;
      const params = {
        page: page - 1,
        size,
        // 只有当 selectedCategory 有值时才传入
        ...(selectedCategory && { categoryId: selectedCategory }),
        ...(keyword && { keyword: keyword.trim() })
      };
      
      console.log('请求题库参数:', params);
      
      const result = await questionBankAPI.getQuestionBanks(params);
      
      console.log('题库数据:', result);
      
      if (result && result.content) {
        this.setData({
          banks: page === 1 ? result.content : [...this.data.banks, ...result.content],
          hasMore: !result.last,
          page: result.last ? page : page + 1,
          loading: false
        });
      }
    } catch (error) {
      console.error('加载题库失败:', error);
      wx.showToast({
        title: '加载题库失败',
        icon: 'none'
      });
    } finally {
      wx.hideLoading();
      this.setData({ loading: false });
    }
  },

  // 跳转到题库详情
  goToDetail(e) {
    if (!checkLogin()) {
      return;
    }
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/question-bank/detail?id=${id}`
    });
  }
})