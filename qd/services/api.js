const BASE_URL = 'http://localhost:8080';

// 封装请求方法
const request = (url, options = {}) => {
  // 确保url是字符串
  if (typeof url !== 'string') {
    console.error('Invalid URL:', url);
    return Promise.reject(new Error('Invalid URL'));
  }

  const requestUrl = `${BASE_URL}${url}`;
  console.log('Request URL:', requestUrl); // 添加日志
  
  return new Promise((resolve, reject) => {
    wx.request({
      url: requestUrl,
      method: options.method || 'GET',
      data: options.data,
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
        console.error('Request failed:', error, 'URL:', requestUrl, 'Options:', options);
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
  getQuestionBanks: (params = {}) => {
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
    return request(`/api/v1/study-records/stats?userId=${userId}`);
  },
  
  // 获取题库练习记录
  getBankRecords: (userId, bankId, params = {}) => {
    const queryString = `userId=${userId}${bankId ? `&bankId=${bankId}` : ''}&page=${params.page || 0}&size=${params.size || 10}`;
    return request(`/api/v1/study-records?${queryString}`);
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

export const studyRecordAPI = {
  // 创建学习记录
  createStudyRecord: (data) => {
    console.log('Creating study record with data:', data); // 添加日志
    return request('/api/v1/study-records', {
      method: 'POST',
      data: data
    });
  },

  // 获取学习记录
  getStudyRecords: (userId, bankId, page = 0, size = 10) => {
    const queryString = `userId=${userId}${bankId ? `&bankId=${bankId}` : ''}&page=${page}&size=${size}`;
    return request(`/api/v1/study-records?${queryString}`, {
      method: 'GET'
    });
  },

  // 获取学习统计
  getStudyStats: (userId) => {
    return request(`/api/v1/study-records/stats?userId=${userId}`, {
      method: 'GET'
    });
  }
}; 