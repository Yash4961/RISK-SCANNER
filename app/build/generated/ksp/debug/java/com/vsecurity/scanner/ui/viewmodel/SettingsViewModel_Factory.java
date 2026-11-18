package com.vsecurity.scanner.ui.viewmodel;

import android.content.Context;
import com.vsecurity.scanner.data.preferences.PreferencesManager;
import com.vsecurity.scanner.data.repository.GuardianRepository;
import com.vsecurity.scanner.data.repository.ScannerRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  private final Provider<ScannerRepository> scannerRepositoryProvider;

  private final Provider<GuardianRepository> guardianRepositoryProvider;

  private SettingsViewModel_Factory(Provider<Context> contextProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<ScannerRepository> scannerRepositoryProvider,
      Provider<GuardianRepository> guardianRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.scannerRepositoryProvider = scannerRepositoryProvider;
    this.guardianRepositoryProvider = guardianRepositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(contextProvider.get(), preferencesManagerProvider.get(), scannerRepositoryProvider.get(), guardianRepositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<Context> contextProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<ScannerRepository> scannerRepositoryProvider,
      Provider<GuardianRepository> guardianRepositoryProvider) {
    return new SettingsViewModel_Factory(contextProvider, preferencesManagerProvider, scannerRepositoryProvider, guardianRepositoryProvider);
  }

  public static SettingsViewModel newInstance(Context context,
      PreferencesManager preferencesManager, ScannerRepository scannerRepository,
      GuardianRepository guardianRepository) {
    return new SettingsViewModel(context, preferencesManager, scannerRepository, guardianRepository);
  }
}
