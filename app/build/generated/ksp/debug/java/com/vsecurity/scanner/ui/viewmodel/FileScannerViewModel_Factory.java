package com.vsecurity.scanner.ui.viewmodel;

import android.content.Context;
import com.vsecurity.scanner.data.preferences.PreferencesManager;
import com.vsecurity.scanner.data.scanner.MalwareScanner;
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
public final class FileScannerViewModel_Factory implements Factory<FileScannerViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<MalwareScanner> malwareScannerProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  private FileScannerViewModel_Factory(Provider<Context> contextProvider,
      Provider<MalwareScanner> malwareScannerProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.contextProvider = contextProvider;
    this.malwareScannerProvider = malwareScannerProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public FileScannerViewModel get() {
    return newInstance(contextProvider.get(), malwareScannerProvider.get(), preferencesManagerProvider.get());
  }

  public static FileScannerViewModel_Factory create(Provider<Context> contextProvider,
      Provider<MalwareScanner> malwareScannerProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new FileScannerViewModel_Factory(contextProvider, malwareScannerProvider, preferencesManagerProvider);
  }

  public static FileScannerViewModel newInstance(Context context, MalwareScanner malwareScanner,
      PreferencesManager preferencesManager) {
    return new FileScannerViewModel(context, malwareScanner, preferencesManager);
  }
}
