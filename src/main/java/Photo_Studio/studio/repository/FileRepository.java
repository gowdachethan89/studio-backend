
package Photo_Studio.studio.repository;

import Photo_Studio.studio.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long>{
 List<FileEntity> findByAlbumId(Long albumId);
}
