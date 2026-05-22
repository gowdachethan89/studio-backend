package Photo_Studio.studio.controller;

import Photo_Studio.studio.entity.Album;
import Photo_Studio.studio.model.AlbumRequestDTO;
import Photo_Studio.studio.model.AlbumResponseDTO;
import Photo_Studio.studio.service.AlbumService;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/admin/albums")
public class AlbumController {

    private final AlbumService service;

    public AlbumController(AlbumService service) {
        this.service = service;
    }

    @PostMapping
    public Album create(@RequestBody AlbumRequestDTO album) {
        return service.create(album);
    }

    @GetMapping
    public List<Album> getAll() {
        return service.getAll();
    }


    @GetMapping("/user/{userId}")
    public Page<AlbumResponseDTO> getAlbumsByUser(
            @PathVariable Long userId,
            Pageable pageable) {
        return service.getAlbumsByUserId(userId, pageable);
    }

    @GetMapping("/{albumId}")
    public AlbumResponseDTO getAlbumById(@PathVariable Long albumId) {
        return service.getAlbumById(albumId);
    }

    @GetMapping("/{albumId}/download")
    public void downloadAlbum(@PathVariable Long albumId, HttpServletResponse response) throws IOException, IOException {

        List<String> filePaths = service.getPhotoPaths(albumId);

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=album-" + albumId + ".zip");

        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());

        for (String path : filePaths) {
            File file = new File(path);

            FileInputStream fis = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zipOut.write(buffer, 0, len);
            }

            fis.close();
            zipOut.closeEntry();
        }

        zipOut.close();
    }
}
