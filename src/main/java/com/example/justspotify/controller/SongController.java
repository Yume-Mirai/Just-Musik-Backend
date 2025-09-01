package com.example.justspotify.controller;
import com.dropbox.core.DbxException;
import com.example.justspotify.model.ERole;
import com.example.justspotify.model.Song;
import com.example.justspotify.model.User;
import com.example.justspotify.repository.UserRepository;
import com.example.justspotify.service.PaymentService;
import com.example.justspotify.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// ... imports ...

@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = "*")
public class SongController {
    
    @Autowired
    private SongService songService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentService paymentService;
    
    @GetMapping
    public List<Song> getAllSongs() {
        return songService.getAllSongs();
    }
    
    @GetMapping("/{id}")
    public Song getSongById(@PathVariable Long id) {
        return songService.getSongById(id);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Song createSong(
            @RequestParam("title") String title,
            @RequestParam("artist") String artist,
            @RequestParam(value = "album", required = false) String album,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam("duration") Integer duration,
            @RequestParam(value = "imageUrl", required = false) String imageUrl,
            @RequestPart("audioFile") MultipartFile audioFile) throws IOException {

        Song song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        song.setAlbum(album);
        song.setGenre(genre);
        song.setDuration(duration);
        song.setImageUrl(imageUrl);

        return songService.createSong(song, audioFile);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Song updateSong(
            @PathVariable Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "artist", required = false) String artist,
            @RequestParam(value = "album", required = false) String album,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "duration", required = false) Integer duration,
            @RequestParam(value = "imageUrl", required = false) String imageUrl,
            @RequestPart(value = "audioFile", required = false) MultipartFile audioFile) throws IOException, DbxException {

        Song songDetails = new Song();
        if (title != null) songDetails.setTitle(title);
        if (artist != null) songDetails.setArtist(artist);
        if (album != null) songDetails.setAlbum(album);
        if (genre != null) songDetails.setGenre(genre);
        if (duration != null) songDetails.setDuration(duration);
        if (imageUrl != null) songDetails.setImageUrl(imageUrl);

        return songService.updateSong(id, songDetails, audioFile);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}/stream")
    public ResponseEntity<String> getStreamUrl(@PathVariable Long id) {
        String streamUrl = songService.getSongStreamUrl(id);
        return ResponseEntity.ok(streamUrl);
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> downloadSong(@PathVariable Long id) {
        try {
            User currentUser = getCurrentUser();

            // Check if user is admin or has paid
            boolean isAdmin = currentUser.getRoles().stream()
                    .anyMatch(role -> role.getName().equals(ERole.ROLE_ADMIN));
            boolean isPaid = paymentService.isUserPaid(currentUser);

            if (!isAdmin && !isPaid) {
                return ResponseEntity.status(403).body("Access denied. Please make a payment to download songs.");
            }

            String downloadUrl = songService.getSongStreamUrl(id);
            return ResponseEntity.ok(Map.of("downloadUrl", downloadUrl));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to get download URL");
        }
    }
    
    @GetMapping("/search")
    public List<Song> searchSongs(@RequestParam String q) {
        return songService.searchSongs(q);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}