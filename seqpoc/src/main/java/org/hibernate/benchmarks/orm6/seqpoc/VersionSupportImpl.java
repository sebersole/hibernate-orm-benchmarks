/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.benchmarks.orm6.seqpoc;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.PersistenceUnitTransactionType;

import org.hibernate.benchmarks.BaseBenchmarkSession;
import org.hibernate.benchmarks.BenchmarkHQLParserTree;
import org.hibernate.benchmarks.BenchmarkHQLSemanticModelInterpreter;
import org.hibernate.bytecode.enhance.spi.EnhancementContext;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.jpa.AvailableSettings;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.spi.Bootstrap;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;

/**
 * @author Steve Ebersole
 */
public class VersionSupportImpl extends BaseBenchmarkSession {

	@Override
	protected EntityManagerFactory buildEntityManagerFactory() {
		return Bootstrap.getEntityManagerFactoryBuilder( buildPersistenceUnitDescriptor(), buildSettings() ).build();
	}

	private PersistenceUnitDescriptor buildPersistenceUnitDescriptor() {
		return new TestingPersistenceUnitDescriptorImpl( getClass().getSimpleName() );
	}

	@Override
	protected BenchmarkHQLParserTree buildHqlParser() {
		return new ParseTreeBuilderImpl();
	}

	@Override
	protected BenchmarkHQLSemanticModelInterpreter buildHqInterpreter() {
		return new SqmBuilderImpl( (SessionFactoryImplementor) entityManagerFactory );
	}

	@SuppressWarnings("unchecked")
	private Map buildSettings() {
		Map settings = getConfig();
		addMappings( settings );
		if ( createSchema ) {
			settings.put( org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO, "create-drop" );
		}
		settings.put( org.hibernate.cfg.AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS, "true" );
		settings.put( org.hibernate.cfg.AvailableSettings.DIALECT, Dialect.getDialect().getClass().getName() );
		return settings;
	}

	@SuppressWarnings("unchecked")
	private Map getConfig() {
		Map<Object, Object> config = Environment.getProperties();

		config.put( AvailableSettings.LOADED_CLASSES, Arrays.asList( annotatedClasses ) );

		return config;
	}

	@SuppressWarnings("unchecked")
	private void addMappings(Map settings) {
		if ( hbmfiles != null ) {
			settings.put( AvailableSettings.HBXML_FILES, String.join( ",", hbmfiles ) );
		}
	}

	public static class TestingPersistenceUnitDescriptorImpl implements PersistenceUnitDescriptor {
		private final String name;

		public TestingPersistenceUnitDescriptorImpl(String name) {
			this.name = name;
		}

		@Override
		public URL getPersistenceUnitRootUrl() {
			return null;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getProviderClassName() {
			return HibernatePersistenceProvider.class.getName();
		}

		@Override
		public boolean isUseQuotedIdentifiers() {
			return false;
		}

		@Override
		public boolean isExcludeUnlistedClasses() {
			return false;
		}

		@Override
		public PersistenceUnitTransactionType getTransactionType() {
			return null;
		}

		@Override
		public ValidationMode getValidationMode() {
			return null;
		}

		@Override
		public SharedCacheMode getSharedCacheMode() {
			return null;
		}

		@Override
		public List<String> getManagedClassNames() {
			return null;
		}

		@Override
		public List<String> getMappingFileNames() {
			return null;
		}

		@Override
		public List<URL> getJarFileUrls() {
			return null;
		}

		@Override
		public Object getNonJtaDataSource() {
			return null;
		}

		@Override
		public Object getJtaDataSource() {
			return null;
		}

		@Override
		public Properties getProperties() {
			return null;
		}

		@Override
		public ClassLoader getClassLoader() {
			return null;
		}

		@Override
		public ClassLoader getTempClassLoader() {
			return null;
		}

		@Override
		public void pushClassTransformer(EnhancementContext enhancementContext) {
		}
	}
}
