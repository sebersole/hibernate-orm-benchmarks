package org.hibernate.benchmarks;

import java.lang.reflect.InvocationTargetException;

/**
 * This class loads the code fragments that are measured on this benchmark
 */
public class BenchmarkFactory {

	public static BenchmarkSession buildTestSession() {
		return getTestSession( new String[] {
				"org.hibernate.benchmarks.ORM5BenchmarkSession",
				"org.hibernate.benchmarks.Orm6BenchmarkSession",
				"org.hibernate.benchmarks.orm6.seqpoc.VersionSupportImpl"
		} );
	}

	private static BenchmarkSession getTestSession(String[] parserClasses) {
		Class<?> loadedClass = loadClassByName( parserClasses );
		if ( loadedClass != null ) {
			try {
				return (BenchmarkSession) loadedClass.getConstructor().newInstance();
			}
			catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// --- //

	private static Class<?> loadClassByName(String[] parserClasses) {
		for ( String parserClass : parserClasses ) {
			try {
				return BenchmarkFactory.class.getClassLoader().loadClass( parserClass );
			}
			catch (ClassNotFoundException e) {
				// proceed
			}
		}
		return null;
	}

	// --- Interfaces representing the various stages that we are interested in measuring

//    public interface BenchmarkHQLParserTree {
//
//        default Object getHQLParser(String hqlString) {
//            return null;
//        }
//
////        default void configure(SessionFactory sessionFactory) {
////        }
//    }
//
//    public interface BenchmarkHQLSemanticModelInterpreter {
//
//        default Object getSemanticModel(String hqlString) {
//            return null;
//        }
//
////        default void configure(SessionFactory sessionFactory) {
////        }
//    }
}
