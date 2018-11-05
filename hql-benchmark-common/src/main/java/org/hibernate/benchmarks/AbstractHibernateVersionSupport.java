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
import javax.persistence.EntityManagerFactory;

/**
 * @author Andrea Boriero
 */
public abstract class AbstractHibernateVersionSupport implements HibernateVersionSupport {
	protected Class[] annotatedClasses;
	protected String[] hbmFiles = new String[0];
	protected boolean createSchema;
	protected EntityManagerFactory entityManagerFactory;

	private HqlParseTreeBuilder hqlParser;
	private HqlSemanticInterpreter hqlInterpreter;

	@Override
	public void setUp(Class[] annotatedClasses, String[] hbmFiles, boolean createSchema) {
		this.annotatedClasses = annotatedClasses;
		this.hbmFiles = hbmFiles;
		this.createSchema = createSchema;

		this.entityManagerFactory = buildEntityManagerFactory();
		this.hqlParser = createHqlParseTreeBuilder();
		this.hqlInterpreter = createHqlSemanticInterpreter();

		System.out.println( "Running benchmark with HQL parse tree builder : " + hqlParser.getClass().getName() );
		System.out.println( "Running benchmark with HQL semantic interpreter : " + hqlInterpreter.getClass().getName() );

	}

	protected abstract EntityManagerFactory buildEntityManagerFactory();

	protected abstract HqlParseTreeBuilder createHqlParseTreeBuilder();

	protected abstract HqlSemanticInterpreter createHqlSemanticInterpreter();

	@Override
	public HqlParseTreeBuilder getHqlParseTreeBuilder() {
		return hqlParser;
	}

	@Override
	public HqlSemanticInterpreter getHqlSemanticInterpreter() {
		return hqlInterpreter;
	}

	@Override
	public <T> T inTransaction(Function<EntityManager, T> function) {
		EntityManager em = entityManagerFactory.createEntityManager();
		T result = null;
		try {
			em.getTransaction().begin();
			result = function.apply( em );
			em.getTransaction().commit();
		}
		catch (Exception e) {
			if ( em.getTransaction().isActive() ) {
				em.getTransaction().rollback();
			}
		}
		finally {
			em.close();
		}
		return result;
	}

	@Override
	public void inTransaction(Consumer<EntityManager> consumer) {
		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			em.getTransaction().begin();
			consumer.accept( em );
			em.getTransaction().commit();
		}
		catch (Exception e) {
			if ( em.getTransaction().isActive() ) {
				em.getTransaction().rollback();
			}
		}
		finally {
			em.close();
		}
	}

	@Override
	public void shutDown() {
		entityManagerFactory.close();
	}


}
