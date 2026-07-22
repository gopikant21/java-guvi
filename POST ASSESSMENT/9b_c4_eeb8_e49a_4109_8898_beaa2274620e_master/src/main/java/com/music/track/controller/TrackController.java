package com.music.track.controller;

import com.music.track.dto.TrackRequest;
import com.music.track.model.Track;
import com.music.track.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("music/platform/v1/tracks")
public class TrackController {
    private final TrackService trackService;

    @Autowired
    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    /**
     * Create a track
     * @param trackRequest request body with track details
     * @return created track with generated id
     */
    @PostMapping()
    public ResponseEntity<Track> createTrack(@RequestBody TrackRequest trackRequest) {
        return new ResponseEntity<>(trackService.createTrack(trackRequest), HttpStatus.CREATED);
    }

    /**
     * Get all tracks
     * @return list of tracks
     */
    @GetMapping()
    public ResponseEntity<List<Track>> getAllTracks() {
        return new ResponseEntity<>(trackService.getAllTracks(), HttpStatus.OK);
    }

    /**
     * Delete a track
     * @param trackId track id
     * @return no content
     */
    @DeleteMapping("/{trackId}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long trackId) {
        trackService.deleteTrack(trackId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Get Track by title
     * @param title title query param
     * @return matching track
     */
    @GetMapping("/search")
    public ResponseEntity<Track> getTrackByTitle(@RequestParam String title) {
        return new ResponseEntity<>(trackService.getTracksByTitle(title), HttpStatus.OK);
    }

}
