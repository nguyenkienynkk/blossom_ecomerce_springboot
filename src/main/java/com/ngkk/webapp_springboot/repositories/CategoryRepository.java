package com.ngkk.webapp_springboot.repositories;

import com.ngkk.webapp_springboot.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
}
