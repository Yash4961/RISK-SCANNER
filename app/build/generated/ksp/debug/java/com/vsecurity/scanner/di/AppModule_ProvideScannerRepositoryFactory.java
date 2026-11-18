package com.vsecurity.scanner.di;

import com.vsecurity.scanner.data.local.ScannedAppDao;
import com.vsecurity.scanner.data.repository.ScannerRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideScannerRepositoryFactory implements Factory<ScannerRepository> {
  private final Provider<ScannedAppDao> scannedAppDaoProvider;

  private AppModule_ProvideScannerRepositoryFactory(Provider<ScannedAppDao> scannedAppDaoProvider) {
    this.scannedAppDaoProvider = scannedAppDaoProvider;
  }

  @Override
  public ScannerRepository get() {
    return provideScannerRepository(scannedAppDaoProvider.get());
  }

  public static AppModule_ProvideScannerRepositoryFactory create(
      Provider<ScannedAppDao> scannedAppDaoProvider) {
    return new AppModule_ProvideScannerRepositoryFactory(scannedAppDaoProvider);
  }

  public static ScannerRepository provideScannerRepository(ScannedAppDao scannedAppDao) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideScannerRepository(scannedAppDao));
  }
}
