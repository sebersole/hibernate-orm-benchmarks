/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.benchmarks;

import java.util.function.Consumer;
import java.util.function.Function;
import javax.persistence.EntityManager;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

/**
 * @author Andrea Boriero
 */
@State(Scope.Benchmark)
public class BenchmarkState {

	private HibernateVersionSupport versionSupport;

	private HqlParseTreeBuilder hqlParseTreeBuilder;
	private HqlSemanticInterpreter hqlSemanticInterpreter;

	@Setup
	public void setup() {
		try {
			versionSupport = BenchmarkFactory.buildHibernateVersionSupport();
			versionSupport.setUp( getAnnotatedClasses(), getHbmFiles(), createSchema() );

			hqlParseTreeBuilder = versionSupport.getHqlParseTreeBuilder();
			hqlSemanticInterpreter = versionSupport.getHqlSemanticInterpreter();
			populateDatabase();
		}
		catch (Throwable t) {
			t.printStackTrace();
			throw t;
		}
	}

	protected void populateDatabase() {
	}

	protected void cleanUpDatabase() {

	}

	protected Class[] getAnnotatedClasses() {
		return new Class[] {};
	}

	protected String[] getHbmFiles() {
		return new String[] { "benchmark.hbm.xml" };
	}

	protected boolean createSchema() {
		return false;
	}

	public HqlParseTreeBuilder getHqlParseTreeBuilder() {
		return hqlParseTreeBuilder;
	}

	public HqlSemanticInterpreter getHqlSemanticInterpreter() {
		return hqlSemanticInterpreter;
	}

	<T> T inTransaction(Function<EntityManager, T> sessionConsumer) {
		return versionSupport.inTransaction( sessionConsumer );
	}

	void inTransaction(Consumer<EntityManager> sessionConsumer) {
		versionSupport.inTransaction( sessionConsumer );
	}

	@TearDown
	public void shutdown() {
		try {
			cleanUpDatabase();
			versionSupport.shutDown();
		}
		catch (Throwable t) {
			t.printStackTrace();
			throw t;
		}
	}
}
