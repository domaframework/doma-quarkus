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
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.runtime.LaunchMode;
import java.util.List;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;
import org.seasar.doma.DaoImplementation;
import org.seasar.doma.quarkus.runtime.DomaConfiguration;
import org.seasar.doma.quarkus.runtime.DomaProducer;
import org.seasar.doma.quarkus.runtime.DomaRecorder;

class DomaProcessor {

  private static final String FEATURE = "doma";

  @BuildStep
  FeatureBuildItem feature() {
    return new FeatureBuildItem(FEATURE);
  }

  @BuildStep
  NativeImageResourceBuildItem resources() {
    DomaResourceScanner scanner = new DomaResourceScanner();
    List<String> resources = scanner.scan();
    return new NativeImageResourceBuildItem(resources);
  }

  @BuildStep
  ReflectiveClassBuildItem classes(BeanArchiveIndexBuildItem indexBuildItem) {
    IndexView indexView = indexBuildItem.getIndex();
    DomaClassScanner scanner = new DomaClassScanner(indexView);
    List<String> classes = scanner.scan();
    return new ReflectiveClassBuildItem(true, true, classes.toArray(new String[0]));
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
  @Record(STATIC_INIT)
  void configure(
      DomaRecorder recorder,
      DomaConfiguration configuration,
      List<JdbcDataSourceBuildItem> dataSources,
      LaunchModeBuildItem launchMode,
      BuildProducer<BeanContainerListenerBuildItem> beanContainerListeners) {
    DomaConfigurationMerger merger = new DomaConfigurationMerger(configuration, dataSources);
    merger.merge();
    beanContainerListeners.produce(
        new BeanContainerListenerBuildItem(recorder.configure(configuration)));
    if (launchMode.getLaunchMode() == LaunchMode.DEVELOPMENT) {
      beanContainerListeners.produce(
          new BeanContainerListenerBuildItem(recorder.configureHotReplacement()));
    }
  }
}
