package com.esquad.esquadbe.chat.repository;

import com.esquad.esquadbe.chat.entity.S3FileEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface S3FileRepository extends CrudRepository<S3FileEntity, String> {
    Optional <S3FileEntity> findByFileName(String fileName);
}
