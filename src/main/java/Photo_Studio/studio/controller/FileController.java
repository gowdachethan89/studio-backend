package Photo_Studio.studio.controller;

import Photo_Studio.studio.entity.Album;
import Photo_Studio.studio.entity.FileEntity;
import Photo_Studio.studio.repository.AlbumRepository;
import Photo_Studio.studio.service.FileServiceImpl;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileServiceImpl fileService;

    private final AlbumRepository albumRepository;

    public FileController(FileServiceImpl fileService, AlbumRepository albumRepository) {
        this.fileService = fileService;
        this.albumRepository = albumRepository;
    }

    @PostMapping
    public FileEntity save(@RequestBody FileEntity file) {
        return fileService.save(file);
    }

    @GetMapping("/{albumId}")
    public List<FileEntity> getFiles(@PathVariable Long albumId) {
        return fileService.getByAlbum(albumId);
    }


    @PostMapping("/uploads/{albumId}")
    public ResponseEntity<?> upload(
            @PathVariable Long albumId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        FileEntity saved = fileService.uploadFile(albumId, file);


        return ResponseEntity.ok(saved.getId());
    }

   /* @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {

        FileEntity file = service.findById(id).orElseThrow();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + file.getFileName())
                .body(file.getData());
    }*/


    @GetMapping("/api/files/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) throws IOException {

        Path path = Paths.get("uploads").resolve(fileName);
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header("Content-Type", Files.probeContentType(path))
                .body(resource);
    }

    @PostMapping("/upload/{albumId}")
    public ResponseEntity<?> uploadMultiple(
            @PathVariable Long albumId,
            @RequestParam("files") MultipartFile[] files
    ) throws IOException {

        List<FileEntity> uploadedFiles = fileService.uploadMultipleFiles(albumId, files);
        return ResponseEntity.ok(Map.of(
                "message", "Files uploaded",
                "count", uploadedFiles.size()
        ));
    }


    @GetMapping("/photos/{id}/downloads")
    public ResponseEntity<Resource> downloadPhotos(@PathVariable Long id) throws IOException {

        FileEntity photo = fileService.getPhoto(id);

        File file = new File(photo.getFilePath());

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource =
                new InputStreamResource(new BufferedInputStream(new FileInputStream(file)));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + photo.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .body(resource);
    }

    @CrossOrigin(
            origins = "http://localhost:4200",
            allowCredentials = "true",
            exposedHeaders = "Content-Disposition"
    )
    @GetMapping("/photos/{id}/download")
    public ResponseEntity<Resource> downloadPhoto(@PathVariable Long id) throws IOException {

        FileEntity photo = fileService.getPhoto(id);
        File file = new File(photo.getFilePath());

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        UrlResource resource = new UrlResource(file.toURI());

        String contentType;
        try {
            contentType = Files.probeContentType(file.toPath());
        } catch (IOException ex) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + photo.getFileName() + "\"")
                .contentLength(file.length())
                .body(resource);
    }
}
