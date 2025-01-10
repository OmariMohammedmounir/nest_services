package net.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import net.springboot.model.Files;



@Repository
public interface FilesRepository extends JpaRepository<Files, Long>{

}

