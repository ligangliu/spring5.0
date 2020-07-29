/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.lang.Nullable;

/**
 * Delegate for AbstractApplicationContext's post-processor handling.
 *
 * @author Juergen Hoeller
 * @since 4.0
 */
final class PostProcessorRegistrationDelegate {

	public static void invokeBeanFactoryPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {
		/**
		 * List<BeanFactoryPostProcessor> beanFactoryPostProcessors
		 * 估计百分之90是不会有值的
		 * beanFactoryPostProcessors 是由getBeanFactoryPostProcessors()得到的
		 * 这个是从一个list中直接获取
		 * 在 我们外部通过api加入的context.addBeanFactoryPostProcessor(xxx);
		 * 会直接放入到这个beanFactoryPostProcessors中。但是绝大多数的情况下是不会有的，
		 * 只不过这是一个拓展点
		 * 但是大部分不会有这种需求吧。。。。。直接@Bean添加进去不好嘛？？除非有一些在扫描之前需要干的活
		 */
		// Invoke BeanDefinitionRegistryPostProcessors first, if any.
		Set<String> processedBeans = new HashSet<>();

		if (beanFactory instanceof BeanDefinitionRegistry) {
			BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
			/**
			 *
			 * BeanDefinitionRegistryPostProcessor
			 * 1，程序员提供的
			 * 2，spring内置的
			 * 3，扫描之后又得到的
			 *
			 * 因为我们自己定义的BeanFactoryPostProcessor可以有两种方式
			 * 1.实现BeanFactoryPostProcessor
			 * 2.实现BeanDefinitionRegistryPostProcessor
			 * 但是这两种BeanDefinitionRegistryPostProcessor在spring中是完成不同的功能。
			 * 因为BeanDefinitionRegistryPostProcessor拓展了BeanFactoryPostProcessor，其中
			 * 添加了postProcessBeanDefinitionRegistry这个方法
			 */
			//存放BeanFactoryPostProcessor的类型的
			List<BeanFactoryPostProcessor> regularPostProcessors = new ArrayList<>();
			//存放BeanDefinitionRegistryPostProcessor类型的
			//BeanDefinitionRegistryPostProcessor是BeanFactoryPostProcessor的子类
			List<BeanDefinitionRegistryPostProcessor> registryProcessors = new ArrayList<>();

			// 这里的beanFactoryPostProcessors 是由getBeanFactoryPostProcessors()得到的，
			// 具体是返回this.beanFactoryPostProcessors，讲道理第一次执行过来的时候应该是为空的
			// 除非程序在外部调用context.addBeanFactoryPostProcessor(null);直接添加的
			for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
				// 这里外部的bean还没有扫描进去的呢，所以讲道理这里是不会有外部的BeanDefinitionRegistryPostProcessor
				//判断其是BeanDefinitionRegistryPostProcessor还是BeanFactoryPostProcessor
				if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
					BeanDefinitionRegistryPostProcessor registryProcessor =
							(BeanDefinitionRegistryPostProcessor) postProcessor;
					// 这个是spring自己实现的，它只不过拓展了，多实现了一个postProcessBeanDefinitionRegistry方法
					// spring最牛逼的一个工厂后置处理器 ConfigurationClassPostProcessor就是进行扫描所有的bd
					registryProcessor.postProcessBeanDefinitionRegistry(registry);
					//执行完上面的方法，也是放入了registryProcessors中的列表中的，
					// 因为下面还需要执行它的BeanFactoryPostProcessor接口的postProcessBeanFactory方法
					registryProcessors.add(registryProcessor);
				}
				else {
					regularPostProcessors.add(postProcessor);
				}
			}

			// Do not initialize FactoryBeans here: We need to leave all regular beans
			// uninitialized to let the bean factory post-processors apply to them!
			// Separate between BeanDefinitionRegistryPostProcessors that implement
			// PriorityOrdered, Ordered, and the rest.
			/**
			 *  放的是spring内部实现了BeanDefinitionRegistryPostProcessor接口的
			 * registryProcessors 放的是我们自己实现了BeanDefinitionRegistryPostProcessor接口的
			 */
			List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();

			// First, invoke the BeanDefinitionRegistryPostProcessors that implement PriorityOrdered.
			/**
			 * 我们在前面不是已经初始化放了6个(5个PostProcessor和1个BeanFactoryPostProcessor)spring内置的处理器
			 * 从beanFactory的存放beanDefinition的map中根据类型BeanDefinitionRegistryPostProcessor
			 * 获取该类型对应的beanName
			 * 如获取到了： ConfigurationClassPostProcessor
			 */
			String[] postProcessorNames =
					//BeanDefinitionRegistryPostProcessor是spring内置的工厂后置处理器
					//讲道理，如果一切按默认的话，这里应该是可以得到ConfigurationClassPostProcessor
					beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			for (String ppName : postProcessorNames) {
				// 难道还必须实现 PriorityOrdered 该接口嘛？这个接口也没啥用呀，除了标记一下
				if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
					//获取到了spring内置的BeanFactoryPostProcessor的ConfigurationClassPostProcessor
					//将其添加到currentRegistryProcessors中list中
					//看到这里没有，是通过getBean获取的，所以说这里的BeanFactoryPostProcessor是初始化好的
					// 通过getBean->将该类的beanDefinition信息初始化出来
					// currentRegistryProcessors 表示正在执行的后置处理器
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					// processedBeans用于后续排除已经被执行过的后置处理器
					processedBeans.add(ppName);
				}
			}
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			//registryProcessors 中的所有的接口都是调用BeanFactoryPostProcessor接口下的 postProcessBeanFactory方法
			registryProcessors.addAll(currentRegistryProcessors);
			/**
			 * 回调用所有自定义的实现了BeanDefinitionRegistryPostProcessor接口的类
			 * 讲道理第一次这里应该只有ConfigurationClassPostProcessor
			 *
			 */
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
			currentRegistryProcessors.clear();

			// Next, invoke the BeanDefinitionRegistryPostProcessors that implement Ordered.
			/**
			 * 这里为啥来一遍？？？
			 * 是因为可能在配置类中扫描得到了我们代码是是是实现了BeanDefinitionRegistryPostProcessor接口。
			 */
			postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			for (String ppName : postProcessorNames) {
				if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					processedBeans.add(ppName);
				}
			}
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			registryProcessors.addAll(currentRegistryProcessors);

			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
			currentRegistryProcessors.clear();

			/**
			 * 一直递归处理，因为有可能，配置类 中又有配置类
			 * 然后 配置类中配置类，再有配置类呢？
			 * 而且上面分别是需要满足
			 *  beanFactory.isTypeMatch(ppName, PriorityOrdered.class)
			 *  beanFactory.isTypeMatch(ppName, Ordered.class)
			 *  下面是没有这个判断的。即使普通的实现BeanDefinitionRegistryPostProcessor接口的
			 *  没有实现PriorityOrdered和Ordered接口的
			 *
			 */
			// Finally, invoke all other BeanDefinitionRegistryPostProcessors until no further ones appear.
			boolean reiterate = true;
			// while循环是因为BeanDefinitionRegistryPostProcessor需要扫描的功能，可能又会扫描得到
			while (reiterate) {
				reiterate = false;
				postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
				for (String ppName : postProcessorNames) {
					if (!processedBeans.contains(ppName)) {
						currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
						processedBeans.add(ppName);
						reiterate = true;
					}
				}
				sortPostProcessors(currentRegistryProcessors, beanFactory);
				registryProcessors.addAll(currentRegistryProcessors);
				invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
				currentRegistryProcessors.clear();
			}

			/**
			 * ===============================================================================
			 * 当while循环执行完成之后我们所有的beanDefiniton(包含@Bean注解，Import等各种情况)
			 * 都已经被添加到beanDefinitionMap中啦
			 * ===============================================================================
			 */

			// Now, invoke the postProcessBeanFactory callback of all processors handled so far.
			/**
			 * 执行BeanFactoryPostProcessor的回调
			 * 前面执行的时BeanFactoryPostProcessor的子类BeanDefinitionRegistryPostProcessor的回调。
			 * 前面执行的回调是扫描所有的配置类，加入到bd中
			 * 这里执行的是BeanFactoryPostProcessor的回调(AOP实现的基础)。
			 * registryProcessors中存放的实现了BeanDefinitionRegistryPostProcessor
			 * 而BeanDefinitionRegistryPostProcessor又是BeanFactoryPostProcessor的子类，所以也会执行
			 *
			 * =====================================================================
			 * registryProcessors：主要是ConfigurationClassPostProcessor，
			 * 它里面的方法会去为@Configuration生成代理，也就是为AppConfig通过cglib生成代理，
			 * 然后放入到AppConfig中bd的setBeanClass(改成生成了cglib的类)
			 * 然后在处理@Bean的时候，肯定是直接通过bd配置的信息找到AppConfig去调用方法生成对象，而这里肯定会先
			 * getBean("appConfig")，但是这是一个被cglib代理的类啦，然后通过调用里面的方法就能对
			 * @Bean构造对象出来
			 *
			 * 并且注册了一个beanFactory.addBeanPostProcessor(new ImportAwareBeanPostProcessor(beanFactory))
			 * 的后置处理器。ImportAwareBeanPostProcessor是一个BeanPostProcessor会为实现了ApplcationAware等
			 * 接口的注入容器
			 * =====================================================================
			 *
			 */
			invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);
			/**
			 * 执行的是我们自定义的BeanFactoryPostProcessor
			 * (是通过context.addBeanFactoryPostProcessor(null);注册进去的)
			 */
			invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
		}

		else {
			// Invoke factory processors registered with the context instance.
			invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
		}
		/**
		 * BeanFactoryPostProcessor
		 * 1，程序提供的
		 * 2，spring内置的
		 * 因为BeanDefinitionRegistryPostProcessor是子接口，上述的情况已经在上面执行完毕啦。
		 * =========================================================
		 * 3，扫描得到的 (下面的代码就是处理这种扫描得到的类)
		 * =========================================================
		 *
		 * 下面的逻辑是处理我们自定义的类的实现了BeanFactoryPostProcessor接口的类
		 * 因为在上面我们已经扫描得到了所有bd啦,上面只是处理了spring内置的BeanFactoryPostProcessor啦
		 */
		// Do not initialize FactoryBeans here: We need to leave all regular beans
		// uninitialized to let the bean factory post-processors apply to them!
		String[] postProcessorNames =
				beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);

		// Separate between BeanFactoryPostProcessors that implement PriorityOrdered,
		// Ordered, and the rest.
		List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
		List<String> orderedPostProcessorNames = new ArrayList<>();
		List<String> nonOrderedPostProcessorNames = new ArrayList<>();
		for (String ppName : postProcessorNames) {
			//如果包含了，说明在上面已经处理过了，如springn内置的实现了BeanDefinitionRegistryPostProcessor
			// 接口，以及程序外部直接注册的都已经被执行完毕了的
			if (processedBeans.contains(ppName)) {
				// skip - already processed in first phase above
			}
			// 下面可能就是一些排序吧,就和上面的逻辑是一样的，相当是有一个优先级执行顺序而已
			else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
			}
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				orderedPostProcessorNames.add(ppName);
			}
			else {
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// First, invoke the BeanFactoryPostProcessors that implement PriorityOrdered.
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
		invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);

		// Next, invoke the BeanFactoryPostProcessors that implement Ordered.
		List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<>();
		for (String postProcessorName : orderedPostProcessorNames) {
			orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		sortPostProcessors(orderedPostProcessors, beanFactory);
		invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);

		// Finally, invoke all other BeanFactoryPostProcessors.
		List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<>();
		for (String postProcessorName : nonOrderedPostProcessorNames) {
			nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);

		// Clear cached merged bean definitions since the post-processors might have
		// modified the original metadata, e.g. replacing placeholders in values...
		beanFactory.clearMetadataCache();
	}

	/**
	 * beanDefinitionMap中有一部分，实现了后置处理器
	 * beanPostProcessors 列表中有一个直接使用add(new XXPostProcessor)
	 * 然后在这个方法里面又添加了几个后置处理器
	 */
	public static void registerBeanPostProcessors(
			ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext) {
		/**
		 * 获取所有在bd中已经注册了的实现了BeanPostProcessor接口的beanName
		 * 如果有自定义的，肯定能够获取到
		 * 如果加了AOP注解@EnableAspectJAutoProxy 也会在bd中有org.springframework.aop.config.internalAutoProxyCreator
		 * 只要AOP的后置处理器是通过@Import的类AspectJAutoProxyRegistrar实现了ImportBeanDefinitionRegistrar
		 * 从而注册AnnotationAutoProxy的bd到beanFactory的bd map中
		 *
		 *
		 * 但是这里注意一下，spring中有一些内置的BeanPostProcessor并没有注册到bd中，
		 * 而是直接添加到了beanFactory中beanPostProcessors(如：ApplicationContextAwareProcessor)列表中
		 * 这些后置处理器是直接调用regisrer.addBeanPostProcessor()直接添加到后置处理器列表中
		 *
		 */
		//从bd map中获取实现了BeanPostProcessor的接口的beanName
		String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);

		// Register BeanPostProcessorChecker that logs an info message when
		// a bean is created during BeanPostProcessor instantiation, i.e. when
		// a bean is not eligible for getting processed by all BeanPostProcessors.
		int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;
		//在这里又添加了以一个BeanPostProcessorChecker的后置处理器
		beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));

		// Separate between BeanPostProcessors that implement PriorityOrdered,
		// Ordered, and the rest.
		List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
		List<BeanPostProcessor> internalPostProcessors = new ArrayList<>();
		List<String> orderedPostProcessorNames = new ArrayList<>();
		List<String> nonOrderedPostProcessorNames = new ArrayList<>();
		for (String ppName : postProcessorNames) {
			if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				/**
				 * 看到没有，这里会通过getBean()初始化我们的后置处理器
				 */
				BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
				priorityOrderedPostProcessors.add(pp);
				if (pp instanceof MergedBeanDefinitionPostProcessor) {
					internalPostProcessors.add(pp);
				}
			}
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				orderedPostProcessorNames.add(ppName);
			}
			else {
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// First, register the BeanPostProcessors that implement PriorityOrdered.
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
		registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);

		// Next, register the BeanPostProcessors that implement Ordered.
		List<BeanPostProcessor> orderedPostProcessors = new ArrayList<>();
		for (String ppName : orderedPostProcessorNames) {
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			orderedPostProcessors.add(pp);
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				internalPostProcessors.add(pp);
			}
		}
		sortPostProcessors(orderedPostProcessors, beanFactory);
		registerBeanPostProcessors(beanFactory, orderedPostProcessors);

		// Now, register all regular BeanPostProcessors.
		List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<>();
		for (String ppName : nonOrderedPostProcessorNames) {
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			nonOrderedPostProcessors.add(pp);
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				internalPostProcessors.add(pp);
			}
		}
		registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);

		// Finally, re-register all internal BeanPostProcessors.
		sortPostProcessors(internalPostProcessors, beanFactory);
		registerBeanPostProcessors(beanFactory, internalPostProcessors);

		// Re-register post-processor for detecting inner beans as ApplicationListeners,
		// moving it to the end of the processor chain (for picking up proxies etc).
		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(applicationContext));
	}

	private static void sortPostProcessors(List<?> postProcessors, ConfigurableListableBeanFactory beanFactory) {
		Comparator<Object> comparatorToUse = null;
		if (beanFactory instanceof DefaultListableBeanFactory) {
			comparatorToUse = ((DefaultListableBeanFactory) beanFactory).getDependencyComparator();
		}
		if (comparatorToUse == null) {
			comparatorToUse = OrderComparator.INSTANCE;
		}
		postProcessors.sort(comparatorToUse);
	}

	/**
	 * Invoke the given BeanDefinitionRegistryPostProcessor beans.
	 */
	private static void invokeBeanDefinitionRegistryPostProcessors(
			Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry) {

		for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
			postProcessor.postProcessBeanDefinitionRegistry(registry);
		}
	}

	/**
	 * Invoke the given BeanFactoryPostProcessor beans.
	 */
	private static void invokeBeanFactoryPostProcessors(
			Collection<? extends BeanFactoryPostProcessor> postProcessors, ConfigurableListableBeanFactory beanFactory) {

		for (BeanFactoryPostProcessor postProcessor : postProcessors) {
			postProcessor.postProcessBeanFactory(beanFactory);
		}
	}

	/**
	 * Register the given BeanPostProcessor beans.
	 */
	private static void registerBeanPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanPostProcessor> postProcessors) {

		for (BeanPostProcessor postProcessor : postProcessors) {
			beanFactory.addBeanPostProcessor(postProcessor);
		}
	}


	/**
	 * BeanPostProcessor that logs an info message when a bean is created during
	 * BeanPostProcessor instantiation, i.e. when a bean is not eligible for
	 * getting processed by all BeanPostProcessors.
	 */
	private static final class BeanPostProcessorChecker implements BeanPostProcessor {

		private static final Log logger = LogFactory.getLog(BeanPostProcessorChecker.class);

		private final ConfigurableListableBeanFactory beanFactory;

		private final int beanPostProcessorTargetCount;

		public BeanPostProcessorChecker(ConfigurableListableBeanFactory beanFactory, int beanPostProcessorTargetCount) {
			this.beanFactory = beanFactory;
			this.beanPostProcessorTargetCount = beanPostProcessorTargetCount;
		}

		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName) {
			return bean;
		}

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) {
			if (!(bean instanceof BeanPostProcessor) && !isInfrastructureBean(beanName) &&
					this.beanFactory.getBeanPostProcessorCount() < this.beanPostProcessorTargetCount) {
				if (logger.isInfoEnabled()) {
					logger.info("Bean '" + beanName + "' of type [" + bean.getClass().getName() +
							"] is not eligible for getting processed by all BeanPostProcessors " +
							"(for example: not eligible for auto-proxying)");
				}
			}
			return bean;
		}

		private boolean isInfrastructureBean(@Nullable String beanName) {
			if (beanName != null && this.beanFactory.containsBeanDefinition(beanName)) {
				BeanDefinition bd = this.beanFactory.getBeanDefinition(beanName);
				return (bd.getRole() == RootBeanDefinition.ROLE_INFRASTRUCTURE);
			}
			return false;
		}
	}

}
