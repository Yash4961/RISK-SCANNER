package com.vsecurity.scanner.di;

import android.content.Context;
import com.vsecurity.scanner.scanner.AppScanner;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideAppScannerFactory implements Factory<AppScanner> {
  private final Provider<Context> contextProvider;

  private AppModule_ProvideAppScannerFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AppScanner get() {
    return provideAppScanner(contextProvider.get());
  }

  public static AppModule_ProvideAppScannerFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideAppScannerFactory(contextProvider);
  }

  public static AppScanner provideAppScanner(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAppScanner(context));
  }
}
