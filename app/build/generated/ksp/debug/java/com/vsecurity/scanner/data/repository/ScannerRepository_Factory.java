package com.vsecurity.scanner.data.repository;

import com.vsecurity.scanner.data.local.ScannedAppDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class ScannerRepository_Factory implements Factory<ScannerRepository> {
  private final Provider<ScannedAppDao> scannedAppDaoProvider;

  private ScannerRepository_Factory(Provider<ScannedAppDao> scannedAppDaoProvider) {
    this.scannedAppDaoProvider = scannedAppDaoProvider;
  }

  @Override
  public ScannerRepository get() {
    return newInstance(scannedAppDaoProvider.get());
  }

  public static ScannerRepository_Factory create(Provider<ScannedAppDao> scannedAppDaoProvider) {
    return new ScannerRepository_Factory(scannedAppDaoProvider);
  }

  public static ScannerRepository newInstance(ScannedAppDao scannedAppDao) {
    return new ScannerRepository(scannedAppDao);
  }
}
