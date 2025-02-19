package com.bitrosh.backend.dao.repository;

import java.util.List;

import com.bitrosh.backend.dao.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("select c from Card c where c.boardColumn.id = :boardColumnId")
    List<Card> findByBoardColumnId(Long boardColumnId);

    @Query("select c from Card c where c.boardColumn.workspace.name = :workspaceName")
    List<Card> findByWorkspaceName(String workspaceName);

}
