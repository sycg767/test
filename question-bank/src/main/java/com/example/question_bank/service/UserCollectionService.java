package com.example.question_bank.service;

import com.example.question_bank.entity.Question;
import com.example.question_bank.entity.User;
import com.example.question_bank.entity.UserCollection;
import com.example.question_bank.repository.UserCollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserCollectionService {

    @Autowired
    private UserCollectionRepository collectionRepository;

    // 检查是否已收藏
    public boolean isCollected(Long userId, Long questionId) {
        return collectionRepository.existsByUserIdAndQuestionId(userId, questionId);
    }

    // 切换收藏状态
    @Transactional
    public boolean toggleCollection(Long userId, Long questionId) {
        if (isCollected(userId, questionId)) {
            // 如果已收藏，则取消收藏
            collectionRepository.deleteByUserIdAndQuestionId(userId, questionId);
            return false;
        } else {
            // 如果未收藏，则添加收藏
            UserCollection collection = new UserCollection();
            
            User user = new User();
            user.setId(userId);
            collection.setUser(user);
            
            Question question = new Question();
            question.setId(questionId);
            collection.setQuestion(question);
            
            collection.setCreatedAt(LocalDateTime.now());
            
            collectionRepository.save(collection);
            return true;
        }
    }

    // 获取收藏列表
    public Page<UserCollection> getCollections(Long userId, Pageable pageable) {
        return collectionRepository.findByUserId(userId, pageable);
    }
} 