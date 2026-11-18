package com.vsecurity.scanner.ui.viewmodel;

import com.vsecurity.scanner.data.preferences.PreferencesManager;
import com.vsecurity.scanner.data.repository.ScannerRepository;
import com.vsecurity.scanner.scanner.AppScanner;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class ScannerViewModel_Factory implements Factory<ScannerViewModel> {
  private final Provider<AppScanner> appScannerProvider;

  private final Provider<ScannerRepository> scannerRepositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  private ScannerViewModel_Factory(Provider<AppScanner> appScannerProvider,
      Provider<ScannerRepository> scannerRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.appScannerProvider = appScannerProvider;
    this.scannerRepositoryProvider = scannerRepositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public ScannerViewModel get() {
    return newInstance(appScannerProvider.get(), scannerRepositoryProvider.get(), preferencesManagerProvider.get());
  }

  public static ScannerViewModel_Factory create(Provider<AppScanner> appScannerProvider,
      Provider<ScannerRepository> scannerRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new ScannerViewModel_Factory(appScannerProvider, scannerRepositoryProvider, preferencesManagerProvider);
  }

  public static ScannerViewModel newInstance(AppScanner appScanner,
      ScannerRepository scannerRepository, PreferencesManager preferencesManager) {
    return new ScannerViewModel(appScanner, scannerRepository, preferencesManager);
  }
}
