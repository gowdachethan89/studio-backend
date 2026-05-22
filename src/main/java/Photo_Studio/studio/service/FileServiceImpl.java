package Photo_Studio.studio.service;

import Photo_Studio.studio.entity.Album;
import Photo_Studio.studio.entity.FileEntity;
import Photo_Studio.studio.repository.AlbumRepository;
import Photo_Studio.studio.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final FileRepository repo;
    private final AlbumRepository albumRepo;


    public FileServiceImpl(FileRepository repo, AlbumRepository albumRepo) {
        this.repo = repo;
        this.albumRepo = albumRepo;
    }

    public FileEntity save(FileEntity file) {
        return repo.save(file);
    }

    public List<FileEntity> getByAlbum(Long albumId) {
        return repo.findByAlbumId(albumId);
    }


    public FileEntity uploadFile(Long albumId, MultipartFile file) throws IOException {

        Album album = albumRepo.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));

        // Unique file name
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path uploadPath = Paths.get(uploadDir);

        // Create folder if not exists
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);

        // Save file to disk
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Save metadata in DB
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(fileName);
        fileEntity.setFilePath(filePath.toString());
        fileEntity.setContentType(file.getContentType());
        fileEntity.setAlbum(album);

        return repo.save(fileEntity);
    }


    public List<FileEntity> uploadMultipleFiles(Long albumId, MultipartFile[] files) throws IOException {

        Album album = albumRepo.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));

        List<FileEntity> savedFiles = new ArrayList<>();

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        for (MultipartFile file : files) {

            if (file.isEmpty()) continue;

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // Save file to disk
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Save metadata
            FileEntity entity = new FileEntity();
            entity.setFileName(fileName);
            entity.setFilePath(filePath.toString());
            entity.setContentType(file.getContentType());
            entity.setAlbum(album);

            savedFiles.add(repo.save(entity));
        }

        return savedFiles;
    }

    public FileEntity getPhoto(Long id) {

        return repo.findById(id).get();
    }
}
