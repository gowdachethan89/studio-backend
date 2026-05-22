
package Photo_Studio.studio.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Album {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private String name;
 private LocalDate eventDate;

 @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
 @JsonManagedReference
 private List<FileEntity> photos;

 @ManyToOne
 private User customer;

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

 public User getCustomer() {
  return customer;
 }

 public void setCustomer(User customer) {
  this.customer = customer;
 }

 public List<FileEntity> getPhotos() {
  return photos;
 }

 public void setPhotos(List<FileEntity> photos) {
  this.photos = photos;
 }
}
