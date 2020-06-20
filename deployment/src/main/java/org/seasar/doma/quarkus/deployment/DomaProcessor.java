package org.seasar.doma.quarkus.deployment;

import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;

import io.quarkus.agroal.deployment.JdbcDataSourceBuildItem;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.BeanArchiveIndexBuildItem;
import io.quarkus.arc.deployment.BeanContainerListenerBuildItem;
import io.quarkus.arc.deployment.BeanDefiningAnnotationBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.ApplicationArchivesBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.HotDeploymentWatchedFileBuildItem;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.runtime.LaunchMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;
import org.seasar.doma.DaoImplementation;
import org.seasar.doma.quarkus.runtime.DomaConfiguration;
import org.seasar.doma.quarkus.runtime.DomaProducer;
import org.seasar.doma.quarkus.runtime.DomaRecorder;
import org.seasar.doma.quarkus.runtime.InitialScriptLoader;

class DomaProcessor {

  private static final String FEATURE = "doma";

  @BuildStep
  FeatureBuildItem feature() {
    return new FeatureBuildItem(FEATURE);
  }

  @BuildStep
  AdditionalBeanBuildItem additionalBeans() {
    return new AdditionalBeanBuildItem(DomaProducer.class);
  }

  @BuildStep
  BeanDefiningAnnotationBuildItem beanDefiningAnnotation() {
    return new BeanDefiningAnnotationBuildItem(
        DotName.createSimple(DaoImplementation.class.getName()));
  }

  @BuildStep
  MergedConfigurationBuildItem mergedConfiguration(
      DomaConfiguration configuration,
      List<JdbcDataSourceBuildItem> dataSources,
      ApplicationArchivesBuildItem applicationArchives,
      LaunchModeBuildItem launchMode) {
    DomaConfigurationMerger merger =
        new DomaConfigurationMerger(configuration, dataSources, applicationArchives, launchMode);
    merger.merge();
    return new MergedConfigurationBuildItem(configuration);
  }

  @BuildStep
  List<HotDeploymentWatchedFileBuildItem> hotDeploymentWatchedFile(
      MergedConfigurationBuildItem mergedConfiguration) {
    DomaConfiguration configuration = mergedConfiguration.getConfiguration();
    Optional<String> sqlLoadScript = configuration.sqlLoadScript;
    return sqlLoadScript
        .map(it -> Collections.singletonList(new HotDeploymentWatchedFileBuildItem(it)))
        .orElse(Collections.emptyList());
  }

  @BuildStep
  NativeImageResourceBuildItem resources(MergedConfigurationBuildItem mergedConfiguration) {
    List<String> resources = new ArrayList<>();
    DomaConfiguration configuration = mergedConfiguration.getConfiguration();
    Optional<String> sqlLoadScript = configuration.sqlLoadScript;
    sqlLoadScript.ifPresent(resources::add);
    DomaResourceScanner scanner = new DomaResourceScanner();
    List<String> scannedResources = scanner.scan();
    resources.addAll(scannedResources);
    return new NativeImageResourceBuildItem(resources);
  }

  @BuildStep
  ReflectiveClassBuildItem classes(BeanArchiveIndexBuildItem beanArchiveIndex) {
    List<String> classes = new ArrayList<>();
    IndexView indexView = beanArchiveIndex.getIndex();
    DomaClassScanner scanner = new DomaClassScanner(indexView);
    List<String> scannedClasses = scanner.scan();
    classes.add(InitialScriptLoader.class.getName());
    classes.addAll(scannedClasses);
    return new ReflectiveClassBuildItem(true, true, classes.toArray(new String[0]));
  }

  @BuildStep
  @Record(STATIC_INIT)
  void configure(
      DomaRecorder recorder,
      MergedConfigurationBuildItem mergedConfiguration,
      LaunchModeBuildItem launchMode,
      BuildProducer<BeanContainerListenerBuildItem> beanContainerListeners) {
    DomaConfiguration configuration = mergedConfiguration.getConfiguration();
    beanContainerListeners.produce(
        new BeanContainerListenerBuildItem(recorder.configure(configuration)));
    if (launchMode.getLaunchMode() == LaunchMode.DEVELOPMENT) {
      beanContainerListeners.produce(
          new BeanContainerListenerBuildItem(recorder.configureHotReplacement()));
    }
  }
}
