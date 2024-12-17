package com.example.question_bank.controller;

import com.example.question_bank.entity.UserCollection;
import com.example.question_bank.service.UserCollectionService;
import com.example.question_bank.exception.BusinessException;
import com.example.question_bank.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/collections")
@Slf4j
public class UserCollectionController {

    @Autowired
    private UserCollectionService collectionService;

    // 检查收藏状态
    @GetMapping("/check")
    public ResponseEntity<?> checkCollection(
        @RequestParam Long userId,
        @RequestParam Long questionId
    ) {
        try {
            boolean isCollected = collectionService.isCollected(userId, questionId);
            return ResponseEntity.ok(Map.of("collected", isCollected));
        } catch (BusinessException e) {
            log.error("检查收藏状态失败", e);
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("检查收藏状态失败", e);
            return ResponseEntity.internalServerError().body(new ErrorResponse("服务器内部错误"));
        }
    }

    // 切换收藏状态
    @PostMapping("/toggle")
    public ResponseEntity<?> toggleCollection(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(String.valueOf(request.get("userId")));
            Long questionId = Long.valueOf(String.valueOf(request.get("questionId")));
            
            boolean isCollected = collectionService.toggleCollection(userId, questionId);
            return ResponseEntity.ok(Map.of(
                "collected", isCollected,
                "message", isCollected ? "收藏成功" : "取消收藏成功"
            ));
        } catch (BusinessException e) {
            log.error("切换收藏状态失败", e);
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("切换收藏状态失败", e);
            return ResponseEntity.internalServerError().body(new ErrorResponse("服务器内部错误"));
        }
    }

    // 获取收藏列表
    @GetMapping
    public ResponseEntity<?> getCollections(
        @RequestParam Long userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        try {
            List<UserCollection> collections = collectionService.getCollections(
                userId,
                PageRequest.of(page, size)
            );
            
            // Create a paginated response
            Map<String, Object> response = new HashMap<>();
            response.put("content", collections);
            response.put("last", collections.size() < size);
            response.put("totalElements", collections.size());
            response.put("number", page);
            response.put("size", size);
            
            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            log.error("获取收藏列表失败", e);
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("获取收藏列表失败", e);
            return ResponseEntity.internalServerError().body(new ErrorResponse("服务器内部错误"));
        }
    }
} 