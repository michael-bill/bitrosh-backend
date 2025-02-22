package com.bitrosh.backend.dao.repository;

import java.util.List;

import com.bitrosh.backend.dao.entity.CardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardCommentRepository extends JpaRepository<CardComment, Long> {
    List<CardComment> findByCardId(Long cardId);
}
