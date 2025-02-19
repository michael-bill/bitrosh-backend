package com.bitrosh.backend.dao.repository;

import java.util.List;

import com.bitrosh.backend.dao.entity.Folder;
import com.bitrosh.backend.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findAllByUser(User user);
}
