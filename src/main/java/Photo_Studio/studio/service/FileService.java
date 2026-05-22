package Photo_Studio.studio.service;

import Photo_Studio.studio.entity.FileEntity;
import Photo_Studio.studio.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private final FileRepository repo;

    public FileService(FileRepository repo) {
        this.repo = repo;
    }

    public FileEntity save(FileEntity file) {
        return repo.save(file);
    }

    public List<FileEntity> getByAlbum(Long albumId) {
        return repo.findByAlbumId(albumId);
    }

    public Optional<FileEntity> findById(Long id) {
        return repo.findById(id);
    }


}
