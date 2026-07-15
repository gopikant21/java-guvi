import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { firstValueFrom } from 'rxjs';

type Artist = {
  id: number;
  firstName: string;
  lastName: string;
};

@Component({
  selector: 'app-root',
  imports: [FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:9090/v1/artists';

  firstName = '';
  lastName = '';
  searchId: number | null = null;

  artists: Artist[] = [];
  selectedArtist: Artist | null = null;
  statusMessage = '';

  ngOnInit(): void {
    void this.loadArtists();
  }

  async createArtist(): Promise<void> {
    if (!this.firstName.trim() || !this.lastName.trim()) {
      this.statusMessage = 'First name and last name are required.';
      return;
    }

    try {
      const payload = {
        firstName: this.firstName.trim(),
        lastName: this.lastName.trim()
      };

      await firstValueFrom(this.http.post<Artist>(this.baseUrl, payload));
      this.firstName = '';
      this.lastName = '';
      this.statusMessage = 'Artist created.';
      await this.loadArtists();
    } catch (error) {
      this.setErrorMessage('Create artist', error);
    }
  }

  async loadArtists(): Promise<void> {
    try {
      this.artists = await firstValueFrom(this.http.get<Artist[]>(this.baseUrl));
    } catch (error) {
      this.setErrorMessage('Load artists', error);
    }
  }

  async findArtistById(): Promise<void> {
    if (this.searchId === null) {
      this.selectedArtist = null;
      this.statusMessage = 'Enter an id to search.';
      return;
    }

    try {
      this.selectedArtist = await firstValueFrom(
        this.http.get<Artist>(`${this.baseUrl}/${this.searchId}`)
      );
      this.statusMessage = 'Artist loaded.';
    } catch (error) {
      if (error instanceof HttpErrorResponse && error.status === 404) {
        this.selectedArtist = null;
        this.statusMessage = `Artist ${this.searchId} not found.`;
        return;
      }

      this.setErrorMessage('Find artist', error);
    }
  }

  async deleteArtist(id: number): Promise<void> {
    try {
      await firstValueFrom(this.http.delete<void>(`${this.baseUrl}/${id}`));

      if (this.selectedArtist?.id === id) {
        this.selectedArtist = null;
      }

      this.statusMessage = `Artist ${id} deleted.`;
      await this.loadArtists();
    } catch (error) {
      if (error instanceof HttpErrorResponse && error.status === 404) {
        this.statusMessage = `Artist ${id} not found.`;
        return;
      }

      this.setErrorMessage('Delete artist', error);
    }
  }

  private setErrorMessage(action: string, error: unknown): void {
    if (error instanceof HttpErrorResponse) {
      const status = error.status === 0 ? 'network error' : `HTTP ${error.status}`;
      this.statusMessage = `${action} failed (${status}).`;
      return;
    }

    this.statusMessage = `${action} failed.`;
  }
}
