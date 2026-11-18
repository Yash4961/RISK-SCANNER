package com.vsecurity.scanner.di;

import com.vsecurity.scanner.data.local.DailySummaryDao;
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
public final class AppModule_ProvideDailySummaryDaoFactory implements Factory<DailySummaryDao> {
  private final Provider<VScannerDatabase> databaseProvider;

  private AppModule_ProvideDailySummaryDaoFactory(Provider<VScannerDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public DailySummaryDao get() {
    return provideDailySummaryDao(databaseProvider.get());
  }

  public static AppModule_ProvideDailySummaryDaoFactory create(
      Provider<VScannerDatabase> databaseProvider) {
    return new AppModule_ProvideDailySummaryDaoFactory(databaseProvider);
  }

  public static DailySummaryDao provideDailySummaryDao(VScannerDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideDailySummaryDao(database));
  }
}
