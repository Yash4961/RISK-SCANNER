package com.vsecurity.scanner;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.vsecurity.scanner.data.local.AppSensorStatsDao;
import com.vsecurity.scanner.data.local.DailySummaryDao;
import com.vsecurity.scanner.data.local.PrivacyAlertDao;
import com.vsecurity.scanner.data.local.ScannedAppDao;
import com.vsecurity.scanner.data.local.SensorAccessLogDao;
import com.vsecurity.scanner.data.local.VScannerDatabase;
import com.vsecurity.scanner.data.preferences.PreferencesManager;
import com.vsecurity.scanner.data.repository.GuardianRepository;
import com.vsecurity.scanner.data.repository.ScannerRepository;
import com.vsecurity.scanner.data.scanner.MalwareScanner;
import com.vsecurity.scanner.di.AppModule_ProvideAppScannerFactory;
import com.vsecurity.scanner.di.AppModule_ProvideAppSensorStatsDaoFactory;
import com.vsecurity.scanner.di.AppModule_ProvideDailySummaryDaoFactory;
import com.vsecurity.scanner.di.AppModule_ProvideDatabaseFactory;
import com.vsecurity.scanner.di.AppModule_ProvideGuardianRepositoryFactory;
import com.vsecurity.scanner.di.AppModule_ProvidePreferencesManagerFactory;
import com.vsecurity.scanner.di.AppModule_ProvidePrivacyAlertDaoFactory;
import com.vsecurity.scanner.di.AppModule_ProvideScannedAppDaoFactory;
import com.vsecurity.scanner.di.AppModule_ProvideScannerRepositoryFactory;
import com.vsecurity.scanner.di.AppModule_ProvideSensorAccessLogDaoFactory;
import com.vsecurity.scanner.guardian.BootReceiver;
import com.vsecurity.scanner.guardian.BootReceiver_MembersInjector;
import com.vsecurity.scanner.guardian.NetworkMonitorVpnService;
import com.vsecurity.scanner.guardian.NetworkMonitorVpnService_MembersInjector;
import com.vsecurity.scanner.guardian.PrivacyGuardianService;
import com.vsecurity.scanner.guardian.PrivacyGuardianService_MembersInjector;
import com.vsecurity.scanner.scanner.AppScanner;
import com.vsecurity.scanner.ui.MainActivity;
import com.vsecurity.scanner.ui.viewmodel.AlertsViewModel;
import com.vsecurity.scanner.ui.viewmodel.AlertsViewModel_HiltModules;
import com.vsecurity.scanner.ui.viewmodel.AlertsViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.vsecurity.scanner.ui.viewmodel.AlertsViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.vsecurity.scanner.ui.viewmodel.DashboardViewModel;
import com.vsecurity.scanner.ui.viewmodel.DashboardViewModel_HiltModules;
import com.vsecurity.scanner.ui.viewmodel.DashboardViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.vsecurity.scanner.ui.viewmodel.DashboardViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.vsecurity.scanner.ui.viewmodel.FileScannerViewModel;
import com.vsecurity.scanner.ui.viewmodel.FileScannerViewModel_HiltModules;
import com.vsecurity.scanner.ui.viewmodel.FileScannerViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.vsecurity.scanner.ui.viewmodel.FileScannerViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.vsecurity.scanner.ui.viewmodel.GuardianViewModel;
import com.vsecurity.scanner.ui.viewmodel.GuardianViewModel_HiltModules;
import com.vsecurity.scanner.ui.viewmodel.GuardianViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.vsecurity.scanner.ui.viewmodel.GuardianViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.vsecurity.scanner.ui.viewmodel.ScannerViewModel;
import com.vsecurity.scanner.ui.viewmodel.ScannerViewModel_HiltModules;
import com.vsecurity.scanner.ui.viewmodel.ScannerViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.vsecurity.scanner.ui.viewmodel.ScannerViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.vsecurity.scanner.ui.viewmodel.SettingsViewModel;
import com.vsecurity.scanner.ui.viewmodel.SettingsViewModel_HiltModules;
import com.vsecurity.scanner.ui.viewmodel.SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.vsecurity.scanner.ui.viewmodel.SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerVScannerApplication_HiltComponents_SingletonC {
  private DaggerVScannerApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public VScannerApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements VScannerApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public VScannerApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements VScannerApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public VScannerApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements VScannerApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public VScannerApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements VScannerApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public VScannerApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements VScannerApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public VScannerApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements VScannerApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public VScannerApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements VScannerApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public VScannerApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends VScannerApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends VScannerApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    FragmentCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends VScannerApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends VScannerApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    ActivityCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    ImmutableMap keySetMapOfClassOfObjectAndBooleanBuilder() {
      ImmutableMap.Builder mapBuilder = ImmutableMap.<String, Boolean>builderWithExpectedSize(6);
      mapBuilder.put(AlertsViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, AlertsViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(DashboardViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, DashboardViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(FileScannerViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, FileScannerViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(GuardianViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, GuardianViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(ScannerViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ScannerViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, SettingsViewModel_HiltModules.KeyModule.provide());
      return mapBuilder.build();
    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(keySetMapOfClassOfObjectAndBooleanBuilder());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }
  }

  private static final class ViewModelCImpl extends VScannerApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    Provider<AlertsViewModel> alertsViewModelProvider;

    Provider<DashboardViewModel> dashboardViewModelProvider;

    Provider<FileScannerViewModel> fileScannerViewModelProvider;

    Provider<GuardianViewModel> guardianViewModelProvider;

    Provider<ScannerViewModel> scannerViewModelProvider;

    Provider<SettingsViewModel> settingsViewModelProvider;

    ViewModelCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        SavedStateHandle savedStateHandleParam, ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    ImmutableMap hiltViewModelMapMapOfClassOfObjectAndProviderOfViewModelBuilder() {
      ImmutableMap.Builder mapBuilder = ImmutableMap.<String, javax.inject.Provider<ViewModel>>builderWithExpectedSize(6);
      mapBuilder.put(AlertsViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (alertsViewModelProvider)));
      mapBuilder.put(DashboardViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (dashboardViewModelProvider)));
      mapBuilder.put(FileScannerViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (fileScannerViewModelProvider)));
      mapBuilder.put(GuardianViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (guardianViewModelProvider)));
      mapBuilder.put(ScannerViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (scannerViewModelProvider)));
      mapBuilder.put(SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (settingsViewModelProvider)));
      return mapBuilder.build();
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.alertsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.fileScannerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.guardianViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.scannerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(hiltViewModelMapMapOfClassOfObjectAndProviderOfViewModelBuilder());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<Class<?>, Object>of();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @Override
      @SuppressWarnings("unchecked")
      public T get() {
        switch (id) {
          case 0: // com.vsecurity.scanner.ui.viewmodel.AlertsViewModel
          return (T) new AlertsViewModel(singletonCImpl.provideGuardianRepositoryProvider.get());

          case 1: // com.vsecurity.scanner.ui.viewmodel.DashboardViewModel
          return (T) new DashboardViewModel(singletonCImpl.provideScannerRepositoryProvider.get(), singletonCImpl.provideGuardianRepositoryProvider.get(), singletonCImpl.providePreferencesManagerProvider.get());

          case 2: // com.vsecurity.scanner.ui.viewmodel.FileScannerViewModel
          return (T) new FileScannerViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.malwareScannerProvider.get(), singletonCImpl.providePreferencesManagerProvider.get());

          case 3: // com.vsecurity.scanner.ui.viewmodel.GuardianViewModel
          return (T) new GuardianViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideGuardianRepositoryProvider.get(), singletonCImpl.providePreferencesManagerProvider.get());

          case 4: // com.vsecurity.scanner.ui.viewmodel.ScannerViewModel
          return (T) new ScannerViewModel(singletonCImpl.provideAppScannerProvider.get(), singletonCImpl.provideScannerRepositoryProvider.get(), singletonCImpl.providePreferencesManagerProvider.get());

          case 5: // com.vsecurity.scanner.ui.viewmodel.SettingsViewModel
          return (T) new SettingsViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.providePreferencesManagerProvider.get(), singletonCImpl.provideScannerRepositoryProvider.get(), singletonCImpl.provideGuardianRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends VScannerApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @Override
      @SuppressWarnings("unchecked")
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends VScannerApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }

    @Override
    public void injectNetworkMonitorVpnService(NetworkMonitorVpnService networkMonitorVpnService) {
      injectNetworkMonitorVpnService2(networkMonitorVpnService);
    }

    @Override
    public void injectPrivacyGuardianService(PrivacyGuardianService privacyGuardianService) {
      injectPrivacyGuardianService2(privacyGuardianService);
    }

    @CanIgnoreReturnValue
    private NetworkMonitorVpnService injectNetworkMonitorVpnService2(
        NetworkMonitorVpnService instance) {
      NetworkMonitorVpnService_MembersInjector.injectRepository(instance, singletonCImpl.provideGuardianRepositoryProvider.get());
      return instance;
    }

    @CanIgnoreReturnValue
    private PrivacyGuardianService injectPrivacyGuardianService2(PrivacyGuardianService instance2) {
      PrivacyGuardianService_MembersInjector.injectRepository(instance2, singletonCImpl.provideGuardianRepositoryProvider.get());
      PrivacyGuardianService_MembersInjector.injectPreferencesManager(instance2, singletonCImpl.providePreferencesManagerProvider.get());
      return instance2;
    }
  }

  private static final class SingletonCImpl extends VScannerApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    Provider<PreferencesManager> providePreferencesManagerProvider;

    Provider<MalwareScanner> malwareScannerProvider;

    Provider<VScannerDatabase> provideDatabaseProvider;

    Provider<GuardianRepository> provideGuardianRepositoryProvider;

    Provider<ScannerRepository> provideScannerRepositoryProvider;

    Provider<AppScanner> provideAppScannerProvider;

    SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    SensorAccessLogDao sensorAccessLogDao() {
      return AppModule_ProvideSensorAccessLogDaoFactory.provideSensorAccessLogDao(provideDatabaseProvider.get());
    }

    PrivacyAlertDao privacyAlertDao() {
      return AppModule_ProvidePrivacyAlertDaoFactory.providePrivacyAlertDao(provideDatabaseProvider.get());
    }

    AppSensorStatsDao appSensorStatsDao() {
      return AppModule_ProvideAppSensorStatsDaoFactory.provideAppSensorStatsDao(provideDatabaseProvider.get());
    }

    DailySummaryDao dailySummaryDao() {
      return AppModule_ProvideDailySummaryDaoFactory.provideDailySummaryDao(provideDatabaseProvider.get());
    }

    ScannedAppDao scannedAppDao() {
      return AppModule_ProvideScannedAppDaoFactory.provideScannedAppDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.providePreferencesManagerProvider = DoubleCheck.provider(new SwitchingProvider<PreferencesManager>(singletonCImpl, 0));
      this.malwareScannerProvider = DoubleCheck.provider(new SwitchingProvider<MalwareScanner>(singletonCImpl, 1));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<VScannerDatabase>(singletonCImpl, 3));
      this.provideGuardianRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<GuardianRepository>(singletonCImpl, 2));
      this.provideScannerRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ScannerRepository>(singletonCImpl, 4));
      this.provideAppScannerProvider = DoubleCheck.provider(new SwitchingProvider<AppScanner>(singletonCImpl, 5));
    }

    @Override
    public void injectVScannerApplication(VScannerApplication vScannerApplication) {
    }

    @Override
    public void injectBootReceiver(BootReceiver bootReceiver) {
      injectBootReceiver2(bootReceiver);
    }

    @Override
    public MalwareScanner getMalwareScanner() {
      return malwareScannerProvider.get();
    }

    @Override
    public GuardianRepository getRepository() {
      return provideGuardianRepositoryProvider.get();
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    @CanIgnoreReturnValue
    private BootReceiver injectBootReceiver2(BootReceiver instance) {
      BootReceiver_MembersInjector.injectPreferencesManager(instance, providePreferencesManagerProvider.get());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @Override
      @SuppressWarnings("unchecked")
      public T get() {
        switch (id) {
          case 0: // com.vsecurity.scanner.data.preferences.PreferencesManager
          return (T) AppModule_ProvidePreferencesManagerFactory.providePreferencesManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 1: // com.vsecurity.scanner.data.scanner.MalwareScanner
          return (T) new MalwareScanner(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.vsecurity.scanner.data.repository.GuardianRepository
          return (T) AppModule_ProvideGuardianRepositoryFactory.provideGuardianRepository(singletonCImpl.sensorAccessLogDao(), singletonCImpl.privacyAlertDao(), singletonCImpl.appSensorStatsDao(), singletonCImpl.dailySummaryDao());

          case 3: // com.vsecurity.scanner.data.local.VScannerDatabase
          return (T) AppModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.vsecurity.scanner.data.repository.ScannerRepository
          return (T) AppModule_ProvideScannerRepositoryFactory.provideScannerRepository(singletonCImpl.scannedAppDao());

          case 5: // com.vsecurity.scanner.scanner.AppScanner
          return (T) AppModule_ProvideAppScannerFactory.provideAppScanner(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
