import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

interface Track {
  id: number;
  title: string;
  albumName: string;
  releaseDate: string;
  playCount: number;
}

interface TrackRequest {
  title: string;
  albumName: string;
  releaseDate: string;
  playCount: number;
}

@Component({
  selector: 'app-root',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private readonly apiBaseUrl = 'http://localhost:8080/music/platform/v1/tracks';
  private readonly http = inject(HttpClient);
  private readonly fb = inject(FormBuilder);

  readonly createForm = this.fb.nonNullable.group({
    title: ['', [Validators.required, Validators.minLength(2)]],
    albumName: ['', [Validators.required, Validators.minLength(2)]],
    releaseDate: ['', Validators.required],
    playCount: [0, [Validators.required, Validators.min(0)]]
  });

  readonly searchForm = this.fb.nonNullable.group({
    title: ['', [Validators.required, Validators.minLength(2)]]
  });

  tracks: Track[] = [];
  selectedTrack: Track | null = null;
  isLoading = false;
  isSubmitting = false;
  message = '';
  errorMessage = '';

  constructor() {
    this.loadTracks();
  }

  loadTracks(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.http.get<Track[]>(this.apiBaseUrl).subscribe({
      next: (tracks) => {
        this.tracks = tracks ?? [];
        this.selectedTrack = null;
        this.message = 'Track list refreshed.';
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Unable to fetch tracks. Check if backend is running.';
        this.isLoading = false;
      }
    });
  }

  createTrack(): void {
    if (this.createForm.invalid) {
      this.createForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';
    this.message = '';

    const payload = this.createForm.getRawValue() as TrackRequest;

    this.http.post<Track>(this.apiBaseUrl, payload).subscribe({
      next: (track) => {
        this.tracks = [track, ...this.tracks];
        this.createForm.reset({
          title: '',
          albumName: '',
          releaseDate: '',
          playCount: 0
        });
        this.selectedTrack = track;
        this.message = 'Track created successfully.';
        this.isSubmitting = false;
      },
      error: () => {
        this.errorMessage = 'Track creation failed. Please retry.';
        this.isSubmitting = false;
      }
    });
  }

  searchByTitle(): void {
    if (this.searchForm.invalid) {
      this.searchForm.markAllAsTouched();
      return;
    }

    const title = this.searchForm.controls.title.value.trim();
    this.isLoading = true;
    this.errorMessage = '';
    this.message = '';

    this.http.get<Track | null>(`${this.apiBaseUrl}/search?title=${encodeURIComponent(title)}`).subscribe({
      next: (track) => {
        this.isLoading = false;

        if (track && track.id) {
          this.selectedTrack = track;
          this.message = 'Track found.';
          return;
        }

        this.selectedTrack = null;
        this.message = 'No track found for this title.';
      },
      error: () => {
        this.isLoading = false;
        this.errorMessage = 'Search failed. Please retry.';
      }
    });
  }

  clearSearch(): void {
    this.searchForm.reset({ title: '' });
    this.selectedTrack = null;
    this.message = 'Search cleared.';
    this.errorMessage = '';
  }

  deleteTrack(trackId: number): void {
    this.errorMessage = '';
    this.message = '';

    this.http.delete<void>(`${this.apiBaseUrl}/${trackId}`).subscribe({
      next: () => {
        this.tracks = this.tracks.filter((track) => track.id !== trackId);
        if (this.selectedTrack?.id === trackId) {
          this.selectedTrack = null;
        }
        this.message = 'Track deleted.';
      },
      error: () => {
        this.errorMessage = 'Delete failed. Please retry.';
      }
    });
  }
}
