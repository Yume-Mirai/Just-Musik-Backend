package com.example.justspotify.controller;
import com.example.justspotify.model.Song;
import com.example.justspotify.model.User;
import com.example.justspotify.repository.SongRepository;
import com.example.justspotify.repository.UserRepository;
import com.example.justspotify.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Set<Song> getUserFavorites(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow();
        return user.getFavoriteSongs();
    }

    @PostMapping("/{songId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> addToFavorites(Authentication authentication, @PathVariable Long songId) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow();
        Song song = songRepository.findById(songId).orElseThrow();

        user.getFavoriteSongs().add(song);
        userRepository.save(user);

        return ResponseEntity.ok().body("Song added to favorites");
    }

    @DeleteMapping("/{songId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> removeFromFavorites(Authentication authentication, @PathVariable Long songId) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow();
        Song song = songRepository.findById(songId).orElseThrow();

        user.getFavoriteSongs().remove(song);
        userRepository.save(user);

        return ResponseEntity.ok().body("Song removed from favorites");
    }

    @GetMapping("/check/{songId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public boolean checkIfFavorite(Authentication authentication, @PathVariable Long songId) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow();
        Song song = songRepository.findById(songId).orElseThrow();

        return user.getFavoriteSongs().contains(song);
    }
}