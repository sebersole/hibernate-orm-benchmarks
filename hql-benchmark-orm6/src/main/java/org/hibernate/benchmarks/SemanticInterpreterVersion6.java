/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.benchmarks;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.query.sqm.produce.internal.hql.HqlParseTreeBuilder;
import org.hibernate.query.sqm.produce.internal.hql.SemanticQueryBuilder;
import org.hibernate.query.sqm.produce.internal.hql.grammar.HqlParser;

/**
 * @author John O`Hara
 * @author Luis Barreiro
 * @author Andrea Boriero
 * @author Steve Ebersole
 */
public class SemanticInterpreterVersion6 implements HqlSemanticInterpreter {
	private final SessionFactoryImplementor sessionFactory;

	public SemanticInterpreterVersion6(SessionFactoryImplementor sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Object getSemanticModel(String hqlString) {
		HqlParser.StatementContext statementContext = HqlParseTreeBuilder.INSTANCE.parseHql( hqlString ).statement();
		return SemanticQueryBuilder.buildSemanticModel( statementContext, sessionFactory );
	}
}
