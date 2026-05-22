package Photo_Studio.studio.service;

import Photo_Studio.studio.entity.Album;
import Photo_Studio.studio.entity.FileEntity;
import Photo_Studio.studio.entity.User;
import Photo_Studio.studio.model.AlbumRequestDTO;
import Photo_Studio.studio.model.AlbumResponseDTO;
import Photo_Studio.studio.model.PhotoDTO;
import Photo_Studio.studio.repository.AlbumRepository;
import Photo_Studio.studio.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {

    private final AlbumRepository repo;
    private final UserRepository userRepository;

    public AlbumService(AlbumRepository repo, UserRepository userRepository) {
        this.repo = repo;
        this.userRepository = userRepository;
    }

    public Album create(AlbumRequestDTO albumRequest) {
        Album album = new Album();
        Optional<User> user = userRepository.findById(albumRequest.getCustomerId());
        album.setCustomer(user.get());
        album.setName(albumRequest.getName());
        album.setEventDate(LocalDate.now());

        return repo.save(album);
    }

    public Page<AlbumResponseDTO> getAlbumsByUserId(Long userId, Pageable pageable) {

        Page<Album> page = repo.findByCustomerId(userId, pageable);

        return page.map(album -> new AlbumResponseDTO(
                album.getId(),
                album.getName(),
                album.getEventDate(),
                album.getPhotos().stream().map(file ->
                        new PhotoDTO(
                                file.getId(),
                                file.getFileName(),
                                "/files/api/files/" + file.getFileName()
                        )
                ).toList()
        ));
    }

    public AlbumResponseDTO getAlbumById(Long albumId) {

        Album album = repo.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));

        return new AlbumResponseDTO(
                album.getId(),
                album.getName(),
                album.getEventDate(),
                album.getPhotos().stream().map(file ->
                        new PhotoDTO(
                                file.getId(),
                                file.getFileName(),
                                "/files/api/files/" + file.getFileName()
                        )
                ).toList()
        );
    }

    public List<Album> getAll() {
        return repo.findAll();
    }

    public List<String> getPhotoPaths(Long albumId) {
        Album album = repo.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));

        return album.getPhotos().stream().map(FileEntity::getFilePath).toList();

    }
}