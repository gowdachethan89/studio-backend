package Photo_Studio.studio.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AlbumResponseDTO {
    private Long id;
    private String name;
    private LocalDate eventDate;
    private List<PhotoDTO> photos;

    public AlbumResponseDTO(Long id, String name, LocalDate eventDate, List<PhotoDTO> photos) {
        this.id = id;
        this.name = name;
        this.eventDate = eventDate;
        this.photos = photos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public List<PhotoDTO> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoDTO> photos) {
        this.photos = photos;
    }
}
