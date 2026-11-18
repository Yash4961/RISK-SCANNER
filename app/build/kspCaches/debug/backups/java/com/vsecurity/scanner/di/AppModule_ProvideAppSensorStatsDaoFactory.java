package com.vsecurity.scanner.di;

import com.vsecurity.scanner.data.local.AppSensorStatsDao;
import com.vsecurity.scanner.data.local.VScannerDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideAppSensorStatsDaoFactory implements Factory<AppSensorStatsDao> {
  private final Provider<VScannerDatabase> databaseProvider;

  private AppModule_ProvideAppSensorStatsDaoFactory(Provider<VScannerDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AppSensorStatsDao get() {
    return provideAppSensorStatsDao(databaseProvider.get());
  }

  public static AppModule_ProvideAppSensorStatsDaoFactory create(
      Provider<VScannerDatabase> databaseProvider) {
    return new AppModule_ProvideAppSensorStatsDaoFactory(databaseProvider);
  }

  public static AppSensorStatsDao provideAppSensorStatsDao(VScannerDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAppSensorStatsDao(database));
  }
}
