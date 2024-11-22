const BASE_URL = 'http://localhost:8080';

// 封装请求方法
const request = (url, options = {}) => {
  return new Promise((resolve, reject) => {
    wx.request({
      url: `${BASE_URL}${url}`,
      ...options,
      header: {
        'Content-Type': 'application/json',
        ...options.header
      },
      success: (res) => {
        if (res.statusCode >= 200 && res.statusCode < 300) {
          resolve(res.data);
        } else {
          reject(new Error(res.data?.message || '请求失败'));
        }
      },
      fail: (error) => {
        reject(error);
      }
    });
  });
};

// 题库相关API
export const questionBankAPI = {
  // 获取分类列表
  getCategories: () => {
    return request('/api/bank-categories');
  },
  
  // 获取题库列表
  getQuestionBanks: (params) => {
    return request('/api/banks', {
      method: 'GET',
      data: params
    });
  },
  
  // 获取题库详情
  getQuestionBank: (id) => {
    return request(`/api/banks/${id}`, {
      method: 'GET'
    });
  }
};

// 练习相关API
export const questionAPI = {
  // 获取练习题目
  getPracticeQuestions: (params) => {
    const queryString = Object.entries(params)
      .filter(([_, value]) => value !== undefined && value !== null)
      .map(([key, value]) => `${key}=${value}`)
      .join('&');
    return request(`/api/v1/practice/questions?${queryString}`);
  },
  
  // 提交答案
  submitAnswer: (data) => {
    return request('/api/v1/practice/submit', {
      method: 'POST',
      data
    });
  },
  
  // 检查收藏状态
  checkCollectionStatus: (questionId) => {
    return request(`/questions/${questionId}/collection/status`);
  },
  
  // 切换收藏状态
  toggleCollection: (questionId) => {
    return request(`/questions/${questionId}/collection/toggle`, {
      method: 'POST'
    });
  },
  
  // 获取错题列表
  getWrongQuestions: (userId, params) => {
    const queryString = Object.entries(params)
      .map(([key, value]) => `${key}=${value}`)
      .join('&');
    return request(`/api/v1/wrong-questions?userId=${userId}&${queryString}`);
  },
  
  // 获取收藏题目列表
  getCollectedQuestions: () => {
    return request('/questions/collected');
  },
  
  // 获取题目详情
  getQuestion: (id) => {
    return request(`/questions/${id}`);
  },
  
  // 获取错题统计
  getWrongStatistics: (userId) => {
    return request(`/api/v1/wrong-questions/statistics?userId=${userId}`);
  },
  
  // 根据ID列表获取题目
  getQuestionsByIds: (questionIds) => {
    return request(`/api/v1/questions/batch?ids=${questionIds}`);
  },
  
  // 获取单个题目
  getQuestion: (id) => {
    return request(`/api/v1/questions/${id}`);
  }
};

// 用户答题相关API
export const userAnswerAPI = {
  // 获取答题统计
  getStatistics: (userId) => {
    return request('/user-answers/statistics', {
      method: 'GET',
      data: { userId }
    });
  }
};

// 用户相关API
export const userApi = {
  // 注册
  register: (data) => {
    return request('/api/v1/users/register', {
      method: 'POST',
      data
    });
  },
  
  // 账号密码登录
  login: (data) => {
    return request('/api/v1/users/account/login', {
      method: 'POST',
      data
    });
  },
  
  // 微信登录
  wechatLogin: (code) => {
    return request('/api/v1/users/wechat/login', {
      method: 'POST',
      data: { code }
    });
  },
  
  // 获取用户统计数据
  getUserStats: (userId) => {
    return request(`/api/v1/users/${userId}/stats`);
  }
};

// 收藏相关API
export const collectionAPI = {
  // 检查收藏状态
  checkCollection: (userId, questionId) => {
    return request(`/api/v1/collections/check?userId=${userId}&questionId=${questionId}`);
  },
  
  // 切换收藏状态
  toggleCollection: (userId, questionId) => {
    return request(`/api/v1/collections/toggle`, {
      method: 'POST',
      data: { userId, questionId }
    });
  },
  
  // 获取收藏列表
  getCollections: (userId, page = 0, size = 10) => {
    return request(`/api/v1/collections?userId=${userId}&page=${page}&size=${size}`);
  }
};

// 用户设置相关API
export const settingsAPI = {
  // 获取用户设置
  getSettings: () => {
    return request('/api/v1/user/settings');
  },
  
  // 更新用户设置
  updateSettings: (settings) => {
    return request('/api/v1/user/settings', {
      method: 'PUT',
      data: settings
    });
  },
  
  // 更新提醒设置
  updateReminder: (reminderSettings) => {
    return request('/api/v1/user/reminder', {
      method: 'PUT',
      data: reminderSettings
    });
  }
}; 