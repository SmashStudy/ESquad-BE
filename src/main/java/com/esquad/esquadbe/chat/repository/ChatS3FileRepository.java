 package com.esquad.esquadbe.chat.repository;

 import com.esquad.esquadbe.chat.entity.ChatS3FileEntity;
 import org.springframework.data.repository.CrudRepository;

 import java.util.Optional;

 public interface ChatS3FileRepository extends CrudRepository<ChatS3FileEntity, String> {
     Optional <ChatS3FileEntity> findByFileName(String fileName);
 }
