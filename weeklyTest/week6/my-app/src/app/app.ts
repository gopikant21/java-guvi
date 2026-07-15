import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

type OrderByField =
  | 'id'
  | 'domainName'
  | 'numPages'
  | 'numBrokenLinks'
  | 'numMissingImages'
  | 'deleted';

interface Scan {
  id?: number;
  domainName: string;
  numPages: number;
  numBrokenLinks: number;
  numMissingImages: number;
  deleted: boolean;
}

@Component({
  selector: 'app-root',
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected readonly title = 'Scan Dashboard';
  protected readonly baseUrl = 'http://localhost:8001';

  protected scans = signal<Scan[]>([]);
  protected searchedScans = signal<Scan[]>([]);
  protected selectedScan = signal<Scan | null>(null);

  protected isLoading = signal(false);
  protected isSearching = signal(false);
  protected isCreating = signal(false);

  protected errorMessage = signal('');
  protected successMessage = signal('');

  protected healthStatus = signal<'unknown' | 'ok' | 'error'>('unknown');
  protected readyStatus = signal<'unknown' | 'ok' | 'error'>('unknown');

  protected createForm: Scan = {
    domainName: '',
    numPages: 0,
    numBrokenLinks: 0,
    numMissingImages: 0,
    deleted: false
  };

  protected searchDomainName = '';
  protected orderBy: OrderByField = 'id';
  protected getByIdValue: number | null = null;

  protected readonly orderByOptions: OrderByField[] = [
    'id',
    'domainName',
    'numPages',
    'numBrokenLinks',
    'numMissingImages',
    'deleted'
  ];

  constructor(private readonly http: HttpClient) {
  }

  ngOnInit(): void {
    this.loadScans();
  }

  protected checkHealth(): void {
    this.clearMessages();
    this.http.get(`${this.baseUrl}/health`, { responseType: 'text' }).subscribe({
      next: () => {
        this.healthStatus.set('ok');
        this.successMessage.set('Health check succeeded.');
      },
      error: (error) => {
        this.healthStatus.set('error');
        this.errorMessage.set(this.extractErrorMessage(error, 'Health check failed.'));
      }
    });
  }

  protected checkReady(): void {
    this.clearMessages();
    this.http.get(`${this.baseUrl}/ready`, { responseType: 'text' }).subscribe({
      next: () => {
        this.readyStatus.set('ok');
        this.successMessage.set('Readiness check succeeded.');
      },
      error: (error) => {
        this.readyStatus.set('error');
        this.errorMessage.set(this.extractErrorMessage(error, 'Readiness check failed.'));
      }
    });
  }

  protected loadScans(): void {
    this.clearMessages();
    this.isLoading.set(true);

    this.http.get<Scan[]>(`${this.baseUrl}/scan`).subscribe({
      next: (scans) => {
        this.scans.set(scans);
        this.isLoading.set(false);
      },
      error: (error) => {
        this.errorMessage.set(this.extractErrorMessage(error, 'Failed to load scans.'));
        this.isLoading.set(false);
      }
    });
  }

  protected createScan(): void {
    if (!this.createForm.domainName.trim()) {
      this.errorMessage.set('Domain name is required.');
      return;
    }

    this.clearMessages();
    this.isCreating.set(true);

    const payload: Scan = {
      domainName: this.createForm.domainName.trim(),
      numPages: Number(this.createForm.numPages),
      numBrokenLinks: Number(this.createForm.numBrokenLinks),
      numMissingImages: Number(this.createForm.numMissingImages),
      deleted: Boolean(this.createForm.deleted)
    };

    this.http.post<Scan>(`${this.baseUrl}/scan`, payload).subscribe({
      next: () => {
        this.successMessage.set('Scan created successfully.');
        this.isCreating.set(false);
        this.createForm = {
          domainName: '',
          numPages: 0,
          numBrokenLinks: 0,
          numMissingImages: 0,
          deleted: false
        };
        this.loadScans();
      },
      error: (error) => {
        this.errorMessage.set(this.extractErrorMessage(error, 'Failed to create scan.'));
        this.isCreating.set(false);
      }
    });
  }

  protected getScanById(): void {
    if (this.getByIdValue === null || Number.isNaN(this.getByIdValue)) {
      this.errorMessage.set('Please enter a valid scan ID.');
      return;
    }

    this.clearMessages();
    this.selectedScan.set(null);

    this.http.get<Scan>(`${this.baseUrl}/scan/${this.getByIdValue}`).subscribe({
      next: (scan) => {
        this.selectedScan.set(scan);
        this.successMessage.set(`Loaded scan with ID ${scan.id}.`);
      },
      error: (error) => {
        this.errorMessage.set(this.extractErrorMessage(error, 'Failed to fetch scan by ID.'));
      }
    });
  }

  protected searchScans(): void {
    if (!this.searchDomainName.trim()) {
      this.errorMessage.set('Please provide a domain name to search.');
      return;
    }

    this.clearMessages();
    this.isSearching.set(true);

    const encodedDomain = encodeURIComponent(this.searchDomainName.trim());
    this.http
      .get<Scan[]>(`${this.baseUrl}/scan/search/${encodedDomain}?orderBy=${this.orderBy}`)
      .subscribe({
        next: (scans) => {
          this.searchedScans.set(scans);
          this.successMessage.set(`Found ${scans.length} result(s).`);
          this.isSearching.set(false);
        },
        error: (error) => {
          this.errorMessage.set(this.extractErrorMessage(error, 'Search request failed.'));
          this.isSearching.set(false);
        }
      });
  }

  protected clearSearch(): void {
    this.searchDomainName = '';
    this.searchedScans.set([]);
    this.clearMessages();
  }

  protected deleteScan(id: number | undefined): void {
    if (id === undefined) {
      this.errorMessage.set('Cannot delete scan without ID.');
      return;
    }

    this.clearMessages();

    this.http.delete(`${this.baseUrl}/scan/${id}`, { responseType: 'text' }).subscribe({
      next: () => {
        this.successMessage.set(`Deleted scan with ID ${id}.`);
        this.loadScans();
        this.searchedScans.update((scans) => scans.filter((scan) => scan.id !== id));
        if (this.selectedScan()?.id === id) {
          this.selectedScan.set(null);
        }
      },
      error: (error) => {
        this.errorMessage.set(this.extractErrorMessage(error, 'Failed to delete scan.'));
      }
    });
  }

  private clearMessages(): void {
    this.errorMessage.set('');
    this.successMessage.set('');
  }

  private extractErrorMessage(error: unknown, fallback: string): string {
    if (!(error instanceof HttpErrorResponse)) {
      return fallback;
    }

    const details = error.error;
    if (typeof details === 'string' && details.trim()) {
      return `${fallback} ${details}`;
    }

    if (details && typeof details === 'object' && 'error' in details) {
      const backendError = String(details.error ?? '').trim();
      const status = 'status' in details ? ` (status: ${String(details.status)})` : '';
      if (backendError) {
        return `${fallback} ${backendError}${status}`;
      }
    }

    if (error.message) {
      return `${fallback} ${error.message}`;
    }

    return fallback;
  }
}
