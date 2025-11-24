package org.example.stamppaw_backend.community.repository;

import org.example.stamppaw_backend.community.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByCommunityIdAndParentIsNull(Long communityId, Pageable pageable);
    List<Comment> findByParentId(Long parentId);

    Long countByCommunityId(Long communityId);
}
