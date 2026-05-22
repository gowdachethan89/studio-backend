
package Photo_Studio.studio.repository;
import Photo_Studio.studio.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AlbumRepository extends JpaRepository<Album, Long>{
    Page<Album> findByCustomerId(Long userId, Pageable pageable);
}
