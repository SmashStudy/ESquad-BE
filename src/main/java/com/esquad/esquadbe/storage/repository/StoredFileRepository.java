package com.esquad.esquadbe.storage.repository;

import com.esquad.esquadbe.storage.entity.StoredFile;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface StoredFileRepository extends CrudRepository<StoredFile, Long> {

    Optional<StoredFile> findByFileInfo_StoredFileName(String storedFileName);

    Boolean existsByFileInfo_StoredFileName(String storedFileName);
}
