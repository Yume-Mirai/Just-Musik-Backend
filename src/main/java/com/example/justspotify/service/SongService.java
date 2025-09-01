package com.example.justspotify.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dropbox.core.DbxException;
import com.example.justspotify.model.Song;
import com.example.justspotify.repository.SongRepository;

import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;
import java.util.List;

@Service
public class SongService {
     private static final Logger logger = LoggerFactory.getLogger(SongService.class);
    
    @Autowired
    private SongRepository songRepository;
    
    @Autowired
    private DropboxService dropboxService;
    
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }
    
    public Song getSongById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Song not found with id: " + id));
    }
    
    public Song createSong(Song song, MultipartFile audioFile) throws IOException {
        logger.info("Creating song: {}", song.getTitle());
        logger.info("Audio file: {} ({} bytes)", 
                   audioFile.getOriginalFilename(), 
                   audioFile.getSize());
        
        try {
            String filePath = dropboxService.uploadFile(audioFile, audioFile.getOriginalFilename());
            song.setFilePath(filePath);
            Song savedSong = songRepository.save(song);
            logger.info("Song created successfully with ID: {}", savedSong.getId());
            return savedSong;
        } catch (Exception e) {
            logger.error("Failed to create song: {}", e.getMessage(), e);
            throw new IOException("Failed to upload audio file", e);
        }
    }
    
    public Song updateSong(Long id, Song songDetails, MultipartFile audioFile) throws IOException, DbxException {
        Song song = getSongById(id);

        if (songDetails.getTitle() != null) song.setTitle(songDetails.getTitle());
        if (songDetails.getArtist() != null) song.setArtist(songDetails.getArtist());
        if (songDetails.getAlbum() != null) song.setAlbum(songDetails.getAlbum());
        if (songDetails.getGenre() != null) song.setGenre(songDetails.getGenre());
        if (songDetails.getDuration() != null) song.setDuration(songDetails.getDuration());
        if (songDetails.getImageUrl() != null) song.setImageUrl(songDetails.getImageUrl());

        // If a new audio file is provided, update it in Dropbox
        if (audioFile != null && !audioFile.isEmpty()) {
            // Delete the old file from Dropbox
            if (song.getFilePath() != null) {
                try {
                    dropboxService.deleteFile(song.getFilePath());
                } catch (Exception e) {
                    // Log the error but continue with the update
                    System.err.println("Failed to delete old file from Dropbox: " + e.getMessage());
                }
            }
            
            // Upload the new file
            String newFilePath = dropboxService.uploadFile(audioFile, audioFile.getOriginalFilename());
            song.setFilePath(newFilePath);
        }
        
        return songRepository.save(song);
    }
    
    public void deleteSong(Long id) {
        Song song = getSongById(id);
        
        // Delete the file from Dropbox
        if (song.getFilePath() != null) {
            try {
                dropboxService.deleteFile(song.getFilePath());
            } catch (Exception e) {
                // Log the error but continue with the deletion
                System.err.println("Failed to delete file from Dropbox: " + e.getMessage());
            }
        }
        
        songRepository.delete(song);
    }
    
    public String getSongStreamUrl(Long id) {
        Song song = getSongById(id);
        try {
            return dropboxService.getTemporaryLink(song.getFilePath());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get stream URL for song", e);
        }
    }
    
    public List<Song> searchSongs(String query) {
        // Search in title, artist, and genre
        List<Song> byTitle = songRepository.findByTitleContainingIgnoreCase(query);
        List<Song> byArtist = songRepository.findByArtistContainingIgnoreCase(query);
        List<Song> byGenre = songRepository.findByGenreContainingIgnoreCase(query);
        
        // Combine results and remove duplicates (by id)
        byTitle.addAll(byArtist);
        byTitle.addAll(byGenre);
        
        return byTitle.stream()
                .distinct()
                .toList();
    }
}