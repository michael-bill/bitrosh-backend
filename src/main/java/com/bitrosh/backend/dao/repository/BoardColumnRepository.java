package com.bitrosh.backend.dao.repository;

import java.util.List;

import com.bitrosh.backend.dao.entity.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
    @Query("select bc from BoardColumn bc where bc.workspace.name = :workspaceName")
    List<BoardColumn> findByWorkspaceName(String workspaceName);
}
